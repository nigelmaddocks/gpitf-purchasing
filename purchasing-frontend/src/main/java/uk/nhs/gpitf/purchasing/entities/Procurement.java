package uk.nhs.gpitf.purchasing.entities;

import lombok.Data;
import uk.nhs.gpitf.purchasing.repositories.PatientCountRepository;
import uk.nhs.gpitf.purchasing.repositories.PatientCountRunRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="procurement", schema="purchasing")
@Data
public class Procurement {
	@Transient
	@JsonIgnore
	PatientCountRunRepository patientCountRunRepository;
	
	@Transient
	@JsonIgnore
	PatientCountRepository patientCountRepository;
	
	
	
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
	private long id;
	
    @NotBlank
	@Size(max = 255)
	private String name;

	private LocalDateTime startedDate;
    
    @ManyToOne(optional=false)
    @JoinColumn(name = "org_contact")
	private OrgContact orgContact;
    
    @ManyToOne(optional=false)
    @JoinColumn(name = "status")
	private ProcStatus status;

	private LocalDateTime statusLastChangedDate;

	private LocalDateTime lastUpdated;

	private LocalDateTime completedDate;
    
	private String searchKeyword;
	
	private String csvCapabilities;
	
	private String csvPractices;
    
    @OneToMany(
    	fetch = FetchType.LAZY
        //cascade = CascadeType.ALL, 
        //orphanRemoval = true
    )
    @JoinTable(name = "proc_shortlist", schema="purchasing", joinColumns = @JoinColumn(name = "procurement"), inverseJoinColumns = @JoinColumn(name = "id"))
    @JsonIgnore
    private List<ProcShortlist> shortlistItems = new ArrayList<>();
	
	private Integer initialPatientCount;
	
	private LocalDate plannedContractStart;

	private Integer contractMonths;
	
	private Integer patientCount;

}
