/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.validation;

import com.baidu.unbiz.fluentvalidator.Validator;
import com.baidu.unbiz.fluentvalidator.ValidatorChain;
import com.baidu.unbiz.fluentvalidator.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This Registry works with the Spring context to lookup validators (ala service locator pattern)
 * Using Spring for lookups allows dependency injection of service/repository layer
 * into the validators
 */
@Component
public class ValidationRegistry implements Registry {

    private final ApplicationContext applicationContext;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    ValidationRegistry(ApplicationContext applicationContext) {

        this.applicationContext = applicationContext;
    }

    public <T extends Validator> ValidatorChain createValidatorChain(Class<T> type) {

        ValidatorChain chain = new ValidatorChain();

        List<Validator> validators = new ArrayList<>(findByType(type));

        chain.setValidators(validators);

        return chain;
    }

    @Override
    public <T> List<T> findByType(Class<T> aClass) {

        logger.debug("Finding {} validators", aClass.getSimpleName());

        Map<String, T> map =
            BeanFactoryUtils.beansOfTypeIncludingAncestors(this.applicationContext, aClass);

        return new ArrayList<>(map.values());
    }

    public <T extends Validator> T findOneByType(Class<T> aClass) {

        logger.trace("Finding {} validators", aClass.getSimpleName());

        return findByType(aClass).stream()
            .findFirst()
            .orElseThrow(() ->
                new IllegalArgumentException(String.format("Class %s does not exist.", aClass.getSimpleName())));
    }
}
