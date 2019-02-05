package uk.nhs.gpitf.purchasing.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Entity
@Table(name="proc_shortlist_removal_reason", schema="purchasing")
@Data
public class ProcShortlistRemovalReason {
	
	public static final long OTHER = 90;

	
    @Id
	private long id;
	
    @NotBlank
	@Size(max = 255)
	private String name;
    
}
