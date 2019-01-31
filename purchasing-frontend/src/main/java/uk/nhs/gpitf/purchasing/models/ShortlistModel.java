package uk.nhs.gpitf.purchasing.models;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import uk.nhs.gpitf.purchasing.entities.swagger.SolutionEx2;

@Data
public class ShortlistModel {
	private long procurementId;
	private List<SolutionEx2> solutions = new ArrayList<>();
	private int numberOfPractices = 0;
	private int numberOfPatients = 0;
	private LocalDate contractStart = null;
	private LocalDate contractEnd = null;
	
	public BigDecimal getPrice(String solutionId) {
		for (var solution : solutions) {
			if (solution.getId().equals(solutionId)) {
				return solution.getPrice().multiply(BigDecimal.valueOf(numberOfPatients)).setScale(0, RoundingMode.HALF_UP);
			}
		}
		return BigDecimal.ZERO;
	}
}
