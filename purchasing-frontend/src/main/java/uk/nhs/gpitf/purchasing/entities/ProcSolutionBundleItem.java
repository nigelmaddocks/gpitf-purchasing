package uk.nhs.gpitf.purchasing.entities;

import lombok.Data;
import uk.nhs.gpitf.purchasing.entities.swagger.SolutionEx2;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.client.model.Capabilities;

@Entity
@Table(name="proc_solution_bundle_item", schema="purchasing")
@Data
public class ProcSolutionBundleItem {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
	private long id;
    
    @ManyToOne(optional=false)
    @JoinColumn(name = "bundle")
    @JsonIgnore
    private ProcSolutionBundle bundle;
    
    @Transient
	private SolutionEx2 solution;
    
    private String solutionId;

    private String additionalService;
    
    @Transient
    private Capabilities[] capabilities;
}
