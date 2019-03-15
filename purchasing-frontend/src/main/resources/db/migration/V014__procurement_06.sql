CREATE TABLE purchasing.proc_solution_bundle (
  id BIGSERIAL PRIMARY KEY,
  procurement BIGINT REFERENCES purchasing.procurement(id),
  evaluation_score_percent NUMERIC(5,2) NULL
);

CREATE TABLE purchasing.proc_solution_bundle_item (
  id BIGSERIAL PRIMARY KEY,
  bundle BIGINT REFERENCES purchasing.proc_solution_bundle(id),
  solution_id VARCHAR(64) NULL,
  additional_service VARCHAR(32) NULL REFERENCES purchasing.tmp_additional_service(id)
);

CREATE TABLE purchasing.proc_contract (
  id BIGSERIAL PRIMARY KEY,
  procurement BIGINT REFERENCES purchasing.procurement(id),
  organisation BIGINT REFERENCES purchasing.organisation(id),
  contract_generated_date DATE NULL,
  contract_generated_by BIGINT REFERENCES purchasing.org_contact(id) NULL,
  contract_approved_date DATE NULL,
  contract_approved_by BIGINT REFERENCES purchasing.org_contact(id) NULL,
  contract_accepted_date DATE NULL,
  contract_accepted_by BIGINT REFERENCES purchasing.org_contact(id) NULL
);

COMMENT ON TABLE purchasing.proc_contract IS 'Procurement''s Contract originating from budget holding organisation, e.g. CCG';

CREATE TABLE purchasing.proc_contract_srv_recipient (
  id BIGSERIAL PRIMARY KEY,
  proc_contract BIGINT REFERENCES purchasing.proc_contract(id),
  organisation BIGINT REFERENCES purchasing.organisation(id)
);

COMMENT ON TABLE purchasing.proc_contract_srv_recipient IS 'Procurement''s Contract''s service recipients, e.g. GP Practices within a CCG within a Procurement';

CREATE TABLE purchasing.unit (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255)
);

COMMENT ON TABLE purchasing.unit IS 'Unit of procurement. If a new unit is encountered from onboarding data, it will be added here.';

INSERT INTO purchasing.unit (id, name) VALUES(1, 'Patient');

CREATE TABLE purchasing.proc_contract_solution (
  id BIGSERIAL PRIMARY KEY,
  proc_contract BIGINT REFERENCES purchasing.proc_contract(id),
  solution_id VARCHAR(64),
  unit BIGINT REFERENCES purchasing.unit(id),
  unit_price NUMERIC (12, 5),
  unit_number NUMERIC (10, 2)
);

COMMENT ON TABLE purchasing.proc_contract_solution IS 'Procurement''s Contract''s chosen solution(s) -  normally one';

/*
Reversal:
drop TABLE purchasing.proc_solution_bundle_item cascade;
drop TABLE purchasing.proc_solution_bundle cascade;
drop TABLE purchasing.proc_contract_solution cascade;
drop TABLE purchasing.unit cascade;
drop TABLE purchasing.proc_contract_srv_recipient cascade;
drop TABLE purchasing.proc_contract cascade;
delete from public.flyway_schema_history where script='V014__procurement_06.sql';
*/