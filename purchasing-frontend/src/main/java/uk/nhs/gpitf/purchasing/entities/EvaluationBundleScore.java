package uk.nhs.gpitf.purchasing.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="evaluation_bundle_score", schema="purchasing")
@Data
public class EvaluationBundleScore {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private Integer bundle;
    private Integer procCriterion;
    private Integer score;
    private Integer scoredBy;
    private Date scoredDate;

}
