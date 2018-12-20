package uk.nhs.gpitf.purchasing.entities;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="org_relationship", schema="purchasing")
@Data
public class OrgRelationship {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
	private long id;
    
    @ManyToOne(optional=false)
    @JoinColumn(name = "parent_org")
	private Organisation parentOrg;
    
    @ManyToOne(optional=false)
    @JoinColumn(name = "child_org")
	private Organisation childOrg;
    
    @ManyToOne(optional=false)
    @JoinColumn(name = "relationship_type")
    private RelationshipType relationshipType;
}
