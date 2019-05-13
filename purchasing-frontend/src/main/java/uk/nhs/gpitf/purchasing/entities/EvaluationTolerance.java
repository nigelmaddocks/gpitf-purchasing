package uk.nhs.gpitf.purchasing.entities;

import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import org.hibernate.annotations.Cache;

@Entity
@Table(name="evaluation_tolerance", schema="purchasing")
@Data
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class EvaluationTolerance {

    @Id
    private Integer id;
    private Integer framework;
    private Integer criteriumType;
    private Integer competitionType;
    private Integer lowerInclusivePercent;
    private Integer upperInclusivePercent;

}
