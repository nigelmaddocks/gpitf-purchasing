package uk.nhs.gpitf.purchasing.models;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import io.swagger.client.model.Capabilities;
import lombok.Data;
import lombok.Setter;
import uk.nhs.gpitf.purchasing.entities.Organisation;
import uk.nhs.gpitf.purchasing.entities.Procurement;
import lombok.AccessLevel;

@Data
public class SearchSolutionByCapabilityModel {

	public static final int MODE_ALL = 1;
	public static final int MODE_SITES_ONLY = 2;
	private int mode;
	private Long procurementId;
	private Procurement procurement;

	@Setter(AccessLevel.PUBLIC)
	private String csvCapabilities;
	
	private List<Capabilities> allCapabilities;
	private String[] arrInitialCapabilities = new String[] {};
	
	public void setCsvCapabilities(String csvCapabilities) {
		this.csvCapabilities = csvCapabilities;
		if (csvCapabilities != null) {
			arrInitialCapabilities = csvCapabilities.split(",");
		} else {
			arrInitialCapabilities = new String[] {};
		}
	}
	
	public boolean inInitialCapabilities(String capabilityId) {
		return Arrays.stream(arrInitialCapabilities).anyMatch(capabilityId::equals);
	}
	
	private List<Organisation> myCCGs;
	private String myCsvCCGIDs;
	
	private String csvPractices;
	private long patientCount;
	
	private Hashtable<Long, String> selectedCCGPracticeIds = new Hashtable<>();
	
	public String getSelectedCCGPracticeIds(long ccgId) {
		if (selectedCCGPracticeIds.containsKey(ccgId)) {
			return selectedCCGPracticeIds.get(ccgId);
		} else {
			return ",";
		}
	}
	
	private String SHORTLIST_MAX;
}
