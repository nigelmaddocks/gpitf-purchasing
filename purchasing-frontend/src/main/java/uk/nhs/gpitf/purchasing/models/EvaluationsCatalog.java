package uk.nhs.gpitf.purchasing.models;

import lombok.Data;

import java.util.List;

@Data
public class EvaluationsCatalog {
    List<Evaluation> evaluations;
    Long procurementId;
}
