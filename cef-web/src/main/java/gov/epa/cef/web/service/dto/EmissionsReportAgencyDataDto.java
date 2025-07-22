package gov.epa.cef.web.service.dto;

import java.io.Serializable;
import java.util.List;

public class EmissionsReportAgencyDataDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private CodeLookupDto programSystemCode;
    private List<Integer> years;

    public CodeLookupDto getProgramSystemCode() {
        return programSystemCode;
    }
    public void setProgramSystemCode(CodeLookupDto programSystemCode) {
        this.programSystemCode = programSystemCode;
    }

    public List<Integer> getYears() {
        return years;
    }
    public void setYears(List<Integer> years) {
        this.years = years;
    }

    public EmissionsReportAgencyDataDto withYears(final List<Integer> years) {

        setYears(years);
        return this;
    }

    public EmissionsReportAgencyDataDto withProgramSystemCode(CodeLookupDto programSystemCode) {

        setProgramSystemCode(programSystemCode);
        return this;
    }

}
