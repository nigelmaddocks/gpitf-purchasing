package uk.nhs.gpitf.purchasing.services.evaluations;

import lombok.Data;
import uk.nhs.gpitf.purchasing.entities.Procurement;

@Data
public class ProcurementValidator {
    public ProcurementValidator(boolean securityFailed) {
        this.securityFailed = securityFailed;
    }
    public ProcurementValidator(boolean securityPassed, Procurement procurement) {
        this.securityPassed = securityPassed;
        this.procurement = procurement;
    }
    private boolean securityFailed;
    private boolean securityPassed;
    private String securityFailedMessage;
    private Procurement procurement;
}