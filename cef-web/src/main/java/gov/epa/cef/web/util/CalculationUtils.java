/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Constant;
import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.mXparser;
import gov.epa.cef.web.domain.EmissionFormulaVariable;
import gov.epa.cef.web.exception.CalculationException;

public class CalculationUtils {

//    private static final Logger logger = LoggerFactory.getLogger(CalculationUtils.class);

    private static final String FT3 = "ft3 = [ft] * [ft] * [ft]";
    private static final String STON = "sTon = 2000 * [lb]";
    private static final String BTU = "btu = 1055.05585 * [J]";
    private static final String W = "w = [J] / [s]";
    private static final String HP = "hp = 745.69987158 * [J] / [s]";
    // Standard blue BBL is 42 GAL
    private static final String BBL = "bbl = 42 * [gall]";
    private static final String YEAR = "year = 365 * [day]";
    private static final String LEAP_YEAR = "year = 366 * [day]";
    public static final int EMISSIONS_PRECISION = 6;

    public static BigDecimal convertMassUnits(BigDecimal sourceValue, MassUomConversion sourceUnit, MassUomConversion targetUnit) {
        BigDecimal result = sourceValue.multiply(sourceUnit.conversionFactor()).divide(targetUnit.conversionFactor());
        return result;
    }

    public static BigDecimal calculateEmissionFormula(String formula, List<EmissionFormulaVariable> inputs) {

        List<Constant> variables = inputs.stream().map(input -> {
            return new Constant(input.getVariableCode().getCode(), input.getValue().doubleValue());
        }).collect(Collectors.toList());

        // EPA emission factor formula variable C conflicts with mXparser built-in Token for Physical Constant
        // removing built-in token to prevent error when selected emission factor formula contains C variable.
        if (formula.contains("C") && !Arrays.asList(mXparser.getBuiltinTokensToRemove()).contains("C")) {
        	mXparser.removeBuiltinTokens("C");
        }

        Expression e = new Expression(formula);
        e.addConstants(variables);

        if (!e.checkSyntax()) {
            throw new CalculationException(Arrays.asList(e.getMissingUserDefinedArguments()));
        }

        return BigDecimal.valueOf(e.calculate());
    }

    public static BigDecimal convertUnits(String sourceFormula, String targetFormula) {
        return convertUnits(sourceFormula, targetFormula, false);
    }

    public static BigDecimal convertUnits(String sourceFormula, String targetFormula, boolean leapYear) {

        String formula = "(1) * (" + sourceFormula + ") / (" + targetFormula + ")";
        Expression e = new Expression(formula);
        e.addArguments(new Argument(FT3), new Argument(STON), new Argument(BTU), new Argument(W), new Argument(HP), new Argument(BBL));

        if (leapYear) {
            e.addArguments(new Argument(LEAP_YEAR));
        } else {
            e.addArguments(new Argument(YEAR));
        }

//        logger.info(formula);

        return BigDecimal.valueOf(e.calculate());
    }

    /**
     * Return a BigDecimal that has a maximum number of significant figures.  If the current value already has less than or equal the max significant figures
     * then the same number will be returned, trailing 0's will not be added.
     */
    public static BigDecimal setSignificantFigures(BigDecimal currentValue, int maxSignificantFigures) {
        if (currentValue.precision() > maxSignificantFigures) {
            int newScale = maxSignificantFigures - currentValue.precision() + currentValue.scale();
            currentValue = currentValue.setScale(newScale, RoundingMode.HALF_UP);
        }

        return currentValue;
    }

    public static String formatEmissionFactorOrRate(BigDecimal emissionFactorOrRate, String numeratorUoM, String denominatorUoM) {
        if (emissionFactorOrRate != null && numeratorUoM != null && denominatorUoM != null) {
            return emissionFactorOrRate + " " + numeratorUoM + "/" + denominatorUoM;
        } else if (emissionFactorOrRate != null && (numeratorUoM == null || denominatorUoM == null)) {
            return emissionFactorOrRate.toString();
        } else {
            return null;
        }
    }

}
