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
