CREATE SCHEMA purchasing;

CREATE TABLE purchasing.org_type (
  id BIGINT PRIMARY KEY,
  name VARCHAR(255)
);

CREATE TABLE purchasing.relationship_type (
  id BIGINT PRIMARY KEY,
  name VARCHAR(255),
  parent_org_type BIGINT REFERENCES purchasing.org_type(id),
  child_org_type BIGINT REFERENCES purchasing.org_type(id)
);

CREATE TABLE purchasing.organisation (
  id BIGSERIAL PRIMARY KEY,
  org_type BIGINT REFERENCES purchasing.org_type(id),
  org_code VARCHAR(30),
  name VARCHAR(255),
  name_proper_case VARCHAR(255)
);

CREATE TABLE purchasing.org_relationship (
  id BIGSERIAL PRIMARY KEY,
  parent_org BIGINT REFERENCES purchasing.organisation(id),
  child_org BIGINT REFERENCES purchasing.organisation(id),
  relationship_type BIGINT REFERENCES purchasing.relationship_type(id)
);

CREATE TABLE purchasing.patient_count_run (
  id BIGSERIAL PRIMARY KEY,
  run_date DATE,
  file_name VARCHAR(255)
);

CREATE TABLE purchasing.patient_count (
  id BIGSERIAL PRIMARY KEY,
  run BIGINT REFERENCES purchasing.patient_count_run(id),
  org BIGINT REFERENCES purchasing.organisation(id),
  patient_count INT
);
