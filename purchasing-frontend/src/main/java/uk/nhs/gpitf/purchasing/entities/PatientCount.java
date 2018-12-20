package uk.nhs.gpitf.purchasing.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="patient_count", schema="purchasing")
@Data
public class PatientCount {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
	private long id;
    
    @ManyToOne(optional=false)
    @JoinColumn(name = "run")
	private PatientCountRun run;
    
    @ManyToOne(optional=false)
    @JoinColumn(name = "org")
	private Organisation org;
	
    @NotNull
	private Integer patientCount;
}
