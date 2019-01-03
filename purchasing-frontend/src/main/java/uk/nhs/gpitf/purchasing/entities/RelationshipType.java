package uk.nhs.gpitf.purchasing.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import lombok.Data;

@Entity
@Table(name="relationship_type", schema="purchasing")
@Data
public class RelationshipType {
	public static final long CCG_TO_PRACTICE = 1;
	public static final long CSU_TO_CCG = 2;
	
    @Id
	private long id;
    
    @Size(max = 255)
    private String name;
    
    @ManyToOne(optional=false)
    @JoinColumn(name = "parent_org_type")
    private OrgType parentOrgType;
    
    @ManyToOne(optional=false)
    @JoinColumn(name = "child_org_type")
    private OrgType childOrgType;
}
