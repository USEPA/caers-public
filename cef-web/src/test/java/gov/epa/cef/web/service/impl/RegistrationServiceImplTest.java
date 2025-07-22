/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.impl;

import gov.epa.cef.web.client.soap.RegisterFacilityClient;
import gov.epa.cef.web.exception.ApplicationException;
import net.exchangenetwork.wsdl.register.program_facility._1.ProgramFacility;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationServiceImplTest extends BaseServiceTest {

	@Mock
	private RegisterFacilityClient registerFacilityClient;

	@InjectMocks
	private RegistrationServiceImpl registrationServiceImpl;

	private List<ProgramFacility> facilities=new ArrayList<>();

	@Before
	public void init() throws ApplicationException, MalformedURLException {

		when(registerFacilityClient.getFacilitiesByUserRoleId(123L)).thenReturn(facilities);
		when(registerFacilityClient.getFacilitiesByUserRoleId(545L)).thenReturn(new ArrayList<>());

        when(registerFacilityClient.getFacilityByProgramIds(Collections.singletonList("pId")))
            .thenReturn(new ArrayList<>());

        when(registerFacilityClient.getFacilityByProgramIds(Collections.singletonList("pId2")))
            .thenReturn(facilities);
	}

	@Test
	public void retrieveFacilities_Should_ReturnFacilitiesList_When_ValidUserRoleIdPassed() {
		ProgramFacility programFacility=new ProgramFacility();
		programFacility.setFacilityName("Test-Facility");
		facilities.add(programFacility);
		assertEquals(registrationServiceImpl.retrieveFacilities(123L).size()==1, Boolean.TRUE);
	}

	@Test
	public void retrieveFacilities_Should_ReturnEmptyFacilitiesList_When_InvalidUserRoleIdPassed() {
	    assertEquals(registrationServiceImpl.retrieveFacilities(545L).size()==0, Boolean.TRUE);
	}

	@Test(expected = ApplicationException.class)
	public void retrieveFacilities_Should_ThrowException_When_FacilitiesListHasNullValues() {
		facilities.add(null);
		registrationServiceImpl.retrieveFacilities(123L);
	}

	@Test
	public void retrieveFacilityByProgramId_Should_ReturnNull_When_ProgramIdHasNoFacilities() {
		assertEquals(null, registrationServiceImpl.retrieveFacilityByProgramId("pId"));
	}

	@Test
	public void retrieveFacilityByProgramId_Should_ReturnFacilitiesList_When_ProgramIdHasFacilities(){
		ProgramFacility programFacility=new ProgramFacility();
		programFacility.setFacilityName("Test-Facility");
		facilities.add(programFacility);
		assertNotEquals(null, registrationServiceImpl.retrieveFacilityByProgramId("pId2"));
	}

	@Test(expected = ApplicationException.class)
	public void retrieveFacilityByProgramId_Should_ThrowException_When_FacilitiesListHasNullValues(){
		facilities.add(null);
		registrationServiceImpl.retrieveFacilityByProgramId("pId2");
	}

}
