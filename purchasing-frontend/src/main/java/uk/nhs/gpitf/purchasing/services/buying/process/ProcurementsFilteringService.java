package uk.nhs.gpitf.purchasing.services.buying.process;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import uk.nhs.gpitf.purchasing.entities.ProcStatus;
import uk.nhs.gpitf.purchasing.entities.Procurement;
import uk.nhs.gpitf.purchasing.models.ListProcurementsModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@Component("buyingService")
public class ProcurementsFilteringService implements IProcurementsFilteringService {

    @Override
    public ListProcurementsModel filterProcurements(ProcurementsFilteringServiceParameterObject bpspo) {
        ListProcurementsModel listProcurementsModel = new ListProcurementsModel();
        listProcurementsModel.setOrgContactId(bpspo.getOrgContactId());
        listProcurementsModel.setOrgContact(bpspo.getOrgContactRepository().findById(bpspo.getOrgContactId()).get());

        listProcurementsModel.setCompletedProcurements(filterCompletedProcurements(bpspo));
        listProcurementsModel.setOpenProcurements(filterOpenProcurements(bpspo));

        return listProcurementsModel;
    }

    private List<Procurement> filterCompletedProcurements(ProcurementsFilteringServiceParameterObject bpspo) {
        Optional<List<Procurement>> completedList = Optional.of(bpspo.getProcurementService()
                .getAllByOrgContactAndStatusOrderByLastUpdatedDesc(bpspo.getOrgContactId(), ProcStatus.COMPLETED));
        return filterProcurementsByName(completedList.get(), bpspo.getSearchListProcurementsModel().getCompletedProcNameSearchField());
    }

    private List<Procurement> filterOpenProcurements(ProcurementsFilteringServiceParameterObject bpspo) {
        Optional<List<Procurement>> uncompletedList = Optional.of(bpspo.getProcurementService().getUncompletedByOrgContactOrderByLastUpdated(bpspo.getOrgContactId()));

        List<Procurement> filteredByNameUncompletedList = new ArrayList<>(); // will invoke gc,naughty naughty!!
        List<Procurement> filteredByStatusAndNameUncompletedList = new ArrayList<>(); // will invoke gc,naughty naughty!!

        if(uncompletedList.isPresent()) {
            String nameSearchString = bpspo.getSearchListProcurementsModel().getOpenProcNameSearchField();
            filteredByNameUncompletedList = filterProcurementsByName(uncompletedList.get(),nameSearchString);
        }

        if(!filteredByNameUncompletedList.isEmpty()) {
            String statusSearchString = bpspo.getSearchListProcurementsModel().getOpenProcStatusSearchField();
            filteredByStatusAndNameUncompletedList = filterProcurementsByStatus(filteredByNameUncompletedList, statusSearchString);
        }

        return filteredByStatusAndNameUncompletedList;
    }

    private List<Procurement> filterProcurementsByName(List<Procurement> unfiltered, String nameSearchString) {
        return unfiltered.stream().parallel()
                .filter(procurement -> {
                    return isEmpty(nameSearchString) || procurement.getName().contains(nameSearchString);
                }).collect(Collectors.toList());
    }

    private List<Procurement> filterProcurementsByStatus(List<Procurement> unfiltered, String statusSearchString) {
        return unfiltered.stream().parallel()
                .filter(procurement -> {
                    boolean allOptionSelecteddFromDropDown = statusSearchString.equalsIgnoreCase("ALL");
                    boolean otherOptionSelectedFromDropDown = procurement.getStatus().getName().equalsIgnoreCase(statusSearchString);
                    return  allOptionSelecteddFromDropDown || otherOptionSelectedFromDropDown;
                })
                .collect(Collectors.toList());
    }





}//TODO remove nulls from stubs!!!