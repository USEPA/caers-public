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
package gov.epa.cef.web.service;

import java.io.OutputStream;

import gov.epa.cef.web.service.dto.EisSubmissionStatus;
import net.exchangenetwork.schema.cer._2._0.CERSDataType;

public interface CersXmlService {

    /**
     * Generate the CERS V2.0 XML classes for the specified emissions report
     * @param reportId
     * @return
     */
    CERSDataType generateCersV2Data(Long reportId, EisSubmissionStatus submissionStatus);

    /**
     * Generate XML from the CERS XML V2.0 classes for the specified emissions report
     * @param reportId
     * @param oututStream
     * @param submissionType
     * @return
     */
    void writeCersV2XmlTo(long reportId, OutputStream outputStream, EisSubmissionStatus submissionStatus);

}
