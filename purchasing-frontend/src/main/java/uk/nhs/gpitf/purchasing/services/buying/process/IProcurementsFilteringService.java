package uk.nhs.gpitf.purchasing.services.buying.process;

import uk.nhs.gpitf.purchasing.models.ListProcurementsModel;

public interface IProcurementsFilteringService {
    ListProcurementsModel filterProcurements(ProcurementsFilteringServiceParameterObject procurementsFilteringServiceParameterObject);
}
