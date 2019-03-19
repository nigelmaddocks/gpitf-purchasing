package uk.nhs.gpitf.purchasing.services.buying.process;

import lombok.Builder;
import lombok.Getter;
import uk.nhs.gpitf.purchasing.entities.ProcStatus;
import uk.nhs.gpitf.purchasing.models.SearchListProcurementsModel;
import uk.nhs.gpitf.purchasing.repositories.OrgContactRepository;
import uk.nhs.gpitf.purchasing.services.IProcurementService;
import uk.nhs.gpitf.purchasing.services.ProcStatusService;
import uk.nhs.gpitf.purchasing.services.ProcurementService;
import uk.nhs.gpitf.purchasing.utils.SecurityInfo;
import java.util.Optional;

@Builder
@Getter
public class ProcurementsFilteringServiceParameterObject {

    private Long orgContactId;
    private Optional<Long> optionalOrgContactId;
    private SearchListProcurementsModel searchListProcurementsModel;

}
