package uk.nhs.gpitf.purchasing.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.nhs.gpitf.purchasing.entities.EvaluationBundleScore;
import uk.nhs.gpitf.purchasing.entities.EvaluationCriterionType;

import java.util.Optional;

@Repository("evaluationCriterionTypeRepository")
public interface EvaluationCriterionTypeRepository extends CrudRepository<EvaluationCriterionType, Integer> {
    Optional<EvaluationCriterionType> findByName(String name);
}
