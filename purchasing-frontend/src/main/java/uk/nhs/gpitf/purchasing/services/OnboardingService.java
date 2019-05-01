package uk.nhs.gpitf.purchasing.services;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import io.swagger.client.api.SolutionsApi;
import io.swagger.client.api.StandardsApi;
import io.swagger.client.api.StandardsApplicableApi;
import io.swagger.client.api.CapabilitiesApi;
import io.swagger.client.api.CapabilitiesImplementedApi;
import io.swagger.client.api.OrganisationsApi;
import io.swagger.client.api.SearchApi;
import io.swagger.client.model.Capabilities;
import io.swagger.client.model.CapabilitiesImplemented;
import io.swagger.client.model.Organisations;
import io.swagger.client.model.SearchResult;
import io.swagger.client.model.Solutions;
import io.swagger.client.model.Standards;
import io.swagger.client.model.StandardsApplicable;
import uk.nhs.gpitf.purchasing.cache.CapabilitiesImplementedCache;
import uk.nhs.gpitf.purchasing.entities.ProcSolutionBundle;
import uk.nhs.gpitf.purchasing.entities.ProcSolutionBundleItem;
import uk.nhs.gpitf.purchasing.entities.swagger.SolutionEx2;
import uk.nhs.gpitf.purchasing.utils.GUtils;

@Service
public class OnboardingService {
	@Autowired
	private CapabilitiesApi capabilitiesApi;
	
	@Autowired
	private SolutionsApi solutionsApi;
	
	@Autowired
	private CapabilitiesImplementedApi capabilitiesImplementedApi;
	
	@Autowired
	private OrganisationsApi organisationsApi;
	
	@Autowired
	private StandardsApi standardsApi;
	
	@Autowired
	private StandardsApplicableApi standardsApplicableApi;
	
	@Autowired
	private SearchApi searchApi;
	
	@Autowired
	CapabilitiesImplementedCache capabilitiesImplementedCache;

	@Value("${sysparam.addDocManToBundles}")
    private String ADD_DOCMAN_TO_BUNDLES_STRING;
	private static boolean ADD_DOCMAN_TO_BUNDLES;
	
	@Value("${sysparam.initialFrameworkId}")
	public String FRAMEWORK_ID;
	public String getDefaultFramework() {
		return FRAMEWORK_ID;
	}
	
	private static final int PAGE_SIZE = 100;
	
	public List<Capabilities> findCapabilities() {		
		List<Capabilities> arl = new ArrayList<>();
		try {
			arl = (List<Capabilities>)loadPaginatedList(capabilitiesApi, "apiCapabilitiesGet", new Object[] {1, PAGE_SIZE});
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return arl;
	}
	
	public List<Capabilities> findCapabilitiesByFramework(String framework) {		
		List<Capabilities> arl = new ArrayList<>();
		try {
			arl = (List<Capabilities>)loadPaginatedList(capabilitiesApi, "apiCapabilitiesByFrameworkByFrameworkIdGet", new Object[] {framework, 1, PAGE_SIZE});
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return arl;
	}
	
	public List<Capabilities> findCapabilitiesByStandard(String standard, Boolean isOptional) {		
		List<Capabilities> arl = new ArrayList<>();
		try {
			arl = (List<Capabilities>)loadPaginatedList(capabilitiesApi, "apiCapabilitiesByStandardByStandardIdGet", new Object[] {standard, isOptional, 1, PAGE_SIZE});
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return arl;
	}
	
	public Capabilities getCapabilityById(String id) {		
		return capabilitiesApi.apiCapabilitiesByIdByIdGet(id);
	}
	
	public List<Capabilities> orderByCoreThenName(List<Capabilities> list) {
		list.sort((object1, object2) -> (object1.getType() + object1.getName()).compareToIgnoreCase(object2.getType() + object2.getName()));
		return list;
	}
	
	
	/**
	 * WARNING: Non-api method that works over cached solutions and capabilities
	 */
	public List<Capabilities> findCapabilitiesFromCache() {
		List<Capabilities> capabilities = new ArrayList<>();
		for (var capability : capabilitiesImplementedCache.getCapabilities().values()) {
			capabilities.add(capability);
		}
		return capabilities;
	}
	
	/**
	 * WARNING: Non-api method that works over cached solutions and capabilities
	 */
	public List<Capabilities> findCapabilitiesBySolutionId(String solutionId) {
		List<Capabilities> capabilities = new ArrayList<>();
		String[] capabilityIds = capabilitiesImplementedCache.getSolutionIdCapabilityIds().get(solutionId);
		for (String capabilityId : capabilityIds) {
			capabilities.add(capabilitiesImplementedCache.getCapabilities().get(capabilityId));
		}
		return capabilities;
	}
	
	// -------------------------------------------------------------------------------------------------------
	
	public List<Solutions> findSolutionsByFramework(String framework) {		
		List<Solutions> arl = new ArrayList<>();
		try {
			arl = (List<Solutions>)loadPaginatedList(solutionsApi, "apiSolutionsByFrameworkByFrameworkIdGet", new Object[] {framework, 1, PAGE_SIZE});
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return arl;
	}

	public Solutions getSolutionById(String id) {
		return solutionsApi.apiSolutionsByIdByIdGet(id);
	}
	
	public List<Solutions> findSolutionsByOrganisation(String organisation) {		
		List<Solutions> arl = new ArrayList<>();
		try {
			arl = (List<Solutions>)loadPaginatedList(solutionsApi, "apiSolutionsByOrganisationByOrganisationIdGet", new Object[] {organisation, 1, PAGE_SIZE});
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return arl;
	}
	
	/**
	 * WARNING: Non-api method that works over cached solutions and capabilities
	 */
	public List<Solutions> findSolutionsHavingAnyCapabilityInList(String csvCapabilityList) {
		String[] arrCapabilityIds = csvCapabilityList.split(",");
		HashSet<String> hshSolutionIds = new HashSet<>();
		for (String capabilityId : arrCapabilityIds) {
			capabilityId = capabilityId.trim();
			if (capabilityId.length() > 0) {
				String[] arrSolutionIds = capabilitiesImplementedCache.getCapabilityIdSolutionIds().get(capabilityId);
				if (arrSolutionIds != null) {
					hshSolutionIds.addAll(Arrays.asList(arrSolutionIds));
				}
			}
		}
		
		List<Solutions> arl = new ArrayList<>();
		for (String solutionId : hshSolutionIds) {
			arl.add(capabilitiesImplementedCache.getSolutions().get(solutionId));
		}

	    return arl;
	}
/*	
	public static class RankedSolution {
		public int rank;
		public SolutionEx2 solution;
		public Capabilities[] capabilities;
	}
*/	
	public static class RankedBundle {
		public int rank;
		public ProcSolutionBundle bundle;
		//public Capabilities[] capabilities;
		public String getId() {
			String id = "";
			for (var item : bundle.getBundleItems()) {
				if (item.getSolution() != null) {
					id += "~S_" + item.getSolution().getId();
				} else
				if (GUtils.nullToString(item.getAdditionalService()).length() > 0) {
					id += "~A_" + item.getAdditionalService();
				}
			}
			if (id.length() > 1) {
				id = id.substring(1);
			}
			
			return id;
		}
		
		public String getName() {
			String name = "";
			for (var item : bundle.getBundleItems()) {
				if (item.getSolution() != null) {
					name += " + " + item.getSolution().getName();
				} else
				if (GUtils.nullToString(item.getAdditionalService()).length() > 0) {
					name += " + " + item.getAdditionalService();
				}
			}
			if (name.length() > 3) {
				name = name.substring(3);
			}
			
			return name;
		}
		
		public BigDecimal getPrice() {
			BigDecimal price = new BigDecimal(0.0d);
			for (var item : bundle.getBundleItems()) {
				if (item.getSolution() != null) {
					price = price.add(item.getSolution().getPrice());
				}
			}			
			
			return price;
		}
	}
	
	/**
	 * WARNING: Non-api method that works over cached solutions and capabilities
	 */
	public List<RankedBundle> findRankedSolutionsHavingCapabilitiesInList(String csvCapabilityList, String csvInteroperables, boolean foundation) {
		int RANK_LIMIT = 99;
		String[] arrCapabilityIds = new String[] {};
		if (csvCapabilityList != null && csvCapabilityList.trim().length() > 0) {
			arrCapabilityIds = csvCapabilityList.split(",");
		}
		
		// Clean the array - do not include Foundation capabilities if we are doing a foundation search. Solutions will be matched for Foundation more directly
		List<String> lstCapabilityIds = new ArrayList<>();
		for (String capabilityId : arrCapabilityIds) {
			if (capabilityId != null && !capabilityId.equals("null") && capabilityId.trim().length() > 0) {
				if (!foundation || !capabilitiesImplementedCache.getCapabilities().get(capabilityId).getType().equals("C")) {
					lstCapabilityIds.add(capabilityId.trim());
				}
			}
		}
		arrCapabilityIds = lstCapabilityIds.toArray(new String[] {});
		
		String[] arrInteroperableIds = new String[] {};
		if (csvInteroperables != null && csvInteroperables.trim().length() > 0) {
			arrInteroperableIds = csvInteroperables.split(",");
		}
		
		// Clean the array 
		List<String> lstInteroperableIds = new ArrayList<>();
		for (String interoperableId : arrInteroperableIds) {
			if (interoperableId != null && !interoperableId.equals("null") && interoperableId.trim().length() > 0) {
				lstInteroperableIds.add(interoperableId.trim());
			}
		}
		arrInteroperableIds = lstInteroperableIds.toArray(new String[] {});

		
		HashSet<String> hshSolutionIds = new HashSet<>();
		for (String capabilityId : arrCapabilityIds) {
			capabilityId = capabilityId.trim();
			if (capabilityId.length() > 0) {
				String[] arrSolutionIds = capabilitiesImplementedCache.getCapabilityIdSolutionIds().get(capabilityId);
				if (arrSolutionIds != null) {
					//hshSolutionIds.addAll(Arrays.asList(arrSolutionIds));
					for (var solutionId : arrSolutionIds) {
						if (!foundation || !capabilitiesImplementedCache.getSolutions().get(solutionId).isFoundation()) {
							// If interoperability with particular foundation systems has been requested, then ensure that non-foundation systems are compatible with such
							boolean bMeetsInteroperability = true;
							if (arrInteroperableIds == null || arrInteroperableIds.length == 0) {
								bMeetsInteroperability = true;
							} else {
								for (String sInteroperabilityWithThisRequested : arrInteroperableIds) {
									SolutionEx2 solution = capabilitiesImplementedCache.getSolutions().get(solutionId);
									if (!solution.isFoundation() 
									 && !List.of(solution.getInteroperableFoundationSolutions()).contains(sInteroperabilityWithThisRequested)) {
										bMeetsInteroperability = false;
										break;
									}
								}
							}
							if (bMeetsInteroperability) {
								hshSolutionIds.add(solutionId);
							}
						}
					}
				}
			}
		}
		
		// If doing a foundation search, just add in foundation solutions
		if (foundation) {
			for (var solution : capabilitiesImplementedCache.getSolutions().values()) {
				if (solution.isFoundation()) {
					hshSolutionIds.add(solution.getId());
				}
			}
		}
		
		// Perform the ranking
		List<RankedBundle> arlRankedSolutions = new ArrayList<>();
		for (String solutionId : hshSolutionIds) {
			String[] arrSolnCapabilities = capabilitiesImplementedCache.getSolutionIdCapabilityIds().get(solutionId);
			
			int iSolutionDeficient = 0;
			for (String requestedCapability : arrCapabilityIds) {
				boolean bRequestedCapabilityFound = false;
				for (String solutionCapability : arrSolnCapabilities) {
					if (requestedCapability.equals(solutionCapability)) {
						bRequestedCapabilityFound = true;
						continue;
					}
				}
				if (!bRequestedCapabilityFound) {
					iSolutionDeficient++;
				}
			}
			
			int iSolutionExceeds = 0;
			for (String solutionCapability : arrSolnCapabilities) {
				boolean bSolutionCapabilityFound = false;
				for (String requestedCapability : arrCapabilityIds) {
					if (requestedCapability.equals(solutionCapability)) {
						bSolutionCapabilityFound = true;
						continue;
					}
				}			
				if (!bSolutionCapabilityFound) {
					iSolutionExceeds++;
				}
			}		
			
			int iRank = iSolutionDeficient + iSolutionExceeds;
			
			RankedBundle rankedSolution = new RankedBundle();
			rankedSolution.rank = iRank;
			rankedSolution.bundle = new ProcSolutionBundle(null, capabilitiesImplementedCache.getSolutions().get(solutionId));
			
			arlRankedSolutions.add(rankedSolution);
		}
		
		// Shuffle solutions of equal rank - also adds solutions in rank order
		List<RankedBundle> arlReturnedBundles = new ArrayList<>();

		for (int iRank=0; iRank<=RANK_LIMIT; iRank++) {
			List<RankedBundle> arlSolutionsOfRank = new ArrayList<>();
			for (var rs : arlRankedSolutions) {
				if (rs.rank == iRank) {
					arlSolutionsOfRank.add(rs);
				}
			}
			Collections.shuffle(arlSolutionsOfRank);
			arlReturnedBundles.addAll(arlSolutionsOfRank);
		}

		// If all the "foundation" capabilities were selected, then move the solutions having all the "foundation" capabilities to the top of the list
//		boolean bRequestHasAllFoundationCapabilities = Arrays.asList(arrCapabilityIds).containsAll(capabilitiesImplementedCache.getFoundationCapabilityIds());
//		if (bRequestHasAllFoundationCapabilities) {
		if (foundation) {
			List <RankedBundle> foundationSolutions = new ArrayList<>();
			for (RankedBundle rankedBundle : arlReturnedBundles) {
				boolean isFoundation = false;
				for (var bundleItem : rankedBundle.bundle.getBundleItems()) {
					if (bundleItem.getSolution() != null
					 && bundleItem.getSolution().isFoundation()) {
						isFoundation = true;
						break;
					}
				}
				
				if (isFoundation) {
					foundationSolutions.add(rankedBundle);
				}
			}
			for (var rs : foundationSolutions) {
				arlReturnedBundles.remove(rs);
			}
			
			// Test - add some combined foundation bundles with DocMan (if DocMan is interoperable with it)
			ADD_DOCMAN_TO_BUNDLES = Boolean.valueOf(ADD_DOCMAN_TO_BUNDLES_STRING);

			List <RankedBundle> foundationCombinedBundles = new ArrayList<>();
			if (ADD_DOCMAN_TO_BUNDLES && foundation) {
				String sDocManId = "C8D558DA-8EC9-4E36-881A-344F0F852284";
				
				for (RankedBundle rb : foundationSolutions) {
					SolutionEx2 dmSolution = capabilitiesImplementedCache.getSolutions().get(sDocManId);
					if (List.of(dmSolution.interoperableFoundationSolutions).contains(rb.bundle.getBundleItems().get(0).getSolution().getId())) {
						RankedBundle foundationCombinedBundel = new RankedBundle();
						foundationCombinedBundel.bundle = new ProcSolutionBundle();
						for (var bundleItem : rb.bundle.getBundleItems()) {
							ProcSolutionBundleItem newBundleItem = new ProcSolutionBundleItem();
							newBundleItem.setAdditionalService(bundleItem.getAdditionalService());
							newBundleItem.setBundle(bundleItem.getBundle());
							newBundleItem.setCapabilities(bundleItem.getCapabilities());
							newBundleItem.setSolution(bundleItem.getSolution());
							newBundleItem.setSolutionId(bundleItem.getSolutionId());
							foundationCombinedBundel.bundle.getBundleItems().add(newBundleItem);
						}
						foundationCombinedBundel.rank = rb.rank+1;
						
						ProcSolutionBundleItem dmBundleItem = new ProcSolutionBundleItem();
						dmBundleItem.setBundle(foundationCombinedBundel.bundle);
						dmBundleItem.setSolution(dmSolution);
						dmBundleItem.setSolutionId(dmSolution.getId());
						foundationCombinedBundel.bundle.getBundleItems().add(dmBundleItem);
						
						foundationCombinedBundles.add(foundationCombinedBundel);
					}
				}
				
			}
			
			arlReturnedBundles.addAll(0, foundationCombinedBundles);
			arlReturnedBundles.addAll(0, foundationSolutions);
		}
		
		// Add each solution's capabilities
		
		for (var rankedBundle : arlReturnedBundles) {
			for (var bundleItem : rankedBundle.bundle.getBundleItems()) {
				if (bundleItem.getSolution() != null) {
					String[] capabilityIds = capabilitiesImplementedCache.getSolutionIdCapabilityIds().get(bundleItem.getSolutionId());
					Capabilities[] capabilities = new Capabilities[capabilityIds.length];
					for (int idx=0; idx<capabilityIds.length; idx++) {
						capabilities[idx] = capabilitiesImplementedCache.getCapabilities().get(capabilityIds[idx]);
					}
					bundleItem.setCapabilities(capabilities);
				}
			}
		}
		
	    return arlReturnedBundles;
	}
	
	/**
	 * WARNING: Non-api method that works over cached solutions and capabilities
	 */
	public List<Solutions> findSolutionsHavingEveryCapabilityInList(String csvCapabilityList) {
		String[] arrCapabilityIds = csvCapabilityList.split(",");
		HashSet<String> hshSolutionIds = new HashSet<>();
		for (String capabilityId : arrCapabilityIds) {
			capabilityId = capabilityId.trim();
			if (capabilityId.length() > 0) {
				String[] arrSolutionIds = capabilitiesImplementedCache.getCapabilityIdSolutionIds().get(capabilityId);
				if (arrSolutionIds != null) {
					hshSolutionIds.addAll(Arrays.asList(arrSolutionIds));
				}
			}
		}
		
		List<Solutions> arl = new ArrayList<>();
		for (String solutionId : hshSolutionIds) {
			boolean bSolutionHasEveryCapability = true;
			String[] arrSolutionsCapabilities = capabilitiesImplementedCache.getSolutionIdCapabilityIds().get(solutionId);
			for (String capabilityId : arrCapabilityIds) {
				capabilityId = capabilityId.trim();
				if (capabilityId.length() > 0) {
					boolean bSolutionHasCapability = Arrays.stream(arrSolutionsCapabilities).anyMatch(capabilityId::equals);
					if (!bSolutionHasCapability) {
						bSolutionHasEveryCapability = false;
						break;
					}
				}
			}
			
			if (bSolutionHasEveryCapability) {
				arl.add(capabilitiesImplementedCache.getSolutions().get(solutionId));
			}
		}

	    return arl;
	}
	
	/**
	 * WARNING: Non-api method that works over cached solutions and capabilities
	 */
	public List<Solutions> findSolutionsHavingKeywords(String keywords) {
		keywords = keywords.toUpperCase();
		ArrayList<String> arlKeywords = new ArrayList<>();
		String[] arrKeywords = keywords.split(" ");
		for (String word : arrKeywords) {
			if (word != null && word.trim().length() > 0) {
				arlKeywords.add(word.trim().toUpperCase());
			}
		}
		arrKeywords = arlKeywords.toArray(new String[] {});
		
		List<Solutions> lstSolutionsMatchingExactly = new ArrayList<>();
		List<Solutions> lstSolutionsMatchingAllKeywords = new ArrayList<>();
		List<Solutions> lstSolutionsMatchingSomeKeywords = new ArrayList<>();
		for (Solutions solution : capabilitiesImplementedCache.getSolutions().values()) {
			// Match on Organisation name
			Organisations org = capabilitiesImplementedCache.getOrganisations().get(solution.getOrganisationId());
			String sOrgName = "";
			if (org != null) {
				sOrgName = org.getName().toUpperCase();
			}
			String sSolutionName = solution.getName().toUpperCase();
			String sSolutionDesc = solution.getDescription().toUpperCase();
			if (sOrgName.contains(keywords) || sSolutionName.contains(keywords) || sSolutionDesc.contains(keywords)) {
				if (!lstSolutionsMatchingExactly.contains(solution)) {
					lstSolutionsMatchingExactly.add(solution);
					continue;
				}
			} 
			
			boolean bAllKeywords = true;
			boolean bSomeKeywords = false;
			for (String word : arrKeywords) {
				if (sOrgName.contains(word) || sSolutionName.contains(word) || sSolutionDesc.contains(word)) {
					bSomeKeywords = true;
				} else {
					bAllKeywords = false;
				}						
			}
			
			if (bAllKeywords) {
				if (!lstSolutionsMatchingAllKeywords.contains(solution)) {
					lstSolutionsMatchingAllKeywords.add(solution);
					continue;
				}				
			}
			
			if (bSomeKeywords) {
				if (!lstSolutionsMatchingSomeKeywords.contains(solution)) {
					lstSolutionsMatchingSomeKeywords.add(solution);
					continue;
				}				
			}
		}
		
		List<Solutions> returnedList = new ArrayList<>();
		
		// Shuffle entries that are equally matched
		Collections.shuffle(lstSolutionsMatchingExactly);
		Collections.shuffle(lstSolutionsMatchingAllKeywords);
		Collections.shuffle(lstSolutionsMatchingSomeKeywords);
		
		// Output in one list
		for (Solutions solution : lstSolutionsMatchingExactly) {
			if (!returnedList.contains(solution)) {
				returnedList.add(solution);
			}
		}
		for (Solutions solution : lstSolutionsMatchingAllKeywords) {
			if (!returnedList.contains(solution)) {
				returnedList.add(solution);
			}
		}
		for (Solutions solution : lstSolutionsMatchingSomeKeywords) {
			if (!returnedList.contains(solution)) {
				returnedList.add(solution);
			}
		}
		
		return returnedList;
		
	}

	/**
	 * WARNING: Non-api method that works over cached solutions and capabilities
	 */
	public SolutionEx2 getSolutionEx2ById(String id) {
		return capabilitiesImplementedCache.getSolutions().get(id);
	}
	
	/**
	 * WARNING: Non-api method that returns foundation solutions
	 */
	public List<SolutionEx2> getFoundationSolutions() {
		List <SolutionEx2> foundationSolutions = new ArrayList<>() ;
		for (var solution : capabilitiesImplementedCache.getSolutions().values()) {
			if (solution.isFoundation()) {
				foundationSolutions.add(solution);
			}
		}
		foundationSolutions.sort((object1, object2) -> (object1.getName()).compareToIgnoreCase(object2.getName()));

		return foundationSolutions;
	}
	// -------------------------------------------------------------------------------------------------------
	
	public List<Standards> findStandardsByCapability(String capability, Boolean isOptional) {		
		List<Standards> arl = new ArrayList<>();
		try {
			arl = (List<Standards>)loadPaginatedList(standardsApi, "apiStandardsByCapabilityByCapabilityIdGet", new Object[] {capability, isOptional, 1, PAGE_SIZE});
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return arl;
	}
	
	public List<Standards> findStandardsByFramework(String framework) {		
		List<Standards> arl = new ArrayList<>();
		try {
			arl = (List<Standards>)loadPaginatedList(standardsApi, "apiStandardsByFrameworkByFrameworkIdGet", new Object[] {framework, 1, PAGE_SIZE});
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return arl;
	}
	
	public List<Standards> findStandards() {		
		List<Standards> arl = new ArrayList<>();
		try {
			arl = (List<Standards>)loadPaginatedList(standardsApi, "apiStandardsGet", new Object[] {1, PAGE_SIZE});
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return arl;
	}

	public Standards getStandardById(String id) {
		return standardsApi.apiStandardsByIdByIdGet(id);
	}
	
	// -------------------------------------------------------------------------------------------------------
	
	public List<CapabilitiesImplemented> findCapabilitiesImplementedBySolution(String solution) {
		List<CapabilitiesImplemented> arl = new ArrayList<>();
		try {
			arl = (List<CapabilitiesImplemented>)loadPaginatedList(capabilitiesImplementedApi, "apiCapabilitiesImplementedBySolutionBySolutionIdGet", new Object[] {solution, 1, PAGE_SIZE});
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return arl;
	}
	
	// -------------------------------------------------------------------------------------------------------
	
	public List<StandardsApplicable> findStandardsApplicableBySolution(String solution) {
		List<StandardsApplicable> arl = new ArrayList<>();
		try {
			arl = (List<StandardsApplicable>)loadPaginatedList(standardsApplicableApi, "apiStandardsApplicableBySolutionBySolutionIdGet", new Object[] {solution, 1, PAGE_SIZE});
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return arl;
	}
	
	// -------------------------------------------------------------------------------------------------------
	
	/** 
	 * @deprecated Actually not yet implemented as the required swagger function does not exist
	 */
	public List<Organisations> findAllOrganisations() {
		List<Organisations> arl = new ArrayList<>();
		try {
			arl = (List<Organisations>)loadPaginatedList(organisationsApi, "apiOrganisationsGet", new Object[] {1, PAGE_SIZE});
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return arl;
	}
	
	// -------------------------------------------------------------------------------------------------------
	
	public List<SearchResult> porcelainSearchByKeyword(String keyword) {
		List<SearchResult> arl = new ArrayList<>();
		try {
			arl = (List<SearchResult>)loadPaginatedList(searchApi, "apiPorcelainSearchByKeywordByKeywordGet", new Object[] {keyword, 1, PAGE_SIZE});
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return arl;
	}
	
	// -------------------------------------------------------------------------------------------------------
	
	private List<?> loadPaginatedList(Object objectApi, String methodName, Object... params) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		List<Object> arl = new ArrayList<>();
		Class<?>[] arrClass = new Class[params.length];
		for (int idx=0; idx<params.length; idx++) {
			arrClass[idx] = params[idx].getClass();
		}		
		Method getMethod = objectApi.getClass().getMethod(methodName, arrClass);
		Object paginatedList = null;
		try {
			paginatedList = getMethod.invoke(objectApi, params);
		} catch (InvocationTargetException ite) {
			if (!(ite.getCause() instanceof HttpClientErrorException.NotFound)) {
				throw ite;
			}
		}
		Method mthGetItems = null;
		Method mthIsHasNextPage = null;
		while (paginatedList != null) {
			if (mthGetItems == null) {
				mthGetItems = paginatedList.getClass().getMethod("getItems");
				mthIsHasNextPage = paginatedList.getClass().getMethod("isHasNextPage");
			}
			List<?> lstItems = (List<?>)mthGetItems.invoke(paginatedList, new Object[] {});
			arl.addAll(lstItems);
			boolean hasNextPage = (boolean)mthIsHasNextPage.invoke(paginatedList, new Object[] {});
			if (!hasNextPage) break;
			
			int iPage = 1 + (int)params[params.length-2];
			params[params.length-2] = iPage;
			try {
				paginatedList = getMethod.invoke(objectApi, params);
			} catch (InvocationTargetException ite) {
				if (ite.getCause() instanceof HttpClientErrorException.NotFound) {
					break;
				} else {
					throw ite;
				}
			}
		}
		
		return arl;
	}
}
