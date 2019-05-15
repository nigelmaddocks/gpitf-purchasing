package uk.nhs.gpitf.purchasing.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import uk.nhs.gpitf.purchasing.entities.Contact;
import uk.nhs.gpitf.purchasing.entities.EvaluationBundleScore;
import uk.nhs.gpitf.purchasing.entities.EvaluationScoreValue;
import uk.nhs.gpitf.purchasing.entities.OrgContact;
import uk.nhs.gpitf.purchasing.entities.ProcSolutionBundle;

@Repository
public interface EvaluationBundleScoreRepository extends CrudRepository<EvaluationBundleScore, Integer> {
	Iterable<EvaluationBundleScore> findAllByBundle(ProcSolutionBundle bundle);	
}
