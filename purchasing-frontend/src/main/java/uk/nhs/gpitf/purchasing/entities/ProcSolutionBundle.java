package uk.nhs.gpitf.purchasing.entities;

import lombok.Data;
import uk.nhs.gpitf.purchasing.entities.swagger.SolutionEx2;
import uk.nhs.gpitf.purchasing.utils.GUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="proc_solution_bundle", schema="purchasing")
@Data
public class ProcSolutionBundle {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
	private long id;
    
    @ManyToOne(optional=false)
    @JoinColumn(name = "procurement")
	private Procurement procurement;

    private BigDecimal evaluationScorePercent;
    
    @OneToMany(
    //	fetch = FetchType.EAGER
    )
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "proc_solution_bundle_item", schema="purchasing", joinColumns = @JoinColumn(name = "bundle"), inverseJoinColumns = @JoinColumn(name = "id"))
    //@JsonIgnore
    private List<ProcSolutionBundleItem> bundleItems = new ArrayList<>();

    
    public ProcSolutionBundle() {
    	
    }
    
    public ProcSolutionBundle(Procurement procurement, SolutionEx2 solution) {
    	this.procurement = procurement;
    	ProcSolutionBundleItem item = new ProcSolutionBundleItem();
    	item.setSolution(solution);
    	item.setSolutionId(solution.getId());
    	item.setBundle(this);
    	this.bundleItems.add(item);
    }    
    
	public String getName() {
		String name = "";
		for (var item : bundleItems) {
			if (item.getSolution() != null) {
				name += " + " + item.getSolution().getName();
			} else
			if (GUtils.nullToString(item.getAdditionalService()).length() > 0) {
				name += " + " + item.getAdditionalService();
			}
		}
		if (name.length() > 3) {
			name = name.substring(3);
		}
		
		return name;
	}
	
	public BigDecimal getPrice() {
		BigDecimal price = new BigDecimal(0.0d);
		for (var item : bundleItems) {
			if (item.getSolution() != null) {
				price = price.add(item.getSolution().getPrice());
			}
		}			
		
		return price;
	}
	
	public boolean isFoundation() {
		for (var item : bundleItems) {
			if (item.getSolution() != null && item.getSolution().isFoundation()) {
				return true;
			}
		}			
		
		return false;
	}
}
