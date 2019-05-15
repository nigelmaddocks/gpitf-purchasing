package uk.nhs.gpitf.purchasing.entities;

import java.io.Serializable;
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

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Table(name="org_contact", schema="purchasing")
@Data
public class OrgContact implements Serializable {
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
    @JsonIgnore
    private List<OrgContactRole> orgContactRoles = new ArrayList<>();

    public String getRolesAsString() {
		ArrayList<Role> arlRoles = new ArrayList<>();
		for (var orc : getOrgContactRoles()) {
			if (!orc.isDeleted()) {
				arlRoles.add(orc.getRole());
			}
		}
		String sRoles = "";
		for (var role : arlRoles) {
			sRoles += ", " + role.getName();
		}
		if (sRoles.length() > 2) {
			sRoles = sRoles.substring(2);
		} else {
			sRoles = "none";
		}
		return sRoles;
	}

}
