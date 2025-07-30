/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.util;

import gov.epa.cef.web.domain.NaicsCodeType;
import gov.epa.cef.web.domain.SccCategory;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ConstantUtils {

    // VALIDATION MESSAGE BUNDLES
    public static final String FEDERAL_QA_CHECK_BUNDLE = "validation/emissionsreport";
    public static final String REPORT_CREATION_CHANGE_BUNDLE = "validation/reportCreationChanges";

	//OPERATING STATUSES
	public static final String STATUS_OPERATING = "OP";
	public static final String STATUS_PERMANENTLY_SHUTDOWN = "PS";
	public static final String STATUS_TEMPORARILY_SHUTDOWN = "TS";
	public static final String STATUS_ONRE = "ONRE";


	//RELEASE POINT TYPE CODES
	public static final String FUGITIVE_RELEASE_POINT_CATEGORY = "Fugitive";
	public static final String FUGITIVE_RELEASE_PT_AREA_TYPE = "1";
	public static final String FUGITIVE_RELEASE_PT_2D_TYPE = "9";
	public static final String FUGITIVE_RELEASE_PT_3D_TYPE = "7";
	public static final List<String> FUGITIVE_RELEASE_POINT_TYPES = Collections.unmodifiableList(Arrays.asList("1","7","9"));

	//UNIT TYPE CODES
	public static final List<String> DESIGN_CAPACITY_TYPE_CODE_ERROR = Collections.unmodifiableList(Arrays.asList("100", "120", "140", "160", "180"));
    public static final String DESIGN_CAPACITY_TYPE_CODE_DESCRIPTION = "100 (Boiler), 120 (Turbine), 140 (Combined Cycle (Boiler/Gas Turbine)), 160 (Reciprocating IC Engine), 180 (Process Heater)";

    // UNIT TYPE CODES
    public static final List<String> DESIGN_CAPACITY_TYPE_CODE_KILNS_ERROR = Collections.unmodifiableList(Arrays.asList("210", "213", "214"));
    public static final String DESIGN_CAPACITY_TYPE_CODE_KILNS_DESCRIPTION = "(210) Kiln or (213) Wet Kiln or (214) Dry Kiln";

    public static final String UNIT_TYPE_CODE_FURNACE = "200";
    public static final String UNIT_TYPE_CODE_INCINERATOR = "270";

    public static final String NAICS_CEMENT_MANUFACTURING_PREFIX = "32731";

    public static final String NAICS_GLASS_PRODUCT_MANUFACTURING_PREFIX = "32721";

    public static final String NAICS_WASTE_TREATMENT_AND_DISPOSAL_PREFIX = "56221";
	//EIS TRANSMISSION TYPES
	public static final String EIS_TRANSMISSION_FACILITY_INVENTORY = "FacilityInventory";
	public static final String EIS_TRANSMISSION_POINT_EMISSIONS = "Point";

	//LANDFILL FACILITY SOURCE TYPE CODE
	public static final String FACILITY_SOURCE_LANDFILL_CODE = "104";

    //NATURAL GAS MATERIAL TYPE CODE
    public static final String NATURAL_GAS_CODE = "209";

    //INPUT CALCULATION (THROUGHPUT) PARAMETER TYPE CODE
    public static final String INPUT_CALCULATION_PARAMETER_TYPE_CODE = "I";

    //CONTINUOUS EMISSIONS MONITORING SYSTEM (CEMS) CALCULATION METHOD CODE
    public static final String CEMS_CODE = "1";

    //UNIT OF MEASURE CODES
    public static final String E6BTU = "E6BTU";
    public static final String FT3S = "FT3S";

	//NUMBER PATTERNS
	public static final String REGEX_ONE_DECIMAL_PRECISION = "^\\d{0,3}(\\.\\d{1})?$";

	//MONTHLY REPORTING PERIOD TYPES
	public static final String JANUARY = "January";
	public static final String FEBRUARY = "February";
	public static final String MARCH = "March";
	public static final String APRIL = "April";
	public static final String MAY = "May";
	public static final String JUNE = "June";
	public static final String JULY = "July";
	public static final String AUGUST = "August";
	public static final String SEPTEMBER = "September";
	public static final String OCTOBER = "October";
	public static final String NOVEMBER = "November";
	public static final String DECEMBER = "December";
	public static final String SEMIANNUAL = "Semiannual";
    public static final String ANNUAL = "Annual";

    public static final List<String> POLLUTANT_CODES_RIDEM = Collections.unmodifiableList(Arrays.asList("309002","140578","103333","7440393","65850","7440428","15541454",
        "7726956","75274","85687","108171262","106478","75683","95578","95830","76062","95692","7440508","120718","135206","110827","615054","124481","95501","156592",
        "156605","60571","75376","540738","105679","16984488","111308","10035106","67630","7439987","7697372","924163","55185","621647","10595956","100754","930552",
        "7664382","11097691","115071","107982","7783075","7446346","1310732","14808798","630206","25167833","62555","26471625","7440622","7440666"));
    public static final List<String> POLLUTANT_CODES_MEDEP = Collections.unmodifiableList(Arrays.asList("359353","7783542","75376","430660"));
    public static final List<String> CONTROL_POLLUTANT_CODES = Collections.unmodifiableList(Arrays.asList("NOX","PM10-FIL"));
    //ALL MONTHS
    public static final List<String> ALL_MONTHS = Collections.unmodifiableList(Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"));

    public static final List<String> SEMI_ANNUAL_MONTHS = Collections.unmodifiableList(Arrays.asList("January", "February", "March", "April", "May", "June"));
    public static final List<String> SECOND_HALF_MONTHS = Collections.unmodifiableList(Arrays.asList("July", "August", "September", "October", "November", "December"));

    // SEASONAL MONTHS
    public static final List<String> SPRING_MONTHS = Collections.unmodifiableList(Arrays.asList(MARCH, APRIL, MAY));
    public static final List<String> SUMMER_MONTHS = Collections.unmodifiableList(Arrays.asList(JUNE, JULY, AUGUST));
    public static final List<String> FALL_MONTHS = Collections.unmodifiableList(Arrays.asList(SEPTEMBER, OCTOBER, NOVEMBER));
    public static final List<String> WINTER_MONTHS = Collections.unmodifiableList(Arrays.asList(DECEMBER, JANUARY, FEBRUARY));

    // DAYS IN YEAR/HALF YEAR
    public static final int DAYS_IN_YEAR = 365;
    public static final int DAYS_IN_HALF_YEAR = 181;

    public static final List<String> SEMIANNUAL_REPORTING_PERIODS = Collections.unmodifiableList(Arrays.asList("January", "February", "March", "April", "May", "June", "Semiannual"));
    public static final List<String> ALL_REPORTING_PERIODS = Collections.unmodifiableList(Arrays.asList("January", "February", "March", "April", "May", "June", "Semiannual", "July", "August", "September", "October", "November", "December", "Annual"));

    public static final List<String> NAICS_CODE_TYPES = Collections.unmodifiableList(
        Arrays.asList(NaicsCodeType.PRIMARY.label(), NaicsCodeType.SECONDARY.label(), NaicsCodeType.TERTIARY.label()));

    // DEFAULT VALUES
    public static final SccCategory DEFAULT_SCC_CATEGORY = SccCategory.Point; // Used in setter method
}
