package uk.nhs.gpitf.purchasing.services.evaluations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.nhs.gpitf.purchasing.repositories.*;

@Component
public class RepositoryFetcherStub implements IRepositoryFetcher {

    @Autowired
    private EvaluationToleranceRepository evaluationToleranceRepositoryStub;

    @Autowired
    private EvaluationCriterionTypeRepository evaluationCriterionTypeRepositoryStub;

    @Autowired
    private EvaluationProcCriterionRepository evaluationProcCriterionRepositoryStub;

    @Autowired
    private ProcurementRepository procurementRepositoryStub;

    @Autowired
    private OffCatCriterionRepository offCatCriterionRepositoryStub;

    @Autowired
    private EvaluationScoreRepository evaluationScoreRepositoryStub;

    @Autowired
    private EvaluationBundleScoreRepository evaluationBundleScoreRepositoryStub;

    @Autowired
    private ProcSolutionBundleRepositoryStub procSolutionBundleRepositoryStub;

    @Override
    public EvaluationCriterionTypeRepository getEvaluationCriterionTypeRepository() {
        return evaluationCriterionTypeRepositoryStub;
    }

    @Override
    public EvaluationToleranceRepository getEvaluationToleranceRepository() {
        return evaluationToleranceRepositoryStub;
    }

    @Override
    public EvaluationProcCriterionRepository getEvaluationProcCriterionRepository() {
        return evaluationProcCriterionRepositoryStub;
    }

    @Override
    public ProcurementRepository getProcurementRepository() {
        return procurementRepositoryStub;
    }

    @Override
    public OffCatCriterionRepository getOffCatCriterionRepository() {
        return offCatCriterionRepositoryStub;
    }

    @Override
    public EvaluationScoreRepository getEvaluationScoreRepository() {
        return evaluationScoreRepositoryStub;
    }

    @Override
    public EvaluationBundleScoreRepository getEvaluationBundleScoreRepository() {
        return evaluationBundleScoreRepositoryStub;
    }

    @Override
    public ProcSolutionBundleRepository getProcSolutionBundleRepository() {
        return procSolutionBundleRepositoryStub;
    }
}