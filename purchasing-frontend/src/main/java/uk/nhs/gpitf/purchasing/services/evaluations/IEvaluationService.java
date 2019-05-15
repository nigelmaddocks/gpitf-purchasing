package uk.nhs.gpitf.purchasing.services.evaluations;

import uk.nhs.gpitf.purchasing.exceptions.InvalidCriterionException;

public interface IEvaluationService {
    String setUpEvaluationsScreen1(EvaluationsServiceParameterObject espo) throws InvalidCriterionException;

    String submitScreen1Form(EvaluationsServiceParameterObject espo);

    String setUpEvaluationsScreen2(EvaluationsServiceParameterObject espo);

    String saveScoresForEachBundle(EvaluationsServiceParameterObject espo);
}