package uk.nhs.gpitf.purchasing.services.evaluations;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.nhs.gpitf.purchasing.repositories.*;

@Getter
@Component
public class RepositoryFetcher implements IRepositoryFetcher {

    @Autowired
    private EvaluationCriterionTypeRepository evaluationCriterionTypeRepository;

    @Autowired
    private EvaluationToleranceRepository evaluationToleranceRepository;

    @Autowired
    private EvaluationProcCriterionRepository evaluationProcCriterionRepository;

    @Autowired
    private ProcurementRepository procurementRepository;

    @Autowired
    private OffCatCriterionRepository offCatCriterionRepository;

    @Autowired
    private EvaluationScoreRepository evaluationScoreRepository;

    @Autowired
    private EvaluationBundleScoreRepository evaluationBundleScoreRepository;

    @Autowired
    private ProcSolutionBundleRepository procSolutionBundleRepository;

}