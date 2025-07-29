/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.impl;

import gov.epa.cef.web.config.SLTBaseConfig;
import gov.epa.cef.web.exception.AppValidationException;
import gov.epa.cef.web.exception.ApplicationErrorCode;
import gov.epa.cef.web.exception.ApplicationException;
import gov.epa.cef.web.service.dto.EisHeaderDto;
import gov.epa.cef.web.util.DateUtils;
import gov.epa.cef.web.util.SLTConfigHelper;
import net.exchangenetwork.schema.header._2.DocumentHeaderType;
import net.exchangenetwork.schema.header._2.DocumentPayloadType;
import net.exchangenetwork.schema.header._2.ExchangeNetworkDocumentType;
import net.exchangenetwork.schema.header._2.NameValuePair;
import net.exchangenetwork.schema.header._2.ObjectFactory;
import net.exchangenetwork.schema.cer._2._0.CERSDataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.XMLConstants;

import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class EisXmlServiceImpl {

    private static final String DataCategoryPropertyName = "DataCategory";

    private static final String DataflowName = "EIS_v1_0";

    private static final String DataflowNameV2 = "CERS_v2";

    private static final String DocumentTitle = "EIS";

    private static final String SubmissionTypePropertyName = "SubmissionType";

    private final CersXmlServiceImpl cersXmlService;

    private final SLTConfigHelper sltConfigHelper;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    EisXmlServiceImpl(CersXmlServiceImpl cersXmlService, SLTConfigHelper sltConfigHelper) {

        this.cersXmlService = cersXmlService;
        this.sltConfigHelper = sltConfigHelper;
    }

    public ExchangeNetworkDocumentType generateEisDocument(EisHeaderDto eisHeader) {

        SLTBaseConfig sltConfig = sltConfigHelper.getCurrentSLTConfig(eisHeader.getProgramSystemCode());

        CERSDataType cersData = new CERSDataType();
        cersData.setUserIdentifier(sltConfig.getSltEisUser());
        cersData.setProgramSystemCode(sltConfig.getSltEisProgramCode());

        Set<XMLGregorianCalendar> reportYears = new HashSet<>();

        eisHeader.getEmissionsReports().forEach(reportId -> {

            CERSDataType reportCersData = this.cersXmlService.generateCersV2Data(reportId, eisHeader.getSubmissionStatus());
            cersData.getFacilitySite().addAll(reportCersData.getFacilitySite());

            cersData.setEmissionsYear(reportCersData.getEmissionsYear());
            reportYears.add(reportCersData.getEmissionsYear());
        });

        if (reportYears.size() > 1) {
            throw new AppValidationException("All reports for a single submission to EIS must be for the same year.");
        }

        net.exchangenetwork.schema.cer._2._0.ObjectFactory cersObjectFactory =
            new net.exchangenetwork.schema.cer._2._0.ObjectFactory();

        try {

            JAXBContext jaxbContext =
                JAXBContext.newInstance(CERSDataType.class);

            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            // create an xml document to marshal CERS xml into so that the CERS namespace declaration will be at the CERS level for EIS
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();

            jaxbMarshaller.marshal( cersObjectFactory.createCERS(cersData), doc );

            return new ExchangeNetworkDocumentType()
                    .withId("_".concat(UUID.randomUUID().toString()))
                    .withHeader(
                        new DocumentHeaderType()
                            .withDataFlowName(DataflowNameV2)
                            .withAuthorName(eisHeader.getAuthorName())
                            .withOrganizationName(eisHeader.getOrganizationName())
                            .withDocumentTitle(DocumentTitle)
                            .withProperty(
                                new NameValuePair()
                                    .withPropertyName(SubmissionTypePropertyName)
                                    .withPropertyValue(eisHeader.getSubmissionStatus().submissionType()),
                                new NameValuePair()
                                    .withPropertyName(DataCategoryPropertyName)
                                    .withPropertyValue(eisHeader.getSubmissionStatus().dataCategory()))
                            .withCreationDateTime(DateUtils.createGregorianCalendar()))
                    .withPayload(
                        new DocumentPayloadType()
                            .withId("_".concat(UUID.randomUUID().toString()))
                            .withAny(doc.getDocumentElement()));

        } catch (JAXBException | ParserConfigurationException e) {

            throw new IllegalStateException(e);
        }
    }

    public void writeEisXmlTo(EisHeaderDto eisHeader, OutputStream outputStream) {

        ExchangeNetworkDocumentType result = generateEisDocument(eisHeader);

       writeEisXmlTo(result, outputStream);
    }

    public void writeEisXmlTo(ExchangeNetworkDocumentType eisDoc, OutputStream outputStream) {

        try {
            ObjectFactory objectFactory = new ObjectFactory();

            JAXBContext jaxbContext =
                JAXBContext.newInstance(ExchangeNetworkDocumentType.class);

            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            jaxbMarshaller.marshal(objectFactory.createDocument(eisDoc), outputStream);

        } catch (JAXBException e) {

            throw new IllegalStateException(e);
        }
    }
}
