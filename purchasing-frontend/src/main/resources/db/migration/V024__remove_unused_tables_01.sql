drop table purchasing.proc_contract_solution;
drop table purchasing.proc_contract_srv_recipient;
drop table purchasing.proc_contract;

drop table purchasing.proc_shortlist;
drop table purchasing.proc_shortlist_removal_reason;

/*
Reversal:
 
delete from public.flyway_schema_history where script='V024__remove_unused_tables_01.sql';

*/