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

import gov.epa.cef.web.domain.QualityCheckResults;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface QualityCheckResultsRepository extends JpaRepository<QualityCheckResults, Long> { // TODO: Need reportIdRetriever?

    /**
     * Retrieve QualityCheckResults by emissions report and semiannual
     * @param reportId
     * @param semiannual
     * @return
     */
    @Query("select qcr from QualityCheckResults qcr join qcr.emissionsReport er" +
        " where er.id = :reportId and qcr.semiannual = :semiannual")
    Optional<QualityCheckResults> findByReportIdAndIsSemiannual(long reportId, boolean semiannual);
}
