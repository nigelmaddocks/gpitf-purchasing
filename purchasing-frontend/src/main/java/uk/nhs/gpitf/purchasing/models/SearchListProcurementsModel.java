package uk.nhs.gpitf.purchasing.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Data
@Getter
@Setter
public class SearchListProcurementsModel {

    String openProcNameSearchField;
    String completedProcNameSearchField;
    String openProcStatusSearchField;
    long orgContactId;

}
