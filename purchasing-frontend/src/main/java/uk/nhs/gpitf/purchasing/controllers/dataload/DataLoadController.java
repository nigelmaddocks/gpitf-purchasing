package uk.nhs.gpitf.purchasing.controllers.dataload;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
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
import uk.nhs.gpitf.purchasing.utils.Breadcrumbs;
import uk.nhs.gpitf.purchasing.utils.CSVUtils;
import uk.nhs.gpitf.purchasing.utils.GUtils;

@Controller
public class DataLoadController {
	
	private static final String ODS_ORD_URL = "https://directory.spineservices.nhs.uk/ORD/2-0-0/organisations";
	private static final String ROLE_PRIMARY_CARE = "RO177";

	private static final String ROLE_GP_PRACTICE = "RO76";
	private static final String ROLE_OOH_PRACTICE = "RO80"; // Out of hours
	private static final String ROLE_WIC_PRACTICE = "RO87"; // Walk-in centre
	
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
	@GetMapping("/dataload/Practices")
	public String loadPractices(Model model) {
		
		List<Organisation> orgsAdded = new ArrayList<Organisation>();
		List<ChangedOrganisation> orgsChanged = new ArrayList<ChangedOrganisation>();
		List<String> relsAdded = new ArrayList<String>();
		List<Exception> exceptions = new ArrayList<Exception>();
		int iPage = 0;
		while (loadPageOfOrgsOfRole(ROLE_OOH_PRACTICE, iPage++, orgsAdded, orgsChanged, relsAdded, exceptions)) {}
		
		iPage = 0;
		while (loadPageOfOrgsOfRole(ROLE_WIC_PRACTICE, iPage++, orgsAdded, orgsChanged, relsAdded, exceptions)) {}	
		
		iPage = 0;
		while (loadPageOfOrgsOfRole(ROLE_GP_PRACTICE, iPage++, orgsAdded, orgsChanged, relsAdded, exceptions)) {}	
		
		
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
	 * Endpoint for loading Legacy Systems into the database on Day Zero. Thereafter, the system will
	 * enable purchasing and storing of new systems.
	 * XSLX file is used as the datasource.
	 * Each line contains a GP Practice and its supplier and system.
	 * @param model
	 * @return
	 */
	@GetMapping("/dataload/Day0LegacySystemsExcel")
	public String loadLegacySystemsExcelSelectFile(Model model, HttpServletRequest request) {
		Breadcrumbs.register("Load GP Legacy Systems", request);
		return "dataload/dataloadLegacySystemsExcel";
	}

	@PostMapping("/dataload/Day0LegacySystemsExcelPost")
	public String loadLegacySystemsExcelSelectFile(@RequestParam("file") MultipartFile file, Model model, HttpServletRequest request) {
		Breadcrumbs.register("Output", request);
		
		LocalDate contractEndDate = LocalDate.of(2019, Month.DECEMBER, 31);
		
		List<Organisation> suppliersAdded = new ArrayList<>();
		List<LegacySolution> legacySolutionsAdded = new ArrayList<>();
		List<Exception> exceptions = new ArrayList<Exception>();
		
		model.addAttribute("suppliersAdded", suppliersAdded);
		model.addAttribute("legacySolutionsAdded", legacySolutionsAdded);
		model.addAttribute("exceptions", exceptions);

		Workbook workbook = null;
		Sheet sheet = null; 
		try {
			workbook = WorkbookFactory.create(file.getInputStream());
			sheet = workbook.getSheetAt(0);
		} catch (Exception e) {
			exceptions.add(e);
		}
		
		
		if (workbook != null && sheet != null) {
			long iLine = 1;
			Iterator<Row> rowIterator = sheet.rowIterator();
			// Omit header line
			rowIterator.next();
			
	        while (rowIterator.hasNext()) {
	            Row line = rowIterator.next();
	            iLine++;

	            String sGPOrgCode = line.getCell(0).getStringCellValue().trim().toUpperCase(); 
//				String sSupplierCompositeCode = line.get(2);
//				String sProductType = line.get(5); // Must be "Clinical system - GP"
				String sProductName = line.getCell(13).getStringCellValue().trim().toUpperCase();
//				String sProductVersion = line.get(7);
				String sSupplierName = line.getCell(12).getStringCellValue().trim().toUpperCase();
//				String sOrderedDate = line.get(9);
//				String sInstalledDate = line.get(11);
				String sSupplierOrgCode = "";
				
				boolean bContinue = true;
				
				if (sProductName != null && sProductName.length() > 0) {
					// Get the GP Practice
					Optional<Organisation> optGP = organisationRepository.findByOrgCode(sGPOrgCode);
					Organisation orgGP = null;
					Optional<Organisation> optSupplier = null;
					Organisation orgSupplier = null;
					LegacySolution legSolution = null;
					OrgSolution orgSolution = null;
					
//					if (sSupplierOrgCode == null || sSupplierOrgCode.trim().length() == 0) {
//						exceptions.add(new Exception("Supplier code blank on line " + iLine));
//						bContinue = false;
//					}
					
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
							sSupplierName = "EMIS";
						} else if (sProductName.toUpperCase().startsWith("EVOLUTION")) {
							sSupplierOrgCode = "YGM16";
							sSupplierName = "Microtest";
						} else if (sProductName.toUpperCase().startsWith("VISION")) {
							sSupplierOrgCode = "YGM11";
							sSupplierName = "In Practice Systems";
						} else if (sProductName.toUpperCase().startsWith("SYSTM")) {
							sSupplierOrgCode = "YGM27";
							sSupplierName = "TPP";
						}
						
						
						optSupplier = organisationRepository.findByOrgCode(sSupplierOrgCode);
						if (optSupplier.isEmpty()) {
							try {
								orgSupplier = new Organisation();
								orgSupplier.setOrgCode(sSupplierOrgCode);
								orgSupplier.setOrgType((OrgType)GUtils.makeObjectForId(OrgType.class, OrgType.SUPPLIER));
								orgSupplier.setName(sSupplierName);
								organisationRepository.save(orgSupplier);
								suppliersAdded.add(orgSupplier);
							} catch (Exception e) {
								exceptions.add(e);
								bContinue = false;								
							}
						} else {
							orgSupplier = optSupplier.get();
							if ((orgSupplier.getName() == null || orgSupplier.getName().trim().length() == 0)
							 && sSupplierName != null && sSupplierName.trim().length() > 0) {
								orgSupplier.setName(sSupplierName);
								organisationRepository.save(orgSupplier);
								exceptions.add(new Exception("**info** Supplier " + orgSupplier.getOrgCode() + " name updated to " + orgSupplier.getName()));
							}
						}
						
					}
					
					// If solution doesn't exist, create it
					if (bContinue) {
						Optional<LegacySolution> optLegSol = legacySolutionRepository.findByNameAndVersionAndSupplier(sProductName, null, orgSupplier);
						if (optLegSol.isEmpty()) {
							legSolution = new LegacySolution();
							legSolution.setName(sProductName);
							//legSolution.setVersion(sProductVersion);
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
						} else {
							orgSolution = optOrgSolution.get();
						}
						orgSolution.setContractEndDate(contractEndDate);
						try {
							orgSolutionRepository.save(orgSolution);
						} catch (Exception e) {
							exceptions.add(e);
							bContinue = false;
						}
					}
				}
	        }
	        try {
	        	workbook.close();
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
		}
		
		return "dataload/dataloadLegacySystemsExcelOutput";
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
		
		RelationshipType relTypeCCGtoPractice = null;
		RestTemplate restTemplate = new RestTemplate();

		long iOrgType = 0;
		Long iOrgSubType = null;
		if (sORDRole.equals(ROLE_CCG)) {
			iOrgType = OrgType.CCG;
		} else if (sORDRole.equals(ROLE_CSU)) {
			iOrgType = OrgType.CSU;
		} else if (sORDRole.equals(ROLE_SHARED_SERVICE)) {
			iOrgType = OrgType.CSU;
		} else if (sORDRole.equals(ROLE_GP_PRACTICE) || sORDRole.equals(ROLE_OOH_PRACTICE) || sORDRole.equals(ROLE_WIC_PRACTICE)) {
			iOrgType = OrgType.PRESCRIBING_PRACTICE;
			try {
				relTypeCCGtoPractice = (RelationshipType)GUtils.makeObjectForId(RelationshipType.class, RelationshipType.CCG_TO_PRACTICE);
			} catch (Exception e) {
				exceptions.add(e);
			}
		}
		
		if (sORDRole.equals(ROLE_GP_PRACTICE)) {
			iOrgSubType = OrgType.GP;
		} else if (sORDRole.equals(ROLE_OOH_PRACTICE)) {
			iOrgSubType = OrgType.OOH;
		} else if (sORDRole.equals(ROLE_WIC_PRACTICE)) {
			iOrgSubType = OrgType.WIC;
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
					boolean bChangedAddress = false;
					String sChange = "";
					if (optOrg.isEmpty()) {
						org = new Organisation();
						org.setOrgType((OrgType)GUtils.makeObjectForId(OrgType.class, iOrgType));
						org.setOrgCode(orgCode);
						org.setName(orgName);
						if (iOrgSubType != null) {
							org.setOrgSubType((OrgType)GUtils.makeObjectForId(OrgType.class, iOrgSubType.longValue()));
						}
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
						org = organisationRepository.save(org);
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
				
					// Add addresses
				
					String url = ODS_ORD_URL + "/" + org.getOrgCode();
					
					String responseOrg = restTemplate.getForObject(url, String.class);
					JsonNode rootOrg = objectMapper.readTree(responseOrg);

					//Iterator<JsonNode> jsonRels = rootOrg.get("Organisation.Rels.Rel").elements();
					JsonNode jsonOrg2 = rootOrg.get("Organisation");
					if (jsonOrg2 == null) {
						exceptions.add(new Exception("\"Organisation\" not found in json: " + rootOrg.toString()));
						continue;
					}
					
					JsonNode jsonGeoloc = jsonOrg2.get("GeoLoc");
					if (jsonGeoloc == null) {
						exceptions.add(new Exception("\"jsonGeoloc\" not found in json: " + jsonOrg2.toString()));
					} else {
						JsonNode jsonLocation = jsonGeoloc.get("Location");
						if (jsonLocation == null) {
							exceptions.add(new Exception("\"Location\" not found in json: " + jsonOrg2.toString()));								
						} else {
							JsonNode jsonAddrLine1 = jsonLocation.get("AddrLn1");
							JsonNode jsonAddrLine2 = jsonLocation.get("AddrLn2");
							JsonNode jsonAddrLine3 = jsonLocation.get("AddrLn3");
							JsonNode jsonAddrTown = jsonLocation.get("Town");
							JsonNode jsonAddrCounty = jsonLocation.get("County");
							JsonNode jsonAddrPostcode = jsonLocation.get("PostCode");
							JsonNode jsonAddrCountry = jsonLocation.get("Country");
							String addrLine1 = jsonAddrLine1 != null ? jsonAddrLine1.asText() : null;
							String addrLine2 = jsonAddrLine2 != null ? jsonAddrLine2.asText() : null;
							String addrLine3 = jsonAddrLine3 != null ? jsonAddrLine3.asText() : null;
							String addrTown = jsonAddrTown != null ? jsonAddrTown.asText() : null;
							String addrCounty = jsonAddrCounty != null ? jsonAddrCounty.asText() : null;
							String addrPostcode = jsonAddrPostcode != null ? jsonAddrPostcode.asText() : null;
							String addrCountry = jsonAddrCountry != null ? jsonAddrCountry.asText() : null;
							if (		bNewOrg
									 ||	!GUtils.nullToString(org.getAddrLine1()).equals(GUtils.nullToString(addrLine1))
									 || !GUtils.nullToString(org.getAddrLine2()).equals(GUtils.nullToString(addrLine2))
									 || !GUtils.nullToString(org.getAddrLine3()).equals(GUtils.nullToString(addrLine3))
									 || !GUtils.nullToString(org.getAddrTown()).equals(GUtils.nullToString(addrTown))
									 || !GUtils.nullToString(org.getAddrCounty()).equals(GUtils.nullToString(addrCounty))
									 || !GUtils.nullToString(org.getAddrPostcode()).equals(GUtils.nullToString(addrPostcode))
									 || !GUtils.nullToString(org.getAddrCountry()).equals(GUtils.nullToString(addrCountry))
							) {
								bChangedAddress = true;
								if (!bNewOrg) {
									sChange = orgCode + " - Address changed from '" + 
											GUtils.nullToString(org.getAddrLine1()) + "," + 
											GUtils.nullToString(org.getAddrLine2()) + "," + 
											GUtils.nullToString(org.getAddrLine3()) + "," + 
											GUtils.nullToString(org.getAddrTown()) + "," + 
											GUtils.nullToString(org.getAddrCounty()) + "," + 
											GUtils.nullToString(org.getAddrPostcode()) + "," + 
											GUtils.nullToString(org.getAddrCountry()) +
											" to " +
											GUtils.nullToString(addrLine1) + "," + 
											GUtils.nullToString(addrLine2) + "," + 
											GUtils.nullToString(addrLine3) + "," + 
											GUtils.nullToString(addrTown) + "," + 
											GUtils.nullToString(addrCounty) + "," + 
											GUtils.nullToString(addrPostcode) + "," + 
											GUtils.nullToString(addrCountry);
								}
								
								org.setAddrLine1(addrLine1);
								org.setAddrLine2(addrLine2);
								org.setAddrLine3(addrLine3);
								org.setAddrTown(addrTown);
								org.setAddrCounty(addrCounty);
								org.setAddrPostcode(addrPostcode);
								org.setAddrCountry(addrCountry);
								org = organisationRepository.save(org);
								
								if (!bNewOrg) {
									ChangedOrganisation chgOrg = new ChangedOrganisation();
									chgOrg.organisation = org;
									chgOrg.change = sChange;
									orgsChanged.add(chgOrg);
								}
								
							}
						}
					}
					// For GP Practices, add the CCG relationship
					if (iOrgType == OrgType.PRESCRIBING_PRACTICE) {
						
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
																Iterable<OrgRelationship> iterOrgRels = orgRelationshipRepository.findAllByParentOrgAndChildOrgAndRelationshipType(orgCCG, org, relTypeCCGtoPractice);
																if (!iterOrgRels.iterator().hasNext()) {
																	OrgRelationship orgRel = new OrgRelationship();
																	orgRel.setRelationshipType(relTypeCCGtoPractice);
																	orgRel.setParentOrg(orgCCG);
																	orgRel.setChildOrg(org);
																	orgRelationshipRepository.save(orgRel);
																	relsAdded.add("CCG to Prescribing Practice relationship added for " + orgCCG.getOrgCode() + " to " + org.getOrgCode());
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

	
	
	
	
	
	
	
	
	
	

	/**
	 * Endpoint for loading Principal and Subsidiary services.
	 * This needs more work. Currently it just lists the live services
	 * but it should be developed to store some of the info against
	 * GP Practices.
	 * See notes in the view for info on how to obtain
	 * the csv input file.
	 *
	 * @param model
	 * @return
	 */
	@GetMapping("/dataload/GPSOC")
	public String loadGPSOCSelectFile(Model model, HttpServletRequest request) {
		Breadcrumbs.register("Load Subsidiary Services", request);
		return "dataload/dataloadGPSOC";
	}

	@PostMapping("/dataload/GPSOC")
	public String loadGPSOCSelectFile(@RequestParam("file") MultipartFile file, Model model, HttpServletRequest request) {
		String OUTPUT_PAGE = "dataload/dataloadGPSOCOutput";
		
		Breadcrumbs.register("Output", request);

		LocalDate contractEndDate = LocalDate.of(2019, Month.DECEMBER, 31);
		
		List<String> subsidiaryServicesAdded = new ArrayList<>();
		List<String> principalServicesAdded = new ArrayList<>();
		List<OrgSolution> orgSolutionsAdded = new ArrayList<>();
		List<Exception> exceptions = new ArrayList<Exception>();
		
		model.addAttribute("principalServices", principalServicesAdded);
		model.addAttribute("subsidiaryServices", subsidiaryServicesAdded);
		model.addAttribute("orgSolutionsAdded", orgSolutionsAdded);
		model.addAttribute("exceptions", exceptions);

		Scanner scanner = null;
		try {
			scanner = new Scanner(file.getInputStream());

		} catch (Exception e) {
			exceptions.add(e);
		}
		long iLine = 0;
		if (scanner != null) {
			CSVUtils csvUtils = new CSVUtils();

			// Omit header line, but test that we're dealing with the correct columns
			List<String> line = csvUtils.parseLine(scanner.nextLine());
			String sExpectedCurrentPrincipalServicesColHdg = "GPSoCRCurrentPrincipalServices";
			String sExpectedCurrentSubsidiaryServicesColHdg = "GPSoCRCurrentSubsidiaryServices";
			String sActualCurrentPrincipalServicesColHdg = GUtils.nullToString(line.get(41)).trim();
			String sActualCurrentSubsidiaryServicesColHdg = GUtils.nullToString(line.get(43)).trim();
			if (!sExpectedCurrentPrincipalServicesColHdg.equals(sActualCurrentPrincipalServicesColHdg)) {
				exceptions.add(new Exception("Column headers not right. Expected '" + sExpectedCurrentPrincipalServicesColHdg + "', found '" + sActualCurrentPrincipalServicesColHdg + "'"));
			}
			if (!sExpectedCurrentPrincipalServicesColHdg.equals(sActualCurrentPrincipalServicesColHdg)) {
				exceptions.add(new Exception("Column headers not right. Expected '" + sExpectedCurrentSubsidiaryServicesColHdg + "', found '" + sActualCurrentSubsidiaryServicesColHdg + "'"));
			}
			
			if (exceptions.size() > 0) {
				return OUTPUT_PAGE;
			}
			
	        while (scanner.hasNext()) {
	            line = csvUtils.parseLine(scanner.nextLine());
	            iLine++;
	            
	            if (line.size() > 41) {
		            String sGPSOC = line.get(41); // GPSOCRCurrentPrincipalServices
		            String[] arrGPSOC = sGPSOC.split(";");
	
		            for (String service : arrGPSOC) {
		            	service = service.trim();
		            	if (service.length() > 0) {
		            		if (!principalServicesAdded.contains(service)) {
		            			principalServicesAdded.add(service);
		            		}
		            	}
		            }
	            }
	            
	            if (line.size() > 43) {
		            String sGPSOC = line.get(43); // GPSOCRCurrentSubsidiaryServices
		            String[] arrGPSOC = sGPSOC.split(";");
	
		            for (String service : arrGPSOC) {
		            	service = service.trim();
		            	if (service.length() > 0) {
		            		if (!subsidiaryServicesAdded.contains(service)) {
		            			subsidiaryServicesAdded.add(service);
		            		}
		            		
		            		// Add Docman to installed Legacy system where it's found
		            		if (service.startsWith("Document Management - Docman GP")
		            		 && service.contains("(Centrally funded)")) {
		            			String sProductName = "Docman GP";
		            			String sProductVersion = service.substring(service.indexOf(sProductName) + sProductName.length(), service.indexOf("(Centrally funded)")).trim();
		            			String sSupplierName = service.substring(service.indexOf("(Centrally funded)") + "(Centrally funded)".length())
		            					.trim().replace("(", "").replace(")", "").trim();
		            			String sGPOrgCode = line.get(5).trim().replace("\"", "").trim().toUpperCase();
		            			
		            			// Add / Amend Supplier
		            			if (sSupplierName.toUpperCase().contains("PCTI")) {
		    						boolean bContinue = true;

		    						Optional<Organisation> optGP  = organisationRepository.findByOrgCode(sGPOrgCode);	
		    						Organisation orgGP = null;
		        					if (optGP.isEmpty()) {
		        						exceptions.add(new Exception("GP for Org Code " + sGPOrgCode + " not found"));
		        						bContinue = false;
		        					} else {
		        						orgGP = optGP.get();
		        					}
		            				
		            				
		            				String sSupplierOrgCode = "8HP20";
		    						Optional<Organisation> optSupplier = organisationRepository.findByOrgCode(sSupplierOrgCode);
		    						Organisation orgSupplier = null;
		    						if (optSupplier.isEmpty()) {
		    							try {
		    								orgSupplier = new Organisation();
		    								orgSupplier.setOrgCode(sSupplierOrgCode);
		    								orgSupplier.setOrgType((OrgType)GUtils.makeObjectForId(OrgType.class, OrgType.SUPPLIER));
		    								orgSupplier.setName(sSupplierName);
		    								organisationRepository.save(orgSupplier);
		    							} catch (Exception e) {
		    								exceptions.add(e);
		    								bContinue = false;
		    							}
		    						} else {
		    							orgSupplier = optSupplier.get();
		    							if ((orgSupplier.getName() == null || orgSupplier.getName().trim().length() == 0)
		    							 && sSupplierName != null && sSupplierName.trim().length() > 0) {
		    								orgSupplier.setName(sSupplierName);
		    								organisationRepository.save(orgSupplier);
		    								exceptions.add(new Exception("**info** Supplier " + orgSupplier.getOrgCode() + " name updated to " + orgSupplier.getName()));
		    							}
		    						}

		    						
		    						// If solution doesn't exist, create it
		    						LegacySolution legSolution = null;
		    						if (bContinue) {
		    							Optional<LegacySolution> optLegSol = legacySolutionRepository.findByNameAndVersionAndSupplier(sProductName, sProductVersion, orgSupplier);
		    							if (optLegSol.isEmpty()) {
		    								legSolution = new LegacySolution();
		    								legSolution.setName(sProductName);
		    								legSolution.setVersion(sProductVersion);
		    								legSolution.setSupplier(orgSupplier);
		    								legSolution.setFoundation(false);
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
		    							OrgSolution orgSolution = null;
		    							if (optOrgSolution.isEmpty()) {
		    								orgSolution = new OrgSolution();
		    								orgSolution.setOrganisation(orgGP);
		    								orgSolution.setLegacySolution(legSolution);
		    							} else {
		    								orgSolution = optOrgSolution.get();
		    							}
		    							orgSolution.setContractEndDate(contractEndDate);
		    							try {
		    								orgSolutionRepository.save(orgSolution);
		    								if (optOrgSolution.isEmpty() ) {
		    									orgSolutionsAdded.add(orgSolution);
		    								}
		    							} catch (Exception e) {
		    								exceptions.add(e);
		    								bContinue = false;
		    							}

		    						}		    						
		            			}		            			
		            		}
		            	}
		            }
	            }
	        }
	        
	        principalServicesAdded.sort((object1, object2) -> object1.compareToIgnoreCase(object2));
	        subsidiaryServicesAdded.sort((object1, object2) -> object1.compareToIgnoreCase(object2));
	        
	        scanner.close();
		}
		
		model.addAttribute("lineCount", iLine);

		return OUTPUT_PAGE;
	}	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private String getPageOfOrganisations(String role, int pageSize, long offset) {
		RestTemplate restTemplate = new RestTemplate();
		String nonPrimaryRole = "";
		if (role.equals(ROLE_GP_PRACTICE) || role.equals(ROLE_OOH_PRACTICE) || role.equals(ROLE_WIC_PRACTICE)) {
			nonPrimaryRole = role;
			role = ROLE_PRIMARY_CARE;
		}
		String url = ODS_ORD_URL + "?Status=Active&PrimaryRoleId=" + role;
		if (nonPrimaryRole.length() > 0) {
			url += "&NonPrimaryRoleId=" + nonPrimaryRole;
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
