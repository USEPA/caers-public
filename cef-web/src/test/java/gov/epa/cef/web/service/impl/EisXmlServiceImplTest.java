/*
 * © Copyright 2019 EPA CAERS Project Team
 *
 * This file is part of the Common Air Emissions Reporting System (CAERS).
 *
 * CAERS is free software: you can redistribute it and/or modify it under the 
 * terms of the GNU General Public License as published by the Free Software Foundation, 
 * either version 3 of the License, or (at your option) any later version.
 *
 * CAERS is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without 
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with CAERS.  If 
 * not, see <https://www.gnu.org/licenses/>.
*/
package gov.epa.cef.web.service.impl;

import com.google.common.io.Resources;

import gov.epa.cef.web.config.TestCategories;
import gov.epa.cef.web.config.mock.MockSLTConfig;
import gov.epa.cef.web.service.dto.EisHeaderDto;
import gov.epa.cef.web.service.dto.EisSubmissionStatus;
import gov.epa.cef.web.util.SLTConfigHelper;
import net.exchangenetwork.schema.cer._2._0.CERSDataType;
import net.exchangenetwork.schema.header._2.DocumentHeaderType;
import net.exchangenetwork.schema.header._2.DocumentPayloadType;
import net.exchangenetwork.schema.header._2.ExchangeNetworkDocumentType;
import net.exchangenetwork.schema.header._2.NameValuePair;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Category(TestCategories.FastTest.class)
public class EisXmlServiceImplTest {

    private JAXBContext cersJaxbContext;

    private EisXmlServiceImpl eisXmlService;

    @Before
    public void _onJunitBeginTest() {

        try {

            this.cersJaxbContext = JAXBContext.newInstance(CERSDataType.class);

            String schemaLocation = "schema/CERS/2/index.xsd";
            URL schemaUrl = Resources.getResource(schemaLocation);
            if (schemaUrl == null) {
                String msg = String.format("XSD is missing; not found on classpath at %s.", schemaLocation);
                throw new IllegalStateException(msg);
            }

        } catch (JAXBException e) {

            throw new IllegalStateException("Unable to create JAXBContext.", e);
        }

        List<CERSDataType> cersData = generateMockData();

        CersXmlServiceImpl cersXmlService = mock(CersXmlServiceImpl.class);
        
        when(cersXmlService.generateCersV2Data(1L, EisSubmissionStatus.ProdEmissions)).thenReturn(cersData.get(0));
        when(cersXmlService.generateCersV2Data(2L, EisSubmissionStatus.ProdEmissions)).thenReturn(cersData.get(1));
        when(cersXmlService.generateCersV2Data(3L, EisSubmissionStatus.ProdEmissions)).thenReturn(cersData.get(2));

        MockSLTConfig gaConfig = new MockSLTConfig();
        gaConfig.setSltEisProgramCode("GADNR");
        gaConfig.setSltEisUser("test");

        SLTConfigHelper sltConfigHelper = mock(SLTConfigHelper.class);
        when(sltConfigHelper.getCurrentSLTConfig("GADNR")).thenReturn(gaConfig);

        this.eisXmlService = new EisXmlServiceImpl(cersXmlService, sltConfigHelper);
    }

    @Test
    public void generateEisDataTest() throws Exception {

        EisHeaderDto eisHeader = new EisHeaderDto()
            .withProgramSystemCode("GADNR")
            .withAuthorName("Jim last name")
            .withOrganizationName("Slate Rock and Gravel")
            .withSubmissionStatus(EisSubmissionStatus.ProdEmissions)
            .withEmissionsReports(Arrays.asList(1L, 2L, 3L));

        ExchangeNetworkDocumentType document = this.eisXmlService.generateEisDocument(eisHeader);

        List<DocumentPayloadType> payloads = document.getPayload();
        assertEquals(1, payloads.size());

        Element cersData = ((Element) payloads.get(0).getAny());
        assertNotNull(cersData);
        assertEquals(3, cersData.getElementsByTagName("FacilitySite").getLength());

    }

    @Test
    public void writeEisXmlToTest() throws Exception {

        EisHeaderDto eisHeader = new EisHeaderDto()
            .withProgramSystemCode("GADNR")
            .withAuthorName("Jim last name")
            .withOrganizationName("Slate Rock and Gravel")
            .withSubmissionStatus(EisSubmissionStatus.ProdEmissions)
            .withEmissionsReports(Arrays.asList(1L, 2L, 3L));

        ExchangeNetworkDocumentType momento;

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            this.eisXmlService.writeEisXmlTo(eisHeader, outputStream);

            try (InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray())) {

                momento = eisDatafromInputStream(inputStream);
            }
        }

        DocumentHeaderType header = momento.getHeader();
        Map<String, String> properties = header.getProperty().stream()
            .collect(Collectors.toMap(
                NameValuePair::getPropertyName, np -> np.getPropertyValue().toString()));

        assertEquals(eisHeader.getSubmissionStatus().dataCategory(), properties.get("DataCategory"));
        assertEquals(eisHeader.getSubmissionStatus().submissionType(), properties.get("SubmissionType"));

        List<DocumentPayloadType> payloads = momento.getPayload();
        assertEquals(1, payloads.size());

        Node cersNode = (Node) payloads.get(0).getAny();
        CERSDataType cersData = cersDataFromSource(new DOMSource(cersNode));

        assertEquals(3, cersData.getFacilitySite().size());
    }

    private CERSDataType cersDataFromSource(Source source) {

        CERSDataType result;

        try {

            Unmarshaller um = this.cersJaxbContext.createUnmarshaller();

            JAXBElement<CERSDataType> root = um.unmarshal(source, CERSDataType.class);
            result = root.getValue();

        } catch (JAXBException e) {

            throw new IllegalStateException(e);
        }

        return result;
    }

    private CERSDataType cersDataFromXml() {

        CERSDataType result;

        URL resource = Resources.getResource("xml/cers-data-eis-test.xml");
        try (InputStream inputStream = resource.openStream()) {

            result = cersDataFromSource(new StreamSource(inputStream));

        } catch (IOException e) {

            throw new IllegalStateException(e);
        }

        return result;
    }

    private ExchangeNetworkDocumentType eisDatafromInputStream(InputStream inputStream) {

        ExchangeNetworkDocumentType result;

        try {

            JAXBContext jaxbContext =
                JAXBContext.newInstance(ExchangeNetworkDocumentType.class, CERSDataType.class);

            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

            String schemaLocation = "schema/header/header_v2.0.xsd";
            URL schemaUrl = Resources.getResource(schemaLocation);
            if (schemaUrl == null) {
                String msg = String.format("XSD is missing; not found on classpath at %s.", schemaLocation);
                throw new IllegalStateException(msg);
            }

            Schema schema = schemaFactory.newSchema(schemaUrl);

            Unmarshaller um = jaxbContext.createUnmarshaller();
            um.setSchema(schema);

            StreamSource streamSource = new StreamSource(inputStream);
            JAXBElement<ExchangeNetworkDocumentType> root =
                um.unmarshal(streamSource, ExchangeNetworkDocumentType.class);
            result = root.getValue();

        } catch (JAXBException | SAXException e) {

            throw new IllegalStateException("Unable to create JAXBContext.", e);
        }

        return result;
    }

    private List<CERSDataType> generateMockData() {

        CERSDataType c1 = cersDataFromXml();
        c1.getFacilitySite().get(0).setFacilitySiteName("FacilitySite-1");

        CERSDataType c2 = cersDataFromXml();
        c2.getFacilitySite().get(0).setFacilitySiteName("FacilitySite-2");

        CERSDataType c3 = cersDataFromXml();
        c3.getFacilitySite().get(0).setFacilitySiteName("FacilitySite-3");

        return Arrays.asList(c1, c2, c3);
    }
}
