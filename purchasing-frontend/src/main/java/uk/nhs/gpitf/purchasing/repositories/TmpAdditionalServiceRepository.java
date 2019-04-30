package uk.nhs.gpitf.purchasing.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import uk.nhs.gpitf.purchasing.entities.TmpAdditionalService;

@Repository
public interface TmpAdditionalServiceRepository extends CrudRepository<TmpAdditionalService, Long> {
	Iterable<TmpAdditionalService> findAllBySolutionIdOrderByName(String solutionId);
}
