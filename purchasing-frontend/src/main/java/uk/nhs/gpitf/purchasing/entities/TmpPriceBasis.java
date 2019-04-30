package uk.nhs.gpitf.purchasing.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="tmp_price_basis", schema="purchasing")
@Data
public class TmpPriceBasis {
    @Id
	private long id;
 
    private String name;
	
    @ManyToOne(optional=true)
    @JoinColumn(name = "unit1")
	private TmpUnitType unit1;
	
    @ManyToOne(optional=true)
    @JoinColumn(name = "unit2")
	private TmpUnitType unit2;

    private String calculation;
    
    private boolean tiered;
    
    public boolean isDependentOnUnits() {
    	String compactCalc = calculation.replace(" ", "");
    	return compactCalc.contains("*{UNITS}") || compactCalc.contains("{UNITS}*");
    }
    
    public boolean isDependentOnTerm() {
    	return isDependentOnTermMonths() || isDependentOnTermYears();
    }
    
    
    public boolean isDependentOnTermMonths() {
    	String compactCalc = calculation.replace(" ", "");
    	return compactCalc.contains("*{MONTHS}") || compactCalc.contains("{MONTHS}*"); 
    }
    
    public boolean isDependentOnTermYears() {
    	String compactCalc = calculation.replace(" ", "");
    	return compactCalc.contains("*{YEARS}")  || compactCalc.contains("{YEARS}*");
    }

    public boolean isFixedPrice() {
    	return !isDependentOnUnits() && !isDependentOnTerm();
    }
}
