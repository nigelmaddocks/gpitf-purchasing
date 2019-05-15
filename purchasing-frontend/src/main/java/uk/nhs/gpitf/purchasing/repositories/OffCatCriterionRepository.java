package uk.nhs.gpitf.purchasing.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.nhs.gpitf.purchasing.entities.OffCatCriterion;

import java.util.Optional;

@Repository("offCatCriterionRepository")
public interface OffCatCriterionRepository extends CrudRepository<OffCatCriterion, Long> {
    Optional<OffCatCriterion> findByName(String name);
}
