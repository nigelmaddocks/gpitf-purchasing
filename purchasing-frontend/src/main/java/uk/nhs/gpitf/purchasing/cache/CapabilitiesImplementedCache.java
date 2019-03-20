package uk.nhs.gpitf.purchasing.cache;

import java.io.File;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneOffset;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import io.swagger.client.model.Capabilities;
import io.swagger.client.model.CapabilitiesImplemented;
import io.swagger.client.model.Solutions;
import io.swagger.client.model.Standards;
import io.swagger.client.model.StandardsApplicable;
import io.swagger.client.model.Organisations;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import uk.nhs.gpitf.purchasing.entities.swagger.SolutionEx2;
import uk.nhs.gpitf.purchasing.services.OnboardingService;

@Data
@Component
public class CapabilitiesImplementedCache {
	
	@Value("${sysparam.writeOnboardingCache}")
    private String WRITE_ONBOARDINGCACHE_STRING;
	private static boolean WRITE_ONBOARDINGCACHE;

	@Value("${sysparam.readOnboardingCache}")
    private String READ_ONBOARDINGCACHE_STRING;
	private static boolean READ_ONBOARDINGCACHE;
	
	protected Integer TTL = 60; // In minutes. defaults to 60 mins
	
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	@Autowired
	OnboardingService onboardingService;
	
	@Autowired
	ObjectMapper objectMapper;
	
	private Hashtable<String, String[]> capabilityIdSolutionIds = new Hashtable<>();
	private Hashtable<String, String[]> solutionIdCapabilityIds = new Hashtable<>();
	private Hashtable<String, Capabilities> capabilities = new Hashtable<>();
	private Hashtable<String, SolutionEx2> solutions = new Hashtable<>();
	private Hashtable<String, Organisations> organisations = new Hashtable<>();
	private ArrayList<String> foundationCapabilityIds = new ArrayList<>();
	private Hashtable<String, Standards> standards = new Hashtable<>();
	private Hashtable<String, List<StandardsApplicable>> solutionStandardsApplicable = new Hashtable<>();

	
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
		WRITE_ONBOARDINGCACHE = Boolean.valueOf(WRITE_ONBOARDINGCACHE_STRING);
		READ_ONBOARDINGCACHE = Boolean.valueOf(READ_ONBOARDINGCACHE_STRING);

		
		Hashtable<String, String[]> newCapabilityIdSolutionIds = new Hashtable<>();
		Hashtable<String, String[]> newSolutionIdCapabilityIds = new Hashtable<>();
		Hashtable<String, Capabilities> newCapabilities = new Hashtable<>();
		Hashtable<String, SolutionEx2> newSolutions = new Hashtable<>();
		Hashtable<String, Organisations> newOrganisations = new Hashtable<>();
		ArrayList<String> newFoundationCapabilityIds = new ArrayList<>();
		Hashtable<String, Standards> newStandards = new Hashtable<>();
		Hashtable<String, List<StandardsApplicable>> newSolutionStandardsApplicable = new Hashtable<>();
		
		if (READ_ONBOARDINGCACHE == false) {
			List<Capabilities> capabilities = onboardingService.findCapabilitiesByFramework(onboardingService.getDefaultFramework());
			List<Solutions> tmpSolutions = onboardingService.findSolutionsByFramework(onboardingService.getDefaultFramework());
			List<SolutionEx2> solutions = new ArrayList<>();
			for (var tmpSolution : tmpSolutions) {
				solutions.add(new SolutionEx2(tmpSolution));
			}
			
			for (var capability : capabilities) {
				if (capability.getType().equals("C")) {
					newFoundationCapabilityIds.add(capability.getId());
				}
			}
			
			List<CapabilitiesImplemented> capabilitiesImplemented = new ArrayList<>();
			for (SolutionEx2 solution : solutions) {
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
				
				// Calculate a static price based on name and capabilities
				long byteTotal = 0;
				for (byte thisByte : solution.getName().getBytes()) {
					byteTotal += (int) thisByte;
				}
				BigDecimal price = BigDecimal.valueOf(byteTotal).divide(BigDecimal.valueOf(solution.getName().getBytes().length), 3, RoundingMode.HALF_UP); 
				price = price.divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP);
				price = price.multiply(BigDecimal.valueOf(arrCapabilities.length));
				solution.setPrice(price);
				
				// Determine if solution contains all foundation capabilities
				boolean bContainsAllFoundationCapabilities = Arrays.asList(arrCapabilities).containsAll(newFoundationCapabilityIds);
				solution.setFoundation(bContainsAllFoundationCapabilities);
				
				// Add to newSolutionIdCapabilityIds
				newSolutionIdCapabilityIds.put(solution.getId(), arrCapabilities);
				
				List<StandardsApplicable> standardsApplicable = onboardingService.findStandardsApplicableBySolution(solution.getId());
				newSolutionStandardsApplicable.put(solution.getId(), standardsApplicable); 
			}
			
			for (Standards standard : onboardingService.findStandardsByFramework(onboardingService.getDefaultFramework())) {
				newStandards.put(standard.getId(), standard);
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
		}
		
		if (READ_ONBOARDINGCACHE == true) {
			try {
				Hashtable<String, ArrayList<String>>loadedCapabilityIdSolutionIds = objectMapper.readValue(getClass().getResourceAsStream("/onboardingCache/capabilityIdSolutionIds.json"), newCapabilityIdSolutionIds.getClass());
				for (var key : loadedCapabilityIdSolutionIds.keySet()) {
					newCapabilityIdSolutionIds.put(key, loadedCapabilityIdSolutionIds.get(key).toArray(new String[] {}));
				}
				Hashtable<String, ArrayList<String>>loadedSolutionIdCapabilityIds = objectMapper.readValue(getClass().getResourceAsStream("/onboardingCache/solutionIdCapabilityIds.json"), newSolutionIdCapabilityIds.getClass());
				for (var key : loadedSolutionIdCapabilityIds.keySet()) {
					newSolutionIdCapabilityIds.put(key, loadedSolutionIdCapabilityIds.get(key).toArray(new String[] {}));
				}
				newCapabilities = objectMapper.readValue(getClass().getResourceAsStream("/onboardingCache/capabilities.json"), newCapabilities.getClass());
/*				
				ArrayList<LinkedHashMap> arlLHM = objectMapper.readValue(getClass().getResourceAsStream("/onboardingCache/capabilities.json"), new ArrayList<LinkedHashMap>().getClass());
				for (var lhm : arlLHM) {
					Capabilities capability = new Capabilities();
					for (var key : lhm.keySet()) {
						Field field = Capabilities.class.getDeclaredField(key.toString());
						field.setAccessible(true); 
						field.set(capability, lhm.get(key));
					}
					newCapabilities.put(capability.getId(), capability);
				}
*/			
				//newSolutions = objectMapper.readValue(new File(folderOnboardingCache, "solutions.json"), newSolutions.getClass());
				//ArrayList<LinkedHashMap> arlLHM = objectMapper.readValue(getClass().getResourceAsStream("/onboardingCache/solutions.json"), new ArrayList<LinkedHashMap>().getClass());
				//for (var lhm : arlLHM) {
				Hashtable<String, LinkedHashMap> hshLHM = objectMapper.readValue(getClass().getResourceAsStream("/onboardingCache/solutions.json"), new Hashtable<String, LinkedHashMap>().getClass());
				for (var key2 : hshLHM.keySet()) {
					LinkedHashMap lhm = hshLHM.get(key2);
					SolutionEx2 solutionEx2 = new SolutionEx2();
					Solutions solution = new Solutions();
					for (var key : lhm.keySet()) { 
						Field field = null;
						Object objSolution = null;
						try {
							field = SolutionEx2.class.getDeclaredField(key.toString());
							objSolution = solutionEx2;
						} catch (NoSuchFieldException nsfe) {
							field = Solutions.class.getDeclaredField(key.toString());
							objSolution = solution;
						}
						field.setAccessible(true); 
						Object value = lhm.get(key);
						if (value instanceof Double && field.getType().equals(BigDecimal.class)) {
							BigDecimal bdValue = BigDecimal.valueOf((Double)value);
			 				field.set(objSolution, bdValue);
						} else if (value instanceof LinkedHashMap && field.getType().equals(OffsetDateTime.class)) {
							// year=2006, monthValue=2, dayOfMonth=21, hour=16, minute=3, second=0
							LinkedHashMap lhmValue = (LinkedHashMap)value;
							OffsetDateTime odtValue = OffsetDateTime.of(
									(Integer)lhmValue.get("year"), 
									(Integer)lhmValue.get("monthValue"), 
									(Integer)lhmValue.get("dayOfMonth"), 
									(Integer)lhmValue.get("hour"), 
									(Integer)lhmValue.get("minute"), 
									(Integer)lhmValue.get("second"), 
									0, 
									ZoneOffset.ofHours(0));
			 				field.set(objSolution, odtValue);
						} else {
							field.set(objSolution, value); 
						}
					}
					BeanUtils.copyProperties(solution, solutionEx2);
					newSolutions.put(solutionEx2.getId(), solutionEx2);
				}
				newOrganisations = objectMapper.readValue(getClass().getResourceAsStream("/onboardingCache/organisations.json"), newOrganisations.getClass());
				newFoundationCapabilityIds = objectMapper.readValue(getClass().getResourceAsStream("/onboardingCache/foundationCapabilityIds.json"), newFoundationCapabilityIds.getClass());
				hshLHM = objectMapper.readValue(getClass().getResourceAsStream("/onboardingCache/standards.json"), new Hashtable<String, LinkedHashMap>().getClass());
				for (var key2 : hshLHM.keySet()) {
					Standards standard = new Standards();
					LinkedHashMap lhm = hshLHM.get(key2);
					for (var key : lhm.keySet()) {
						Field field = Standards.class.getDeclaredField(key.toString());
						field.setAccessible(true); 
						field.set(standard, lhm.get(key));
					}
					newStandards.put(standard.getId(), standard);
				}
				
				Hashtable<String, ArrayList<LinkedHashMap>> hshArlLHM = objectMapper.readValue(getClass().getResourceAsStream("/onboardingCache/solutionIdStandards.json"), new Hashtable<String, ArrayList<LinkedHashMap>>().getClass());
				for (var key2 : hshArlLHM.keySet()) {
					ArrayList<LinkedHashMap> arlLHM = hshArlLHM.get(key2);
					ArrayList<StandardsApplicable> arlStdApp = new ArrayList<>();
					
					for (var lhm : arlLHM) {
						StandardsApplicable stdapp = new StandardsApplicable();
						for (var key : lhm.keySet()) {
							Field field = StandardsApplicable.class.getDeclaredField(key.toString());
							field.setAccessible(true); 
							field.set(stdapp, lhm.get(key));
						}
						arlStdApp.add(stdapp);
					}
					newSolutionStandardsApplicable.put(key2, arlStdApp);
				}


			} catch (Exception owex) {
				owex.printStackTrace();
			}
		}
		
		this.capabilityIdSolutionIds = newCapabilityIdSolutionIds;
		this.solutionIdCapabilityIds = newSolutionIdCapabilityIds;
		this.capabilities = newCapabilities;
		this.solutions = newSolutions;
		this.organisations = newOrganisations;
		this.foundationCapabilityIds = newFoundationCapabilityIds;
		this.standards = newStandards;
		this.solutionStandardsApplicable = newSolutionStandardsApplicable;
		System.out.println("*** CapabilitiesImplemented loaded into Cache ***");
 
		if (WRITE_ONBOARDINGCACHE) {
			ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
			try {
				URL urlOnboardingCacheDummy = this.getClass().getResource("/onboardingCache/dummy.json");
				File folderOnboardingCache = new File(urlOnboardingCacheDummy.getPath()).getParentFile();
				ow.writeValue(new File(folderOnboardingCache, "capabilityIdSolutionIds.json"), capabilityIdSolutionIds);
				ow.writeValue(new File(folderOnboardingCache, "solutionIdCapabilityIds.json"), solutionIdCapabilityIds);
				ow.writeValue(new File(folderOnboardingCache, "capabilities.json"), capabilities);
				ow.writeValue(new File(folderOnboardingCache, "solutions.json"), solutions);
				ow.writeValue(new File(folderOnboardingCache, "organisations.json"), organisations);
				ow.writeValue(new File(folderOnboardingCache, "foundationCapabilityIds.json"), foundationCapabilityIds);
				ow.writeValue(new File(folderOnboardingCache, "standards.json"), standards);
				ow.writeValue(new File(folderOnboardingCache, "solutionIdStandards.json"), solutionStandardsApplicable);
			} catch (Exception owex) {
				owex.printStackTrace();
			}
		}
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
