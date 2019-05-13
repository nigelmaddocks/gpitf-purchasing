package uk.nhs.gpitf.purchasing.controllers.evaluations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.nhs.gpitf.purchasing.exceptions.InvalidCriterionException;
import uk.nhs.gpitf.purchasing.models.EvaluationsModel;
import uk.nhs.gpitf.purchasing.services.evaluations.EvaluationsServiceParameterObject;
import uk.nhs.gpitf.purchasing.services.evaluations.IEvaluationService;
import uk.nhs.gpitf.purchasing.utils.IBreadCrumbsWrapper;

import javax.servlet.http.HttpServletRequest;

@Controller
public class EvaluationsController {

    private static final Logger logger = LoggerFactory.getLogger(EvaluationsController.class);

    private final IEvaluationService evaluationService;
    private final IBreadCrumbsWrapper breadcrumbs;

    @Autowired
    public EvaluationsController(IEvaluationService evaluationService, IBreadCrumbsWrapper breadCrumbsWrapper) {
        this.evaluationService = evaluationService;
        this.breadcrumbs = breadCrumbsWrapper;
    }

    @Value("${sysparam.shortlist.max}")
    private String SHORTLIST_MAX;

    @RequestMapping(value = "/buyingprocess/evaluations/{procurementId}", method = { RequestMethod.GET, RequestMethod.POST })
    @Transactional
    public String initialiseScreen1 (@PathVariable long procurementId, Model model, RedirectAttributes attr, HttpServletRequest request,
                                         @ModelAttribute("evaluationsModel") EvaluationsModel evaluationsModel) throws InvalidCriterionException {

        breadcrumbs.removeTitle("By capability", request);
        breadcrumbs.removeTitle("By keyword", request);
        breadcrumbs.register("Evaluation", request);

        EvaluationsServiceParameterObject espo = EvaluationsServiceParameterObject.builder()
                .attr(attr)
                .request(request)
                .evaluationsModel(evaluationsModel)
                .model(model)
                .procurementId(procurementId).build();

        return evaluationService.setUpEvaluationsScreen1(espo);
    }


    @RequestMapping(value = "/buyingprocess/evaluations/weightings/{procurementId}", method = { RequestMethod.GET, RequestMethod.POST })
    public String saveWeightings(@PathVariable Long procurementId, HttpServletRequest request,
                                    @ModelAttribute("evaluationsModel") EvaluationsModel evaluationsModel,
                                    Model model,
                                    RedirectAttributes attr) {

        EvaluationsServiceParameterObject espo = EvaluationsServiceParameterObject.builder()
                .evaluationsModel(evaluationsModel)
                .model(model)
                .request(request)
                .attr(attr)
                .procurementId(procurementId)
                .build();

        return evaluationService.submitScreen1Form(espo);
    }

    @RequestMapping(value = "/buyingprocess/solutionsReview/{procurementId}", method = { RequestMethod.GET, RequestMethod.POST })
    public String initialiseScreen2(@PathVariable Long procurementId, HttpServletRequest request,
                                    @ModelAttribute("evaluationsModel") EvaluationsModel evaluationsModel,
                                    Model model,
                                    RedirectAttributes attr) {

        EvaluationsServiceParameterObject espo = EvaluationsServiceParameterObject.builder()
                .evaluationsModel(evaluationsModel)
                .model(model)
                .request(request)
                .attr(attr)
                .procurementId(procurementId)
                .build();

        return evaluationService.setUpEvaluationsScreen2(espo);//TODO plug into solutions review
    }

    @PostMapping(value = {"/buyingprocess/evaluations/scores/{procurementId}"})
    public String saveScores(@PathVariable Long procurementId, HttpServletRequest request,
                                 @ModelAttribute("evaluationsModel") EvaluationsModel evaluationsModel,
                                 Model model,
                                 RedirectAttributes attr) {

        EvaluationsServiceParameterObject espo = EvaluationsServiceParameterObject.builder()
                .evaluationsModel(evaluationsModel)
                .model(model)
                .request(request)
                .attr(attr)
                .procurementId(procurementId)
                .build();

        return evaluationService.saveScoresForEachBundle(espo);
    }

}