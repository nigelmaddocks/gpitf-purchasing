ALTER TABLE purchasing.procurement 
ADD COLUMN search_keyword VARCHAR(255) NULL,
ADD COLUMN csv_Capabilities VARCHAR(1023) NULL;

UPDATE purchasing.proc_status SET name='Longlist' 				WHERE id=2;
UPDATE purchasing.proc_status SET name='Shortlist' 				WHERE id=3;
UPDATE purchasing.proc_status SET name='Internal competition' 	WHERE id=4;
UPDATE purchasing.proc_status SET name='External tender' 		WHERE id=5;
UPDATE purchasing.proc_status SET name='Contract offered' 		WHERE id=6;
INSERT INTO purchasing.proc_status (id, name) values(80, 'Completed');
