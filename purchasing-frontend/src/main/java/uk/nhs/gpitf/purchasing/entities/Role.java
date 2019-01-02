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
@Table(name="role", schema="purchasing")
@Data
public class Role {
	
	public static final long PURCHASER = 1;
	public static final long APPROVER = 2;
	public static final long LOCAL_ADMIN = 3;
	public static final long ADMINISTRATOR = 4;

	
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
	private long id;
	
    @NotBlank
	@Size(max = 255)
	private String name;
    
}
