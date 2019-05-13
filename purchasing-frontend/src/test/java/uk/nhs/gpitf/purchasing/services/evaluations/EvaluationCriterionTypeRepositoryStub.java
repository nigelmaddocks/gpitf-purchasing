package uk.nhs.gpitf.purchasing.services.evaluations;

import org.springframework.stereotype.Component;
import uk.nhs.gpitf.purchasing.entities.EvaluationCriterionType;
import uk.nhs.gpitf.purchasing.repositories.EvaluationCriterionTypeRepository;

import java.util.ArrayList;
import java.util.Optional;

@Component("evaluationCriterionTypeRepositoryStub")
public class EvaluationCriterionTypeRepositoryStub implements EvaluationCriterionTypeRepository {
    @Override
    public Optional<EvaluationCriterionType> findByName(String name) {
        return Optional.empty();
    }

    @Override
    public <S extends EvaluationCriterionType> S save(S s) {
        return null;
    }

    @Override
    public <S extends EvaluationCriterionType> Iterable<S> saveAll(Iterable<S> iterable) {
        return new ArrayList<>();
    }

    @Override
    public Optional<EvaluationCriterionType> findById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Integer integer) {
        return false;
    }

    @Override
    public Iterable<EvaluationCriterionType> findAll() {
        return new ArrayList<>();
    }

    @Override
    public Iterable<EvaluationCriterionType> findAllById(Iterable<Integer> iterable) {
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
    public void delete(EvaluationCriterionType evaluationCriterionType) {

    }

    @Override
    public void deleteAll(Iterable<? extends EvaluationCriterionType> iterable) {

    }

    @Override
    public void deleteAll() {

    }
}
