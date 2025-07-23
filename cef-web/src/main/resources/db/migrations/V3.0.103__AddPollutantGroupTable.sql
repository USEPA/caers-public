CREATE TABLE IF NOT EXISTS pollutant_group
(
    id bigserial,
    description VARCHAR(200),
    pollutant_code VARCHAR(12),
    CONSTRAINT pollutant_group_pkey PRIMARY KEY (id)
);

INSERT INTO pollutant_group (description, pollutant_code) VALUES ('Chromium Total Grouping', '7440473');
INSERT INTO pollutant_group (description, pollutant_code) VALUES ('Pentachlorobiphenyl', '25429292');
INSERT INTO pollutant_group (description, pollutant_code) VALUES ('Tetrachlorobiphenyl', '26914330');
INSERT INTO pollutant_group (description, pollutant_code) VALUES ('Hexachlorobiphenyl', '26601649');
INSERT INTO pollutant_group (description, pollutant_code) VALUES ('Polychlorinated Biphenyls Total Grouping', '1336363');
INSERT INTO pollutant_group (description, pollutant_code) VALUES ('PAH Grouping', '130498292');

CREATE TABLE IF NOT EXISTS pollutant_pollutant_group
(
    id bigserial,
    pollutant_code VARCHAR(12),
    pollutant_group_id bigint,
    CONSTRAINT pollutant_polluntant_group_pkey PRIMARY KEY (id),
    CONSTRAINT pollutant_pollutant_group_pol_fkey FOREIGN KEY (pollutant_code)
        REFERENCES pollutant (pollutant_code) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT pollutant_pollutant_group_pol_grp_fkey FOREIGN KEY (pollutant_group_id)
        REFERENCES pollutant_group (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

INSERT INTO pollutant_pollutant_group (pollutant_code, pollutant_group_id) SELECT codes.pollutant_code, pg.id FROM pollutant_group pg  JOIN (VALUES('16065831'),('18540299'),('7440473')) AS codes(pollutant_code)
ON pg.description = 'Chromium Total Grouping';

INSERT INTO pollutant_pollutant_group (pollutant_code, pollutant_group_id) SELECT codes.pollutant_code, pg.id FROM pollutant_group pg  JOIN (VALUES('74472370'),('31508006'),('32598144')) AS codes(pollutant_code)
ON pg.description = 'Pentachlorobiphenyl';

INSERT INTO pollutant_pollutant_group (pollutant_code, pollutant_group_id) SELECT codes.pollutant_code, pg.id FROM pollutant_group pg  JOIN (VALUES('32598133')) AS codes(pollutant_code)
ON pg.description = 'Tetrachlorobiphenyl';

INSERT INTO pollutant_pollutant_group (pollutant_code, pollutant_group_id) SELECT codes.pollutant_code, pg.id FROM pollutant_group pg  JOIN (VALUES('52663726'),('38380084')) AS codes(pollutant_code)
ON pg.description = 'Hexachlorobiphenyl';

INSERT INTO pollutant_pollutant_group (pollutant_code, pollutant_group_id) SELECT codes.pollutant_code, pg.id FROM pollutant_group pg JOIN (VALUES
('2051243'),('28655712'),('2050682'),('2051607'),('25429292'),('26601649'),('7012375'),('26914330'),('1336363'),('53742077'),('55722264'),('52663726'),
('31508006'),('32598133'),('32598144'),('74472370'),('38380084'),('189640')) AS codes(pollutant_code)
ON pg.description = 'Polychlorinated Biphenyls';

INSERT INTO pollutant_pollutant_group (pollutant_code, pollutant_group_id) SELECT codes.pollutant_code, pg.id FROM pollutant_group pg JOIN (VALUES
('250'),('284'),('50328'),('53703'),('56495'),('56553'),('57976'),('83329'),('85018'),('86737'),('86748'),('90120'),('91576'),('91587'),('120127'),
('129000'),('189559'),('189640'),('191242'),('191300'),('192654'),('192972'),('193395'),('194592'),('195197'),('198550'),('203123'),('203338'),
('205823'),('205992'),('206440'),('207089'),('208968'),('218019'),('224420'),('226368'),('602879'),('607578'),('779022'),('832699'),('2381217'),('2422799'),
('2531842'),('3697243'),('5385751'),('5522430'),('7496028'),('8007452'),('26914181'),('41637905'),('42397648'),('42397659'),('56832736'),('57835924'),('65357699')) AS codes(pollutant_code)
ON pg.description = 'PAH Grouping';