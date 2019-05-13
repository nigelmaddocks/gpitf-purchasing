package uk.nhs.gpitf.purchasing.models;

import lombok.Data;

import uk.nhs.gpitf.purchasing.entities.ProcSolutionBundle;

import java.util.List;

@Data
public class EvaluationsModel {

    private Long procurementId;
    private List<String> offCatCriterion;
    private List<Integer> scores;
    private List<Evaluation> evaluations;
    private List<BundleScoring> bundleScorings;

}

