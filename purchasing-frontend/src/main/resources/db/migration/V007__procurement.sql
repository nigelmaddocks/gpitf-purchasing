CREATE TABLE purchasing.proc_status (
  id BIGINT PRIMARY KEY,
  name VARCHAR(255)
);

INSERT INTO purchasing.proc_status (id, name) values(1, 'Draft');
INSERT INTO purchasing.proc_status (id, name) values(2, 'Shortlist');
INSERT INTO purchasing.proc_status (id, name) values(3, 'Internal competition');
INSERT INTO purchasing.proc_status (id, name) values(4, 'External tender');
INSERT INTO purchasing.proc_status (id, name) values(5, 'Contract offered');
INSERT INTO purchasing.proc_status (id, name) values(6, 'Completed');
INSERT INTO purchasing.proc_status (id, name) values(99, 'Deleted');

CREATE TABLE purchasing.procurement (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255),
  started_date TIMESTAMP NULL,
  org_contact BIGINT REFERENCES purchasing.org_contact(id),
  status BIGINT REFERENCES purchasing.proc_status(id),
  status_last_changed_date TIMESTAMP NULL,
  completed_date TIMESTAMP NULL,
  last_updated TIMESTAMP NULL
);

