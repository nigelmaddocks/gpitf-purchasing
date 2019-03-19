package uk.nhs.gpitf.purchasing.services.buying.process;

import uk.nhs.gpitf.purchasing.models.ListProcurementsModel;
import uk.nhs.gpitf.purchasing.models.SearchListProcurementsModel;

public interface IProcurementsFilteringService {
    ListProcurementsModel filterProcurements(Long orgContactId, SearchListProcurementsModel searchListProcurementsModel);
}
