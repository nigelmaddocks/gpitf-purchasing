package uk.nhs.gpitf.purchasing.entities;

import lombok.Data;
import lombok.Getter;
import lombok.AccessLevel;
import uk.nhs.gpitf.purchasing.SpringConfiguration;
import uk.nhs.gpitf.purchasing.entities.swagger.SolutionEx2;
import uk.nhs.gpitf.purchasing.repositories.PatientCountRepository;
import uk.nhs.gpitf.purchasing.repositories.PatientCountRunRepository;
import uk.nhs.gpitf.purchasing.services.OnboardingService;
import uk.nhs.gpitf.purchasing.utils.GUtils;

import javax.persistence.*;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.client.model.Capabilities;

@Entity
@Table(name="proc_solution_bundle_item", schema="purchasing")
@Data
public class ProcSolutionBundleItem {
	@Transient
	@JsonIgnore
	OnboardingService onboardingService;
	
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
	private long id;
    
    @ManyToOne(optional=false)
    @JoinColumn(name = "bundle")
    @JsonIgnore
    private ProcSolutionBundle bundle;
    
    @Transient
    @Getter(AccessLevel.NONE) 
    private SolutionEx2 solution;
    public SolutionEx2 getSolution() {
    	if (solution == null && GUtils.nullToString(solutionId).length() > 0) {
    		if (onboardingService == null ) {
    			onboardingService = (OnboardingService) SpringConfiguration.contextProvider().getApplicationContext().getBean("onboardingService");
    		}
    		solution = onboardingService.getSolutionEx2ById(solutionId);
    	}
    	return solution;
    }
    
    private String solutionId;

    private String additionalService;
    
    @Transient
    private Capabilities[] capabilities;
}
