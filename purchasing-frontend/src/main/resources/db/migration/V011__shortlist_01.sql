ALTER TABLE purchasing.procurement 
DROP COLUMN csv_shortlist_solutions,
ADD COLUMN planned_contract_start DATE NULL,
ADD COLUMN contract_months INT NULL;

CREATE TABLE purchasing.proc_shortlist_removal_reason (
  id BIGINT PRIMARY KEY,
  name VARCHAR(255)
);

INSERT INTO purchasing.proc_shortlist_removal_reason (id, name) values(1, 'Too expensive');
INSERT INTO purchasing.proc_shortlist_removal_reason (id, name) values(2, 'Bad customer reviews');
INSERT INTO purchasing.proc_shortlist_removal_reason (id, name) values(90, 'Other');

CREATE TABLE purchasing.proc_shortlist (
  id BIGSERIAL PRIMARY KEY,
  procurement BIGINT REFERENCES purchasing.procurement(id),
  solution_id VARCHAR(64),
  removed boolean,
  removal_reason BIGINT REFERENCES purchasing.proc_shortlist_removal_reason(id),
  removal_reason_text VARCHAR(255) NULL
);

