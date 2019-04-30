package uk.nhs.gpitf.purchasing.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="proc_bundle_sr_service", schema="purchasing")
@Data
public class ProcBundleSrService {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
	private long id;
	
    @ManyToOne(optional=false)
    @JoinColumn(name = "service_recipient")
	private ProcSrvRecipient serviceRecipient;
	
    @ManyToOne(optional=false)
    @JoinColumn(name = "bundle")
	private ProcSolutionBundle bundle;
	
    @ManyToOne(optional=false)
    @JoinColumn(name = "service_type")
	private ServiceType serviceType;
   
	private String associatedService;

    private String additionalService;
	
    private Integer numberOfUnits;
	
    private boolean patientCountBased;
    
}
