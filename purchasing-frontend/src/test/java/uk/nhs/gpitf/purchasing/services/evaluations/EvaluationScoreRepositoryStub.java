package uk.nhs.gpitf.purchasing.services.evaluations;

import org.springframework.stereotype.Component;
import uk.nhs.gpitf.purchasing.entities.EvaluationScoreValue;
import uk.nhs.gpitf.purchasing.repositories.EvaluationScoreRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component("evaluationScoreRepositoryStub")
public class EvaluationScoreRepositoryStub implements EvaluationScoreRepository {
    @Override
    public <S extends EvaluationScoreValue> S save(S s) {
        return (S) new EvaluationScoreValue();
    }

    @Override
    public <S extends EvaluationScoreValue> Iterable<S> saveAll(Iterable<S> iterable) {
        return new ArrayList<>();
    }

    @Override
    public Optional<EvaluationScoreValue> findById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Integer integer) {
        return false;
    }

    @Override
    public Iterable<EvaluationScoreValue> findAll() {
        List<EvaluationScoreValue> list = new ArrayList<>();
        EvaluationScoreValue evaluationScoreValue = new EvaluationScoreValue();
        evaluationScoreValue.setName("stubby");
        evaluationScoreValue.setScore(1);
        list.add(evaluationScoreValue);
        return list;
    }

    @Override
    public Iterable<EvaluationScoreValue> findAllById(Iterable<Integer> iterable) {
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
    public void delete(EvaluationScoreValue evaluationScoreValue) {

    }

    @Override
    public void deleteAll(Iterable<? extends EvaluationScoreValue> iterable) {

    }

    @Override
    public void deleteAll() {

    }
}
