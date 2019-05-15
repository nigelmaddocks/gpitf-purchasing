package uk.nhs.gpitf.purchasing.services.buying.process;


import org.springframework.stereotype.Component;
import uk.nhs.gpitf.purchasing.entities.OffCatCriterion;
import uk.nhs.gpitf.purchasing.repositories.OffCatCriterionRepository;

import java.util.ArrayList;
import java.util.Optional;

@Component("offCatCriterionRepositoryStub")
public class OffCatCriterionRepositoryStub implements OffCatCriterionRepository {
    @Override
    public <S extends OffCatCriterion> S save(S s) {
        return null;
    }

    @Override
    public <S extends OffCatCriterion> Iterable<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<OffCatCriterion> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public Iterable<OffCatCriterion> findAll() {
        return new ArrayList<>();
    }

    @Override
    public Iterable<OffCatCriterion> findAllById(Iterable<Long> iterable) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(OffCatCriterion offCatCriterion) {

    }

    @Override
    public void deleteAll(Iterable<? extends OffCatCriterion> iterable) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public Optional<OffCatCriterion> findByName(String name) {
        return Optional.empty();
    }
}
