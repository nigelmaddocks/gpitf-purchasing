CREATE TABLE purchasing.framework (
  id BIGSERIAL PRIMARY KEY,
  frameworkId VARCHAR(36),
  name VARCHAR(255) NULL
);

INSERT INTO purchasing.framework (id, frameworkId) VALUES(1, '5A8D06DD-8C32-4821-AC65-BD47294ACD8E');

/* Referenced by an attribute of the procurement */
CREATE TABLE purchasing.competition_type (
  id BIGINT PRIMARY KEY,
  name VARCHAR(255)
);
INSERT INTO purchasing.competition_type (id, name) VALUES(1, 'On-Catalogue');
INSERT INTO purchasing.competition_type (id, name) VALUES(2, 'Off-Catalogue');

/* Referenced by an attribute of the procurement */
CREATE TABLE purchasing.evaluation_type (
  id BIGINT PRIMARY KEY,
  name VARCHAR(255)
);
INSERT INTO purchasing.evaluation_type (id, name) VALUES(1, 'Price');
INSERT INTO purchasing.evaluation_type (id, name) VALUES(2, 'Price & Quality');

CREATE TABLE purchasing.evaluation_criterion_type (
  id BIGINT PRIMARY KEY,
  name VARCHAR(255)
);

INSERT INTO purchasing.evaluation_criterion_type (id, name) VALUES(1, 'Price');
INSERT INTO purchasing.evaluation_criterion_type (id, name) VALUES(2, 'Non-Price');

CREATE TABLE purchasing.evaluation_score_value (
  score BIGINT PRIMARY KEY,
  name VARCHAR(255)
);

INSERT INTO purchasing.evaluation_score_value (score, name) VALUES(0, 'Absent or does not meet the criterion.');
INSERT INTO purchasing.evaluation_score_value (score, name) VALUES(1, 'Response has some major shortcomings and/or clear deficiencies, which on balance outweigh positive aspects of the response to the criterion.');
INSERT INTO purchasing.evaluation_score_value (score, name) VALUES(2, 'Response has minor shortcomings and/or deficiencies, but on balance these are outweighed by the positive aspects that meet the majority of the criterion.');
INSERT INTO purchasing.evaluation_score_value (score, name) VALUES(3, 'The criterion is fully met in all areas.');
INSERT INTO purchasing.evaluation_score_value (score, name) VALUES(4, 'The response meets all aspects of the criterion and adds value in a number of areas.');
INSERT INTO purchasing.evaluation_score_value (score, name) VALUES(5, 'The response meets all aspects of the criterion, adds significant value in all areas and exceeds the requirements.');

CREATE TABLE purchasing.evaluation_tolerance (
  id BIGSERIAL PRIMARY KEY,
  framework BIGINT REFERENCES purchasing.framework(id),
  criterium_type BIGINT REFERENCES purchasing.evaluation_criterion_type(id),
  competition_type BIGINT REFERENCES purchasing.competition_type(id),
  lower_inclusive_percent INT,
  upper_inclusive_percent INT
);

INSERT INTO purchasing.evaluation_tolerance (id, framework, criterium_type, competition_type, lower_inclusive_percent, upper_inclusive_percent) 
VALUES(1, 1, 1, 1, 30, 90);
INSERT INTO purchasing.evaluation_tolerance (id, framework, criterium_type, competition_type, lower_inclusive_percent, upper_inclusive_percent) 
VALUES(2, 1, 2, 1, 10, 70);

INSERT INTO purchasing.evaluation_tolerance (id, framework, criterium_type, competition_type, lower_inclusive_percent, upper_inclusive_percent) 
VALUES(3, 1, 1, 2, 1, 100);
INSERT INTO purchasing.evaluation_tolerance (id, framework, criterium_type, competition_type, lower_inclusive_percent, upper_inclusive_percent) 
VALUES(4, 1, 2, 2, 1, 100);

CREATE TABLE purchasing.evaluation_off_cat_criterion (
  id BIGSERIAL PRIMARY KEY,
  tolerance BIGINT REFERENCES purchasing.evaluation_tolerance(id),
  name VARCHAR(255)
);
INSERT INTO purchasing.evaluation_off_cat_criterion (id, tolerance, name) VALUES(1, 3, 'Price');
INSERT INTO purchasing.evaluation_off_cat_criterion (id, tolerance, name) VALUES(2, 4, 'Quality');
INSERT INTO purchasing.evaluation_off_cat_criterion (id, tolerance, name) VALUES(3, 4, 'Delivery timescales');
INSERT INTO purchasing.evaluation_off_cat_criterion (id, tolerance, name) VALUES(4, 4, 'Technical merit');
INSERT INTO purchasing.evaluation_off_cat_criterion (id, tolerance, name) VALUES(5, 4, 'Target Performance Indicators offered');
INSERT INTO purchasing.evaluation_off_cat_criterion (id, tolerance, name) VALUES(6, 4, 'Transition / Migration');

CREATE TABLE purchasing.evaluation_proc_criterion (
  id BIGSERIAL PRIMARY KEY,
  procurement BIGINT REFERENCES purchasing.procurement(id),
  off_cat_criterion BIGINT NULL REFERENCES purchasing.evaluation_off_cat_criterion(id), /* Either set this or evaluation_tolerance and name */
  tolerance BIGINT NULL REFERENCES purchasing.evaluation_tolerance(id),
  name VARCHAR(255) NULL,
  weighting_percent NUMERIC (5, 2),
  seq INT
);

CREATE TABLE purchasing.evaluation_bundle_score (
  id BIGSERIAL PRIMARY KEY,
  bundle BIGINT REFERENCES purchasing.proc_solution_bundle(id),
  proc_criterion BIGINT REFERENCES purchasing.evaluation_proc_criterion(id),
  score BIGINT NULL REFERENCES purchasing.evaluation_score_value(score),
  scored_by BIGINT NULL REFERENCES purchasing.org_contact(id),
  scored_date TIMESTAMP NULL
);

ALTER TABLE purchasing.procurement ADD COLUMN IF NOT EXISTS evaluation_type  BIGINT NULL REFERENCES purchasing.evaluation_type(id);
ALTER TABLE purchasing.procurement ADD COLUMN IF NOT EXISTS competition_type BIGINT NULL REFERENCES purchasing.competition_type(id);

/* Adding address information to Organisation */
ALTER TABLE purchasing.organisation ADD COLUMN IF NOT EXISTS addr_line_1 VARCHAR(255) NULL;
ALTER TABLE purchasing.organisation ADD COLUMN IF NOT EXISTS addr_line_2 VARCHAR(255) NULL;
ALTER TABLE purchasing.organisation ADD COLUMN IF NOT EXISTS addr_line_3 VARCHAR(255) NULL;
ALTER TABLE purchasing.organisation ADD COLUMN IF NOT EXISTS addr_town VARCHAR(255) NULL;
ALTER TABLE purchasing.organisation ADD COLUMN IF NOT EXISTS addr_county VARCHAR(255) NULL;
ALTER TABLE purchasing.organisation ADD COLUMN IF NOT EXISTS addr_postcode VARCHAR(20) NULL;
ALTER TABLE purchasing.organisation ADD COLUMN IF NOT EXISTS addr_country VARCHAR(255) NULL;



/*
Reversal:
drop TABLE purchasing.evaluation_bundle_score cascade;
drop TABLE purchasing.evaluation_proc_criterion cascade;
drop TABLE purchasing.evaluation_off_cat_criterion cascade;
drop TABLE purchasing.evaluation_tolerance cascade;
drop TABLE purchasing.evaluation_score_value cascade;
drop TABLE purchasing.evaluation_criterion_type cascade;
drop TABLE purchasing.evaluation_type cascade;
drop TABLE purchasing.competition_type cascade;
drop TABLE purchasing.framework cascade;
delete from public.flyway_schema_history where script='V015__evaluation_01.sql'; 

*/