alter table purchasing.tmp_additional_service 
ALTER COLUMN solution_id DROP NOT NULL;

delete from purchasing.tmp_additional_service_capability;

/* TPP Systm One */
insert into purchasing.tmp_additional_service_capability (id, additional_service, capability) values(1, '9bdd44c5-5ada-4b46-9af0-ffe85ed1682d', 'C9'); 

insert into purchasing.tmp_additional_service_capability (id, additional_service, capability) values(2, '97d1434a-95eb-47f2-a3f9-56add90efd0f', 'C4'); 
insert into purchasing.tmp_additional_service_capability (id, additional_service, capability) values(3, '97d1434a-95eb-47f2-a3f9-56add90efd0f', 'C18'); 
insert into purchasing.tmp_additional_service_capability (id, additional_service, capability) values(4, '97d1434a-95eb-47f2-a3f9-56add90efd0f', 'C3'); 
insert into purchasing.tmp_additional_service_capability (id, additional_service, capability) values(5, '97d1434a-95eb-47f2-a3f9-56add90efd0f', 'C33'); 
insert into purchasing.tmp_additional_service_capability (id, additional_service, capability) values(6, '97d1434a-95eb-47f2-a3f9-56add90efd0f', 'C32'); 
insert into purchasing.tmp_additional_service_capability (id, additional_service, capability) values(7, '97d1434a-95eb-47f2-a3f9-56add90efd0f', 'C2'); 

/* EMIS */
insert into purchasing.tmp_additional_service_capability (id, additional_service, capability) values(8, '24a0c17c-27ce-4417-98e2-41fc67b07627', 'C4'); 
insert into purchasing.tmp_additional_service_capability (id, additional_service, capability) values(9, '24a0c17c-27ce-4417-98e2-41fc67b07627', 'C18'); 
insert into purchasing.tmp_additional_service_capability (id, additional_service, capability) values(10, '24a0c17c-27ce-4417-98e2-41fc67b07627', 'C3'); 
insert into purchasing.tmp_additional_service_capability (id, additional_service, capability) values(11, '24a0c17c-27ce-4417-98e2-41fc67b07627', 'C33'); 
insert into purchasing.tmp_additional_service_capability (id, additional_service, capability) values(12, '24a0c17c-27ce-4417-98e2-41fc67b07627', 'C32'); 
insert into purchasing.tmp_additional_service_capability (id, additional_service, capability) values(13, '24a0c17c-27ce-4417-98e2-41fc67b07627', 'C2'); 

/* Microtest */
insert into purchasing.tmp_additional_service_capability (id, additional_service, capability) values(14, 'ebc27c06-b3d5-42d3-b96b-0d90d547c29c', 'C4'); 
insert into purchasing.tmp_additional_service_capability (id, additional_service, capability) values(15, 'ebc27c06-b3d5-42d3-b96b-0d90d547c29c', 'C18'); 
insert into purchasing.tmp_additional_service_capability (id, additional_service, capability) values(16, 'ebc27c06-b3d5-42d3-b96b-0d90d547c29c', 'C3'); 
insert into purchasing.tmp_additional_service_capability (id, additional_service, capability) values(17, 'ebc27c06-b3d5-42d3-b96b-0d90d547c29c', 'C33'); 
insert into purchasing.tmp_additional_service_capability (id, additional_service, capability) values(18, 'ebc27c06-b3d5-42d3-b96b-0d90d547c29c', 'C32'); 
insert into purchasing.tmp_additional_service_capability (id, additional_service, capability) values(19, 'ebc27c06-b3d5-42d3-b96b-0d90d547c29c', 'C2'); 

/* Vision */
insert into purchasing.tmp_additional_service_capability (id, additional_service, capability) values(20, '5a5a31a5-eeeb-425d-baea-84e23b31bb2b', 'C4'); 
insert into purchasing.tmp_additional_service_capability (id, additional_service, capability) values(21, '5a5a31a5-eeeb-425d-baea-84e23b31bb2b', 'C18'); 
insert into purchasing.tmp_additional_service_capability (id, additional_service, capability) values(22, '5a5a31a5-eeeb-425d-baea-84e23b31bb2b', 'C3'); 
insert into purchasing.tmp_additional_service_capability (id, additional_service, capability) values(23, '5a5a31a5-eeeb-425d-baea-84e23b31bb2b', 'C33'); 
insert into purchasing.tmp_additional_service_capability (id, additional_service, capability) values(24, '5a5a31a5-eeeb-425d-baea-84e23b31bb2b', 'C32'); 
insert into purchasing.tmp_additional_service_capability (id, additional_service, capability) values(25, '5a5a31a5-eeeb-425d-baea-84e23b31bb2b', 'C2'); 

/* EMIS Web - EMIS Mobile  */
insert into purchasing.tmp_additional_service (id, solution_id, name) values('fdbaae13-12c1-4ef1-91f8-37564772503a', 'D8922903-D02E-48F0-8340-3D94D3DE9147', 'EMIS Mobile'); 
insert into purchasing.tmp_additional_service_capability (id, additional_service, capability) values(26, 'fdbaae13-12c1-4ef1-91f8-37564772503a', 'C41'); 
/* EMIS Web - EMIS Health Analytics  */
insert into purchasing.tmp_additional_service (id, solution_id, name) values('bdaad54f-f64b-47ee-81bf-1b27c6fa8fec', 'D8922903-D02E-48F0-8340-3D94D3DE9147', 'Primary Care Analytics'); 
insert into purchasing.tmp_additional_service_capability (id, additional_service, capability) values(27, 'bdaad54f-f64b-47ee-81bf-1b27c6fa8fec', 'C26'); 
insert into purchasing.tmp_additional_service_capability (id, additional_service, capability) values(28, 'bdaad54f-f64b-47ee-81bf-1b27c6fa8fec', 'C35'); 
insert into purchasing.tmp_additional_service_capability (id, additional_service, capability) values(29, 'bdaad54f-f64b-47ee-81bf-1b27c6fa8fec', 'C16'); 
/* EMIS Web - Document Management  */
insert into purchasing.tmp_additional_service (id, solution_id, name) values('6de1fb09-e6d7-49c3-b404-e72a59c05ec3', 'D8922903-D02E-48F0-8340-3D94D3DE9147', 'Document Management'); 
insert into purchasing.tmp_additional_service_capability (id, additional_service, capability) values(30, '6de1fb09-e6d7-49c3-b404-e72a59c05ec3', 'C9'); 
/* EMIS Web Dispensing  */
insert into purchasing.tmp_additional_service (id, solution_id, name) values('47586934-a075-4265-ad90-6452e79470d3', 'D8922903-D02E-48F0-8340-3D94D3DE9147', 'EMIS Web Dispensing'); 
insert into purchasing.tmp_additional_service_capability (id, additional_service, capability) values(31, '47586934-a075-4265-ad90-6452e79470d3', 'C42'); 


/* ASSOCIATED SERVICES */

create table purchasing.tmp_associated_service (
  id VARCHAR(36) PRIMARY KEY,
  solution_id VARCHAR(36) NULL,
  additional_service VARCHAR(36) REFERENCES purchasing.tmp_additional_service(id) NULL,
  name VARCHAR(255)
);

/* TPP Systm One */
insert into purchasing.tmp_associated_service (id, solution_id, name) values('c274e19d-660d-4eba-a832-15529bc7b395', '908C9350-7392-4CB9-9633-19E00C9469A3', 'Training'); 
insert into purchasing.tmp_associated_service (id, solution_id, name) values('d83f98e8-ac11-4733-918f-c4a57955e0bc', '908C9350-7392-4CB9-9633-19E00C9469A3', 'Data Migration'); 

/* EMIS Web */
insert into purchasing.tmp_associated_service (id, solution_id, name) values('359fe7cd-2c1e-4f22-857a-28d9b342760d', 'D8922903-D02E-48F0-8340-3D94D3DE9147', 'Training Day at Practice - Full Day'); 
insert into purchasing.tmp_associated_service (id, solution_id, name) values('d4cf5c16-720f-42e8-940b-7a7578f9bbde', 'D8922903-D02E-48F0-8340-3D94D3DE9147', 'Migration'); 
insert into purchasing.tmp_associated_service (id, solution_id, name) values('46b422ac-3356-47ed-b5a4-f181e0d89f69', 'D8922903-D02E-48F0-8340-3D94D3DE9147', 'Data extraction'); 
insert into purchasing.tmp_associated_service (id, solution_id, name) values('2bc6792b-da3c-4279-a324-9664b3a97699', 'D8922903-D02E-48F0-8340-3D94D3DE9147', 'Practice merger');

/* Microtest */
insert into purchasing.tmp_associated_service (id, solution_id, name) values('126cb42e-3bce-4fe6-98e1-21b000ab6fa1', '8CFFD174-0831-411B-9FF0-7E1AF172BC8A', 'Training'); 
insert into purchasing.tmp_associated_service (id, solution_id, name) values('105ecab7-4243-42bc-8343-bba503fbccba', '8CFFD174-0831-411B-9FF0-7E1AF172BC8A', 'Data Migration'); 

/* Vision */
insert into purchasing.tmp_associated_service (id, solution_id, name) values('59871e12-b858-4b8a-9789-84aa01c0d09f', '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'Training'); 
insert into purchasing.tmp_associated_service (id, solution_id, name) values('cda13cb0-eea8-4d78-9354-60a07005fb9b', '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'Data Migration'); 

/* [EMIS] Primary Care Analytics */
insert into purchasing.tmp_associated_service (id, additional_service, name) values('e1931301-5ea3-40be-b1c4-4debd480ae34', 'bdaad54f-f64b-47ee-81bf-1b27c6fa8fec', 'Migration');
insert into purchasing.tmp_associated_service (id, additional_service, name) values('f6b73a95-dca2-4f7d-93bb-25f207f4ca93', 'bdaad54f-f64b-47ee-81bf-1b27c6fa8fec', 'Training Day at Practice - Half Day');

/* [EMIS] Document Management */
insert into purchasing.tmp_associated_service (id, additional_service, name) values('f95582d1-91ca-449e-8f18-ffffc5a6b023', '6de1fb09-e6d7-49c3-b404-e72a59c05ec3', 'Migration');
insert into purchasing.tmp_associated_service (id, additional_service, name) values('8722c103-f6d2-4b81-af73-33250244c0e4', '6de1fb09-e6d7-49c3-b404-e72a59c05ec3', 'Training Day at Practice - Half Day');

/* EMIS Web Dispensing  */
insert into purchasing.tmp_associated_service (id, additional_service, name) values('60e97287-18a5-4d54-a04c-bdb113af49aa', '47586934-a075-4265-ad90-6452e79470d3', 'Migration');
insert into purchasing.tmp_associated_service (id, additional_service, name) values('0c62bca8-cff0-422e-8fec-62aa37947704', '47586934-a075-4265-ad90-6452e79470d3', 'Training Day at Practice - Half Day');

/* UNITS and PRICE calculation */
create table purchasing.tmp_unit_type (
	id BIGSERIAL PRIMARY KEY,
	name VARCHAR(255)
);

insert into purchasing.tmp_unit_type (id, name) values(1, 'Generic'); 
insert into purchasing.tmp_unit_type (id, name) values(2, 'Patient'); 
insert into purchasing.tmp_unit_type (id, name) values(3, 'Service Recipient'); 

create table purchasing.tmp_price_basis (
	id BIGSERIAL PRIMARY KEY,
	name VARCHAR(255),
	unit1 BIGINT REFERENCES purchasing.tmp_unit_type(id) NULL,
	unit2 BIGINT REFERENCES purchasing.tmp_unit_type(id) NULL,
	calculation VARCHAR(255)
);

insert into purchasing.tmp_price_basis (id, name, unit1, calculation) 	values(1, 'per Patient per year', 2, '{PRICE} * {UNITS} * {YEARS}');
insert into purchasing.tmp_price_basis (id, name, calculation) 			values(2, 'fixed value', '{PRICE}');
insert into purchasing.tmp_price_basis (id, name, unit1, calculation) 	values(3, 'per Patient', 2, '{PRICE} * {UNITS}');
insert into purchasing.tmp_price_basis (id, name, unit1, calculation) 	values(4, 'per Patient per month', 2, '{PRICE} * {UNITS} * {MONTHS}');
insert into purchasing.tmp_price_basis (id, name, unit1, calculation) 	values(5, 'per Service Recipient per year', 3, '{PRICE} * {UNITS} * {YEARS}');
insert into purchasing.tmp_price_basis (id, name, unit1, calculation) 	values(6, 'per Service Recipient', 3, '{PRICE} * {UNITS}');
insert into purchasing.tmp_price_basis (id, name, unit1, calculation) 	values(7, 'per Service Recipient per month', 3, '{PRICE} * {UNITS} * {MONTHS}');
insert into purchasing.tmp_price_basis (id, name, unit1, calculation) 	values(8, 'per Unit per year', 1, '{PRICE} * {UNITS} * {YEARS}');
insert into purchasing.tmp_price_basis (id, name, unit1, calculation) 	values(9, 'per Unit', 1, '{PRICE} * {UNITS}');
insert into purchasing.tmp_price_basis (id, name, unit1, calculation) 	values(10, 'per Unit per month', 1, '{PRICE} * {UNITS} * {MONTHS}');

create table purchasing.tmp_solution_price_band (
	id BIGSERIAL PRIMARY KEY,
	solution_id VARCHAR(36),
  	additional_service VARCHAR(36) REFERENCES purchasing.tmp_additional_service(id) NULL,
  	associated_service VARCHAR(36) REFERENCES purchasing.tmp_associated_service(id) NULL,
	unit_name VARCHAR(36) NULL,
	lower_limit_incl INT NULL,
	upper_limit_incl INT NULL,
	price_basis BIGINT REFERENCES purchasing.tmp_price_basis(id),
	price NUMERIC(10,4)
);

/* EMIS */
insert into purchasing.tmp_solution_price_band (id, solution_id, price_basis, price) values(1, 'D8922903-D02E-48F0-8340-3D94D3DE9147', 1, 1.22);
/* TPP */
insert into purchasing.tmp_solution_price_band (id, solution_id, lower_limit_incl, upper_limit_incl, price_basis, price) values(2, '908C9350-7392-4CB9-9633-19E00C9469A3', NULL, 50000, 1, 1.50); 
insert into purchasing.tmp_solution_price_band (id, solution_id, lower_limit_incl, upper_limit_incl, price_basis, price) values(3, '908C9350-7392-4CB9-9633-19E00C9469A3', 50001, 200000, 1, 1.20);
insert into purchasing.tmp_solution_price_band (id, solution_id, lower_limit_incl, upper_limit_incl, price_basis, price) values(4, '908C9350-7392-4CB9-9633-19E00C9469A3', 200001, NULL, 1, 1.03);
/* Microtest */
insert into purchasing.tmp_solution_price_band (id, solution_id, lower_limit_incl, upper_limit_incl, price_basis, price) values(5, '8CFFD174-0831-411B-9FF0-7E1AF172BC8A', NULL, 5, 5, 60000); 
insert into purchasing.tmp_solution_price_band (id, solution_id, lower_limit_incl, upper_limit_incl, price_basis, price) values(6, '8CFFD174-0831-411B-9FF0-7E1AF172BC8A', 6, 25, 5, 56000); 
insert into purchasing.tmp_solution_price_band (id, solution_id, lower_limit_incl, upper_limit_incl, price_basis, price) values(7, '8CFFD174-0831-411B-9FF0-7E1AF172BC8A', 26, NULL, 5, 52000); 
/* Vision */
insert into purchasing.tmp_solution_price_band (id, solution_id, unit_name, lower_limit_incl, upper_limit_incl, price_basis, price) values(8, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'Per 10,000 transactions per year', NULL, 100, 8, 60000); 
insert into purchasing.tmp_solution_price_band (id, solution_id, unit_name, lower_limit_incl, upper_limit_incl, price_basis, price) values(9, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'Per 10,000 transactions per year', 101, NULL, 8, 55000); 

/* EMIS Associated Services */
insert into purchasing.tmp_solution_price_band (id, associated_service, price_basis, price) values(10, '359fe7cd-2c1e-4f22-857a-28d9b342760d', 2, 650.00);
insert into purchasing.tmp_solution_price_band (id, associated_service, price_basis, price) values(11, 'd4cf5c16-720f-42e8-940b-7a7578f9bbde', 2, 3000.00);
insert into purchasing.tmp_solution_price_band (id, associated_service, price_basis, price) values(12, '46b422ac-3356-47ed-b5a4-f181e0d89f69', 2, 2635.00);
insert into purchasing.tmp_solution_price_band (id, associated_service, price_basis, price) values(13, '2bc6792b-da3c-4279-a324-9664b3a97699', 2, 1500.00);

/* EMIS Mobile */
insert into purchasing.tmp_solution_price_band (id, additional_service, price_basis, price) values(101, 'fdbaae13-12c1-4ef1-91f8-37564772503a', 1, 0.10);
/* [EMIS] Primary Care Analytics */
insert into purchasing.tmp_solution_price_band (id, additional_service, price_basis, price) values(102, 'bdaad54f-f64b-47ee-81bf-1b27c6fa8fec', 1, 0.21);
insert into purchasing.tmp_solution_price_band (id, associated_service, price_basis, price) values(103, 'e1931301-5ea3-40be-b1c4-4debd480ae34', 2, 1000.00);
insert into purchasing.tmp_solution_price_band (id, associated_service, price_basis, price) values(104, 'f6b73a95-dca2-4f7d-93bb-25f207f4ca93', 2, 650.00);
/* [EMIS] Document Management */
insert into purchasing.tmp_solution_price_band (id, additional_service, price_basis, price) values(105, '6de1fb09-e6d7-49c3-b404-e72a59c05ec3', 1, 0.13);
insert into purchasing.tmp_solution_price_band (id, associated_service, price_basis, price) values(106, 'f95582d1-91ca-449e-8f18-ffffc5a6b023', 2, 1000.00);
insert into purchasing.tmp_solution_price_band (id, associated_service, price_basis, price) values(107, '8722c103-f6d2-4b81-af73-33250244c0e4', 2, 650.00);
/* EMIS Web Dispensing */
insert into purchasing.tmp_solution_price_band (id, additional_service, price_basis, price) values(108, '47586934-a075-4265-ad90-6452e79470d3', 1, 0.20);
insert into purchasing.tmp_solution_price_band (id, associated_service, price_basis, price) values(109, '60e97287-18a5-4d54-a04c-bdb113af49aa', 2, 1000.00);
insert into purchasing.tmp_solution_price_band (id, associated_service, price_basis, price) values(110, '0c62bca8-cff0-422e-8fec-62aa37947704', 2, 650.00);



/*
Reversal:

drop table purchasing.tmp_solution_price_band;
drop table purchasing.tmp_price_basis;
drop table purchasing.tmp_unit_type;
drop table purchasing.tmp_associated_service; 
delete from purchasing.tmp_additional_service_capability;
delete from purchasing.tmp_additional_service where id in ('fdbaae13-12c1-4ef1-91f8-37564772503a','bdaad54f-f64b-47ee-81bf-1b27c6fa8fec','6de1fb09-e6d7-49c3-b404-e72a59c05ec3',
'fdbaae13-12c1-4ef1-91f8-37564772503a', 'bdaad54f-f64b-47ee-81bf-1b27c6fa8fec', '6de1fb09-e6d7-49c3-b404-e72a59c05ec3', '47586934-a075-4265-ad90-6452e79470d3');
delete from public.flyway_schema_history where script='V019__tmp_onboarding_02.sql';
*/

