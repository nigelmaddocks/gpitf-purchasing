
CREATE TABLE purchasing.contact (
  id BIGSERIAL PRIMARY KEY,
  email VARCHAR(255),
  forename VARCHAR(255),
  surname VARCHAR(255),
  deleted BOOLEAN
);

CREATE TABLE purchasing.org_contact (
  id BIGSERIAL PRIMARY KEY,
  organisation BIGINT REFERENCES purchasing.organisation(id),
  contact BIGINT REFERENCES purchasing.contact(id),
  deleted BOOLEAN
);

CREATE TABLE purchasing.role (
  id BIGINT PRIMARY KEY,
  name VARCHAR(255)
);

CREATE TABLE purchasing.org_contact_role (
  id BIGSERIAL PRIMARY KEY,
  org_contact BIGINT REFERENCES purchasing.org_contact(id),
  role BIGINT REFERENCES purchasing.role(id),
  deleted BOOLEAN
);


INSERT INTO purchasing.role (id, name) values(1, 'Purchaser');
INSERT INTO purchasing.role (id, name) values(2, 'Approver');
INSERT INTO purchasing.role (id, name) values(3, 'Local Admin');
INSERT INTO purchasing.role (id, name) values(4, 'Administrator');
