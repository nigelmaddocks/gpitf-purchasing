package uk.nhs.gpitf.purchasing.endpoints;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.client.model.Capabilities;
import io.swagger.client.model.CapabilitiesImplemented;
import io.swagger.client.model.SearchResult;
import io.swagger.client.model.Solutions;
import io.swagger.client.model.Standards;
import uk.nhs.gpitf.purchasing.services.OnboardingService;

@RestController
public class OnboardingController {
	
	@Autowired
	OnboardingService onboardingService;

    @GetMapping(value = "/onboarding/capabilities/all")
    public List<Capabilities> getCapabilities() {
    	List<Capabilities> list = onboardingService.findCapabilities();
    	return list;
    }

    @GetMapping(value = "/onboarding/capabilitiesByFramework/{framework}")
    public List<Capabilities> getCapabilitiesByFramework(
    		@PathVariable("framework") String framework
    		) {
    	List<Capabilities> list = onboardingService.findCapabilitiesByFramework(framework);
    	return list;
    }

    @GetMapping(value = "/onboarding/capabilities/{id}")
    public Capabilities getCapabilityById(
    		@PathVariable("id") String id
    		) {
    	Capabilities item = onboardingService.getCapabilityById(id);
    	return item;
    }

    @GetMapping(value = "/onboarding/capabilitiesByStandard/{standard}/{isOptional}")
    public List<Capabilities> getCapabilitiesByStandard(
    		@PathVariable("standard") String standard, 
    		@PathVariable("isOptional") Boolean isOptional) {
    	List<Capabilities> list = onboardingService.findCapabilitiesByStandard(standard, isOptional);
    	return list;
    }

    @GetMapping(value = "/onboarding/solutionsByFramework/{framework}")
    public List<Solutions> getSolutionsByFramework(
    		@PathVariable("framework") String framework
    		) {
    	List<Solutions> list = onboardingService.findSolutionsByFramework(framework);
    	return list;
    }

    @GetMapping(value = "/onboarding/solutions/{id}")
    public Solutions getSolutionyById(
    		@PathVariable("id") String id
    		) {
    	Solutions item = onboardingService.getSolutionById(id);
    	return item;
    }

    @GetMapping(value = "/onboarding/solutionsByOrganisation/{organisation}")
    public List<Solutions> getSolutionsByOrganisation(
    		@PathVariable("organisation") String organisation
    		) {
    	List<Solutions> list = onboardingService.findSolutionsByOrganisation(organisation);
    	return list;
    }

    public static final String ENDPOINT_SOLUTIONS_WITH_ANY_CAPABILITY_IN_LIST = "/onboarding/solutionsHavingAnyCapabilityInList/";
    @GetMapping(value = ENDPOINT_SOLUTIONS_WITH_ANY_CAPABILITY_IN_LIST + "{csvCapabilityList}")
    public List<Solutions> getSolutionsHavingAnyCapabilityInList(
    		@PathVariable("csvCapabilityList") String csvCapabilityList
    		) {
    	List<Solutions> list = onboardingService.findSolutionsHavingAnyCapabilityInList(csvCapabilityList);
    	return list;
    }

    public static final String ENDPOINT_SOLUTIONS_WITH_EVERY_CAPABILITY_IN_LIST = "/onboarding/solutionsHavingEveryCapabilityInList/";
    @GetMapping(value = ENDPOINT_SOLUTIONS_WITH_EVERY_CAPABILITY_IN_LIST + "{csvCapabilityList}")
    public List<Solutions> getSolutionsHavingEveryCapabilityInList(
    		@PathVariable("csvCapabilityList") String csvCapabilityList
    		) {
    	List<Solutions> list = onboardingService.findSolutionsHavingEveryCapabilityInList(csvCapabilityList);
    	return list;
    }

    public static final String ENDPOINT_SOLUTIONS_BY_RANK_WITH_CAPABILITIES_IN_LIST = "/onboarding/solutionsByRankWithCapabilitiesInList/";
    @GetMapping(value = {
    		ENDPOINT_SOLUTIONS_BY_RANK_WITH_CAPABILITIES_IN_LIST, 
    		ENDPOINT_SOLUTIONS_BY_RANK_WITH_CAPABILITIES_IN_LIST + "{optCsvCapabilities}"})
    public List<OnboardingService.RankedBundle> getSolutionsByRankHavingCapabilitiesInList(
    		@PathVariable("optCsvCapabilities") Optional<String> optCsvCapabilities,
    		@RequestParam(value = "foundation", defaultValue = "") String sFoundationFromQuerystring,
    		@RequestParam(value = "interoperables", defaultValue = "") String csvInteroperables
    		) {
    	boolean foundation = Boolean.valueOf(sFoundationFromQuerystring);
    	String csvCapabilities = "";
    	if (optCsvCapabilities.isPresent()) {
    		csvCapabilities = optCsvCapabilities.get();
    	}
    	List<OnboardingService.RankedBundle> list = onboardingService.findRankedSolutionsHavingCapabilitiesInList(csvCapabilities, csvInteroperables, foundation);
    	return list;
    }

    @GetMapping(value = "/onboarding/standardsByCapability/{capability}/{isOptional}")
    public List<Standards> getStandardsByCapability (
    		@PathVariable("capability") String capability,
    		@PathVariable("isOptional") Boolean isOptional
    		) {
    	List<Standards> list = onboardingService.findStandardsByCapability(capability, isOptional);
    	return list;
    }

    @GetMapping(value = "/onboarding/standardsByFramework/{framework}")
    public List<Standards> getStandardsByFramework (
    		@PathVariable("framework") String framework
    		) {
    	List<Standards> list = onboardingService.findStandardsByFramework(framework);
    	return list;
    }

    @GetMapping(value = "/onboarding/standards/all")
    public List<Standards> getStandards () {
    	List<Standards> list = onboardingService.findStandards();
    	return list;
    }

    @GetMapping(value = "/onboarding/standards/{id}")
    public Standards getStandardById(
    		@PathVariable("id") String id
    		) {
    	Standards item = onboardingService.getStandardById(id);
    	return item;
    }

    @GetMapping(value = "/onboarding/capabilitiesImplementedBySolution/{solution}")
    public List<CapabilitiesImplemented> getCapabilitiesImplementedBySolution(
    		@PathVariable("solution") String solution
    		) {
    	List<CapabilitiesImplemented> list = onboardingService.findCapabilitiesImplementedBySolution(solution);
    	return list;
    }

    @GetMapping(value = "/onboarding/porcelainSearch/{keyword}")
    public List<SearchResult> getPorcelainSearch(
    		@PathVariable("keyword") String keyword
    		) {
    	List<SearchResult> item = onboardingService.porcelainSearchByKeyword(keyword);
    	return item;
    }

}
