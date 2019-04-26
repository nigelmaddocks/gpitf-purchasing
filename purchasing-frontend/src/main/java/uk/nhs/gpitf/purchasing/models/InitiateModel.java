package uk.nhs.gpitf.purchasing.models;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.Data;
import uk.nhs.gpitf.purchasing.entities.ProcShortlistRemovalReason;
import uk.nhs.gpitf.purchasing.entities.ProcSolutionBundle;
import uk.nhs.gpitf.purchasing.entities.ProcSrvRecipient;
import uk.nhs.gpitf.purchasing.entities.TmpAdditionalService;
import uk.nhs.gpitf.purchasing.entities.TmpAssociatedService;
import uk.nhs.gpitf.purchasing.entities.swagger.SolutionEx2;
import uk.nhs.gpitf.purchasing.services.TmpSolutionPriceBandService;

@Data
public class InitiateModel {
	TmpSolutionPriceBandService tmpSolutionPriceBandService;
	
	private long procurementId;
	//private List<SolutionEx2> solutions = new ArrayList<>();
	private List<ProcSolutionBundle> dbBundles = new ArrayList<>();
	private List<ProcSrvRecipient> srvRecipients = new ArrayList<>();

	private int numberOfPractices = 0;
	private int numberOfPatients = 0;
	private LocalDate plannedContractStart = null;
	public Integer[] contractTermMonths = new Integer[] {}; // One element per Service Recipient
	public Integer[] patientCount = new Integer[] {}; // One element per Service Recipient
	public RowDetail[][] rowDetailForBaseSystemPerBundleAndSR = new RowDetail[][] {};
	public RowDetail[][][] rowDetailForAssocSrvPerBundleAndSR = new RowDetail[][][] {};
	public RowDetail[][][] rowDetailForAdditSrvPerBundleAndSR = new RowDetail[][][] {};
	
	public String[][][] assocSrv = new String[][][] {};
	public Integer[][][] assocSrvUnits = new Integer[][][] {};
	public String[][][] additSrv = new String[][][] {};
	public Integer[][][] additSrvUnits = new Integer[][][] {};
	public String[][][][] additAssocSrv = new String[][][][] {};
	public Integer[][][][] additAssocSrvUnits = new Integer[][][][] {};
	
	public String removeSolutionId = null;
	public Long removalReasonId = null;
	@Size(max = 255)
	public String removalReasonText = "";
	public int DIRECTAWARD_MAXVALUE = 0;
	public String directAwardBundleId = "";
	private int[] possibleContractTermMonths = new int[] {}; 
	private Hashtable<Long, List<TmpAssociatedService>> possibleBundleAssociatedServices = new Hashtable<>();
	private Hashtable<Long, List<TmpAdditionalService>> possibleBundleAdditionalServices = new Hashtable<>();
	private Hashtable<String, List<TmpAssociatedService>>PossibleAdditAssociatedServices = new Hashtable<>();
	
	
	public BigDecimal getUnitPriceForBundle(long bundleId, int bandingUnits) {
		for (var bundle : dbBundles) {
			if (bundle.getId() == bundleId) {
				BigDecimal price = BigDecimal.valueOf(0L);
				for (var bundleItem : bundle.getBundleItems()) {
					String solutionId = bundleItem.getSolutionId();
					if (StringUtils.isNotEmpty(solutionId)) {
						price = price.add(tmpSolutionPriceBandService.getUnitPriceForSolution(solutionId, bandingUnits));						
					} else {
						String additionalService = bundleItem.getAdditionalService();
						if (StringUtils.isNotEmpty(additionalService)) {
							price = price.add(tmpSolutionPriceBandService.getUnitPriceForAdditionalService(additionalService, bandingUnits));						
						}
					}
				}
				return price;
			}
		}
		return BigDecimal.ZERO;
	}

	public BigDecimal getPriceForBundle(long bundleId, int bandingUnits, int priceUnits) {
		for (var bundle : dbBundles) {
			if (bundle.getId() == bundleId) {
				//return bundle.getPrice().multiply(BigDecimal.valueOf(numberOfPatients)).setScale(0, RoundingMode.HALF_UP);
				BigDecimal price = BigDecimal.valueOf(0L);
				for (var bundleItem : bundle.getBundleItems()) {
					String solutionId = bundleItem.getSolutionId();
					if (StringUtils.isNotEmpty(solutionId)) {
						price = price.add(tmpSolutionPriceBandService.getPriceForSolution(solutionId, bandingUnits, priceUnits));						
					} else {
						String additionalService = bundleItem.getAdditionalService();
						if (StringUtils.isNotEmpty(additionalService)) {
							price = price.add(tmpSolutionPriceBandService.getPriceForAdditionalService(additionalService, bandingUnits, priceUnits));						
						}
					}
				}
				return price;
			}
		}
		return BigDecimal.ZERO;
	}

	public BigDecimal getPriceOverTermForBundle(long bundleId, int bandingUnits, int priceUnits, int termMonths) {
		for (var bundle : dbBundles) {
			if (bundle.getId() == bundleId) {
				BigDecimal price = BigDecimal.valueOf(0L);
				for (var bundleItem : bundle.getBundleItems()) {
					String solutionId = bundleItem.getSolutionId();
					if (StringUtils.isNotEmpty(solutionId)) {
						price = price.add(tmpSolutionPriceBandService.getPriceOverTermForSolution(solutionId, bandingUnits, priceUnits, termMonths));						
					} else {
						String additionalService = bundleItem.getAdditionalService();
						if (StringUtils.isNotEmpty(additionalService)) {
							price = price.add(tmpSolutionPriceBandService.getPriceOverTermForAdditionalService(additionalService, bandingUnits, priceUnits, termMonths));						
						}
					}
				}
				return price;
			}
		}
		return BigDecimal.ZERO;
	}

	
	
	public boolean canDirectAward(long bundleId, int bandingUnits, int priceUnits, int termMonths) {
		BigDecimal price = getPriceOverTermForBundle(bundleId, bandingUnits, priceUnits, termMonths);
		return price.compareTo(new BigDecimal(DIRECTAWARD_MAXVALUE)) <= 0;
		// Direct award no longer depends on whether foundation solution
/*		
		for (var solution : solutions) {
			if (solution.getId().equals(solutionId)) {
				return !solution.isFoundation();
			}
		}
*/
	}
	
	public static class RowDetail {
		public long bundleId;
		public String solutionId;
		public String associatedService;
		public String additionalService;
		public RowDetail[] additAssociatedServices = new RowDetail[] {};
		public String name;
		public String unitTypeName;
		public BigDecimal unitPrice;
		public Integer bandingUnits;
		public Integer priceUnits;
		public BigDecimal price;
		public BigDecimal priceOverTerm;
		public boolean readonly;
	}
}
