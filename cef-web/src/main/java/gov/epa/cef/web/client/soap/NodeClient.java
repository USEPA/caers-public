/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.client.soap;

import com.google.common.net.MediaType;
import gov.epa.cef.web.config.CdxConfig;
import gov.epa.cef.web.config.NetworkNodeConfig;
import gov.epa.cef.web.config.NetworkNodeName;
import gov.epa.cef.web.config.NodeEndpointConfig;
import gov.epa.cef.web.exception.ApplicationErrorCode;
import gov.epa.cef.web.exception.ApplicationException;
import net.exchangenetwork.schema.node._2.AttachmentType;
import net.exchangenetwork.schema.node._2.DocumentFormatType;
import net.exchangenetwork.schema.node._2.NodeDocumentType;
import net.exchangenetwork.schema.node._2.TransactionStatusCode;
import net.exchangenetwork.wsdl.node._2.NetworkNodePortType2;
import net.exchangenetwork.wsdl.node._2.NodeFaultMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.xml.ws.Holder;
import java.io.File;
import java.util.Collections;
import java.util.UUID;

@Component
public class NodeClient extends AbstractClient {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final CdxConfig cdxConfig;

    private final NetworkNodeConfig networkNodeConfig;

    @Autowired
    NodeClient(CdxConfig cdxConfig, NetworkNodeConfig networkNodeConfig) {

        this.cdxConfig = cdxConfig;
        this.networkNodeConfig = networkNodeConfig;

        logger.info("Initialised NetworkNode2 Service with {}", this.networkNodeConfig);
    }

    public NodeTransaction submit(NetworkNodeName nodeName, String zipFileName, File zipFile) {

        Holder<String> transactionId = new Holder<>(UUID.randomUUID().toString());
        Holder<TransactionStatusCode> status = new Holder<>();
        Holder<String> statusDetail = new Holder<>();

        NodeEndpointConfig endpoint = this.networkNodeConfig.getNetworkNodes().get(nodeName);

        NetworkNodePortType2 service = getClient(endpoint.getServiceUrl(),
            NetworkNodePortType2.class, true, true);

        try {

            NodeDocumentType document = new NodeDocumentType();
            document.setDocumentFormat(DocumentFormatType.ZIP);
            document.setDocumentName(zipFileName);

            AttachmentType attachmentType = new AttachmentType();
            attachmentType.setContentType(MediaType.OCTET_STREAM.toString());
            attachmentType.setValue(new DataHandler(new FileDataSource(zipFile)));
            document.setDocumentContent(attachmentType);

            service.submit(authenticate(service), transactionId, endpoint.getDataflow(), "",
                null, null, Collections.singletonList(document), status, statusDetail);

        } catch (NodeFaultMessage fault) {
            throw this.handleException(convertFault(fault), logger);
        } catch (Exception e) {
            throw this.handleException(e, logger);
        }

        return new NodeTransaction(transactionId.value, status.value, statusDetail.value);
    }

    public NodeTransaction retrieveStatus(NetworkNodeName nodeName, String id) {

        Holder<String> transactionId = new Holder<>(id);
        Holder<TransactionStatusCode> status = new Holder<>();
        Holder<String> statusDetail = new Holder<>();

        NodeEndpointConfig endpoint = this.networkNodeConfig.getNetworkNodes().get(nodeName);

        NetworkNodePortType2 service = getClient(endpoint.getServiceUrl(),
            NetworkNodePortType2.class, true, true);

        try {

            service.getStatus(authenticate(service), transactionId, status, statusDetail);

        } catch (NodeFaultMessage fault) {
            throw this.handleException(convertFault(fault), logger);
        } catch (Exception e) {
            throw this.handleException(e, logger);
        }

        return new NodeTransaction(transactionId.value, status.value, statusDetail.value);
    }

    ApplicationException convertFault(NodeFaultMessage fault) {

        if (fault.getFaultInfo() == null) {
            return new ApplicationException(ApplicationErrorCode.E_REMOTE_SERVICE_ERROR, fault.getMessage());
        } else {
            ApplicationErrorCode code;
            try {
                code = ApplicationErrorCode.valueOf(fault.getFaultInfo().getErrorCode().value());
            } catch (Exception e) {
                logger.warn("Could not translate fault.", e);
                code = ApplicationErrorCode.E_REMOTE_SERVICE_ERROR;
            }
            return new ApplicationException(code, fault.getFaultInfo().getDescription());
        }
    }

    private String authenticate(NetworkNodePortType2 service) {

        try {
            String userId = this.cdxConfig.getNaasUser();
            String password = this.cdxConfig.getNaasPassword();

            return service.authenticate(userId, password, DOMAIN, AUTH_METHOD);

        } catch (NodeFaultMessage fault) {
            throw this.handleException(convertFault(fault), logger);
        } catch (Exception e) {
            throw this.handleException(e, logger);
        }
    }
}
