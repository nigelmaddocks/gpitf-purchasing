package uk.nhs.gpitf.purchasing.models;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
public class SearchListProcurementsModel {

    String openProcNameSearchField;
    String completedProcNameSearchField;
    String openProcStatusSearchField;

    // These represent the procstatus constants, 
    // an enum appears to be the only way to get Java fields into a thymeleaf dropdown
    // ok for now, but refactor procstatus to use enums 
    // or switch to an angular/react solution where
    // the FE dropdown gets the procstatus model from a REST call.
    public enum Status {
        DRAFT, LONGLIST, SHORTLIST, INTERNAL_COMPETITION, EXTERNAL_TENDER, CONTRACT_OFFERED
    }

}
