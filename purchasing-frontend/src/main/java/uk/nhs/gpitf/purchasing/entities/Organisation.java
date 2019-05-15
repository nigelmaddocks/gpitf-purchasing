package uk.nhs.gpitf.purchasing.entities;

import lombok.Data;
import uk.nhs.gpitf.purchasing.SpringConfiguration;
import uk.nhs.gpitf.purchasing.repositories.PatientCountRepository;
import uk.nhs.gpitf.purchasing.repositories.PatientCountRunRepository;
import uk.nhs.gpitf.purchasing.utils.GUtils;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Optional;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="organisation", schema="purchasing")
@Data
public class Organisation implements Serializable {
	@Transient
	@JsonIgnore
	PatientCountRunRepository patientCountRunRepository;
	
	@Transient
	@JsonIgnore
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

    
    @ManyToOne(optional=true)
    @JoinColumn(name = "org_sub_type")
	private OrgType orgSubType;
    
    @Column(name = "addr_line_1")
    @Size(max = 255)
	private String addrLine1;
    
    @Column(name = "addr_line_2")
	@Size(max = 255)
	private String addrLine2;
    
    @Column(name = "addr_line_3")
	@Size(max = 255)
	private String addrLine3;
    
	@Size(max = 255)
	private String addrTown;
    
	@Size(max = 255)
	private String addrCounty;
    
	@Size(max = 20)
	private String addrPostcode;
    
	@Size(max = 255)
	private String addrCountry;
    
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
    @JsonIgnore
    public PatientCount getLatestPatientCount() {
    	if (orgType.getId() == OrgType.PRESCRIBING_PRACTICE) {
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
    
    @Transient
    @JsonIgnore
    public String getAddress() {
    	String s = addrLine1;
    	if (StringUtils.isNotEmpty(addrLine2)) {
    		if (StringUtils.isNotEmpty(s)) {
    			s += ",\n";
    		}
    		s += addrLine2;
    	}
    	if (StringUtils.isNotEmpty(addrLine3)) {
    		if (StringUtils.isNotEmpty(s)) {
    			s += ",\n";
    		}
    		s += addrLine3;
    	}
    	if (StringUtils.isNotEmpty(addrTown)) {
    		if (StringUtils.isNotEmpty(s)) {
    			s += ",\n";
    		}
    		s += addrTown;
    	}
    	if (StringUtils.isNotEmpty(addrCounty)) {
    		if (StringUtils.isNotEmpty(s)) {
    			s += ",\n";
    		}
    		s += addrCounty;
    	}
    	if (StringUtils.isNotEmpty(addrPostcode)) {
    		if (StringUtils.isNotEmpty(s)) {
    			s += ",\n";
    		}
    		s += addrPostcode;
    	}
    	if (StringUtils.isNotEmpty(addrCountry) && !addrCountry.equalsIgnoreCase("ENGLAND")) {
    		if (StringUtils.isNotEmpty(s)) {
    			if (StringUtils.isEmpty(addrPostcode)) {
    				s += ",";
    			}
    			s += "\n";
    		}
    		s += addrCountry;
    	}

    	return s;
    }
    
}
