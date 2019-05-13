package uk.nhs.gpitf.purchasing.services.evaluations;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import uk.nhs.gpitf.purchasing.entities.EvaluationBundleScore;
import uk.nhs.gpitf.purchasing.repositories.EvaluationBundleScoreRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.util.ReflectionTestUtils.setField;

@Component("evaluationBundleScoreRepositoryStub")
public class EvaluationBundleScoreRepositoryStub implements EvaluationBundleScoreRepository {

    @Override
    public <S extends EvaluationBundleScore> S save(S s) {
        return (S) new EvaluationBundleScore();
    }

    @Override
    public <S extends EvaluationBundleScore> Iterable<S> saveAll(Iterable<S> iterable) {
        List<EvaluationBundleScore> evaluationBundleScores = (List<EvaluationBundleScore>) iterable;
        if(evaluationBundleScores.get(0).getScore()==999) {
            throw new DataIntegrityViolationException("");
        }
        setField(EvaluationsServiceTest.class, "scoresPersistedToDB", true);
        return new ArrayList<>();
    }

    @Override
    public Optional<EvaluationBundleScore> findById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Integer integer) {
        return false;
    }

    @Override
    public Iterable<EvaluationBundleScore> findAll() {
        return new ArrayList<>();
    }

    @Override
    public Iterable<EvaluationBundleScore> findAllById(Iterable<Integer> iterable) {
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
    public void delete(EvaluationBundleScore evaluationBundleScore) {

    }

    @Override
    public void deleteAll(Iterable<? extends EvaluationBundleScore> iterable) {

    }

    @Override
    public void deleteAll() {

    }

}