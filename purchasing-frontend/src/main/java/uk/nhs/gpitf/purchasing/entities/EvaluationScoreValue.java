package uk.nhs.gpitf.purchasing.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name="evaluation_score_value", schema="purchasing")
@Data
public class EvaluationScoreValue {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int score;

    @Size(max = 255)
    private String name;

}
