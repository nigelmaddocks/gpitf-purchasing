-- SQL Server - Capabilities Implemented

if not exists (select * from capabilities where name='Meds Verification') begin
	insert into capabilities (id, name, type) values ('C901', 'Meds Verification', 'N');
	insert into capabilityframework (capabilityid, frameworkid) values('C901', '5A8D06DD-8C32-4821-AC65-BD47294ACD8E');
end

if not exists (select * from capabilities where name='Telecare') begin
	insert into capabilities (id, name, type) values ('C902', 'Telecare', 'N');
	insert into capabilityframework (capabilityid, frameworkid) values('C902', '5A8D06DD-8C32-4821-AC65-BD47294ACD8E');
end


-- Vision 3
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(1, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'C1', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(2, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'C2', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(3, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'C3', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(4, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'C4', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(5, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'C5', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(6, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'C6', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(7, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'C8', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(8, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'C11', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(9, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'C10', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(10, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'C14', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(11, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'C13', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(12, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'C15', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(13, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'C7', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(14, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'C16', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(15, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'C18', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(16, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'C9', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(17, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'C20', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(18, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'C21', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(19, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'C28', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(20, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'C29', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(21, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'C30', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(22, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'C35', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(23, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'C36', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(24, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'C37', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(25, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'C901', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(26, '4813F419-9A9A-4AFA-BB61-ECF9FB0E5F0A', 'C902', 3);

-- TPP SystmOne

insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(101, '908C9350-7392-4CB9-9633-19E00C9469A3', 'C1', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(102, '908C9350-7392-4CB9-9633-19E00C9469A3', 'C2', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(103, '908C9350-7392-4CB9-9633-19E00C9469A3', 'C3', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(104, '908C9350-7392-4CB9-9633-19E00C9469A3', 'C4', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(105, '908C9350-7392-4CB9-9633-19E00C9469A3', 'C5', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(106, '908C9350-7392-4CB9-9633-19E00C9469A3', 'C6', 3);

insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(107, '908C9350-7392-4CB9-9633-19E00C9469A3', 'C8', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(108, '908C9350-7392-4CB9-9633-19E00C9469A3', 'C11', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(109, '908C9350-7392-4CB9-9633-19E00C9469A3', 'C10', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(110, '908C9350-7392-4CB9-9633-19E00C9469A3', 'C12', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(111, '908C9350-7392-4CB9-9633-19E00C9469A3', 'C13', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(112, '908C9350-7392-4CB9-9633-19E00C9469A3', 'C14', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(113, '908C9350-7392-4CB9-9633-19E00C9469A3', 'C15', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(114, '908C9350-7392-4CB9-9633-19E00C9469A3', 'C7', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(115, '908C9350-7392-4CB9-9633-19E00C9469A3', 'C16', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(116, '908C9350-7392-4CB9-9633-19E00C9469A3', 'C17', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(117, '908C9350-7392-4CB9-9633-19E00C9469A3', 'C18', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(118, '908C9350-7392-4CB9-9633-19E00C9469A3', 'C19', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(119, '908C9350-7392-4CB9-9633-19E00C9469A3', 'C9', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(120, '908C9350-7392-4CB9-9633-19E00C9469A3', 'C20', 3); 
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(121, '908C9350-7392-4CB9-9633-19E00C9469A3', 'C901', 3); 

-- Microtest

insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(201, '8CFFD174-0831-411B-9FF0-7E1AF172BC8A', 'C1', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(202, '8CFFD174-0831-411B-9FF0-7E1AF172BC8A', 'C2', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(203, '8CFFD174-0831-411B-9FF0-7E1AF172BC8A', 'C3', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(204, '8CFFD174-0831-411B-9FF0-7E1AF172BC8A', 'C4', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(205, '8CFFD174-0831-411B-9FF0-7E1AF172BC8A', 'C5', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(206, '8CFFD174-0831-411B-9FF0-7E1AF172BC8A', 'C6', 3);

insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(207, '8CFFD174-0831-411B-9FF0-7E1AF172BC8A', 'C8', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(208, '8CFFD174-0831-411B-9FF0-7E1AF172BC8A', 'C10', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(209, '8CFFD174-0831-411B-9FF0-7E1AF172BC8A', 'C12', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(210, '8CFFD174-0831-411B-9FF0-7E1AF172BC8A', 'C14', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(211, '8CFFD174-0831-411B-9FF0-7E1AF172BC8A', 'C15', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(212, '8CFFD174-0831-411B-9FF0-7E1AF172BC8A', 'C7', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(213, '8CFFD174-0831-411B-9FF0-7E1AF172BC8A', 'C16', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(214, '8CFFD174-0831-411B-9FF0-7E1AF172BC8A', 'C17', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(215, '8CFFD174-0831-411B-9FF0-7E1AF172BC8A', 'C19', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(216, '8CFFD174-0831-411B-9FF0-7E1AF172BC8A', 'C9', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(217, '8CFFD174-0831-411B-9FF0-7E1AF172BC8A', 'C20', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(218, '8CFFD174-0831-411B-9FF0-7E1AF172BC8A', 'C30', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(219, '8CFFD174-0831-411B-9FF0-7E1AF172BC8A', 'C901', 3);
insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(220, '8CFFD174-0831-411B-9FF0-7E1AF172BC8A', 'C902', 3);

-- Docman

insert into capabilitiesimplemented (id, solutionid, capabilityid, status) values(301, 'C8D558DA-8EC9-4E36-881A-344F0F852284', 'C14', 3);
 