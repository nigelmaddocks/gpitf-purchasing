package uk.nhs.gpitf.purchasing.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import uk.nhs.gpitf.purchasing.entities.*;

@Repository
public interface OrgTypeRepository extends CrudRepository<OrgType, Long> {
}
