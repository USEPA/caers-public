
ALTER TABLE emissions_report ADD COLUMN threshold_status character varying(255);

INSERT INTO slt_properties (name, label, description, datatype, required) values (
'slt-feature.gadnr-threshold-screen.enabled','GADNR Threshold Opt-In','Enables the GADNR Threshold Screening Opt-In for users associated with SLT in the CAER application.','boolean', false);
