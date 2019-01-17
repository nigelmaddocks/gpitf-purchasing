package uk.nhs.gpitf.purchasing.models;

import java.util.Arrays;
import java.util.List;

import io.swagger.client.model.Capabilities;
import lombok.Data;
import lombok.Setter;
import uk.nhs.gpitf.purchasing.entities.Procurement;
import lombok.AccessLevel;

@Data
public class SearchSolutionByCapabilityModel {

	private Long procurementId;
	private Procurement procurement;

	@Setter(AccessLevel.PUBLIC)
	private String csvCapabilities;
	
	private List<Capabilities> allCapabilities;
	private String[] arrInitialCapabilities = new String[] {};
	
	public void setCsvCapabilities(String csvCapabilities) {
		this.csvCapabilities = csvCapabilities;
		arrInitialCapabilities = csvCapabilities.split(",");
	}
	
	public boolean inInitialCapabilities(String capabilityId) {
		return Arrays.stream(arrInitialCapabilities).anyMatch(capabilityId::equals);
	}
}
