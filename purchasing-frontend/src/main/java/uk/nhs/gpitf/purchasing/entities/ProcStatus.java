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
@Table(name="proc_status", schema="purchasing")
@Data
public class ProcStatus {
	
	public static final long DRAFT 					= 1;
	public static final long LONGLIST 				= 2;
	public static final long SHORTLIST 				= 3;
	public static final long INTERNAL_COMPETITION 	= 4;
	public static final long EXTERNAL_TENDER 		= 5;
	public static final long CONTRACT_OFFERED 		= 6;
	public static final long COMPLETED 				= 80;
	public static final long DELETED 				= 99;

	
    @Id
	private long id;
	
    @NotBlank
	@Size(max = 255)
	private String name;
    
}
