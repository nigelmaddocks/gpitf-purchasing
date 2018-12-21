package uk.nhs.gpitf.purchasing.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import uk.nhs.gpitf.purchasing.entities.*;

@Repository
public interface OrgSolutionRepository extends CrudRepository<OrgSolution, Long> {
	Optional<OrgSolution> findByOrganisationAndLegacySolution(Organisation organisation, LegacySolution legacySolution);
}
