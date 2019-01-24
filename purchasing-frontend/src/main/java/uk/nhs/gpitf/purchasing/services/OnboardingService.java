package uk.nhs.gpitf.purchasing.services;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import io.swagger.client.api.SolutionsApi;
import io.swagger.client.api.StandardsApi;
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
import uk.nhs.gpitf.purchasing.cache.CapabilitiesImplementedCache;

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
	private SearchApi searchApi;
	
	@Autowired
	CapabilitiesImplementedCache capabilitiesImplementedCache;
	
	private static final String FRAMEWORK_ID = "5A8D06DD-8C32-4821-AC65-BD47294ACD8E";
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
		list.sort((object1, object2) -> (object1.getId().substring(0,5) +object1.getName()).compareToIgnoreCase(object2.getId().substring(0,5) +object2.getName()));
		return list;
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
	
	public static class RankedSolution {
		public int rank;
		public Solutions solution;
		public Capabilities[] capabilities;
	}
	
	/**
	 * WARNING: Non-api method that works over cached solutions and capabilities
	 */
	public List<RankedSolution> findRankedSolutionsHavingCapabilitiesInList(String csvCapabilityList) {
		int RANK_LIMIT = 99;
		String[] arrCapabilityIds = csvCapabilityList.split(",");
		
		// Clean the array
		List<String> lstCapabilityIds = new ArrayList<>();
		for (String capabilityId : arrCapabilityIds) {
			if (capabilityId != null && capabilityId.trim().length() > 0) {
				lstCapabilityIds.add(capabilityId.trim());
			}
		}
		arrCapabilityIds = lstCapabilityIds.toArray(new String[] {});
		
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
		
		List<RankedSolution> arlRankedSolutions = new ArrayList<>();
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
			
			RankedSolution rankedSolution = new RankedSolution();
			rankedSolution.rank = iRank;
			rankedSolution.solution = capabilitiesImplementedCache.getSolutions().get(solutionId);
			
			arlRankedSolutions.add(rankedSolution);
		}
		
		// Shuffle solutions of equal rank
		List<RankedSolution> arlReturnedSolutions = new ArrayList<>();

		for (int iRank=0; iRank<=RANK_LIMIT; iRank++) {
			List<RankedSolution> arlSolutionsOfRank = new ArrayList<>();
			for (var rs : arlRankedSolutions) {
				if (rs.rank == iRank) {
					arlSolutionsOfRank.add(rs);
				}
			}
			Collections.shuffle(arlSolutionsOfRank);
			arlReturnedSolutions.addAll(arlSolutionsOfRank);
		}

		// Add each solution's capabilities
		
		for (var rs : arlReturnedSolutions) {
			String[] capabilityIds = capabilitiesImplementedCache.getSolutionIdCapabilityIds().get(rs.solution.getId());
			Capabilities[] capabilities = new Capabilities[capabilityIds.length];
			for (int idx=0; idx<capabilityIds.length; idx++) {
				capabilities[idx] = capabilitiesImplementedCache.getCapabilities().get(capabilityIds[idx]);
			}
			rs.capabilities = capabilities;
		}
		
	    return arlReturnedSolutions;
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
