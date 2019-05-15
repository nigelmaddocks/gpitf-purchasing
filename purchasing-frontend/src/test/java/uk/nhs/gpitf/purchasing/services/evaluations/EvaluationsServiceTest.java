package uk.nhs.gpitf.purchasing.services.evaluations;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import uk.nhs.gpitf.purchasing.exceptions.InvalidCriterionException;
import uk.nhs.gpitf.purchasing.models.BundleScoring;
import uk.nhs.gpitf.purchasing.models.CriterionScore;
import uk.nhs.gpitf.purchasing.models.Evaluation;
import uk.nhs.gpitf.purchasing.models.EvaluationsModel;
import uk.nhs.gpitf.purchasing.services.OnboardingServiceStub;
import uk.nhs.gpitf.purchasing.services.buying.process.OffCatCriterionRepositoryStub;
import uk.nhs.gpitf.purchasing.services.buying.process.OrgContactRepositoryStub;
import uk.nhs.gpitf.purchasing.services.buying.process.ProcurementRepositoryStub;
import uk.nhs.gpitf.purchasing.utils.HttpServletRequestStub;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes={
        OrgContactRepositoryStub.class, ProcurementRepositoryStub.class, EvaluationService.class,
        EvaluationProcCriterionRepositoryStub.class, OffCatCriterionRepositoryStub.class, OnboardingServiceStub.class,
        EvaluationScoreRepositoryStub.class, EvaluationBundleScoreRepositoryStub.class, ProcSolutionBundleRepositoryStub.class,
        EvaluationCriterionTypeRepositoryStub.class, EvaluationToleranceRepositoryStub.class, RepositoryFetcherStub.class
})
public class EvaluationsServiceTest {

    @Autowired
    private IEvaluationService evaluationService;

    private static boolean criteriaPersistedToDB;
    private static boolean scoresPersistedToDB;
    private static boolean toleranceSet;
    private static int idOfOnCatProcurement = 1;
    private static int idOfOffCatProcurement = 2;
    private static int evaluationScorePercent;
    private static int score = 1;
    private static String weighting;

    @Before
    public void setUp() {
        criteriaPersistedToDB = false;
        scoresPersistedToDB = false;
        toleranceSet = false;
        weighting = "100";
        evaluationScorePercent = 0;
        score = 1;
    }

    @Test
    public void testSetUpScreen1_whenOnCatalogPathSelected() throws InvalidCriterionException {
        // given
        String expected = "buying-process/evaluationsOnCatScreen1";
        EvaluationsServiceParameterObject espo = EvaluationsServiceParameterObject.builder()
                .procurementId(idOfOnCatProcurement)
                .evaluationsModel(new EvaluationsModel())
                .model(new ModelStub())
                .request(new HttpServletRequestStub())
                .build();

        // when
        String actual = evaluationService.setUpEvaluationsScreen1(espo);

        // then
        assertEquals(expected, actual);
    }

    @Test
    public void testSetUpScreen1_whenOffCatalogPathSelected() throws InvalidCriterionException {
        // given
        String expected = "buying-process/evaluationsOffCatScreen1";
        EvaluationsServiceParameterObject espo = EvaluationsServiceParameterObject.builder()
                .procurementId(idOfOffCatProcurement)
                .evaluationsModel(new EvaluationsModel())
                .model(new ModelStub())
                .request(new HttpServletRequestStub())
                .build();

        // when
        String actual = evaluationService.setUpEvaluationsScreen1(espo);

        // then
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSetUpScreen2() throws InvalidCriterionException {
        // given
        String expected = "buying-process/evaluationsScreen2";
        EvaluationsServiceParameterObject espo = EvaluationsServiceParameterObject.builder()
                .procurementId(idOfOnCatProcurement)
                .evaluationsModel(generateStubEvaluationsModel(score, weighting))
                .model(new ModelStub())
                .request(new HttpServletRequestStub())
                .build();

        // when
        String actual = evaluationService.setUpEvaluationsScreen2(espo);

        // then
        assertEquals(expected, actual);
    }

    @Test
    public void testSubmitScreen1Form_persistsCriteriaFromScreen1ToDBReadyForScoring() {
        // given
        EvaluationsServiceParameterObject espo = EvaluationsServiceParameterObject.builder()
                .procurementId(idOfOnCatProcurement)
                .evaluationsModel(generateStubEvaluationsModel(score, weighting))
                .model(new ModelStub())
                .request(new HttpServletRequestStub())
                .build();

        // when
        evaluationService.submitScreen1Form(espo);

        // then
        assertTrue(criteriaPersistedToDB);
    }

    @Test
    public void testSubmitScreen1Form_persistsTolerancesWithCriteria_whenOnCatProcuremetTypeUsed() {
        // given
        EvaluationsServiceParameterObject espo = EvaluationsServiceParameterObject.builder()
                .procurementId(idOfOnCatProcurement)
                .evaluationsModel(generateStubEvaluationsModel(score, weighting))
                .model(new ModelStub())
                .request(new HttpServletRequestStub())
                .build();

        // when
        evaluationService.submitScreen1Form(espo);

        // then
         assertTrue(toleranceSet);
    }

    @Test
    public void testSubmitScreen1Form_persistsTolerancesWithCriteria_whenOffCatProcuremetTypeUsed() {
        // given
        EvaluationsServiceParameterObject espo = EvaluationsServiceParameterObject.builder()
                .procurementId(idOfOffCatProcurement)
                .evaluationsModel(generateStubEvaluationsModel(score, weighting))
                .model(new ModelStub())
                .request(new HttpServletRequestStub())
                .build();

        // when
        evaluationService.submitScreen1Form(espo);

        // then
        assertTrue(toleranceSet);
    }

    @Test
    public void testSaveScores() {
        // given
        String expected = "redirect:/buyingprocess/evaluations/solutionsReview/{procurementId}";
        EvaluationsServiceParameterObject espo = EvaluationsServiceParameterObject.builder()
                .procurementId(idOfOnCatProcurement)
                .evaluationsModel(generateStubEvaluationsModel(score, weighting))
                .model(new ModelStub())
                .request(new HttpServletRequestStub())
                .build();

        // when
        String actual = evaluationService.saveScoresForEachBundle(espo);

        // then
        assertEquals(expected, actual);
    }

    @Test
    public void testSaveScoresUpdatesProcSolutionBundle() {
        // given
        int expected = Integer.valueOf(weighting)*score/5;
        EvaluationsServiceParameterObject espo = EvaluationsServiceParameterObject.builder()
                .procurementId(idOfOnCatProcurement)
                .evaluationsModel(generateStubEvaluationsModel(score, weighting))
                .model(new ModelStub())
                .request(new HttpServletRequestStub())
                .build();

        // when
        String actual = evaluationService.saveScoresForEachBundle(espo);

        // then
        assertEquals(expected, evaluationScorePercent);
    }

    @Test
    public void testSaveScores_reloadsPage_whenNoScoreSelected() {
        // given
        String expected = "buying-process/evaluationsScreen2";
        EvaluationsServiceParameterObject espo = EvaluationsServiceParameterObject.builder()
                .procurementId(idOfOnCatProcurement)
                .evaluationsModel(generateStubEvaluationsModel(999, weighting))
                .model(new ModelStub())
                .bindingResult(new BindingResultStub())
                .request(new HttpServletRequestStub())
                .build();

        // when
        String actual = evaluationService.saveScoresForEachBundle(espo);

        // then
        assertEquals(expected, actual);
    }

    @Test
    public void testSaveScores_persistsScoresToDB() {
        // given
        EvaluationsServiceParameterObject espo = EvaluationsServiceParameterObject.builder()
                .procurementId(idOfOnCatProcurement)
                .evaluationsModel(generateStubEvaluationsModel(score, weighting))
                .model(new ModelStub())
                .bindingResult(new BindingResultStub())
                .request(new HttpServletRequestStub())
                .build();

        // when
        evaluationService.saveScoresForEachBundle(espo);

        // then
        assertTrue(scoresPersistedToDB);
    }

    @Test
    public void testSubmitScreen1Form_redirectsToErrorPage_whenWeightingViolatesTolerance() {
        // given
        String expected = "buying-process/evaluationsOffCatScreen1";
        String invalidWeighting = "0";

        // and
        EvaluationsServiceParameterObject espo = EvaluationsServiceParameterObject.builder()
                .procurementId(idOfOffCatProcurement)
                .evaluationsModel(generateStubEvaluationsModel(score, invalidWeighting))
                .model(new ModelStub())
                .bindingResult(new BindingResultStub())
                .request(new HttpServletRequestStub())
                .build();

        // when
        String actual = evaluationService.submitScreen1Form(espo);

        // then
        assertEquals(expected, actual);
    }

    private static EvaluationsModel generateStubEvaluationsModel(int score, String weighting) {
        Evaluation e = new Evaluation();
        e.setCriteria("StubbyMcStubFace");
        e.setProcurementId(1L);
        e.setScore(String.valueOf(score));
        e.setSeq("1");
        e.setWeighting(weighting);

        List<Evaluation> evaluations = new ArrayList<>();
        evaluations.add(e);

        List<CriterionScore> criterionScores = new ArrayList<>();
        CriterionScore criterionScore = new CriterionScore();
        criterionScore.setScore(score);
        criterionScore.setCriterionId(1);
        criterionScores.add(criterionScore);

        BundleScoring bundleScoring = new BundleScoring();
        bundleScoring.setCriterionScores(criterionScores);

        List<BundleScoring> bundleScorings = new ArrayList<>();
        bundleScorings.add(bundleScoring);

        EvaluationsModel ev = new EvaluationsModel();
        ev.setEvaluations(evaluations);
        ev.setProcurementId(1L);
        ev.setBundleScorings(bundleScorings);

        return ev;
    }


}