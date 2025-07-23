--facility
CREATE TABLE facility
(
  id                    SERIAL PRIMARY KEY,
  name					VARCHAR(255) NOT NULL,
  state                 VARCHAR(2) NOT NULL,
  county				VARCHAR(255),
  naics					VARCHAR(255),
  eis_id				NUMERIC(19),
  current_tons			NUMERIC(6),
  previous_tons			NUMERIC(6),
  created_by            VARCHAR(255),
  created_date          timestamp NOT NULL,
  last_modified_by      VARCHAR(255),
  last_modified_date    timestamp NOT NULL
);