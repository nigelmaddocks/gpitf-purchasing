ALTER TABLE purchasing.procurement 
	ADD COLUMN IF NOT EXISTS single_site_continuity boolean NULL;

/*
Reversal:
 
delete from public.flyway_schema_history where script='V023__single_site_continuity.sql';

*/