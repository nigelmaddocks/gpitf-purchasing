package uk.nhs.gpitf.purchasing.repositories;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.nhs.gpitf.purchasing.entities.EvaluationCriterionType;
import uk.nhs.gpitf.purchasing.entities.EvaluationTolerance;
import uk.nhs.gpitf.purchasing.entities.Procurement;

import java.util.Optional;

@Repository
public interface EvaluationToleranceRepository extends CrudRepository<EvaluationTolerance, Integer> {
    Optional<EvaluationTolerance> findByCompetitionTypeAndCriteriumType(int competitionType, int criteriumType);
    Optional<EvaluationTolerance> findById(Integer id);
}
