package uk.nhs.gpitf.purchasing.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.nhs.gpitf.purchasing.entities.CompetitionType;
import uk.nhs.gpitf.purchasing.entities.Contact;

import java.util.Optional;

@Repository
public interface CompetitionTypeRepository extends CrudRepository<CompetitionType, Long> {
	Optional<CompetitionType> findByName(String name);
}
