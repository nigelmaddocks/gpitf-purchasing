package uk.nhs.gpitf.purchasing.entities.swagger;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.BeanUtils;

import io.swagger.client.model.Solutions;
import lombok.Data;

@Data
public class SolutionEx2 extends Solutions {
	private boolean foundation;
	private BigDecimal price;
	
	public SolutionEx2 () {
	}
	
	public SolutionEx2 (Solutions solutions) {
		BeanUtils.copyProperties(solutions, this);
	}
	
	public SolutionEx2 (Solutions solutions, boolean foundation) {
		this(solutions);
		this.foundation = foundation;
	}
}
