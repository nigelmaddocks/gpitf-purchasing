package uk.nhs.gpitf.purchasing.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="org_type", schema="purchasing")
@Getter @Setter
@NoArgsConstructor
public class OrgType {
	
	public static final long PRESCRIBING_PRACTICE = 1;
	public static final long CCG = 2;
	public static final long CSU = 3;
	public static final long SUPPLIER = 4;

	public static final long GP = 5;  // Prescribing Practice sub-type of GP Practice
	public static final long OOH = 6; // Prescribing Practice sub-type of Out of Hours 
	public static final long WIC = 7; // Prescribing Practice sub-type of Walk-in Centre 

	@Id
	private long id;
	@Size(max = 255)
	private String name;
	
}
