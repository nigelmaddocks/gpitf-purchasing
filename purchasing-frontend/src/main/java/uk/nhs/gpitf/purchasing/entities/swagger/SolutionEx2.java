package uk.nhs.gpitf.purchasing.entities.swagger;

import java.math.BigDecimal;
import java.math.RoundingMode;

import io.swagger.client.model.Solutions;
import lombok.Data;

@Data
public class SolutionEx2 extends Solutions {
	private boolean foundation;
	private BigDecimal price;
	
	public SolutionEx2 (Solutions solutions) {
		this.setId(				solutions.getId());
		this.setName(			solutions.getName());
		this.setDescription(	solutions.getDescription());
		this.setCreatedById(	solutions.getCreatedById());
		this.setCreatedOn(		solutions.getCreatedOn());
		this.setModifiedById(	solutions.getModifiedById());
		this.setModifiedOn(		solutions.getModifiedOn());
		this.setOrganisationId(	solutions.getOrganisationId());
		this.setPreviousId(		solutions.getPreviousId());
		this.setProductPage(	solutions.getProductPage());
		this.setStatus(			solutions.getStatus());
		this.setVersion(		solutions.getVersion());
	}
	
	public SolutionEx2 (Solutions solutions, boolean foundation) {
		this(solutions);
		this.foundation = foundation;
	}
}
