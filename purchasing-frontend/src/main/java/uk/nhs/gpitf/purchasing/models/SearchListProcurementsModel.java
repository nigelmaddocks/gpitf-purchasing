package uk.nhs.gpitf.purchasing.models;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
public class SearchListProcurementsModel {

    String openProcNameSearchField;
    String completedProcNameSearchField;
    String openProcStatusSearchField;

}
