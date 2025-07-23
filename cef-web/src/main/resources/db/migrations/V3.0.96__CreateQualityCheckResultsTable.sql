CREATE TABLE IF NOT EXISTS quality_check_results
(
    id BIGSERIAL PRIMARY KEY,
    semiannual BOOLEAN NOT NULL,
    federal_errors_count INTEGER NOT NULL DEFAULT 0,
    federal_warnings_count INTEGER NOT NULL DEFAULT 0,
    state_errors_count INTEGER NOT NULL DEFAULT 0,
    state_warnings_count INTEGER NOT NULL DEFAULT 0,
    facility_inventory_changes_count INTEGER NOT NULL DEFAULT 0,
    report_id BIGINT NOT NULL REFERENCES emissions_report,
    created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    last_modified_date TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    last_modified_by CHARACTER VARYING(255) NOT NULL,
    created_by CHARACTER VARYING(255) NOT NULL,
    qa_results_status CHARACTER VARYING(255) NOT NULL,

    current_results JSONB,

    UNIQUE (semiannual, report_id)
);
