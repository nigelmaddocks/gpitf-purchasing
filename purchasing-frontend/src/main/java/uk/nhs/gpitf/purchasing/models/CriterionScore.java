package uk.nhs.gpitf.purchasing.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Getter
@Setter
public class CriterionScore {

    Integer score;
    private String criterion;
    private Integer criterionId;

}
