package uk.nhs.gpitf.purchasing.entities;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="tmp_solution_price_band", schema="purchasing")
@Data
public class TmpSolutionPriceBand {
    @Id
	private long id;
 
    private String solutionId;
   
	private String associatedService;

    private String additionalService;

    private String unitName;
	
    private Integer lowerLimitIncl;
	
    private Integer upperLimitIncl;
	
    @ManyToOne(optional=false)
    @JoinColumn(name = "price_basis")
	private TmpPriceBasis priceBasis;
    
    private BigDecimal price;
    	
    @ManyToOne(optional=true)
    @JoinColumn(name = "banding_unit_type")
	private TmpUnitType bandingUnitType;
    
}
