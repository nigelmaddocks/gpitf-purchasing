package uk.nhs.gpitf.purchasing.services.buying.process;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.nhs.gpitf.purchasing.entities.ProcStatus;
import uk.nhs.gpitf.purchasing.entities.Procurement;
import uk.nhs.gpitf.purchasing.models.ListProcurementsModel;
import uk.nhs.gpitf.purchasing.models.SearchListProcurementsModel;
import uk.nhs.gpitf.purchasing.repositories.OrgContactRepository;
import uk.nhs.gpitf.purchasing.services.IProcStatusService;
import uk.nhs.gpitf.purchasing.services.IProcurementService;
import uk.nhs.gpitf.purchasing.services.ProcStatusService;
import uk.nhs.gpitf.purchasing.services.ProcurementService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Optional.of;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static uk.nhs.gpitf.purchasing.entities.ProcStatus.COMPLETED;

;

@Component("procurementsFilteringService")
public class ProcurementsFilteringService implements IProcurementsFilteringService {

    private final OrgContactRepository orgContactRepository;
    private final IProcurementService procurementService;
    private final IProcStatusService procStatusService;

    @Autowired
    public ProcurementsFilteringService(OrgContactRepository orgContactRepository, IProcurementService procurementService, IProcStatusService procStatusService) {
        this.orgContactRepository = orgContactRepository;
        this.procurementService = procurementService;
        this.procStatusService = procStatusService;
    }

    @Override
    public ListProcurementsModel filterProcurements(Long orgContactId, SearchListProcurementsModel searchListProcurementsModel) {
        ListProcurementsModel filteredProcurements = new ListProcurementsModel();

        filteredProcurements.setOrgContactId(orgContactId);
        filteredProcurements.setOrgContact(orgContactRepository.findById(orgContactId).get());
        filteredProcurements.setStatusFilter(getProcStatusesExceptCompletedAndDeleted());

        filteredProcurements.setCompletedProcurements(filterCompletedProcurements(orgContactId, searchListProcurementsModel));
        filteredProcurements.setOpenProcurements(filterOpenProcurements(searchListProcurementsModel, orgContactId));

        return filteredProcurements;
    }

    private List<Procurement> filterCompletedProcurements(Long orgContactId, SearchListProcurementsModel searchListProcurementsModel) {
        Optional<List<Procurement>> completedLst = of(procurementService.getAllByOrgContactAndStatusOrderByLastUpdatedDesc(orgContactId, COMPLETED));
        return filterProcurementsByName(completedLst.get(), searchListProcurementsModel.getCompletedProcNameSearchField());
    }

    private List<Procurement> filterOpenProcurements(SearchListProcurementsModel searchListProcurementsModel, Long orgContactId) {
        Optional<List<Procurement>> uncompletedList = of(procurementService.getUncompletedByOrgContactOrderByLastUpdated(orgContactId));

        List<Procurement> filteredByNameUncompletedList = new ArrayList<>(); // will invoke gc,naughty naughty!!
        List<Procurement> filteredByStatusAndNameUncompletedList = new ArrayList<>(); // will invoke gc,naughty naughty!!

        if (uncompletedList.isPresent()) {
            String nameSearchString = searchListProcurementsModel.getOpenProcNameSearchField();
            filteredByNameUncompletedList = filterProcurementsByName(uncompletedList.get(), nameSearchString);
        }

        if (!filteredByNameUncompletedList.isEmpty()) {
            String statusSearchString = searchListProcurementsModel.getOpenProcStatusSearchField();
            filteredByStatusAndNameUncompletedList = filterProcurementsByStatus(filteredByNameUncompletedList, statusSearchString);
        }

        return filteredByStatusAndNameUncompletedList;
    }

    private List<Procurement> filterProcurementsByName(List<Procurement> unfiltered, String nameSearchString) {
        return unfiltered.stream().parallel()
                .filter(procurement -> {
                    String procurementNameLowerCased = procurement.getName().toLowerCase();
                    return isEmpty(nameSearchString) || procurementNameLowerCased.contains(nameSearchString.toLowerCase());
                }).collect(Collectors.toList());
    }

    private List<Procurement> filterProcurementsByStatus(List<Procurement> unfiltered, String statusSearchString) {
        return unfiltered.stream().parallel()
                .filter(procurement -> {
                    boolean allOptionSelecteddFromDropDown = statusSearchString.equalsIgnoreCase("ALL");
                    boolean otherOptionSelectedFromDropDown = String.valueOf(procurement.getStatus().getId()).equals(statusSearchString);
                    return allOptionSelecteddFromDropDown || otherOptionSelectedFromDropDown;
                })
                .collect(Collectors.toList());
    }

    private List<ProcStatus> getProcStatusesExceptCompletedAndDeleted() {
        return procStatusService.getAll().parallelStream()
                .filter(status -> {
                    boolean notCompleted = status.getId() != COMPLETED;
                    boolean notDeleted = status.getId() != ProcStatus.DELETED;
                    return notCompleted && notDeleted;
                })
                .collect(Collectors.toList());
    }

}