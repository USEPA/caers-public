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
package gov.epa.cef.web.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;

public class WebUtils {

	protected static final long  GIGABYTE = 1024L * 1024L *1024L;
	
	/**
	 * Use the data in the csvBuilder to create a CSV file and copy it to the HTTP response
	 * @param response
	 * @param csvBuilder
	 */
	public static void WriteCsv(HttpServletResponse response, CsvBuilder<?> csvBuilder) {

    	try (InputStream inputStream = new ByteArrayInputStream(csvBuilder.build().toString().getBytes()); OutputStream outputStream = response.getOutputStream()) {
			response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
			response.setHeader("Content-Disposition", String.format("attachment; filename=%s", csvBuilder.fileName()));
			response.setHeader("Content-Length", String.valueOf(csvBuilder.build().toString().length()));
			if(csvBuilder.build().toString().length() > GIGABYTE*2) {
				IOUtils.copyLarge(inputStream, outputStream);
			}else {
				IOUtils.copy(inputStream, outputStream);
			}
		} catch (Exception ex) {
			throw new RuntimeException("There is an error writing the CSV:\n\n", ex);
		}

	}
	
}
