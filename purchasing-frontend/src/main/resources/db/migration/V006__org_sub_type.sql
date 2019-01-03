UPDATE purchasing.org_type SET name='Prescribing Practice' WHERE id=1;
UPDATE purchasing.relationship_type SET name='CCG to Practice' WHERE id=1;
INSERT INTO purchasing.org_type (id, name) values(5, 'GP Practice');
INSERT INTO purchasing.org_type (id, name) values(6, 'Out of Hours');
INSERT INTO purchasing.org_type (id, name) values(7, 'Walk-in Centre');
INSERT INTO purchasing.org_type (id, name) values(8, 'Other Statutory Authority');

ALTER TABLE purchasing.organisation ADD COLUMN
  org_sub_type BIGINT REFERENCES purchasing.org_type(id) NULL;

INSERT INTO purchasing.organisation (org_type, org_code, name, name_proper_case, org_sub_type)
VALUES (8, 'X24', 'NHS ENGLAND', 'NHS England', null);

INSERT INTO purchasing.contact (email, forename, surname, deleted)
VALUES ('your.name@nhs.net', 'Your', 'Name', false);

INSERT INTO purchasing.org_contact (organisation, contact, deleted)
VALUES (
(SELECT id FROM purchasing.organisation WHERE org_code = 'X24'),
(SELECT id FROM purchasing.contact WHERE email = 'your.name@nhs.net'),
false
);

INSERT INTO purchasing.org_contact_role (org_contact, role, deleted)
VALUES (
(SELECT MAX(id) FROM purchasing.org_contact),
4, false
);
