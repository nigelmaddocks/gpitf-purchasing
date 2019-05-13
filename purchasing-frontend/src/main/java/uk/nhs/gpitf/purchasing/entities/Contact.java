package uk.nhs.gpitf.purchasing.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name="contact", schema="purchasing")
@Data
public class Contact implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
	private long id;
	
    @NotBlank
    @Email
	@Size(max = 255)
	private String email;
	
    @NotBlank
	@Size(max = 255)
    private String forename;
	
    @NotBlank
	@Size(max = 255)
	private String surname;
	
	private boolean deleted;
 
	public boolean hasAnyPropertySet() {
		return email != null && email.trim().length() > 0
		 || forename != null && forename.trim().length() > 0
		 || surname != null && surname.trim().length() > 0;
	}
	
}
