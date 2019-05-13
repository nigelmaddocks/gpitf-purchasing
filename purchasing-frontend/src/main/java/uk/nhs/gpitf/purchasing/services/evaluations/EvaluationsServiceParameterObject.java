package uk.nhs.gpitf.purchasing.services.evaluations;

import lombok.Builder;
import lombok.Getter;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.nhs.gpitf.purchasing.models.EvaluationsModel;

import javax.servlet.http.HttpServletRequest;

@Builder
@Getter
public class EvaluationsServiceParameterObject {
    private RedirectAttributes attr;
    private HttpServletRequest request;
    private long procurementId;
    private EvaluationsModel evaluationsModel;
    private Model model;
}
