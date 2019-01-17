-- SQL Server - Capabilities Implemented

if not exists (select * from capabilities where name='Meds Verification') insert into capabilities (id, name) values ('CAP-N-901', 'Meds Verification');

if not exists (select * from capabilities where name='Telecare') insert into capabilities (id, name) values ('CAP-N-902', 'Telecare');


-- Vision 3
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(1, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'CAP-C-001', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(2, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'CAP-C-002', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(3, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'CAP-C-003', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(4, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'CAP-C-004', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(5, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'CAP-C-005', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(6, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'CAP-C-006', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(7, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'CAP-N-008', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(8, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'CAP-N-011', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(9, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'CAP-N-010', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(10, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'CAP-N-014', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(11, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'CAP-N-013', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(12, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'CAP-N-015', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(13, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'CAP-N-007', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(14, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'CAP-N-016', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(15, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'CAP-N-018', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(16, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'CAP-N-009', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(17, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'CAP-N-020', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(18, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'CAP-N-021', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(19, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'CAP-N-028', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(20, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'CAP-N-029', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(21, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'CAP-N-030', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(22, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'CAP-N-035', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(23, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'CAP-N-036', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(24, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'CAP-N-037', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(25, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'CAP-N-901', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(26, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'CAP-N-902', 3);

-- TPP SystmOne

insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(101, '908C9350-7392-4CB9-9633-19E00C9469A3', 'CAP-C-001', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(102, '908C9350-7392-4CB9-9633-19E00C9469A3', 'CAP-C-002', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(103, '908C9350-7392-4CB9-9633-19E00C9469A3', 'CAP-C-003', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(104, '908C9350-7392-4CB9-9633-19E00C9469A3', 'CAP-C-004', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(105, '908C9350-7392-4CB9-9633-19E00C9469A3', 'CAP-C-005', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(106, '908C9350-7392-4CB9-9633-19E00C9469A3', 'CAP-C-006', 3);

insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(107, '908C9350-7392-4CB9-9633-19E00C9469A3', 'CAP-N-008', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(108, '908C9350-7392-4CB9-9633-19E00C9469A3', 'CAP-N-011', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(109, '908C9350-7392-4CB9-9633-19E00C9469A3', 'CAP-N-010', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(110, '908C9350-7392-4CB9-9633-19E00C9469A3', 'CAP-N-012', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(111, '908C9350-7392-4CB9-9633-19E00C9469A3', 'CAP-N-013', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(112, '908C9350-7392-4CB9-9633-19E00C9469A3', 'CAP-N-014', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(113, '908C9350-7392-4CB9-9633-19E00C9469A3', 'CAP-N-015', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(114, '908C9350-7392-4CB9-9633-19E00C9469A3', 'CAP-N-007', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(115, '908C9350-7392-4CB9-9633-19E00C9469A3', 'CAP-N-016', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(116, '908C9350-7392-4CB9-9633-19E00C9469A3', 'CAP-N-017', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(117, '908C9350-7392-4CB9-9633-19E00C9469A3', 'CAP-N-018', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(118, '908C9350-7392-4CB9-9633-19E00C9469A3', 'CAP-N-019', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(119, '908C9350-7392-4CB9-9633-19E00C9469A3', 'CAP-N-009', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(120, '908C9350-7392-4CB9-9633-19E00C9469A3', 'CAP-N-020', 3); 
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(121, '908C9350-7392-4CB9-9633-19E00C9469A3', 'CAP-N-901', 3); 

-- Microtest

insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(201, '8CFFD174-0831-411B-9FF0-7E1AF172BC8A', 'CAP-C-001', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(202, '8CFFD174-0831-411B-9FF0-7E1AF172BC8A', 'CAP-C-002', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(203, '8CFFD174-0831-411B-9FF0-7E1AF172BC8A', 'CAP-C-003', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(204, '8CFFD174-0831-411B-9FF0-7E1AF172BC8A', 'CAP-C-004', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(205, '8CFFD174-0831-411B-9FF0-7E1AF172BC8A', 'CAP-C-005', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(206, '8CFFD174-0831-411B-9FF0-7E1AF172BC8A', 'CAP-C-006', 3);

insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(207, '8CFFD174-0831-411B-9FF0-7E1AF172BC8A', 'CAP-N-008', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(208, '8CFFD174-0831-411B-9FF0-7E1AF172BC8A', 'CAP-N-010', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(209, '8CFFD174-0831-411B-9FF0-7E1AF172BC8A', 'CAP-N-012', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(210, '8CFFD174-0831-411B-9FF0-7E1AF172BC8A', 'CAP-N-014', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(211, '8CFFD174-0831-411B-9FF0-7E1AF172BC8A', 'CAP-N-015', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(212, '8CFFD174-0831-411B-9FF0-7E1AF172BC8A', 'CAP-N-007', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(213, '8CFFD174-0831-411B-9FF0-7E1AF172BC8A', 'CAP-N-016', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(214, '8CFFD174-0831-411B-9FF0-7E1AF172BC8A', 'CAP-N-017', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(215, '8CFFD174-0831-411B-9FF0-7E1AF172BC8A', 'CAP-N-019', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(216, '8CFFD174-0831-411B-9FF0-7E1AF172BC8A', 'CAP-N-009', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(217, '8CFFD174-0831-411B-9FF0-7E1AF172BC8A', 'CAP-N-020', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(218, '8CFFD174-0831-411B-9FF0-7E1AF172BC8A', 'CAP-N-030', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(219, '8CFFD174-0831-411B-9FF0-7E1AF172BC8A', 'CAP-N-901', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(220, '8CFFD174-0831-411B-9FF0-7E1AF172BC8A', 'CAP-N-902', 3);
