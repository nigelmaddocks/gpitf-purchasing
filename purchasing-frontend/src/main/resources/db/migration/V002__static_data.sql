
INSERT INTO purchasing.org_type (id, name) values(1, 'GP Practice');
INSERT INTO purchasing.org_type (id, name) values(2, 'CCG');
INSERT INTO purchasing.org_type (id, name) values(3, 'CSU');

INSERT INTO purchasing.relationship_type (id, name, parent_org_type, child_org_type) values(1, 'CCG to GP Practice', 2, 1);
INSERT INTO purchasing.relationship_type (id, name, parent_org_type, child_org_type) values(2, 'CSU to CCG', 3, 2);

