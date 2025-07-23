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
