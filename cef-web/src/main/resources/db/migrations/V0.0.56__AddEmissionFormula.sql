ALTER TABLE emission ADD COLUMN formula_indicator boolean NOT NULL DEFAULT false;

ALTER TABLE emission ADD COLUMN emissions_factor_formula character varying(100);