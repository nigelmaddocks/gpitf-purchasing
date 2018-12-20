package uk.nhs.gpitf.purchasing.models;

import java.util.List;

import io.swagger.client.model.Capabilities;
import lombok.Data;

@Data
public class SearchSolutionByCapabilityModel {
	private List<Capabilities> allCapabilities;
}
