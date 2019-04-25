package uk.nhs.gpitf.purchasing.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import uk.nhs.gpitf.purchasing.entities.ProcBundleSrService;
import uk.nhs.gpitf.purchasing.entities.ProcSolutionBundle;


@Repository
public interface ProcBundleSrServiceRepository extends CrudRepository<ProcBundleSrService, Long> {
	//Iterable<ProcBundleSrService> findAllByBundleOrderByServiceTypeIdAscAdditionalServiceAscAssociatedServiceAsc(ProcSolutionBundle bundle);
	Iterable<ProcBundleSrService> findAllByBundle(ProcSolutionBundle bundle);

}
