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

import java.io.Serializable;

@Entity
@Table(name="org_contact_role", schema="purchasing")
@Data
public class OrgContactRole implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
	private long id;
    
    @ManyToOne(optional=false)
    @JoinColumn(name = "org_contact")
	private OrgContact orgContact;    
    
    @ManyToOne(optional=false)
    @JoinColumn(name = "role")
	private Role role;    
	
	private boolean deleted;
    
}
