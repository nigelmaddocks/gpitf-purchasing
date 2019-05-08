package uk.nhs.gpitf.purchasing.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.nhs.gpitf.purchasing.entities.EvaluationProcCriterion;

import java.util.List;

@Repository("evaluationProcCriterionRepository")
public interface EvaluationProcCriterionRepository extends CrudRepository<EvaluationProcCriterion, Long> {
    List<EvaluationProcCriterion> findByProcurement(Long id);
}

