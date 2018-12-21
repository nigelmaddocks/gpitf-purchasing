package uk.nhs.gpitf.purchasing.entities;

import lombok.Data;
import uk.nhs.gpitf.purchasing.SpringConfiguration;
import uk.nhs.gpitf.purchasing.repositories.PatientCountRepository;
import uk.nhs.gpitf.purchasing.repositories.PatientCountRunRepository;
import uk.nhs.gpitf.purchasing.utils.GUtils;

import java.util.Iterator;
import java.util.Optional;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;

@Entity
@Table(name="organisation", schema="purchasing")
@Data
public class Organisation {
	@Transient
	PatientCountRunRepository patientCountRunRepository;
	
	@Transient
	PatientCountRepository patientCountRepository;
	
	
	
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
	private long id;
    
    @ManyToOne(optional=false)
    @JoinColumn(name = "org_type")
	private OrgType orgType;
	
    @NotBlank
	@Column(name = "org_code")
	@Size(max = 30)
	private String orgCode;
	
    @NotBlank
	@Size(max = 255)
	private String name;
    public void setName(String value) {
    	this.name = value;
    	this.nameProperCase = GUtils.getCapitalized(value);
    }
    
    @NotBlank
	@Size(max = 255)
	private String nameProperCase;
    
    @Transient
    public int getPatientCount() {
    	PatientCount patientCount = getLatestPatientCount();
    	if (patientCount != null) {
    		return patientCount.getPatientCount();
    	} else {
    		return 0;
    	}
    }
    
    @Transient
    public PatientCount getLatestPatientCount() {
    	if (orgType.getId() == OrgType.GPPRACTICE) {
    		if (patientCountRunRepository == null || patientCountRepository == null) {
    			patientCountRunRepository = (PatientCountRunRepository) SpringConfiguration.contextProvider().getApplicationContext().getBean("patientCountRunRepository");
    			patientCountRepository = (PatientCountRepository) SpringConfiguration.contextProvider().getApplicationContext().getBean("patientCountRepository");
    		}
    		Optional<PatientCountRun> optRun = patientCountRunRepository.findAllByOrderByRunDate();
    		if (optRun.isPresent()) {
    			PatientCountRun run = optRun.get();
    			Iterator<PatientCount> iterPC = patientCountRepository.findAllByRunAndOrg(run, this).iterator();
    			if (iterPC.hasNext()) {
    				return iterPC.next();
    			}
    		}
    	}
    	return null;
    }
    
}