/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.impl;

import gov.epa.cef.web.domain.NaicsCodeIndustry;
import gov.epa.cef.web.repository.NaicsCodeIndustryRepository;
import gov.epa.cef.web.service.NaicsCodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class NaicsCodeServiceImpl implements NaicsCodeService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private NaicsCodeIndustryRepository naicsIndustryRepo;

    /**
     * Retrieve all naics code industries
     */
    public List<NaicsCodeIndustry> getNaicsIndustries() {
        Iterable<NaicsCodeIndustry> entities = naicsIndustryRepo.findAll();

        List<NaicsCodeIndustry> result = new ArrayList<>();
        entities.forEach(result::add);

        return result;
    }
}
