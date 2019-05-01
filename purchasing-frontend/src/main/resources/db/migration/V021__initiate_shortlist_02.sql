ALTER TABLE purchasing.tmp_associated_service
  RENAME COLUMN id TO associated_service_id;

ALTER TABLE purchasing.tmp_solution_price_band
  DROP CONSTRAINT IF EXISTS tmp_solution_price_band_associated_service_fkey;
  
ALTER TABLE purchasing.tmp_associated_service
  DROP CONSTRAINT IF EXISTS tmp_associated_service_pkey;
  
ALTER TABLE purchasing.tmp_associated_service
  ADD COLUMN id BIGSERIAL PRIMARY KEY;

ALTER TABLE purchasing.tmp_price_basis
  ADD COLUMN tiered BOOLEAN DEFAULT false;
  
ALTER TABLE purchasing.proc_solution_bundle
  ADD COLUMN number_of_units INT NULL;
  
ALTER TABLE purchasing.proc_solution_bundle
  ADD COLUMN price NUMERIC(12,4) NULL;

ALTER TABLE purchasing.tmp_solution_price_band
  ADD COLUMN banding_unit_type BIGINT REFERENCES purchasing.tmp_unit_type(id) NULL;
 
UPDATE purchasing.tmp_solution_price_band
SET banding_unit_type=2
WHERE price_basis=1;
 
UPDATE purchasing.tmp_solution_price_band
SET banding_unit_type=3
WHERE price_basis=5;
 
UPDATE purchasing.tmp_solution_price_band
SET banding_unit_type=1
WHERE price_basis=8;
 
UPDATE purchasing.tmp_solution_price_band
SET price = price/50000.00
WHERE banding_unit_type=3 AND price>50000;
 
UPDATE purchasing.tmp_solution_price_band
SET price_basis = 1
WHERE banding_unit_type=3;



/*
Reversal:

alter table purchasing.tmp_solution_price_band DROP COLUMN banding_unit_type;
alter table purchasing.proc_solution_bundle DROP COLUMN price;
alter table purchasing.proc_solution_bundle DROP COLUMN number_of_units;
alter table purchasing.tmp_associated_service drop column id;
alter table purchasing.tmp_associated_service rename column associated_service_id to id;
alter table purchasing.tmp_price_basis drop column tiered;
delete from public.flyway_schema_history where script='V021__initiate_shortlist_02.sql';
*/

