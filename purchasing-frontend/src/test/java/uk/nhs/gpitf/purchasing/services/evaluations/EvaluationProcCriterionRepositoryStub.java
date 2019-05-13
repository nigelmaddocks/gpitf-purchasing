package uk.nhs.gpitf.purchasing.services.evaluations;

import org.springframework.stereotype.Component;
import uk.nhs.gpitf.purchasing.entities.EvaluationProcCriterion;
import uk.nhs.gpitf.purchasing.models.Evaluation;
import uk.nhs.gpitf.purchasing.repositories.EvaluationProcCriterionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.util.ReflectionTestUtils.getField;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@Component("evaluationProcCriterionRepositoryStub")
public class EvaluationProcCriterionRepositoryStub implements EvaluationProcCriterionRepository {
    @Override
    public List<EvaluationProcCriterion> findByProcurement(Long id) {

        EvaluationProcCriterion evaluationProcCriterion = new EvaluationProcCriterion();
        List<EvaluationProcCriterion> evaluationProcCriterionList = new ArrayList<>();
        evaluationProcCriterionList.add(evaluationProcCriterion);
       // return evaluationProcCriterionList;
        return new ArrayList<>();
    }

    @Override
    public <S extends EvaluationProcCriterion> S save(S s) {
        return null;
    }

    @Override
    public <S extends EvaluationProcCriterion> Iterable<S> saveAll(Iterable<S> iterable) {
        setField(EvaluationsServiceTest.class, "criteriaPersistedToDB", true);
        setField(EvaluationsServiceTest.class, "toleranceSet", true);
        return new ArrayList<>();
    }

    @Override
    public Optional<EvaluationProcCriterion> findById(Long aLong) {
        long weighting = Long.valueOf((String) getField(EvaluationsServiceTest.class, "weighting"));
        EvaluationProcCriterion e = new EvaluationProcCriterion();
        e.setWeightingPercent(weighting);
        return Optional.of(e);
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public Iterable<EvaluationProcCriterion> findAll() {
        List<EvaluationProcCriterion> list = new ArrayList<>();
        EvaluationProcCriterion evaluationProcCriterion = new EvaluationProcCriterion();
        evaluationProcCriterion.setProcurement(1L);
        list.add(evaluationProcCriterion);
        return list;
    }

    @Override
    public Iterable<EvaluationProcCriterion> findAllById(Iterable<Long> iterable) {
        return new ArrayList<>();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(EvaluationProcCriterion evaluationProcCriterion) {

    }

    @Override
    public void deleteAll(Iterable<? extends EvaluationProcCriterion> iterable) {

    }

    @Override
    public void deleteAll() {

    }
}
