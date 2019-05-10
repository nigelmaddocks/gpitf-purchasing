package uk.nhs.gpitf.purchasing.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import javax.validation.constraints.Size;

import lombok.Data;

@Entity
@Table(name="org_solution", schema="purchasing")
@Data
public class OrgSolution {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
	private long id;
    
    @ManyToOne(optional=true)
    @JoinColumn(name = "organisation")
	private Organisation organisation;    
   
    @Size(max = 255)
    private String solution;
    
    @ManyToOne(optional=true)
    @JoinColumn(name = "legacySolution")
	private LegacySolution legacySolution;    
    
	private LocalDate orderedDate;
    
	private LocalDate installedDate;
	
	private boolean deleted;
    
	private LocalDate contractEndDate;

}
