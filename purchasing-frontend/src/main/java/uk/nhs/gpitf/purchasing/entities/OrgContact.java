package uk.nhs.gpitf.purchasing.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="org_contact", schema="purchasing")
@Data
public class OrgContact {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
	private long id;
    
    @ManyToOne(optional=false)
    @JoinColumn(name = "organisation")
	private Organisation organisation;    
    
    @ManyToOne(optional=false)
    @JoinColumn(name = "contact")
	private Contact contact;    
	
	private boolean deleted;
    
    @OneToMany(
    	fetch = FetchType.EAGER,
        //cascade = CascadeType.ALL, 
        orphanRemoval = true
    )
    @JoinTable(name = "org_contact_role", schema="purchasing", joinColumns = @JoinColumn(name = "org_contact"), inverseJoinColumns = @JoinColumn(name = "id"))
    private List<OrgContactRole> orgContactRoles = new ArrayList<>();

}
