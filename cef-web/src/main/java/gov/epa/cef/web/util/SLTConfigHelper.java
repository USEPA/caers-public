/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.epa.cef.web.config.SLTBaseConfig;
import gov.epa.cef.web.config.SLTPropertyName;
import gov.epa.cef.web.config.slt.SLTConfigImpl;
import gov.epa.cef.web.provider.system.SLTPropertyProvider;

@Service
public class SLTConfigHelper {

    private final SLTPropertyProvider propertyProvider;

    @Autowired
    public SLTConfigHelper(SLTPropertyProvider propertyProvider) {

        super();

        this.propertyProvider = propertyProvider;
    }

    //find and return the current SLT Configuration for the user/report that is currently being worked on
    public SLTBaseConfig getCurrentSLTConfig(String slt) {

        return new SLTConfigImpl(slt, propertyProvider);
    }
    
    public List<String> getAllSltByMonthlyEnabled() {
    	List<String> result = propertyProvider.getAllSltWithEnabledProperty(SLTPropertyName.MonthlyFuelReportingEnabled);
    	return result;
    }

}
