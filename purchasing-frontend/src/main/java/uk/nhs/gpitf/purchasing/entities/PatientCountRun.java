package uk.nhs.gpitf.purchasing.entities;

import lombok.Data;

import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name="patient_count_run", schema="purchasing")
@Data
public class PatientCountRun {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
	private long id;
	private Date runDate;
	@Size(max = 255)
	private String fileName;
}
