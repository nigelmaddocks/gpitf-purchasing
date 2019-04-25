package uk.nhs.gpitf.purchasing.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.nhs.gpitf.purchasing.entities.TmpSolutionPriceBand;

@Repository
public interface TmpSolutionPriceBandRepository extends CrudRepository<TmpSolutionPriceBand, Long> {
	Iterable<TmpSolutionPriceBand> findAllBySolutionIdOrderByLowerLimitIncl(String solutionId);
	Iterable<TmpSolutionPriceBand> findAllByAdditionalServiceOrderByLowerLimitIncl(String additionalService);
	Iterable<TmpSolutionPriceBand> findAllByAssociatedServiceOrderByLowerLimitIncl(String associatedService);
}
