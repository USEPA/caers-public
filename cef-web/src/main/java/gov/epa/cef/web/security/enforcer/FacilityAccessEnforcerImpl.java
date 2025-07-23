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

import com.google.common.base.Preconditions;
import gov.epa.cef.web.exception.FacilityAccessException;
import gov.epa.cef.web.exception.NotExistException;
import gov.epa.cef.web.repository.FacilitySiteRepository;
import gov.epa.cef.web.repository.ProgramIdRetriever;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class FacilityAccessEnforcerImpl implements FacilityAccessEnforcer {

    private final Collection<Long> authorizedMasterIds;

    private final ProgramIdRepoLocator repoLocator;

    public FacilityAccessEnforcerImpl(ProgramIdRepoLocator repoLocator,
                                      Collection<Long> authorizedMasterIds) {

        this.repoLocator = repoLocator;
        this.authorizedMasterIds = authorizedMasterIds;
    }

    @Override
    public <T extends ProgramIdRetriever> void enforceEntities(Collection<Long> ids, Class<T> repoClazz) {

        ProgramIdRetriever repo = repoLocator.getProgramIdRepository(repoClazz);

        enforceMasterIds(ids.stream()
            .map(id -> {

                return repo.retrieveMasterFacilityRecordIdById(id)
                    .orElseThrow(() -> {

                        String entity = repoClazz.getSimpleName().replace("Repository", "");
                        return new NotExistException(entity, id);
                    });
            })
            .collect(Collectors.toList()));
    }

    @Override
    public <T extends ProgramIdRetriever> void enforceEntity(Long id, Class<T> repoClazz) {

    	Preconditions.checkArgument(id != null,"ID for %s repository can not be null.", repoClazz.getSimpleName());

        enforceEntities(Collections.singletonList(id), repoClazz);
    }

    @Override
    public void enforceFacilitySite(Long id) {

        enforceEntity(id, FacilitySiteRepository.class);
    }

    @Override
    public void enforceFacilitySites(Collection<Long> ids) {

        enforceEntities(ids, FacilitySiteRepository.class);
    }

    @Override
    public void enforceMasterId(Long id) {

        enforceMasterIds(Collections.singletonList(id));
    }

    @Override
    public Collection<Long> getAuthorizedMasterIds() {

        return Collections.unmodifiableCollection(this.authorizedMasterIds);
    }

    @Override
    public void enforceMasterIds(Collection<Long> ids) {

        Collection<String> unauthorized = ids.stream()
            .filter(p -> this.authorizedMasterIds.contains(p) == false)
            .map(p -> p.toString())
            .collect(Collectors.toList());

        if (unauthorized.size() > 0) {

            throw new FacilityAccessException(unauthorized);
        }
    }
}
