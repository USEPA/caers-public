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
package gov.epa.cef.web.security;

import com.google.common.collect.Sets;
import gov.epa.cef.web.exception.FacilityAccessException;
import gov.epa.cef.web.exception.NotExistException;
import gov.epa.cef.web.repository.ControlRepository;
import gov.epa.cef.web.repository.EmissionRepository;
import gov.epa.cef.web.repository.EmissionsReportRepository;
import gov.epa.cef.web.repository.EmissionsUnitRepository;
import gov.epa.cef.web.repository.ProgramIdRetriever;
import gov.epa.cef.web.repository.ReleasePointRepository;
import gov.epa.cef.web.repository.ReportingPeriodRepository;
import gov.epa.cef.web.security.enforcer.FacilityAccessEnforcerImpl;
import gov.epa.cef.web.security.enforcer.ProgramIdRepoLocator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FacilityAccessEnforcerTest extends BaseSecurityTest {

    @Mock
    ProgramIdRepoLocator programIdRepoLocator;

    @Mock
    ProgramIdRetriever programIdRetriever;

    @Test(expected = Test.None.class /* no exception expected */)
    public void enforceEntities_Authorized() {

        when(programIdRetriever.retrieveMasterFacilityRecordIdById(2L)).thenReturn(Optional.of(2L));
        when(programIdRetriever.retrieveMasterFacilityRecordIdById(4L)).thenReturn(Optional.of(4L));

        Set<Long> authorized = Sets.newHashSet(1L, 2L, 3L, 4L);

        createEnforcer(authorized).enforceEntities(Arrays.asList(2L, 4L), EmissionsReportRepository.class);
    }

    @Test(expected = FacilityAccessException.class)
    public void enforceEntities_NotAuthorized() {

        when(programIdRetriever.retrieveMasterFacilityRecordIdById(2L)).thenReturn(Optional.of(2L));
        when(programIdRetriever.retrieveMasterFacilityRecordIdById(4L)).thenReturn(Optional.of(4L));
        when(programIdRetriever.retrieveMasterFacilityRecordIdById(5L)).thenReturn(Optional.of(5L));

        Set<Long> authorized = Sets.newHashSet(1L, 2L, 3L, 4L);

        createEnforcer(authorized).enforceEntities(Arrays.asList(2L, 4L, 5L), ReportingPeriodRepository.class);
    }

    @Test(expected = NotExistException.class)
    public void enforceEntities_NotExist() {

        when(programIdRetriever.retrieveMasterFacilityRecordIdById(2L)).thenReturn(Optional.of(2L));
        when(programIdRetriever.retrieveMasterFacilityRecordIdById(4L)).thenReturn(Optional.of(4L));
        when(programIdRetriever.retrieveMasterFacilityRecordIdById(6L)).thenReturn(Optional.empty());

        Set<Long> authorized = Sets.newHashSet(1L, 2L, 3L, 4L);

        createEnforcer(authorized).enforceEntities(Arrays.asList(2L, 4L, 6L), EmissionRepository.class);
    }

    @Test(expected = Test.None.class)
    public void enforceEntity_Authorized() {

        when(programIdRetriever.retrieveMasterFacilityRecordIdById(4L)).thenReturn(Optional.of(4L));

        Set<Long> authorized = Sets.newHashSet(1L, 2L, 3L, 4L);

        createEnforcer(authorized).enforceEntity(4L, ReleasePointRepository.class);
    }

    @Test(expected = FacilityAccessException.class)
    public void enforceEntity_NotAuthorized() {

        when(programIdRetriever.retrieveMasterFacilityRecordIdById(5L)).thenReturn(Optional.of(5L));

        Set<Long> authorized = Sets.newHashSet(1L, 2L, 3L, 4L);

        createEnforcer(authorized).enforceEntity(5L, EmissionsUnitRepository.class);
    }

    @Test(expected = NotExistException.class)
    public void enforceEntity_NotExist() {

        when(programIdRetriever.retrieveMasterFacilityRecordIdById(6L)).thenReturn(Optional.empty());

        Set<Long> authorized = Sets.newHashSet(1L, 2L, 3L, 4L);

        createEnforcer(authorized).enforceEntity(6L, ControlRepository.class);
    }


    @Test(expected = Test.None.class)
    public void enforceFacilitySite_Authorized() {

        when(programIdRetriever.retrieveMasterFacilityRecordIdById(4L)).thenReturn(Optional.of(4L));

        Set<Long> authorized = Sets.newHashSet(1L, 2L, 3L, 4L);

        createEnforcer(authorized).enforceFacilitySite(4L);
    }

    @Test(expected = FacilityAccessException.class)
    public void enforceFacilitySite_NotAuthorized() {

        when(programIdRetriever.retrieveMasterFacilityRecordIdById(5L)).thenReturn(Optional.of(5L));

        Set<Long> authorized = Sets.newHashSet(1L, 2L, 3L, 4L);

        createEnforcer(authorized).enforceFacilitySite(5L);
    }

    @Test(expected = NotExistException.class)
    public void enforceFacilitySite_NotExist() {

        when(programIdRetriever.retrieveMasterFacilityRecordIdById(6L)).thenReturn(Optional.empty());

        Set<Long> authorized = Sets.newHashSet(1L, 2L, 3L, 4L);

        createEnforcer(authorized).enforceFacilitySite(6L);
    }

    @Test(expected = Test.None.class)
    public void enforceFacilitySites_Authorized() {

        when(programIdRetriever.retrieveMasterFacilityRecordIdById(2L)).thenReturn(Optional.of(2L));
        when(programIdRetriever.retrieveMasterFacilityRecordIdById(4L)).thenReturn(Optional.of(4L));

        Set<Long> authorized = Sets.newHashSet(1L, 2L, 3L, 4L);

        createEnforcer(authorized).enforceFacilitySites(Arrays.asList(2L, 4L));
    }

    @Test(expected = FacilityAccessException.class)
    public void enforceFacilitySites_NotAuthorized() {

        when(programIdRetriever.retrieveMasterFacilityRecordIdById(2L)).thenReturn(Optional.of(2L));
        when(programIdRetriever.retrieveMasterFacilityRecordIdById(4L)).thenReturn(Optional.of(4L));
        when(programIdRetriever.retrieveMasterFacilityRecordIdById(5L)).thenReturn(Optional.of(5L));

        Set<Long> authorized = Sets.newHashSet(1L, 2L, 3L, 4L);

        createEnforcer(authorized).enforceFacilitySites(Arrays.asList(2L, 4L, 5L));
    }

    @Test(expected = NotExistException.class)
    public void enforceFacilitySites_NotExist() {

        when(programIdRetriever.retrieveMasterFacilityRecordIdById(2L)).thenReturn(Optional.of(2L));
        when(programIdRetriever.retrieveMasterFacilityRecordIdById(4L)).thenReturn(Optional.of(4L));
        when(programIdRetriever.retrieveMasterFacilityRecordIdById(6L)).thenReturn(Optional.empty());

        Set<Long> authorized = Sets.newHashSet(1L, 2L, 3L, 4L);

        createEnforcer(authorized).enforceFacilitySites(Arrays.asList(2L, 4L, 6L));
    }

    @Test(expected = Test.None.class)
    public void enforceProgramId_Authorized() {

        Set<Long> authorized = Sets.newHashSet(1L, 2L, 3L, 4L);

        createEnforcer(authorized).enforceMasterId(4L);
    }

    @Test(expected = FacilityAccessException.class)
    public void enforceProgramId_NotAuthorized() {

        Set<Long> authorized = Sets.newHashSet(1L, 2L, 3L, 4L);

        createEnforcer(authorized).enforceMasterId(5L);
    }

    @Test(expected = Test.None.class)
    public void enforceProgramIds_Authorized() {

        Set<Long> authorized = Sets.newHashSet(1L, 2L, 3L, 4L);
        Set<Long> goodcheck = Sets.newHashSet(2L, 4L);

        createEnforcer(authorized).enforceMasterIds(goodcheck);
    }

    @Test(expected = FacilityAccessException.class)
    public void enforceProgramIds_NotAuthorized() {

        Set<Long> authorized = Sets.newHashSet(1L, 2L, 3L, 4L);
        Set<Long> badcheck = Sets.newHashSet(2L, 4L, 5L, 6L, 7L);

        createEnforcer(authorized).enforceMasterIds(badcheck);
    }

    private FacilityAccessEnforcerImpl createEnforcer(Collection<Long> masterIds) {

        when(this.programIdRepoLocator.getProgramIdRepository(any()))
            .thenReturn(this.programIdRetriever);

        return new FacilityAccessEnforcerImpl(this.programIdRepoLocator, masterIds);
    }
}
