package uk.nhs.gpitf.purchasing.controllers.evaluations;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import uk.nhs.gpitf.purchasing.controllers.buyingprocess.BuyingProcessController;
import uk.nhs.gpitf.purchasing.entities.CompetitionType;
import uk.nhs.gpitf.purchasing.entities.Procurement;
import uk.nhs.gpitf.purchasing.models.EvaluationsModel;
import uk.nhs.gpitf.purchasing.repositories.ProcurementRepository;
import uk.nhs.gpitf.purchasing.utils.Breadcrumbs;

@Controller 
public class EvaluationsController {
	
	@Autowired
	ProcurementRepository procurementRepository;
	
    private static final Logger logger = LoggerFactory.getLogger(EvaluationsController.class);
    @RequestMapping(value = "/buyingprocess/evaluations/{procurementId}", method = { RequestMethod.GET, RequestMethod.POST })
    @Transactional
    public String initialiseScreen1 (@PathVariable long procurementId, Model model, RedirectAttributes attr, HttpServletRequest request,
                                         @ModelAttribute("evaluationsModel") EvaluationsModel evaluationsModel) {
		Breadcrumbs.register("Evaluation criteria", request);
    	evaluationsModel.setId(procurementId);
    	model.addAttribute("pageModel", evaluationsModel);
    	
    	Procurement procurement = procurementRepository.findById(procurementId).get();
    	if (request.getMethod().equalsIgnoreCase("GET")) {
	    	if (procurement.getCompetitionType() == null || procurement.getCompetitionType().getId() == CompetitionType.OFF_CATALOGUE) {
	    		return "buying-process/evaluationsOffCatScreen1";
	    	} else {
		    	return "buying-process/evaluationsOnCatScreen1";
	    	}
    	} else {
    		return "redirect:"
				+ BuyingProcessController.URL_PATH
        		+ "/" + procurement.getId()
        		+ "/gotoProcurement";
    	}
    }
  

 

    @RequestMapping(value = "/buyingprocess/evaluations/weightings/{procurementId}", method = { RequestMethod.GET, RequestMethod.POST })
    public String initialiseScreen2(@PathVariable Long procurementId, HttpServletRequest request,
                                    @ModelAttribute("evaluationsModel") EvaluationsModel evaluationsModel,
                                    Model model,
                                    RedirectAttributes attr) {
		Breadcrumbs.register("Evaluation scoring", request);
    	evaluationsModel.setId(procurementId);
    	model.addAttribute("pageModel", evaluationsModel);

    	return "buying-process/evaluationsScreen2";
    }

    @PostMapping(value = {"/buyingprocess/evaluations/scores/{procurementId}"})
    public String saveScores(@PathVariable Long procurementId, HttpServletRequest request,
                                 @ModelAttribute("evaluationsModel") EvaluationsModel evaluationsModel,
                                 Model model,
                                 RedirectAttributes attr) {    	
    	evaluationsModel.setId(procurementId);
    	model.addAttribute("pageModel", evaluationsModel);
   	return "buying-process/evaluationsScreen2";
    }
}
