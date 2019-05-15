package uk.nhs.gpitf.purchasing.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name="evaluation_criterion_type", schema="purchasing")
@Data
public class EvaluationCriterionType {

    @Id
    private long id;

    @Size(max = 255)
    private String name;

}
