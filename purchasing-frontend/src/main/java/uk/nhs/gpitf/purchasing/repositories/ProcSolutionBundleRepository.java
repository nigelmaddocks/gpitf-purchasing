package uk.nhs.gpitf.purchasing.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.nhs.gpitf.purchasing.entities.ProcSolutionBundle;
import uk.nhs.gpitf.purchasing.entities.Procurement;

import java.math.BigDecimal;
import java.util.List;


@Repository
public interface ProcSolutionBundleRepository extends CrudRepository<ProcSolutionBundle, Long> {
    @Query("SELECT b FROM ProcSolutionBundle b WHERE b.procurement = ?1")
    List<ProcSolutionBundle> findAllByProcurement(Procurement p);
}
