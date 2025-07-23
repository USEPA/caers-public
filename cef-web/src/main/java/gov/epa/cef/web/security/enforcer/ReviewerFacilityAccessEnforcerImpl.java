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
package gov.epa.cef.web.security.enforcer;

import gov.epa.cef.web.repository.ProgramIdRetriever;

import java.util.Collection;

public class ReviewerFacilityAccessEnforcerImpl implements FacilityAccessEnforcer {

    @Override
    public <T extends ProgramIdRetriever> void enforceEntities(Collection<Long> ids, Class<T> repositoryClazz) {
      //Reviewer should be able to access everything
    }

    @Override
    public <T extends ProgramIdRetriever> void enforceEntity(Long id, Class<T> repositoryClazz) {
        //Reviewer should be able to access everything
    }

    @Override
    public Collection<Long> getAuthorizedMasterIds() {

        return null;
    }

    @Override
    public void enforceFacilitySite(Long id) {
        //Reviewer should be able to access everything
    }

    @Override
    public void enforceFacilitySites(Collection<Long> ids) {
        //Reviewer should be able to access everything
    }

    @Override
    public void enforceMasterId(Long id) {
      //Reviewer should be able to access everything
    }

    @Override
    public void enforceMasterIds(Collection<Long> ids) {
      //Reviewer should be able to access everything
    }
}
