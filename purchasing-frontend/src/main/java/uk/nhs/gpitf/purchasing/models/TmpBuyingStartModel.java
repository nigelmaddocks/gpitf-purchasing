package uk.nhs.gpitf.purchasing.models;

import lombok.Data;
import uk.nhs.gpitf.purchasing.entities.EvaluationTypeEnum;

@Data
public class TmpBuyingStartModel {

    private EvaluationTypeEnum evaluationType;

	private long foundation;


}
