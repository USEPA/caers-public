package gov.epa.cef.web.util;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;

import gov.epa.cef.web.domain.EmissionsReport;
import gov.epa.cef.web.domain.ReportChange;
import gov.epa.cef.web.domain.ReportChangeType;
import gov.epa.cef.web.domain.common.IReportComponent;
import gov.epa.cef.web.service.dto.ReportChangeDto;
import gov.epa.cef.web.service.validation.ValidationField;

public class ReportCreationContext {
    
    private final List<ReportChangeDto> changes;

    private final ResourceBundle resourceBundle;

    public ReportCreationContext(String bundleName) {


        this.resourceBundle = ResourceBundle.getBundle(bundleName,
            Locale.getDefault(), Thread.currentThread().getContextClassLoader());

        Preconditions.checkNotNull(this.resourceBundle,
            "Unable to find resource bundle %s.", bundleName);

        this.changes = new ArrayList<>();

    }

    public List<ReportChange> buildReportChanges(EmissionsReport report) {

        return changes.stream().map(c -> {
            if (c.getComponent() != null) {
                // create component details now that ids are generated
                return new ReportChange(report, c.getMessage(), c.getField(), c.getType(), c.getComponent().getComponentDetails());
            } else {
                return new ReportChange(report, c.getMessage(), c.getField(), c.getType(), null);
            }
        }).collect(Collectors.toList());
    }

    public void addCreationChange(ValidationField field, String messageKey, IReportComponent component, ReportChangeType type, Object... args) {

        // use bundle to create warning message and set error code to warning
        changes.add(createCreationChange(field.value(), messageKey, component, type, args));
    }

    private ReportChangeDto createCreationChange(String field, String messageKey, IReportComponent component, ReportChangeType type, Object... args) {

        if (this.resourceBundle.containsKey(messageKey) == false) {
            String msg = String.format("Validation Message Key %s does not exist in %s.properties file.",
                    messageKey, this.resourceBundle.getBaseBundleName());
            throw new IllegalArgumentException(msg);
        }

        ReportChangeDto result = new ReportChangeDto().withField(field).withType(type).withComponent(component);

        String msg = this.resourceBundle.getString(messageKey);
        if (args != null && args.length > 0) {

            result.setMessage(MessageFormat.format(msg, args));

        } else {

            result.setMessage(msg);
        }

        return result;
    }

}
