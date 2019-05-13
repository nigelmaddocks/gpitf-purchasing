package uk.nhs.gpitf.purchasing.services.evaluations;

import uk.nhs.gpitf.purchasing.repositories.*;

public interface IRepositoryFetcher {

    EvaluationCriterionTypeRepository getEvaluationCriterionTypeRepository();

    EvaluationToleranceRepository getEvaluationToleranceRepository();

    EvaluationProcCriterionRepository getEvaluationProcCriterionRepository();

    ProcurementRepository getProcurementRepository();

    OffCatCriterionRepository getOffCatCriterionRepository();

    EvaluationScoreRepository getEvaluationScoreRepository();

    EvaluationBundleScoreRepository getEvaluationBundleScoreRepository();

    ProcSolutionBundleRepository getProcSolutionBundleRepository();

}