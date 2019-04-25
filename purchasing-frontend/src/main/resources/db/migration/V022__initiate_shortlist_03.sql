ALTER TABLE purchasing.tmp_additional_service
  RENAME COLUMN id TO additional_service_id;

ALTER TABLE purchasing.tmp_associated_service
  DROP CONSTRAINT IF EXISTS tmp_associated_service_additional_service_fkey;
  
ALTER TABLE purchasing.proc_solution_bundle_item
  DROP CONSTRAINT IF EXISTS proc_solution_bundle_item_additional_service_fkey;  
  
ALTER TABLE purchasing.tmp_additional_service_capability
  DROP CONSTRAINT IF EXISTS tmp_additional_service_capability_additional_service_fkey;
  
ALTER TABLE purchasing.tmp_solution_price_band
  DROP CONSTRAINT IF EXISTS tmp_solution_price_band_additional_service_fkey;
  
ALTER TABLE purchasing.tmp_additional_service
  DROP CONSTRAINT IF EXISTS tmp_additional_service_pkey;
  
ALTER TABLE purchasing.tmp_additional_service
  ADD COLUMN id BIGSERIAL PRIMARY KEY;



CREATE TABLE purchasing.service_type (
	id BIGINT PRIMARY KEY,
	name VARCHAR(255)
);

INSERT INTO purchasing.service_type (id, name) VALUES(1, 'Base solution');
INSERT INTO purchasing.service_type (id, name) VALUES(2, 'Associated Service');
INSERT INTO purchasing.service_type (id, name) VALUES(3, 'Additional Service');
INSERT INTO purchasing.service_type (id, name) VALUES(4, 'Associated Service of Additional Service');

CREATE TABLE purchasing.proc_bundle_sr_service (
	id 					BIGSERIAL PRIMARY KEY,
	service_recipient 	BIGINT REFERENCES purchasing.proc_srv_recipient(id),
	bundle 				BIGINT REFERENCES purchasing.proc_solution_bundle(id),
	service_type		BIGINT REFERENCES purchasing.service_type(id),
	associated_service  VARCHAR(36) NULL,
	additional_service  VARCHAR(36) NULL,
	number_of_units		INT NULL,
	patient_count_based BOOLEAN
);

ALTER TABLE purchasing.proc_solution_bundle  DROP COLUMN IF EXISTS number_of_units;  
ALTER TABLE purchasing.proc_solution_bundle  DROP COLUMN IF EXISTS price;

/*
Reversal:

  
ALTER TABLE purchasing.proc_solution_bundle  ADD COLUMN number_of_units INT NULL;  
ALTER TABLE purchasing.proc_solution_bundle  ADD COLUMN price NUMERIC(12,4) NULL;
drop table purchasing.proc_bundle_sr_service;
drop table purchasing.service_type;
alter table purchasing.tmp_additional_service drop column id;
alter table purchasing.tmp_additional_service rename column additional_service_id to id;

delete from public.flyway_schema_history where script='V022__initiate_shortlist_03.sql';
*/

