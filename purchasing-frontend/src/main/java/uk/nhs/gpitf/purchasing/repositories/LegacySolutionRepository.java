package uk.nhs.gpitf.purchasing.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import uk.nhs.gpitf.purchasing.entities.*;

@Repository
public interface LegacySolutionRepository extends CrudRepository<LegacySolution, Long> {
	Optional<LegacySolution> findByNameAndVersionAndSupplier(String name, String version, Organisation supplier);
}
