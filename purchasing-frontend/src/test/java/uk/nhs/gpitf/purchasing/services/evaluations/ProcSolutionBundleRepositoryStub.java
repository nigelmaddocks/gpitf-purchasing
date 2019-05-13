package uk.nhs.gpitf.purchasing.services.evaluations;

import org.springframework.stereotype.Component;
import org.springframework.test.util.ReflectionTestUtils;
import uk.nhs.gpitf.purchasing.entities.ProcSolutionBundle;
import uk.nhs.gpitf.purchasing.entities.Procurement;
import uk.nhs.gpitf.purchasing.repositories.ProcSolutionBundleRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.util.ReflectionUtils.setField;

@Component("procSolutionBundleRepositoryStub")
public class ProcSolutionBundleRepositoryStub implements ProcSolutionBundleRepository {

    @Override
    public List<ProcSolutionBundle> findAllByProcurement(Procurement p) {
        ProcSolutionBundle p1 = new ProcSolutionBundle();
        List<ProcSolutionBundle> list = new ArrayList<>();
        list.add(p1);
        return list;
    }

    @Override
    public <S extends ProcSolutionBundle> S save(S s) {
        ReflectionTestUtils.setField(EvaluationsServiceTest.class, "evaluationScorePercent", s.getEvaluationScorePercent().intValue());
        return (S) new ProcSolutionBundle();
    }

    @Override
    public <S extends ProcSolutionBundle> Iterable<S> saveAll(Iterable<S> iterable) {
        return new ArrayList<>();
    }

    @Override
    public Optional<ProcSolutionBundle> findById(Long aLong) {
        ProcSolutionBundle procSolutionBundle = new ProcSolutionBundle();
        return Optional.of(procSolutionBundle);
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public Iterable<ProcSolutionBundle> findAll() {
        return new ArrayList<>();
    }

    @Override
    public Iterable<ProcSolutionBundle> findAllById(Iterable<Long> iterable) {
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
    public void delete(ProcSolutionBundle procSolutionBundle) {

    }

    @Override
    public void deleteAll(Iterable<? extends ProcSolutionBundle> iterable) {

    }

    @Override
    public void deleteAll() {

    }

}