package uk.nhs.gpitf.purchasing.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.nhs.gpitf.purchasing.entities.TmpAssociatedService;

@Repository
public interface TmpAssociatedServiceRepository extends CrudRepository<TmpAssociatedService, Long> {
	Iterable<TmpAssociatedService> findAllBySolutionIdOrderByName(String solutionId);
}
