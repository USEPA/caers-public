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

import gov.epa.cef.web.service.PropertyService;
import gov.epa.cef.web.service.dto.PropertyDto;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.springframework.stereotype.Service;

@Service
public class PropertyServiceImpl implements PropertyService {

   public PropertyDto sanitizeHtml(PropertyDto prop) {
	   
	   PolicyFactory pf = new HtmlPolicyBuilder()
			   				.allowElements(
			   						"b", "br", "em", "h1", "h2", "h3", "h4", "h5", "h6",
	   								"i", "li", "mark", "ol", "p", "strong", "span", "u", "ul"
			   				).toFactory();
	   
	   pf.sanitize(prop.getValue());
	   
	   prop.setValue(pf.sanitize(prop.getValue()));
	   
	   return prop;
   }
}
