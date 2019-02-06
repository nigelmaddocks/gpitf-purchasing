package uk.nhs.gpitf.purchasing.entities;

import lombok.Data;
import uk.nhs.gpitf.purchasing.repositories.PatientCountRepository;
import uk.nhs.gpitf.purchasing.repositories.PatientCountRunRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="proc_shortlist", schema="purchasing")
@Data
public class ProcShortlist {
	
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
	private long id;
	
    @ManyToOne(optional=false)
    @JoinColumn(name = "procurement")
	private Procurement procurement;
    
    private String solutionId;
    
    private boolean removed;
	
    @ManyToOne(optional=true)
    @JoinColumn(name = "removal_reason")
	private ProcShortlistRemovalReason removalReason;
    
	private String removalReasonText;
}
