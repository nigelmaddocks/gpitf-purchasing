package uk.nhs.gpitf.purchasing.models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import uk.nhs.gpitf.purchasing.entities.EvaluationTypeEnum;

@Data
public class TmpBuyingStartModel {

	@Setter(AccessLevel.NONE)
	private EvaluationTypeEnum evaluationType;
    public void setEvaluationType(long evaluationTypeId) {
    	this.evaluationType = EvaluationTypeEnum.getById(evaluationTypeId);
    }

	private long singleSiteContinuity;

	private long foundation;


}
