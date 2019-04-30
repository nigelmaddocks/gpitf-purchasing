alter table purchasing.framework 
ADD COLUMN IF NOT EXISTS max_term_months INT NULL;

UPDATE purchasing.framework SET max_term_months = 36 WHERE id = 1;

UPDATE purchasing.proc_status SET name = 'Initiate' WHERE id = 3;

CREATE TABLE purchasing.proc_srv_recipient (
	id BIGSERIAL PRIMARY KEY,
	procurement BIGINT REFERENCES purchasing.procurement(id),
	organisation BIGINT REFERENCES purchasing.organisation(id),
	term INT NULL,
	patient_count INT NULL
);


/*
Reversal:

UPDATE purchasing.proc_status SET name = 'Shortlist' WHERE id = 3;
DROP TABLE purchasing.proc_srv_recipient;
delete from public.flyway_schema_history where script='V020__initiate_shortlist_01.sql';
*/

