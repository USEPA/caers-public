/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
