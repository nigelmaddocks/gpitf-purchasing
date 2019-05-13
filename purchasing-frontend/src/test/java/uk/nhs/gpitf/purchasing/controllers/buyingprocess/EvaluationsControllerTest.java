package uk.nhs.gpitf.purchasing.controllers.buyingprocess;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import uk.nhs.gpitf.purchasing.controllers.evaluations.EvaluationsController;
import uk.nhs.gpitf.purchasing.services.evaluations.EvaluationsServiceParameterObject;
import uk.nhs.gpitf.purchasing.services.evaluations.IEvaluationService;
import uk.nhs.gpitf.purchasing.utils.IBreadCrumbsWrapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class EvaluationsControllerTest {

    @InjectMocks
    private EvaluationsController underTest;

    @Mock
    private IBreadCrumbsWrapper breadCrumbsWrapper;

    @Mock
    private IEvaluationService evaluationService;

    @Test
    public void testInitialiseScreen1_whenOffCatalogCompetitionType() throws Exception {
        Mockito.when(evaluationService.setUpEvaluationsScreen1(Mockito.any(EvaluationsServiceParameterObject.class))).thenReturn("buying-process/evaluationsOffCatScreen1");
        MockMvc mockMvc = standaloneSetup(underTest).build();
        MockHttpServletRequestBuilder mockBuilder = post("/buyingprocess/evaluations/1");
        mockMvc.perform(mockBuilder).andExpect(view().name("buying-process/evaluationsOffCatScreen1"));
    }

    @Test
    public void testInitialiseScreen1_whenOnCatalogCompetitionType() throws Exception {
        Mockito.when(evaluationService.setUpEvaluationsScreen1(Mockito.any(EvaluationsServiceParameterObject.class))).thenReturn("buying-process/evaluationsOnCatScreen1");
        MockMvc mockMvc = standaloneSetup(underTest).build();
        MockHttpServletRequestBuilder mockBuilder = post("/buyingprocess/evaluations/2");
        mockMvc.perform(mockBuilder).andExpect(view().name("buying-process/evaluationsOnCatScreen1"));
    }

    @Test
    public void testSaveWeightings() throws Exception {
        Mockito.when(evaluationService.submitScreen1Form(Mockito.any(EvaluationsServiceParameterObject.class))).thenReturn("redirect:/buyingprocess/1/solutionByCapability");
        MockMvc mockMvc = standaloneSetup(underTest).build();
        MockHttpServletRequestBuilder mockBuilder = post("/buyingprocess/evaluations/weightings/1");
        mockMvc.perform(mockBuilder).andExpect(view().name("redirect:/buyingprocess/1/solutionByCapability"));
    }

    @Test
    public void testInitialseScreen2() throws Exception {
        Mockito.when(evaluationService.setUpEvaluationsScreen2(Mockito.any(EvaluationsServiceParameterObject.class))).thenReturn("buying-process/evaluationsScreen2");
        MockMvc mockMvc = standaloneSetup(underTest).build();
        MockHttpServletRequestBuilder mockBuilder = post("/buyingprocess/solutionsReview/1");
        mockMvc.perform(mockBuilder).andExpect(view().name("buying-process/evaluationsScreen2"));
    }

    @Test
    public void testSaveScores() throws Exception {
        Mockito.when(evaluationService.saveScoresForEachBundle(Mockito.any(EvaluationsServiceParameterObject.class))).thenReturn("unknown");
        MockMvc mockMvc = standaloneSetup(underTest).build();
        MockHttpServletRequestBuilder mockBuilder = post("/buyingprocess/evaluations/scores/1");
        mockMvc.perform(mockBuilder).andExpect(view().name("unknown"));
    }

}