package uk.nhs.gpitf.purchasing.models;

import lombok.Data;
import uk.nhs.gpitf.purchasing.entities.ProcSolutionBundle;

import java.util.List;

@Data
public class BundleScoring {

    private ProcSolutionBundle bundle;
    List<CriterionScore> criterionScores;

}
