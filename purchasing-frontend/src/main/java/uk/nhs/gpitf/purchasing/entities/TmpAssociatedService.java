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

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Entity
@Table(name="tmp_associated_service", schema="purchasing")
@Data
public class TmpAssociatedService {
    @Id
	private long id;
    
	private String associatedServiceId;

    private String solutionId;

    private String additionalService;
	
    @NotBlank
	@Size(max = 255)
	private String name;
}
