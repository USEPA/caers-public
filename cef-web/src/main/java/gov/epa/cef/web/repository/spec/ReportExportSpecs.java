package gov.epa.cef.web.repository.spec;

import gov.epa.cef.web.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class ReportExportSpecs {

    public static Specification<EmissionsReport> forReportYear(Integer year) {
        return (root, query, builder) -> {
            return builder.equal(root.get(EmissionsReport_.year), year);
        };
    }

    public static Specification<EmissionsReport> afterModifiedDate(Date date) {
        return (root, query, builder) -> {
            return builder.greaterThanOrEqualTo(root.get(EmissionsReport_.LAST_MODIFIED_DATE), date);
        };
    }

    public static Specification<EmissionsReport> beforeModifiedDate(Date date) {
        return (root, query, builder) -> {
            return builder.lessThanOrEqualTo(root.get(EmissionsReport_.LAST_MODIFIED_DATE), date);
        };
    }

    public static Specification<EmissionsReport> afterCertifiedDate(Date date) {
        return certifiedAction().and((root, query, builder) -> {
            return builder.greaterThanOrEqualTo(root.join(EmissionsReport_.reportHistory).get(ReportHistory_.ACTION_DATE), date);
        });
    }

    public static Specification<EmissionsReport> beforeCertifiedDate(Date date) {
        return certifiedAction().and((root, query, builder) -> {
            return builder.lessThanOrEqualTo(root.join(EmissionsReport_.reportHistory).get(ReportHistory_.ACTION_DATE), date);
        });
    }

    public static Specification<EmissionsReport> certifiedAction() {
        return (root, query, builder) -> {
            return builder.equal(root.join(EmissionsReport_.reportHistory).get(ReportHistory_.REPORT_ACTION), ReportAction.SUBMITTED);
        };
    }

    public static Specification<EmissionsReport> forSlt(String slt) {
        return (root, query, builder) -> {
            return builder.equal(root.get(EmissionsReport_.programSystemCode).get(ProgramSystemCode_.CODE), slt);
        };
    }

    public static Specification<EmissionsReport> forAgencyFacilityId(List<String> facilities) {
        return (root, query, builder) -> {
            return root.get(EmissionsReport_.masterFacilityRecord).get(MasterFacilityRecord_.agencyFacilityIdentifier).in(facilities);
        };
    }

    public static Specification<EmissionsReport> forReportId(List<String> reports) {
        return (root, query, builder) -> {
            return root.get(EmissionsReport_.ID).in(reports);
        };
    }

    public static Specification<EmissionsReport> forStatuses(List<ReportStatus> statuses) {
        return (root, query, builder) -> {
            return root.get(EmissionsReport_.STATUS).in(statuses);
        };
    }
}
