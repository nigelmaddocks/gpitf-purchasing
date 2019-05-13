package uk.nhs.gpitf.purchasing.services.evaluations;

import org.springframework.stereotype.Component;
import uk.nhs.gpitf.purchasing.entities.EvaluationTolerance;
import uk.nhs.gpitf.purchasing.repositories.EvaluationToleranceRepository;

import java.util.ArrayList;
import java.util.Optional;

@Component("evaluationToleranceRepositoryStub")
public class EvaluationToleranceRepositoryStub implements EvaluationToleranceRepository {
    @Override
    public Optional<EvaluationTolerance> findByCompetitionTypeAndCriteriumType(int competitionType, int criteriumType) {
        EvaluationTolerance evaluationTolerance = new EvaluationTolerance();
        evaluationTolerance.setId(1);
        return Optional.of(evaluationTolerance);
    }

    @Override
    public <S extends EvaluationTolerance> S save(S s) {
        return (S) new EvaluationTolerance();
    }

    @Override
    public <S extends EvaluationTolerance> Iterable<S> saveAll(Iterable<S> iterable) {
        return new ArrayList<>();
    }

    @Override
    public Optional<EvaluationTolerance> findById(Integer integer) {
        EvaluationTolerance evaluationTolerance = new EvaluationTolerance();
        evaluationTolerance.setId(1);
        evaluationTolerance.setLowerInclusivePercent(1);
        evaluationTolerance.setUpperInclusivePercent(100);
        return Optional.of(evaluationTolerance);
    }

    @Override
    public boolean existsById(Integer integer) {
        return false;
    }

    @Override
    public Iterable<EvaluationTolerance> findAll() {
        return new ArrayList<>();
    }

    @Override
    public Iterable<EvaluationTolerance> findAllById(Iterable<Integer> iterable) {
        return new ArrayList<>();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Integer integer) {

    }

    @Override
    public void delete(EvaluationTolerance evaluationTolerance) {

    }

    @Override
    public void deleteAll(Iterable<? extends EvaluationTolerance> iterable) {

    }

    @Override
    public void deleteAll() {

    }
}
