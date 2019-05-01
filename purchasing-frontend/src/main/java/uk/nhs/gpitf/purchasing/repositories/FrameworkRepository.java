package uk.nhs.gpitf.purchasing.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import uk.nhs.gpitf.purchasing.entities.Framework;

@Repository
public interface FrameworkRepository extends CrudRepository<Framework, Long> {
	Optional<Framework> findByFrameworkId(String frameworkId);

}
