/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LookupRepositories {

	@Autowired
	private CalculationMaterialCodeRepository materialCodeRepo;

	@Autowired
	private CalculationMethodCodeRepository methodCodeRepo;

	@Autowired
	private UnitTypeCodeRepository UnitTypeCodeRepo;

	@Autowired
	private CalculationParameterTypeCodeRepository paramTypeCodeRepo;

	@Autowired
	private OperatingStatusCodeRepository operatingStatusRepo;

	@Autowired
	private PollutantRepository pollutantRepo;

	@Autowired
	private ReportingPeriodCodeRepository periodCodeRepo;

	@Autowired
	private UnitMeasureCodeRepository uomRepo;

	@Autowired
	private ContactTypeCodeRepository contactTypeRepo;

	@Autowired
    private FipsCountyRepository countyRepo;

	@Autowired
	private FipsStateCodeRepository stateCodeRepo;

	@Autowired
	private ReleasePointTypeCodeRepository releasePtCodeRepo;

	@Autowired
	private ProgramSystemCodeRepository programSystemCodeRepo;

	@Autowired
	private ControlMeasureCodeRepository controlMeasureCodeRepo;

	@Autowired
	private TribalCodeRepository tribalCodeRepo;

	@Autowired
	private AircraftEngineTypeCodeRepository aircraftEngCodeRepo;

	@Autowired
	private PointSourceSccCodeRepository pointSourceSccCodeRepo;

	@Autowired
	private FacilitySourceTypeCodeRepository facilitySourceTypeCodeRepo;

	@Autowired
	private FacilityCategoryCodeRepository facilityCategoryCodeRepo;

    @Autowired
    private WebfireEmissionFactorRepository webfireEmissionFactorRepo;

	public CalculationMaterialCodeRepository materialCodeRepo() {
		return materialCodeRepo;
	}

	public CalculationMethodCodeRepository methodCodeRepo() {
		return methodCodeRepo;
	}

	public CalculationParameterTypeCodeRepository paramTypeCodeRepo() {
		return paramTypeCodeRepo;
	}

	public OperatingStatusCodeRepository operatingStatusRepo() {
		return operatingStatusRepo;
	}

	public PollutantRepository pollutantRepo() {
		return pollutantRepo;
	}

	public ReportingPeriodCodeRepository periodCodeRepo() {
		return periodCodeRepo;
	}

	public UnitMeasureCodeRepository uomRepo() {
		return uomRepo;
	}

	public ContactTypeCodeRepository contactTypeRepo() {
		return contactTypeRepo;
	}

	public FipsCountyRepository countyRepo() {
        return countyRepo;
    }

	public FipsStateCodeRepository stateCodeRepo() {
		return stateCodeRepo;
	}

	public UnitTypeCodeRepository UnitTypeCodeRepo() {
		return UnitTypeCodeRepo;
	}

	public ReleasePointTypeCodeRepository releasePtCodeRepo() {
		return releasePtCodeRepo;
	}

	public ProgramSystemCodeRepository programSystemCodeRepo() {
		return programSystemCodeRepo;
	}

	public ControlMeasureCodeRepository controlMeasureCodeRepo() {
		return controlMeasureCodeRepo;
	}

	public TribalCodeRepository tribalCodeRepo() {
		return tribalCodeRepo;
	}

	public AircraftEngineTypeCodeRepository aircraftEngCodeRepo() {
		return aircraftEngCodeRepo;
	}

	public PointSourceSccCodeRepository pointSourceSccCodeRepo() {
		return pointSourceSccCodeRepo;
	}

	public FacilitySourceTypeCodeRepository facilitySourceTypeCodeRepo() {
		return facilitySourceTypeCodeRepo;
	}

	public FacilityCategoryCodeRepository facilityCategoryCodeRepo() {
		return facilityCategoryCodeRepo;
	}

    public WebfireEmissionFactorRepository webfireEmissionFactorRepo() { return webfireEmissionFactorRepo; }

}
