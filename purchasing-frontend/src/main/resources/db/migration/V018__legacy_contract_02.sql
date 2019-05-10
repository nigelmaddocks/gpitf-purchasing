ALTER TABLE purchasing.legacy_solution
  ADD COLUMN IF NOT EXISTS foundation BOOLEAN DEFAULT(true);
