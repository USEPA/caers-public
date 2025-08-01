/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
