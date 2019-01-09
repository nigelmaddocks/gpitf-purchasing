package uk.nhs.gpitf.purchasing.cache;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.swagger.client.model.Capabilities;
import io.swagger.client.model.CapabilitiesImplemented;
import io.swagger.client.model.Solutions;
import io.swagger.client.model.Organisations;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import uk.nhs.gpitf.purchasing.services.OnboardingService;

@Data
@Component
public class CapabilitiesImplementedCache {
	protected Integer TTL = 60; // In minutes. defaults to 60 mins
	
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	@Autowired
	OnboardingService onboardingService;
	
	private Hashtable<String, String[]> capabilityIdSolutionIds = new Hashtable<>();
	private Hashtable<String, String[]> solutionIdCapabilityIds = new Hashtable<>();
	private Hashtable<String, Capabilities> capabilities = new Hashtable<>();
	private Hashtable<String, Solutions> solutions = new Hashtable<>();
	private Hashtable<String, Organisations> organisations = new Hashtable<>();

	
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
    private static AtomicReference<CapabilitiesImplementedCache> INSTANCE = new AtomicReference<CapabilitiesImplementedCache>();

    public CapabilitiesImplementedCache() {
        final CapabilitiesImplementedCache previous = INSTANCE.getAndSet(this);
        if (previous != null) {
            throw new IllegalStateException("Second CapabilitiesImplementedCache " + this + " created after " + previous);
        }
        startScheduledLoad();
    }

    public static CapabilitiesImplementedCache getInstance() {
        return INSTANCE.get();
    }
	
	public void load() {
		Hashtable<String, String[]> newCapabilityIdSolutionIds = new Hashtable<>();
		Hashtable<String, String[]> newSolutionIdCapabilityIds = new Hashtable<>();
		Hashtable<String, Capabilities> newCapabilities = new Hashtable<>();
		Hashtable<String, Solutions> newSolutions = new Hashtable<>();
		Hashtable<String, Organisations> newOrganisations = new Hashtable<>();
		
		List<Capabilities> capabilities = onboardingService.findCapabilitiesByFramework(onboardingService.getDefaultFramework());
		List<Solutions> solutions = onboardingService.findSolutionsByFramework(onboardingService.getDefaultFramework());
		List<CapabilitiesImplemented> capabilitiesImplemented = new ArrayList<>();
		for (Solutions solution : solutions) {
			List<CapabilitiesImplemented> ciForSolution = onboardingService.findCapabilitiesImplementedBySolution(solution.getId());
			capabilitiesImplemented.addAll(ciForSolution);
					// Add to newSolutions
			newSolutions.put(solution.getId(), solution);
			String[] arrCapabilities = new String[ciForSolution.size()];
			int idx = 0;
			for (CapabilitiesImplemented ci : ciForSolution) {
				arrCapabilities[idx] = ci.getCapabilityId();
				idx++;
			}
			// Add to newSolutionIdCapabilityIds
			newSolutionIdCapabilityIds.put(solution.getId(), arrCapabilities);
		}
		
		// Add to newCapabilities
		for (Capabilities capability : capabilities) {
			newCapabilities.put(capability.getId(), capability);
			ArrayList<String> arlSolutions = new ArrayList<>();
			for (CapabilitiesImplemented ci : capabilitiesImplemented) {
				if (ci.getCapabilityId().equals(capability.getId())
				 && !arlSolutions.contains(ci.getSolutionId())) {
					arlSolutions.add(ci.getSolutionId());
				}
			}
			newCapabilityIdSolutionIds.put(capability.getId(), arlSolutions.toArray(new String[]{}));
		}
		
		// Add Organisations for Solutions
		// TODO: Once the swagger api layer contains something to get Organisations
		
		
		this.capabilityIdSolutionIds = newCapabilityIdSolutionIds;
		this.solutionIdCapabilityIds = newSolutionIdCapabilityIds;
		this.capabilities = newCapabilities;
		this.solutions = newSolutions;
		this.organisations = newOrganisations;
		System.out.println("*** CapabilitiesImplemented loaded into Cache ***");

	}
	
	private void startScheduledLoad() {
		Timer timer = new Timer();
	    timer.scheduleAtFixedRate(new TimerTask() {
			
	        @Override
	        public void run() {
	             load(); // Call the containing class method
	        }
	    }, 10000, TTL * 60000);
	}	
}
