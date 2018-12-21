package uk.nhs.gpitf.purchasing.controllers.dataload;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import antlr.Utils;
import uk.nhs.gpitf.purchasing.entities.LegacySolution;
import uk.nhs.gpitf.purchasing.entities.OrgRelationship;
import uk.nhs.gpitf.purchasing.entities.OrgSolution;
import uk.nhs.gpitf.purchasing.entities.OrgType;
import uk.nhs.gpitf.purchasing.entities.Organisation;
import uk.nhs.gpitf.purchasing.entities.PatientCount;
import uk.nhs.gpitf.purchasing.entities.PatientCountRun;
import uk.nhs.gpitf.purchasing.entities.RelationshipType;
import uk.nhs.gpitf.purchasing.repositories.LegacySolutionRepository;
import uk.nhs.gpitf.purchasing.repositories.OrgRelationshipRepository;
import uk.nhs.gpitf.purchasing.repositories.OrgSolutionRepository;
import uk.nhs.gpitf.purchasing.repositories.OrganisationRepository;
import uk.nhs.gpitf.purchasing.repositories.PatientCountRepository;
import uk.nhs.gpitf.purchasing.repositories.PatientCountRunRepository;
import uk.nhs.gpitf.purchasing.services.OrganisationService;
import uk.nhs.gpitf.purchasing.utils.Breadcrumbs;
import uk.nhs.gpitf.purchasing.utils.CSVUtils;
import uk.nhs.gpitf.purchasing.utils.GUtils;

@Controller
public class DataLoadController {
	
	private static final String ODS_ORD_URL = "https://directory.spineservices.nhs.uk/ORD/2-0-0/organisations";
	private static final String ROLE_PRIMARY_CARE = "RO177";
	private static final String ROLE_GP_PRACTICE = "RO76"; //"RO177";
	private static final String ROLE_CCG = "RO98";
	private static final String ROLE_CSU = "RO213";
	private static final String ROLE_SHARED_SERVICE = "RO161";
	private static final int PAGE_SIZE = 250;
	
	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	OrganisationRepository organisationRepository;

	@Autowired
	OrgRelationshipRepository orgRelationshipRepository;

	@Autowired
	PatientCountRunRepository patientCountRunRepository;

	@Autowired
	PatientCountRepository patientCountRepository;

	@Autowired
	LegacySolutionRepository legacySolutionRepository;

	@Autowired
	OrgSolutionRepository orgSolutionRepository;

	/**
	 * Endpoint for loading CCGs into a database. 
	 * ODS ORD REST endpoints are used as the datasource which is used to page result sets.
	 * @param model
	 * @return
	 */
	@GetMapping("/dataload/CCGs")
	public String loadCCGs(Model model) {
		
		List<Organisation> orgsAdded = new ArrayList<Organisation>();
		List<ChangedOrganisation> orgsChanged = new ArrayList<ChangedOrganisation>();
		List<String> relsAdded = new ArrayList<String>();
		List<Exception> exceptions = new ArrayList<Exception>();
		int iPage = 0;
		while (loadPageOfOrgsOfRole(ROLE_CCG, iPage++, orgsAdded, orgsChanged, relsAdded, exceptions)) {}
		
		model.addAttribute("orgTypeName", "CCGs");
		model.addAttribute("orgsAdded", orgsAdded);
		model.addAttribute("relsAdded", relsAdded);
		model.addAttribute("orgsChanged", orgsChanged);
		model.addAttribute("exceptions", exceptions);
		
		return "dataload/dataloadOrgs";
    }	

	/**
	 * Endpoint for loading GP Practices into a database.
	 * ODS ORD REST endpoints are used as the datasource which is used to page result sets.
	 * Each GP Practice is looked-up on ORD to find its CCG parent. For each found, a relationship 
	 * record is written.
	 * @param model
	 * @return
	 */
	@GetMapping("/dataload/GPs")
	public String loadGPs(Model model) {
		
		List<Organisation> orgsAdded = new ArrayList<Organisation>();
		List<ChangedOrganisation> orgsChanged = new ArrayList<ChangedOrganisation>();
		List<String> relsAdded = new ArrayList<String>();
		List<Exception> exceptions = new ArrayList<Exception>();
		int iPage = 0;
		while (loadPageOfOrgsOfRole(ROLE_PRIMARY_CARE, iPage++, orgsAdded, orgsChanged, relsAdded, exceptions)) {}
			
		model.addAttribute("orgTypeName", "GP Practices");
		model.addAttribute("orgsAdded", orgsAdded);
		model.addAttribute("relsAdded", relsAdded);
		model.addAttribute("orgsChanged", orgsChanged);
		model.addAttribute("exceptions", exceptions);
		
		return "dataload/dataloadOrgs";
    }	


	/**
	 * Endpoint for loading CSUs into a database. 
	 * ODS ORD REST endpoints are used as the datasource which is used to page result sets.
	 * @param model
	 * @return
	 */
	@GetMapping("/dataload/CSUs")
	public String loadCSUs(Model model) {
		
		List<Organisation> orgsAdded = new ArrayList<Organisation>();
		List<ChangedOrganisation> orgsChanged = new ArrayList<ChangedOrganisation>();
		List<String> relsAdded = new ArrayList<String>();
		List<Exception> exceptions = new ArrayList<Exception>();
		int iPage = 0;
		while (loadPageOfOrgsOfRole(ROLE_CSU, iPage++, orgsAdded, orgsChanged, relsAdded, exceptions)) {}
		
		model.addAttribute("orgTypeName", "CSUs");
		model.addAttribute("orgsAdded", orgsAdded);
		model.addAttribute("relsAdded", relsAdded);
		model.addAttribute("orgsChanged", orgsChanged);
		model.addAttribute("exceptions", exceptions);
		
		return "dataload/dataloadOrgs";
    }	

	/**
	 * Endpoint for loading Shared Services into a database. 
	 * ODS ORD REST endpoints are used as the datasource which is used to page result sets.
	 * @param model
	 * @return
	 */
	@GetMapping("/dataload/SSs")
	public String loadSSs(Model model) {
		
		List<Organisation> orgsAdded = new ArrayList<Organisation>();
		List<ChangedOrganisation> orgsChanged = new ArrayList<ChangedOrganisation>();
		List<String> relsAdded = new ArrayList<String>();
		List<Exception> exceptions = new ArrayList<Exception>();
		int iPage = 0;
		while (loadPageOfOrgsOfRole(ROLE_SHARED_SERVICE, iPage++, orgsAdded, orgsChanged, relsAdded, exceptions)) {}
		
		model.addAttribute("orgTypeName", "Shared Services");
		model.addAttribute("orgsAdded", orgsAdded);
		model.addAttribute("relsAdded", relsAdded);
		model.addAttribute("orgsChanged", orgsChanged);
		model.addAttribute("exceptions", exceptions);
		
		return "dataload/dataloadOrgs";
    }	

	/**
	 * Endpoint for loading CSUs into a database on Day Zero. Thereafter, they are manually-maintained
	 * Tracking Database csv file is used as the datasource.
	 * Each line contains a CCG and its parent CSU.
	 * @param model
	 * @return
	 */
	@GetMapping("/dataload/Day0CSUs")
	public String loadCSUsSelectFile(Model model) {
		return "dataload/dataloadCCGtoCSU";
	}

	@PostMapping("/dataload/Day0CSUs")
	public String loadCSUs(@RequestParam("file") MultipartFile file, Model model) {
		
		List<Organisation> orgsAdded = new ArrayList<Organisation>();
		List<ChangedOrganisation> orgsChanged = new ArrayList<ChangedOrganisation>();
		List<String> relsAdded = new ArrayList<String>();
		List<Exception> exceptions = new ArrayList<Exception>();

		Hashtable<String, CCGAndCSU> hshOrgs = new Hashtable<String, CCGAndCSU>();
		
		model.addAttribute("orgTypeName", "GP Practices");
		model.addAttribute("orgsAdded", orgsAdded);
		model.addAttribute("relsAdded", relsAdded);
		model.addAttribute("orgsChanged", orgsChanged);
		model.addAttribute("exceptions", exceptions);
		
		
		RelationshipType relTypeCSUtoCCG = null;
		try {
			relTypeCSUtoCCG = (RelationshipType)GUtils.makeObjectForId(RelationshipType.class, RelationshipType.CSU_TO_CCG);
		} catch (Exception e) {
			exceptions.add(e);
			return "dataload/dataloadOrgs";
		}
		
		Scanner scanner = null;
		try {
			scanner = new Scanner(file.getInputStream());
		} catch (Exception e) {
			exceptions.add(e);
		}
		
		if (scanner != null) {
			CSVUtils csvUtils = new CSVUtils();
			// Omit header line
			scanner.nextLine();
			
	        while (scanner.hasNext()) {
	            List<String> line = csvUtils.parseLine(scanner.nextLine());
	            if (line.size() > 5) {
	            	String ccgOrgCode = line.get(0).trim().replace("\"", "");
	            	String ccgName = line.get(1).trim().replace("\"", "");
	            	String csuOrgCode = line.get(3).trim().replace("\"", "");
	            	String csuName = line.get(4).trim().replace("\"", "");
	            	
	            	if (!csuName.toUpperCase().contains(" CLOSED")) {
	            		CCGAndCSU ccgAndCsu = new CCGAndCSU();
	            		ccgAndCsu.ccgOrgCode = ccgOrgCode;
	            		ccgAndCsu.ccgName = ccgName;
	            		ccgAndCsu.csuOrgCode = csuOrgCode;
	            		ccgAndCsu.csuName = csuName;
	            		hshOrgs.put(ccgOrgCode + "-" + csuOrgCode, ccgAndCsu);
	            	}
	            }
	        }
	        scanner.close();

			
	        for (String key : hshOrgs.keySet()) {
	        	CCGAndCSU ccgAndCsu = hshOrgs.get(key);
            	String ccgOrgCode = ccgAndCsu.ccgOrgCode;
            	String ccgName = ccgAndCsu.ccgName;
            	String csuOrgCode = ccgAndCsu.csuOrgCode;
            	String csuName = ccgAndCsu.csuName;
            	
            	if (!csuName.toUpperCase().contains(" CLOSED")) {
	            	// Look up CCG
					Optional<Organisation> optOrgCCG = organisationRepository.findByOrgCode(ccgOrgCode);
					if (optOrgCCG.isEmpty()) {
						exceptions.add(new Exception("Trying to load CCG having code " + ccgOrgCode + ", however it is not found on our database. Ensure that CCGs are loaded prior to this step via endpoint /dataload/CCGs"));
						continue;
					}

	            	Organisation orgCCG = optOrgCCG.get();
	            	
	            	Organisation orgCSU = null;
	            	boolean bNewOrg = false;
					Optional<Organisation> optOrgCSU = organisationRepository.findByOrgCode(csuOrgCode);
					
					try {
						// Add CSU if not found. No not change an existing CSU's name as we're getting that from ODS
						if (optOrgCSU.isEmpty()) {
							orgCSU = new Organisation();
							orgCSU.setOrgType((OrgType)GUtils.makeObjectForId(OrgType.class, OrgType.CSU));
							orgCSU.setOrgCode(csuOrgCode);
							orgCSU.setName(csuName);
							bNewOrg = true;
						} else {
							orgCSU = optOrgCSU.get();
						}		            	

						if (bNewOrg) {
							organisationRepository.save(orgCSU);
							orgsAdded.add(orgCSU);
						}
						
						// Delete any CCG to CSU relationships that are not in this csv file
						Iterator<OrgRelationship> iterOrgRels = orgRelationshipRepository.findAllByChildOrgAndRelationshipType(orgCCG, relTypeCSUtoCCG).iterator();
						while (iterOrgRels.hasNext()) {
							// If this database CSU - CCG relationship does not exist in this csv file, then delete it from database
							OrgRelationship orgRel = iterOrgRels.next();
							if (!hshOrgs.containsKey(orgRel.getChildOrg().getOrgCode() + "-" + orgRel.getParentOrg().getOrgCode()) ) {
								orgRelationshipRepository.delete(orgRel);
								relsAdded.add("CSU to CCG relationship deleted for CSU " + orgRel.getParentOrg().getOrgCode() + " to CCG " + orgRel.getChildOrg().getOrgCode());
							}
						}						
						
						// Add relationship from CSU to CCG if it's not in the database
						iterOrgRels = orgRelationshipRepository.findAllByParentOrgAndChildOrgAndRelationshipType(orgCSU, orgCCG, relTypeCSUtoCCG).iterator();
						if (!iterOrgRels.hasNext()) {
							OrgRelationship orgRel = new OrgRelationship();
							orgRel.setRelationshipType(relTypeCSUtoCCG);
							orgRel.setParentOrg(orgCSU);
							orgRel.setChildOrg(orgCCG);
							orgRelationshipRepository.save(orgRel);
							relsAdded.add("CSU to CCG relationship added for CSU " + orgCSU.getOrgCode() + " to CCG " + orgCCG.getOrgCode());
						}
						
						
					} catch (Exception e) {
						exceptions.add(e);
					}
            	}
            }
			
		}
		
		return "dataload/dataloadOrgs";
	}

	/**
	 * Endpoint for loading Legacy Systems into the database on Day Zero. Thereafter, the system will
	 * enable purchasing and storing of new systems.
	 * Tracking Database csv file is used as the datasource.
	 * Each line contains a GP Practice and its supplier and system.
	 * @param model
	 * @return
	 */
	@GetMapping("/dataload/Day0LegacySystems")
	public String loadLegacySystemsSelectFile(Model model, HttpServletRequest request) {
		Breadcrumbs.register("Load GP Legacy Systems", request);
		return "dataload/dataloadLegacySystems";
	}

	@PostMapping("/dataload/Day0LegacySystemsPost")
	public String loadLegacySystemsSelectFile(@RequestParam("file") MultipartFile file, Model model, HttpServletRequest request) {
		Breadcrumbs.register("Output", request);
		
		List<Organisation> suppliersAdded = new ArrayList<>();
		List<LegacySolution> legacySolutionsAdded = new ArrayList<>();
		List<Exception> exceptions = new ArrayList<Exception>();
		
		model.addAttribute("suppliersAdded", suppliersAdded);
		model.addAttribute("legacySolutionsAdded", legacySolutionsAdded);
		model.addAttribute("exceptions", exceptions);

		Scanner scanner = null;
		try {
			scanner = new Scanner(file.getInputStream());

		} catch (Exception e) {
			exceptions.add(e);
		}
		if (scanner != null) {
			long iLine = 1;
			CSVUtils csvUtils = new CSVUtils();
			// Omit header line
			scanner.nextLine();
			
	        while (scanner.hasNext()) {
	            List<String> line = csvUtils.parseLine(scanner.nextLine());
	            iLine++;

	            String sGPOrgCode = line.get(0).toUpperCase(); 
				String sSupplierCompositeCode = line.get(2);
				String sProductType = line.get(5); // Must be "Clinical system - GP"
				String sProductName = line.get(6);
				String sProductVersion = line.get(7);
				String sInstalledBy = line.get(8);
				String sOrderedDate = line.get(9);
				String sInstalledDate = line.get(11);
				String sSupplierOrgCode = "";
				String[] arrSupplierCompositeCode = sSupplierCompositeCode.split("-");
				if (arrSupplierCompositeCode.length > 0) {
					sSupplierOrgCode = arrSupplierCompositeCode[0].toUpperCase();
				}
				
				boolean bContinue = true;
				
				if (sProductType.equals("Clinical system - GP")) {
					// Get the GP Practice
					Optional<Organisation> optGP = organisationRepository.findByOrgCode(sGPOrgCode);
					Organisation orgGP = null;
					Optional<Organisation> optSupplier = null;
					Organisation orgSupplier = null;
					LegacySolution legSolution = null;
					OrgSolution orgSolution = null;
					
					if (sSupplierOrgCode == null || sSupplierOrgCode.trim().length() == 0) {
						exceptions.add(new Exception("Supplier code blank on line " + iLine));
						bContinue = false;
					}
					
					if (optGP.isEmpty()) {
						exceptions.add(new Exception("GP for Org Code " + sGPOrgCode + " not found"));
						bContinue = false;
					} else {
						orgGP = optGP.get();
					}
					
					// Get or setup the supplier
					if (bContinue) {
						// Massage the data slightly because of company mergers and acquisitions
						if (sProductName.toUpperCase().startsWith("EMIS")) {
							sSupplierOrgCode = "YGM06"; // YGM12 also used for EMIS, but this was originally iSoft, who were taken over by CSC, who then withdrew products from the market and GPs migrated to EMIS
							sInstalledBy = "EMIS";
						} else if (sProductName.toUpperCase().startsWith("SYNERGY")) {
								sSupplierOrgCode = "YGM06"; // iSoft -> CSC -> EMIS
								sInstalledBy = "EMIS";
						} else if (sProductName.toUpperCase().startsWith("VISUAL PHENIX")) {
							sSupplierOrgCode = "YGM06"; // Torex -> iSoft -> CSC -> EMIS
							sInstalledBy = "EMIS";
						} else if (sProductName.toUpperCase().startsWith("EVOLUTION")) {
							sSupplierOrgCode = "YGM16";
							sInstalledBy = "Microtest";
						} else if (sProductName.toUpperCase().startsWith("VISION")) {
							sSupplierOrgCode = "YGM11";
							sInstalledBy = "In Practice Systems";
						} else if (sProductName.toUpperCase().startsWith("SYSTM")) {
							sSupplierOrgCode = "YGM27";
							sInstalledBy = "TPP";
						}
						
						
						optSupplier = organisationRepository.findByOrgCode(sSupplierOrgCode);
						if (optSupplier.isEmpty()) {
							try {
								orgSupplier = new Organisation();
								orgSupplier.setOrgCode(sSupplierOrgCode);
								orgSupplier.setOrgType((OrgType)GUtils.makeObjectForId(OrgType.class, OrgType.SUPPLIER));
								orgSupplier.setName(sInstalledBy);
								organisationRepository.save(orgSupplier);
								suppliersAdded.add(orgSupplier);
							} catch (Exception e) {
								exceptions.add(e);
								bContinue = false;								
							}
						} else {
							orgSupplier = optSupplier.get();
							if ((orgSupplier.getName() == null || orgSupplier.getName().trim().length() == 0)
							 && sInstalledBy != null && sInstalledBy.trim().length() > 0) {
								orgSupplier.setName(sInstalledBy);
								organisationRepository.save(orgSupplier);
								exceptions.add(new Exception("**info** Supplier " + orgSupplier.getOrgCode() + " name updated to " + orgSupplier.getName()));
							}
						}
						
					}
					
					// If solution doesn't exist, create it
					if (bContinue) {
						Optional<LegacySolution> optLegSol = legacySolutionRepository.findByNameAndVersionAndSupplier(sProductName, sProductVersion, orgSupplier);
						if (optLegSol.isEmpty()) {
							legSolution = new LegacySolution();
							legSolution.setName(sProductName);
							legSolution.setVersion(sProductVersion);
							legSolution.setSupplier(orgSupplier);
							try {
								legacySolutionRepository.save(legSolution);
							} catch (Exception e) {
								exceptions.add(e);
								bContinue = false;
							}
						} else {
							legSolution = optLegSol.get();
						}
					}
					
					if (bContinue) {
						Optional<OrgSolution> optOrgSolution = orgSolutionRepository.findByOrganisationAndLegacySolution(orgGP, legSolution);
						if (optOrgSolution.isEmpty()) {
							orgSolution = new OrgSolution();
							orgSolution.setOrganisation(orgGP);
							orgSolution.setLegacySolution(legSolution);
							try {
								orgSolutionRepository.save(orgSolution);
							} catch (Exception e) {
								exceptions.add(e);
								bContinue = false;
							}
						} else {
							orgSolution = optOrgSolution.get();
						}
					}
				}
	        }
	        
	        scanner.close();
		}
		
		return "dataload/dataloadLegacySystemsOutput";
	}		
	/**
	 * Endpoint for loading GP Practice Patient numbers.
	 * NHS Digital csv file produced from GP Payments system is used as the datasource.
	 * Each line (except the header) contains a GP Practice Code and its patient count.
	 * @param model
	 * @return
	 */
	@GetMapping("/dataload/PatientNumbers")
	public String loadPatientNumbers(Model model, HttpServletRequest request) {
		Breadcrumbs.register("Load Patient Numbers", request);
		return "dataload/dataloadPatientNumbers";
	}

	@PostMapping("/dataload/PatientNumbers")
	public String loadPatientNumbers(@RequestParam("file") MultipartFile file, Model model, HttpServletRequest request) {
		Breadcrumbs.register("Output", request);
		
		List<Organisation> orgsAdded = new ArrayList<Organisation>();
		List<ChangedOrganisation> orgsChanged = new ArrayList<ChangedOrganisation>();
		List<String> relsAdded = new ArrayList<String>();
		List<Exception> exceptions = new ArrayList<Exception>();

		Hashtable<String, CCGAndCSU> hshOrgs = new Hashtable<String, CCGAndCSU>();
		
		model.addAttribute("orgTypeName", "GP Practices");
		model.addAttribute("orgsAdded", orgsAdded);
		model.addAttribute("relsAdded", relsAdded);
		model.addAttribute("orgsChanged", orgsChanged);
		model.addAttribute("exceptions", exceptions);
		
		
		RelationshipType relTypeCSUtoCCG = null;
		try {
			relTypeCSUtoCCG = (RelationshipType)GUtils.makeObjectForId(RelationshipType.class, RelationshipType.CSU_TO_CCG);
		} catch (Exception e) {
			exceptions.add(e);
			return "dataload/dataloadOrgs";
		}
		
		Scanner scanner = null;
		try {
			scanner = new Scanner(file.getInputStream());

		} catch (Exception e) {
			exceptions.add(e);
		}
		if (scanner != null) {
			CSVUtils csvUtils = new CSVUtils();
			// Omit header line
			scanner.nextLine();
			
			PatientCountRun run = null;
			
	        while (scanner.hasNext()) {
	            List<String> line = csvUtils.parseLine(scanner.nextLine());
				
				// GP Practice Org Code
				String sOrgCode = line.get(5); 
				String sPatientCount = line.get(9); 
				int iPatientCount = 0;
				try {
					iPatientCount = Integer.parseInt(sPatientCount);
				} catch (Exception e) {
					exceptions.add(new Exception("Number [" + sPatientCount + "] not valid for code " + sOrgCode));
					continue;
				}
				Organisation org = null;
				Optional<Organisation> optOrg = organisationRepository.findByOrgCode(sOrgCode);
				if (optOrg.isEmpty()) {
					relsAdded.add("Organisation " + sOrgCode + "  not found");
					continue;
				}
				org = optOrg.get();
				
				if (run == null) {
					run = new PatientCountRun();
					run.setRunDate(new Date());
					run.setFileName(StringUtils.right(file.getOriginalFilename(), 255));
					run = patientCountRunRepository.save(run);
				}
				
				PatientCount patientCount = new PatientCount();
				patientCount.setOrg(org);
				patientCount.setRun(run);
				patientCount.setPatientCount(iPatientCount);
				patientCountRepository.save(patientCount);				
	        }
	        scanner.close();
		}
		
		return "dataload/dataloadOrgs";
	}
	
	
	private boolean loadPageOfOrgsOfRole(
			String sORDRole,
			long page,  // zero-based
			List<Organisation> orgsAdded,
			List<ChangedOrganisation> orgsChanged,
			List<String> relsAdded,
			List<Exception> exceptions
			) {
		
		RelationshipType relTypeCCGtoGP = null;
		RestTemplate restTemplate = null;

		long iOrgType = 0;
		if (sORDRole.equals(ROLE_CCG)) {
			iOrgType = OrgType.CCG;
		} else if (sORDRole.equals(ROLE_CSU)) {
			iOrgType = OrgType.CSU;
		} else if (sORDRole.equals(ROLE_SHARED_SERVICE)) {
			iOrgType = OrgType.CSU;
		} else if (sORDRole.equals(ROLE_GP_PRACTICE) || sORDRole.equals(ROLE_PRIMARY_CARE)) {
			iOrgType = OrgType.GPPRACTICE;
			restTemplate = new RestTemplate();
			try {
				relTypeCCGtoGP = (RelationshipType)GUtils.makeObjectForId(RelationshipType.class, RelationshipType.CCG_TO_GPPRACTICE);
			} catch (Exception e) {
				exceptions.add(e);
			}
		}
				
		String response = getPageOfOrganisations(sORDRole, PAGE_SIZE, page * PAGE_SIZE + 1);
		//System.out.println(response);
		
		try {
			JsonNode root = objectMapper.readTree(response);
			Iterator<JsonNode> rootOrgs = root.get("Organisations").elements();
			if (!rootOrgs.hasNext()) {
				return false;
			}
			while (rootOrgs.hasNext()) {
				String orgName = "";
				String orgCode = "";
				try {
					
					JsonNode jsonOrg = rootOrgs.next();
					JsonNode jsonOrgName = jsonOrg.get("Name");
					if (jsonOrgName == null) {
						exceptions.add(new Exception("\"Name\" not found in json: " + jsonOrg.toString()));
						continue;
					}
					orgName = jsonOrgName.asText();
					if (iOrgType == OrgType.CSU) {
						orgName = orgName.replace("COMMISSIONING SUPPORT UNIT", "CSU");
					}	
					
					if (iOrgType == OrgType.CCG) {
						if (orgName.contains("COMMISSIONING HUB")) { 
							exceptions.add(new Exception(orgName + " not added as it's a Commissioning Hub"));
							continue;
						}						
					}
					
					JsonNode jsonOrgCode = jsonOrg.get("OrgId");
					if (jsonOrgCode == null) {
						exceptions.add(new Exception("\"OrgId\" not found in json: " + jsonOrg.toString()));
						continue;
					}
					orgCode = jsonOrgCode.asText();
					
					Organisation org = null;
					Optional<Organisation> optOrg = organisationRepository.findByOrgCode(orgCode);
					boolean bNewOrg = false;
					boolean bChangedOrg = false;
					String sChange = "";
					if (optOrg.isEmpty()) {
						org = new Organisation();
						org.setOrgType((OrgType)GUtils.makeObjectForId(OrgType.class, iOrgType));
						org.setOrgCode(orgCode);
						org.setName(orgName);
						bNewOrg = true;
					} else {
						org = optOrg.get();
						if (!orgName.equals(org.getName())) {
							sChange = orgCode + " - Name changed from '" + org.getName() + "' to '" + orgName + "'";
							org.setName(orgName);
							bChangedOrg = true;
						}
					}
					if (bNewOrg || bChangedOrg) {
						organisationRepository.save(org);
					}
					if (bNewOrg) {
						orgsAdded.add(org);
					}
					if (bChangedOrg) {
						ChangedOrganisation chgOrg = new ChangedOrganisation();
						chgOrg.organisation = org;
						chgOrg.change = sChange;
						orgsChanged.add(chgOrg);
					}
					
					// For GP Practices, add the CCG relationship
					if (iOrgType == OrgType.GPPRACTICE) {
						String url = ODS_ORD_URL + "/" + org.getOrgCode();
						
						String responseOrg = restTemplate.getForObject(url, String.class);
						JsonNode rootOrg = objectMapper.readTree(responseOrg);

						//Iterator<JsonNode> jsonRels = rootOrg.get("Organisation.Rels.Rel").elements();
						JsonNode jsonOrg2 = rootOrg.get("Organisation");
						if (jsonOrg2 == null) {
							exceptions.add(new Exception("\"Organisation\" not found in json: " + rootOrg.toString()));
							continue;
						}
						JsonNode jsonRels = jsonOrg2.get("Rels");
						if (jsonRels == null) {
							exceptions.add(new Exception("\"Rels\" not found in json: " + jsonOrg2.toString()));
							continue;
						}
						
						JsonNode jsonRelsRel = jsonRels.get("Rel");
						if (jsonRelsRel == null) {
							exceptions.add(new Exception("\"Rel\" not found in json: " + jsonRels.toString()));
							continue;
						}
						
						Iterator<JsonNode> jsonRelSet = jsonRelsRel.elements();
						boolean bFoundCCG = false;
						while (jsonRelSet.hasNext()) {
							JsonNode jsonRel = jsonRelSet.next();
							JsonNode jsonStatus = jsonRel.get("Status");
							if (jsonStatus == null) {
								exceptions.add(new Exception("\"Status\" not found in json: " + jsonRel.toString()));
								continue;
							}
							
							String sStatus = jsonStatus.asText();
							if (sStatus.equals("Active")) {
								JsonNode jsonTarget = jsonRel.get("Target");
								if (jsonTarget == null) {
									exceptions.add(new Exception("\"Target\" not found in json: " + jsonRel.toString()));
									continue;
								} else {
									JsonNode jsonPrimaryRoleId = jsonTarget.get("PrimaryRoleId");
									if (jsonPrimaryRoleId == null) {
										exceptions.add(new Exception("\"PrimaryRoleId\" not found in json: " + jsonTarget.toString()));
										continue;
									} else {
										JsonNode jsonPrimaryRoleIdId = jsonPrimaryRoleId.get("id");
										if (jsonPrimaryRoleIdId == null) {
											exceptions.add(new Exception("\"id\" not found in json: " + jsonPrimaryRoleId.toString()));
											continue;
										} else {
											String sRoleId = jsonPrimaryRoleIdId.asText();
											if (sRoleId.equals(ROLE_CCG)) {
												JsonNode jsonOrgId = jsonTarget.get("OrgId");
												if (jsonOrgId == null) {
													exceptions.add(new Exception("\"OrgId\" not found in json: " + jsonTarget.toString()));
													continue;
												} else {
													JsonNode jsonExtension = jsonOrgId.get("extension");
													if (jsonExtension == null) {
														exceptions.add(new Exception("\"extension\" not found in json: " + jsonOrgId.toString()));
														continue;
													} else {
														String sCCGOrgCode = jsonExtension.asText();
														if (sCCGOrgCode != null && !sCCGOrgCode.isBlank()) {
															sCCGOrgCode = sCCGOrgCode.trim();
															Optional<Organisation> optCCG = organisationRepository.findByOrgCode(sCCGOrgCode);
															if (optCCG.isPresent()) {
																Organisation orgCCG = optCCG.get();
																Iterable<OrgRelationship> iterOrgRels = orgRelationshipRepository.findAllByParentOrgAndChildOrgAndRelationshipType(orgCCG, org, relTypeCCGtoGP);
																if (!iterOrgRels.iterator().hasNext()) {
																	OrgRelationship orgRel = new OrgRelationship();
																	orgRel.setRelationshipType(relTypeCCGtoGP);
																	orgRel.setParentOrg(orgCCG);
																	orgRel.setChildOrg(org);
																	orgRelationshipRepository.save(orgRel);
																	relsAdded.add("CCG to GP Practice relationship added for " + orgCCG.getOrgCode() + " to " + org.getOrgCode());
																}
															}
														}
													}
												}
												bFoundCCG = true;
												break;
											}
										}
									}
								}
							}
						}
						if (!bFoundCCG) {
							exceptions.add(new Exception("No CCG found for OrgCode: " + orgCode + " - " + orgName));
						}
					}
					
				} catch (Exception e) {
					exceptions.add(e);
				}
			}
		} catch (IOException ioe) {
			exceptions.add(ioe);
		}
		return true;
	}

	private String getPageOfOrganisations(String role, int pageSize, long offset) {
		RestTemplate restTemplate = new RestTemplate();
		String url = ODS_ORD_URL + "?Status=Active&PrimaryRoleId=" + role;
		if (role.equals(ROLE_PRIMARY_CARE)) {
			url += "&NonPrimaryRoleId=" + ROLE_GP_PRACTICE;
		}
		url += "&Limit=" + pageSize + "&Offset=" + offset;
		
		String response = restTemplate.getForObject(url, String.class);
		return response;
	}
	
	public static class ChangedOrganisation {
		public Organisation organisation;
		public String change;
	}
	
	
	public static class CCGAndCSU {
    	String ccgOrgCode;
    	String ccgName;
    	String csuOrgCode;
    	String csuName;
	}
}
