/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.provider.system;

import com.google.common.base.Splitter;

import gov.epa.cef.web.domain.AdminProperty;
import gov.epa.cef.web.exception.NotExistException;
import gov.epa.cef.web.repository.AdminPropertyRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class AdminPropertyProvider {

    @Autowired
    private AdminPropertyRepository propertyRepo;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public void delete(IPropertyKey propertyKey) {

        String name = propertyKey.configKey();

        logger.info("Deleting system property '{}'", name);

        this.propertyRepo.deleteById(name);
    }

    public BigDecimal getBigDecimal(IPropertyKey propertyKey) {

        String strValue = this.getString(propertyKey);

        BigDecimal result = null;
        try {

            result = new BigDecimal(strValue);

        } catch (NumberFormatException e) {

            String msg = String.format("BigDecimal '%s' is not a valid BigDecimal for '%s'.", strValue, propertyKey.configKey());
            throw new IllegalStateException(msg, e);
        }

        return result;
    }

    public boolean getBoolean(IPropertyKey propertyKey) {

        String strValue = this.getString(propertyKey);

        return Boolean.valueOf(strValue);
    }

    public boolean getBoolean(IPropertyKey propertyKey, boolean defvalue) {

        boolean result = defvalue;

        String name = propertyKey.configKey();

        if (exists(name)) {


            result = this.getBoolean(propertyKey);
        }

        return result;
    }

    public Integer getInt(IPropertyKey propertyKey) {

        String strValue = this.getString(propertyKey);

        Integer result = null;
        try {

            result = Integer.valueOf(strValue);

        } catch (NumberFormatException e) {

            String msg = String.format("Integer '%s' is not a valid Integer for '%s'.", strValue, propertyKey.configKey());
            throw new IllegalStateException(msg, e);
        }

        return result;
    }

    public Integer getInt(IPropertyKey propertyKey, Integer defvalue) {

        Integer result = defvalue;

        String name = propertyKey.configKey();

        if (exists(name)) {

            result = this.getInt(propertyKey);
        }

        return result;
    }

    public LocalDate getLocalDate(IPropertyKey propertyKey) {

        String strValue = this.getString(propertyKey);

        LocalDate result = null;
        try {

            result = LocalDate.parse(strValue, DateTimeFormatter.ISO_LOCAL_DATE);

        } catch (DateTimeParseException e) {

            String msg = String.format("LocalTime '%s' is not a valid LocalTime for '%s'.", strValue, propertyKey.configKey());
            throw new IllegalStateException(msg, e);
        }

        return result;
    }

    public Long getLong(IPropertyKey propertyKey) {

        String strValue = this.getString(propertyKey);

        Long result = null;
        try {

            result = Long.valueOf(strValue);

        } catch (NumberFormatException e) {

            String msg = String.format("Long '%s' is not a valid Long for '%s'.", strValue, propertyKey.configKey());
            throw new IllegalStateException(msg, e);
        }

        return result;
    }

    public Long getLong(IPropertyKey propertyKey, Long defvalue) {

        Long result = defvalue;

        String name = propertyKey.configKey();

        if (exists(name)) {

            result = this.getLong(propertyKey);
        }

        return result;
    }

    public String getString(IPropertyKey propertyKey) {

        String name = propertyKey.configKey();

        AdminProperty property = this.propertyRepo.findById(name).orElseThrow(() -> {

            return new NotExistException("AdminProperty", name);
        });

        return property.getValue();
    }

    public String getString(IPropertyKey propertyKey, String defvalue) {

        String result = defvalue;

        String name = propertyKey.configKey();

        if (exists(name)) {


            result = this.getString(propertyKey);
        }

        return result;
    }

    public List<String> getStringList(IPropertyKey propertyKey) {

        String csv = this.getString(propertyKey);

        List<String> result = Splitter.on(",").trimResults().splitToList(csv);

        return result;
    }

    public URL getUrl(IPropertyKey propertyKey) {

        String strurl = this.getString(propertyKey);

        URL result = null;
        try {

            result = new URL(strurl);

        } catch (MalformedURLException e) {

            String msg = String.format("URL '%s' is not a valid URL for '%s'.", strurl, propertyKey.configKey());
            throw new IllegalStateException(msg, e);
        }

        return result;
    }

    public AdminProperty retrieve(IPropertyKey propertyKey) {

        String name = propertyKey.configKey();

        AdminProperty property = this.propertyRepo.findById(name).orElseThrow(() -> {

            return new NotExistException("AdminProperty", name);
        });

        return property;
    }

    public List<AdminProperty> retrieveAll() {

        return this.propertyRepo.findAll();
    }

    public AdminProperty update(IPropertyKey propertyKey, String value) {

        String name = propertyKey.configKey();

        logger.info("Updating system property '{}' = '{}'", name, value);

        AdminProperty property = this.propertyRepo.findById(name).orElseThrow(() -> {

            return new NotExistException("AdminProperty", name);
        });

        property.setValue(value);

        return this.propertyRepo.save(property);
    }

    private boolean exists(String name) {

        return this.propertyRepo.findById(name).isPresent();
    }
}
