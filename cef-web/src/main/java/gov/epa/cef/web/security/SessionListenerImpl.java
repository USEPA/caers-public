/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.UUID;

public class SessionListenerImpl implements HttpSessionListener {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void sessionCreated(HttpSessionEvent se) {

        String uuid = UUID.randomUUID().toString();
        se.getSession().setAttribute(SessionKey.SessionUuid.key(), uuid);
        logger.debug("HttpSession {} created.", uuid);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {

        HttpSession session = se.getSession();

        String uuid = (String) session.getAttribute(SessionKey.SessionUuid.key());
        Long userRoleId = (Long) session.getAttribute(SessionKey.UserRoleId.key());

        if (userRoleId != null) {

            logger.debug("Pulled userRoleId {} from HttpSession {}.", userRoleId, uuid);

            WebApplicationContext context =
                WebApplicationContextUtils.getRequiredWebApplicationContext(session.getServletContext());

            context.getBean(SecurityService.class).evictUserCachedItems(userRoleId);

        } else {

            logger.warn("No UserRoleId found in HttpSession{}. No cache items were evicted.", uuid);
        }
    }
}
