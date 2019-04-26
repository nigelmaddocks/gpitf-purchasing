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

UPDATE purchasing.tmp_solution_price_band
set price_basis=9
WHERE price_basis=2 AND price=650.00;

/* Some optional test data */
INSERT INTO purchasing.proc_bundle_sr_service (id, service_recipient, bundle, service_type, associated_service, additional_service, number_of_units, patient_count_based)
	VALUES(1, 1, 90, 1, NULL, NULL, 6, false);
INSERT INTO purchasing.proc_bundle_sr_service (id, service_recipient, bundle, service_type, associated_service, additional_service, number_of_units, patient_count_based)
	VALUES(2, 1, 87, 2, '2bc6792b-da3c-4279-a324-9664b3a97699', NULL, 1, false);
INSERT INTO purchasing.proc_bundle_sr_service (id, service_recipient, bundle, service_type, associated_service, additional_service, number_of_units, patient_count_based)
	VALUES(3, 1, 87, 3, NULL, 'fdbaae13-12c1-4ef1-91f8-37564772503a', NULL, false);
INSERT INTO purchasing.proc_bundle_sr_service (id, service_recipient, bundle, service_type, associated_service, additional_service, number_of_units, patient_count_based)
	VALUES(4, 1, 87, 3, NULL, 'bdaad54f-f64b-47ee-81bf-1b27c6fa8fec', NULL, false);
INSERT INTO purchasing.proc_bundle_sr_service (id, service_recipient, bundle, service_type, associated_service, additional_service, number_of_units, patient_count_based)
	VALUES(5, 1, 87, 4, 'e1931301-5ea3-40be-b1c4-4debd480ae34', 'bdaad54f-f64b-47ee-81bf-1b27c6fa8fec', NULL, false);
INSERT INTO purchasing.proc_bundle_sr_service (id, service_recipient, bundle, service_type, associated_service, additional_service, number_of_units, patient_count_based)
	VALUES(6, 1, 87, 4, 'f6b73a95-dca2-4f7d-93bb-25f207f4ca93', 'bdaad54f-f64b-47ee-81bf-1b27c6fa8fec', NULL, false);

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

