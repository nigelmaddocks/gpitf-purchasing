package uk.nhs.gpitf.purchasing.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import uk.nhs.gpitf.purchasing.entities.*;

@Repository
public interface PatientCountRepository extends CrudRepository<PatientCount, Long> {
	Iterable<PatientCount> findAllByRun(PatientCountRun patientCountrun);
	Iterable<PatientCount> findAllByRunAndOrg(PatientCountRun patientCountrun, Organisation org);
}
