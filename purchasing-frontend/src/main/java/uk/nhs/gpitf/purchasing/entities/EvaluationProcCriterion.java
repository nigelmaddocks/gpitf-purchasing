package uk.nhs.gpitf.purchasing.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import javax.validation.constraints.Size;

@Entity
@Table(name="evaluation_proc_criterion", schema="purchasing")
@Data
@NoArgsConstructor
public class EvaluationProcCriterion {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    private long weightingPercent;
    private Long procurement;
    private Integer offCatCriterion;
    private Integer tolerance;
    private Integer seq;

    @Size(max = 255)
    private String name;

    public EvaluationProcCriterion(EvaluationProcCriterion evaluationProcCriterion) {
        super();
        this.id = evaluationProcCriterion.id;
        this.weightingPercent = evaluationProcCriterion.weightingPercent;
        this.procurement = evaluationProcCriterion.procurement;
        this.offCatCriterion = evaluationProcCriterion.offCatCriterion;
        this.tolerance = evaluationProcCriterion.tolerance;
        this.seq = evaluationProcCriterion.seq;
        this.name = evaluationProcCriterion.name;
    }
}

