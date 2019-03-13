create table purchasing.tmp_additional_service (
  id VARCHAR(36) PRIMARY KEY,
  solution_id VARCHAR(36),
  name VARCHAR(255)
);



/* Patient facing services */

create table purchasing.tmp_additional_service_capability (
  id BIGSERIAL PRIMARY KEY,
  additional_service VARCHAR(36) REFERENCES purchasing.tmp_additional_service(id),
  capability VARCHAR(36)
);

/* Data */

/* TPP Systm One */
insert into purchasing.tmp_additional_service (id, solution_id, name) values('9bdd44c5-5ada-4b46-9af0-ffe85ed1682d', '908C9350-7392-4CB9-9633-19E00C9469A3', 'Document Management'); 
insert into purchasing.tmp_additional_service_capability (id, additional_service, capability) values(1, '9bdd44c5-5ada-4b46-9af0-ffe85ed1682d', 'C14'); 

insert into purchasing.tmp_additional_service (id, solution_id, name) values('97d1434a-95eb-47f2-a3f9-56add90efd0f', '908C9350-7392-4CB9-9633-19E00C9469A3', 'Patient-facing Services'); 
insert into purchasing.tmp_additional_service_capability (id, additional_service, capability) values(2, '97d1434a-95eb-47f2-a3f9-56add90efd0f', 'C29'); 

/* EMIS */
insert into purchasing.tmp_additional_service (id, solution_id, name) values('24a0c17c-27ce-4417-98e2-41fc67b07627', 'D8922903-D02E-48F0-8340-3D94D3DE9147', 'Patient-facing Services'); 
insert into purchasing.tmp_additional_service_capability (id, additional_service, capability) values(3, '24a0c17c-27ce-4417-98e2-41fc67b07627', 'C29'); 

/* Microtest */
insert into purchasing.tmp_additional_service (id, solution_id, name) values('ebc27c06-b3d5-42d3-b96b-0d90d547c29c', '8CFFD174-0831-411B-9FF0-7E1AF172BC8A', 'Patient-facing Services'); 
insert into purchasing.tmp_additional_service_capability (id, additional_service, capability) values(4, 'ebc27c06-b3d5-42d3-b96b-0d90d547c29c', 'C29'); 

/* Vision */
insert into purchasing.tmp_additional_service (id, solution_id, name) values('5a5a31a5-eeeb-425d-baea-84e23b31bb2b', '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'Patient-facing Services'); 
insert into purchasing.tmp_additional_service_capability (id, additional_service, capability) values(5, '5a5a31a5-eeeb-425d-baea-84e23b31bb2b', 'C29'); 


/*
Reversal:
drop TABLE purchasing.tmp_additional_service_capability cascade;
drop TABLE purchasing.tmp_additional_service cascade;
delete from public.flyway_schema_history where script='V013__tmp_onboarding_01.sql';

*/