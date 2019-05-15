package uk.nhs.gpitf.purchasing.models;

import lombok.Data;
import java.io.Serializable;
import org.apache.commons.lang3.StringUtils;

@Data
public class SearchListProcurementsModel implements Serializable {

	private static final long serialVersionUID = -8058180382836492293L;

	String openProcNameSearchField;
    String completedProcNameSearchField;
    String openProcStatusSearchField;
    long orgContactId;

    public boolean containsData() {
    	return
    		StringUtils.isNotEmpty(openProcNameSearchField)
    		|| StringUtils.isNotEmpty(completedProcNameSearchField)
    		|| StringUtils.isNotEmpty(openProcStatusSearchField);
    }
}
