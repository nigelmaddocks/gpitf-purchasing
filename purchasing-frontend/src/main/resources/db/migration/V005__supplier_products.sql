INSERT INTO purchasing.org_type (id, name) values(4, 'Supplier');

CREATE TABLE purchasing.legacy_solution (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255),
  version VARCHAR(255) NULL,
  supplier BIGINT REFERENCES purchasing.organisation(id)
);

CREATE TABLE purchasing.org_solution (
  id BIGSERIAL PRIMARY KEY,
  organisation BIGINT REFERENCES purchasing.organisation(id),
  solution VARCHAR(255) NULL,
  legacy_solution BIGINT NULL REFERENCES purchasing.legacy_solution(id),
  ordered_date DATE NULL,
  installed_date DATE NULL,
  deleted BOOLEAN
);