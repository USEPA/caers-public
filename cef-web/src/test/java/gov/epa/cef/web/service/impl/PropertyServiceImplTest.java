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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import gov.epa.cef.web.config.AppPropertyName;
import gov.epa.cef.web.service.dto.PropertyDto;

import static org.junit.Assert.assertEquals;


@RunWith(MockitoJUnitRunner.Silent.class)
public class PropertyServiceImplTest extends BaseServiceTest {

    @InjectMocks
    private PropertyServiceImpl propServiceImpl;

    @Test
    public void testDataSanitization() {
    	
    	String unsafeHtml = "<p onclick=\"alert('hello')\"><strong>test test </strong><em>test</em></p>" + 
    						"<script>document.getElementById(\"element\").innerHTML = \"Hello JavaScript!\";</script>";
    	
    	String expectedSafeHtml = "<p><strong>test test </strong><em>test</em></p>";
    	
    	PropertyDto dto = new PropertyDto();
    	dto.setName(AppPropertyName.FeatureAnnouncementText.configKey());
    	dto.setValue(unsafeHtml);
    	
    	propServiceImpl.sanitizeHtml(dto);
    	
    	assertEquals(dto.getValue(), expectedSafeHtml);
    	
    }

}
