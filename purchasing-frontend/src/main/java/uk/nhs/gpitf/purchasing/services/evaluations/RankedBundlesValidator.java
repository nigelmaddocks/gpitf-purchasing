package uk.nhs.gpitf.purchasing.services.evaluations;

import lombok.Data;
import uk.nhs.gpitf.purchasing.entities.ProcSolutionBundle;

import java.util.List;

@Data
public class RankedBundlesValidator {

    public RankedBundlesValidator(boolean validationFailed, String validationFailedMessage) {
        this.validationFailed = validationFailed;
        this.validationFailedMessage = validationFailedMessage;
    }

    public RankedBundlesValidator(boolean validationFailed, List<ProcSolutionBundle> bundles) {
        this.validationFailed = validationFailed;
        this.bundles = bundles;
    }

    private boolean validationFailed;
    private String validationFailedMessage;
    private List<ProcSolutionBundle> bundles;

}
