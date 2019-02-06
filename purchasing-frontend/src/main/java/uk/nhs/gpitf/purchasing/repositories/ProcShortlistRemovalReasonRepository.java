package uk.nhs.gpitf.purchasing.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import uk.nhs.gpitf.purchasing.entities.ProcShortlist;
import uk.nhs.gpitf.purchasing.entities.ProcShortlistRemovalReason;


@Repository
public interface ProcShortlistRemovalReasonRepository extends CrudRepository<ProcShortlistRemovalReason, Long> {
}
