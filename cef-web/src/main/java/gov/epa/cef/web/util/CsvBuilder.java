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

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gov.epa.cef.web.annotation.CsvColumn;
import gov.epa.cef.web.annotation.CsvFileName;

/**
 * Encapsulation of CSV builder logic to create a CSV formatted string based on a generic type
 *
 * @param <T> Type of data being used to generate the data in the CSV
 */
public class CsvBuilder<T> {	
	
	private Class<T> klass;
	private List<T> csvRows;
	
	public CsvBuilder(Class<T> klass, List<T> csvRows) {
		this.klass = klass;
		this.csvRows = csvRows;
	}
	
	
	/**
	 * Create a StringBuilder that is formatted as a CSV file with a header row based on methods with the CsvColumn annotation
	 * 
	 * @return StringBuilder representing a CSV file
	 */
	public StringBuilder build() {
		StringBuilder sb = new StringBuilder();
		List<Method> csvColList = getMethodsWithAnnotation(CsvColumn.class);
		csvColList.sort((Method m1, Method m2) -> m1.getAnnotation(CsvColumn.class).order()-m2.getAnnotation(CsvColumn.class).order());
		
		createCsvHeaders(sb, csvColList);
		createCsvDataRows(sb, csvColList);
		
		return sb;
	}
	
	
	/**
	 * Retrieve the file name to be used for the CSV file
	 * 
	 * @return CSV file name; defaults to file.csv if the CsvFileName doesn't exist in this class' generic type
	 */
	public String fileName() {
		if (this.klass.isAnnotationPresent(CsvFileName.class)) {
			CsvFileName fileNameAnnotation = this.klass.getAnnotation(CsvFileName.class);
			if (fileNameAnnotation.name() != null && fileNameAnnotation.name() != "") {
				return fileNameAnnotation.name();
			}
		}
		return "file.csv";
	}
	
	
	/**
	 * Iterate though the list of methods declared in the class represented by klass variable, and add those annotated with the specified annotation
	 * 
	 * @param annotation The type of annotation being searched for within the class's methods
	 * @return A list of methods that have the specified annotation
	 */
	private List<Method> getMethodsWithAnnotation(Class<? extends Annotation> annotation) {
	    final List<Method> methods = new ArrayList<Method>();
	    final List<Method> allMethods = new ArrayList<Method>(Arrays.asList(this.klass.getDeclaredMethods()));
	    for (final Method method : allMethods) {
	        if (method.isAnnotationPresent(annotation)) {
        		methods.add(method);
	        }
	    }
	    return methods;
	}
	
	
	/**
	 * Add comma delimited headers to the StringBuilder for each method in the csvColList
	 */
	private void createCsvHeaders(StringBuilder sb, List<Method> csvColList) {
		//add headers for CSV
		for (Method csvCol : csvColList) {
			try {
				CsvColumn colAnnotation = csvCol.getAnnotation(CsvColumn.class);			
				sb.append("\"").append(escape(colAnnotation.name())).append("\",");
			} catch (NullPointerException npe) {
				sb.append("\"").append(escape(npe.getMessage())).append("\",");
			}
		}
		if (sb.length() > 0) {
			sb.replace(sb.length()-1, sb.length(), "").append("\n");
		}
	}
	
	
	/**
	 * Add comma delimited values to the StringBuilder for each method in the csvColList for each T in csvRows
	 */
	private void createCsvDataRows(StringBuilder sb, List<Method> csvColList) {
		for (T row : this.csvRows) {
			for (Method csvCol : csvColList) {	
				try {
					sb.append("\"").append(escape(csvCol.invoke(row))).append("\",");
				}
				catch (IllegalAccessException iae) {
					sb.append("\"IllegalAccessException: ").append(escape(iae.getMessage())).append("\",");
				}
				catch (IllegalArgumentException argEx) {
					sb.append("\"IllegalArgumentException: ").append(escape(argEx.getMessage())).append("\",");
				}
				catch (InvocationTargetException ite) {
					sb.append("\"InvocationTargetException: ").append(escape(ite.getMessage())).append("\",");
				}
				catch (NullPointerException npe) {
					sb.append("\"NullPointerException: ").append(escape(npe.getMessage())).append("\",");
				}
				catch (ExceptionInInitializerError eiie) {
					sb.append("\"ExceptionInIntializerError: ").append(escape(eiie.getMessage())).append("\",");
				}
			}
			if (sb.length() > 0) {
				sb.replace(sb.length()-1, sb.length(), "").append("\n");
			}
		}
	}
	

	/**
	 * Add escape characters for double quotes contained in the string value of the Object passed to the method
	 */
	private String escape(final Object o) {
		if (o == null) {
			return "";
		} else {
			return o.toString().replaceAll("\"", "\"\"");
		}
	}
}
