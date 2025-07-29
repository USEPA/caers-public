/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.client.soap;

import gov.epa.cef.web.exception.ApplicationErrorCode;
import gov.epa.cef.web.exception.ApplicationException;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dfladung
 */
public abstract class AbstractClient {

    public static final long READ_TIMEOUT = 1000 * 60 * 5;
    public static final long CONN_TIMEOUT = READ_TIMEOUT;
    public static final String DOMAIN = "default";
    public static final String AUTH_METHOD = "password";
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractClient.class);

    protected ApplicationException handleException(Exception e, Logger logger) {
        ApplicationException ae = null;
        if (e instanceof ApplicationException) {
            ae = (ApplicationException) e;
        } else {
            ae = new ApplicationException(ApplicationErrorCode.E_INTERNAL_ERROR, e.getMessage());
        }
        logger.error(String.format("%s: %s", ae.getErrorCode(), ae.getMessage()), ae);
        return ae;
    }

    protected <T> T getClient(URL address, Class<T> service, boolean enableMtom, boolean enableChunking) {

        return getClient(address.toString(), service, enableMtom, enableChunking);
    }

    protected <T> T getClient(String address, Class<T> service, boolean enableMtom, boolean enableChunking) {
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();

        // set the endpoint
        factory.setAddress(address);
        // set Soap 1.2 binding
        factory.setBindingId("http://www.w3.org/2003/05/soap/bindings/HTTP/");
        // set the service for which this client binds
        factory.setServiceClass(service);

        // log the soap requests
        LoggingOutInterceptor loggingOutInterceptor = new LoggingOutInterceptor();
        loggingOutInterceptor.setPrettyLogging(true);
        loggingOutInterceptor.setShowBinaryContent(false);
        factory.getOutInterceptors().add(loggingOutInterceptor);
        factory.getOutFaultInterceptors().add(loggingOutInterceptor);

        // log the soap responses
        LoggingInInterceptor loggingInInterceptor = new LoggingInInterceptor();
        loggingInInterceptor.setPrettyLogging(true);
        loggingInInterceptor.setShowBinaryContent(false);
        factory.getInInterceptors().add(loggingInInterceptor);
        factory.getInFaultInterceptors().add(loggingInInterceptor);

        // enable MTOM
        if (enableMtom) {
            Map<String, Object> props = factory.getProperties();
            if (props == null) {
                props = new HashMap<>();
                factory.setProperties(props);
            }
            props.put("mtom-enabled", "true");
        }

        // get a handle to the client for configuration
        Object requester = factory.create();
        Client client = ClientProxy.getClient(requester);
        HTTPConduit http = (HTTPConduit) client.getConduit();

        // enable chunking
        HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
        httpClientPolicy.setAllowChunking(enableChunking);
        // set timeout
        httpClientPolicy.setConnectionTimeout(CONN_TIMEOUT);
        httpClientPolicy.setReceiveTimeout(READ_TIMEOUT);
        httpClientPolicy.setAutoRedirect(true);
        // set the policy into the http conduit
        http.setClient(httpClientPolicy);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(
                    "({})getClient(address={}, service={}, enableMtom={}, "
                            + "enableChunking={}, connectionTimeout={}, receiveTimeout={})",
                service.getSimpleName(), address, service.getName(),
                enableMtom, enableChunking, CONN_TIMEOUT, READ_TIMEOUT);
        }
        return (T) requester;
    }
}
