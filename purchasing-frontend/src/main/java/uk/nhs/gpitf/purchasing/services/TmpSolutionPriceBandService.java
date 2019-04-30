package uk.nhs.gpitf.purchasing.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.nhs.gpitf.purchasing.entities.*;
import uk.nhs.gpitf.purchasing.repositories.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class TmpSolutionPriceBandService {

    @Autowired
    private TmpSolutionPriceBandRepository thisRepository;

    public BigDecimal getPriceForSolution(String solutionId, int bandingUnits, int priceUnits) {
        List<TmpSolutionPriceBand> coll = new ArrayList<>();
        thisRepository.findAllBySolutionIdOrderByLowerLimitIncl(solutionId).forEach(coll::add);        
        return getPrice(coll, bandingUnits, priceUnits);
    }

    public BigDecimal getPriceForAssociatedService(String associatedService, int bandingUnits, int priceUnits) {
        List<TmpSolutionPriceBand> coll = new ArrayList<>();
        thisRepository.findAllByAssociatedServiceOrderByLowerLimitIncl(associatedService).forEach(coll::add);        
        return getPrice(coll, bandingUnits, priceUnits);
    }

    public BigDecimal getPriceForAdditionalService(String additionalService, int bandingUnits, int priceUnits) {
        List<TmpSolutionPriceBand> coll = new ArrayList<>();
        thisRepository.findAllByAdditionalServiceOrderByLowerLimitIncl(additionalService).forEach(coll::add);        
        return getPrice(coll, bandingUnits, priceUnits);
    }        

    public BigDecimal getPriceOverTermForSolution(String solutionId, int bandingUnits, int priceUnits, int termMonths) {
        List<TmpSolutionPriceBand> coll = new ArrayList<>();
        thisRepository.findAllBySolutionIdOrderByLowerLimitIncl(solutionId).forEach(coll::add);        
        return getPriceOverTerm(coll, bandingUnits, priceUnits, termMonths);
    }

    public BigDecimal getPriceOverTermForAssociatedService(String associatedService, int bandingUnits, int priceUnits, int termMonths) {
        List<TmpSolutionPriceBand> coll = new ArrayList<>();
        thisRepository.findAllByAssociatedServiceOrderByLowerLimitIncl(associatedService).forEach(coll::add);        
        return getPriceOverTerm(coll, bandingUnits, priceUnits, termMonths);
    }

    public BigDecimal getPriceOverTermForAdditionalService(String additionalService, int bandingUnits, int priceUnits, int termMonths) {
        List<TmpSolutionPriceBand> coll = new ArrayList<>();
        thisRepository.findAllByAdditionalServiceOrderByLowerLimitIncl(additionalService).forEach(coll::add);        
        return getPriceOverTerm(coll, bandingUnits, priceUnits, termMonths);
    }        

    public BigDecimal getUnitPriceForSolution(String solutionId, int numberOf) {
        List<TmpSolutionPriceBand> coll = new ArrayList<>();
        thisRepository.findAllBySolutionIdOrderByLowerLimitIncl(solutionId).forEach(coll::add);        
        return getUnitPrice(coll, numberOf);
    }

    public BigDecimal getUnitPriceForAssociatedService(String associatedService, int numberOf) {
        List<TmpSolutionPriceBand> coll = new ArrayList<>();
        thisRepository.findAllByAssociatedServiceOrderByLowerLimitIncl(associatedService).forEach(coll::add);        
        return getUnitPrice(coll, numberOf);
    }

    public BigDecimal getUnitPriceForAdditionalService(String additionalService, int numberOf) {
        List<TmpSolutionPriceBand> coll = new ArrayList<>();
        thisRepository.findAllByAdditionalServiceOrderByLowerLimitIncl(additionalService).forEach(coll::add);        
        return getUnitPrice(coll, numberOf);
    }

    public String getUnitTextForSolution(String solutionId) {
        List<TmpSolutionPriceBand> coll = new ArrayList<>();
        thisRepository.findAllBySolutionIdOrderByLowerLimitIncl(solutionId).forEach(coll::add);        
        return getUnitText(coll);
    }

    public String getUnitTextForAssociatedService(String associatedService) {
        List<TmpSolutionPriceBand> coll = new ArrayList<>();
        thisRepository.findAllByAssociatedServiceOrderByLowerLimitIncl(associatedService).forEach(coll::add);        
        return getUnitText(coll);
    }

    public String getUnitTextForAdditionalService(String additionalService) {
        List<TmpSolutionPriceBand> coll = new ArrayList<>();
        thisRepository.findAllByAdditionalServiceOrderByLowerLimitIncl(additionalService).forEach(coll::add);        
        return getUnitText(coll);
    }

    public TmpPriceBasis getPriceBasisForSolution(String solutionId) {
        List<TmpSolutionPriceBand> coll = new ArrayList<>();
        thisRepository.findAllBySolutionIdOrderByLowerLimitIncl(solutionId).forEach(coll::add);        
        return getPriceBasis(coll);
    }

    public TmpPriceBasis getPriceBasisForAssociatedService(String associatedService) {
        List<TmpSolutionPriceBand> coll = new ArrayList<>();
        thisRepository.findAllByAssociatedServiceOrderByLowerLimitIncl(associatedService).forEach(coll::add);        
        return getPriceBasis(coll);
    }

    public TmpPriceBasis getPriceBasisForAdditionalService(String additionalService) {
        List<TmpSolutionPriceBand> coll = new ArrayList<>();
        thisRepository.findAllByAdditionalServiceOrderByLowerLimitIncl(additionalService).forEach(coll::add);        
        return getPriceBasis(coll);
    }

    public TmpUnitType getBandingUnitForSolution(String solutionId) {
        List<TmpSolutionPriceBand> coll = new ArrayList<>();
        thisRepository.findAllBySolutionIdOrderByLowerLimitIncl(solutionId).forEach(coll::add);        
        return getBandingUnit(coll);
    }

    public TmpUnitType getBandingUnitForAssociatedService(String associatedService) {
        List<TmpSolutionPriceBand> coll = new ArrayList<>();
        thisRepository.findAllByAssociatedServiceOrderByLowerLimitIncl(associatedService).forEach(coll::add);        
        return getBandingUnit(coll);
    }

    public TmpUnitType getBandingUnitForAdditionalService(String additionalService) {
        List<TmpSolutionPriceBand> coll = new ArrayList<>();
        thisRepository.findAllByAdditionalServiceOrderByLowerLimitIncl(additionalService).forEach(coll::add);        
        return getBandingUnit(coll);
    }
    
    private BigDecimal getPrice(List<TmpSolutionPriceBand> coll, int bandingUnits, int priceUnits) {
    	BigDecimal price = BigDecimal.valueOf(0L);
    	BigDecimal catchAllPrice = null;
        boolean matchesNonNullBand = false;
        TmpPriceBasis nullBandPriceBasis = null;
    	for (TmpSolutionPriceBand band : coll) {
    		if (band.getLowerLimitIncl() == null && band.getUpperLimitIncl() == null) {
    			catchAllPrice = band.getPrice();
    			nullBandPriceBasis = band.getPriceBasis();
    		}
    		if ((band.getLowerLimitIncl() == null || band.getLowerLimitIncl() <= bandingUnits)
    		 && (band.getUpperLimitIncl() == null || band.getUpperLimitIncl() >= bandingUnits)
       		 && (band.getLowerLimitIncl() != null || band.getUpperLimitIncl() != null)) {
    			matchesNonNullBand = true;
    			TmpPriceBasis priceBasis = band.getPriceBasis();
    			if (priceBasis.isDependentOnUnits()) { 
	    			if (priceBasis.isTiered()) {
	    				price = price.add(band.getPrice().multiply(BigDecimal.valueOf(priceUnits)));
	    			} else {
	    				price = band.getPrice().multiply(BigDecimal.valueOf(priceUnits));
	    			}
    			} else {
	    			if (priceBasis.isTiered()) {
	    				price = price.add(band.getPrice());
	    			} else {
	    				price = band.getPrice();
	    			}
    			}
    		}
    	}
        
    	if (!matchesNonNullBand && nullBandPriceBasis != null) {
    		if (nullBandPriceBasis.isDependentOnUnits()) { 
    			price = catchAllPrice.multiply(BigDecimal.valueOf(priceUnits));
    		} else {
    			price = catchAllPrice;
    		}
    	}
        
        return price.setScale(2, RoundingMode.HALF_UP);
    }    
    
    private BigDecimal getPriceOverTerm(List<TmpSolutionPriceBand> coll, int bandingUnits, int priceUnits, int termMonths) {
    	BigDecimal price = BigDecimal.valueOf(0L);
    	BigDecimal catchAllPrice = null;
        boolean matchesNonNullBand = false;
        TmpPriceBasis nullBandPriceBasis = null;
    	for (TmpSolutionPriceBand band : coll) {
    		if (band.getLowerLimitIncl() == null && band.getUpperLimitIncl() == null) {
    			catchAllPrice = band.getPrice();
    			nullBandPriceBasis = band.getPriceBasis();
    		}
    		if ((band.getLowerLimitIncl() == null || band.getLowerLimitIncl() <= bandingUnits)
    		 && (band.getUpperLimitIncl() == null || band.getUpperLimitIncl() >= bandingUnits)
       		 && (band.getLowerLimitIncl() != null || band.getUpperLimitIncl() != null)) {
    			matchesNonNullBand = true;
    			TmpPriceBasis priceBasis = band.getPriceBasis();
    			if (priceBasis.isDependentOnUnits()) { 
	    			if (priceBasis.isTiered()) {
	    				price = price.add(band.getPrice().multiply(BigDecimal.valueOf(priceUnits)));
	    			} else {
	    				price = band.getPrice().multiply(BigDecimal.valueOf(priceUnits));
	    			}
    			} else {
	    			if (priceBasis.isTiered()) {
	    				price = price.add(band.getPrice());
	    			} else {
	    				price = band.getPrice();
	    			}
    			}
    			
    			if (priceBasis.isDependentOnTermMonths()) {
    				price = price.multiply(BigDecimal.valueOf(termMonths));
    			} else
    			if (priceBasis.isDependentOnTermYears()) {
    				price = price.multiply(BigDecimal.valueOf(termMonths)).divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
    			}
    		}
    	}
        
    	if (!matchesNonNullBand && nullBandPriceBasis != null) {
    		if (nullBandPriceBasis.isDependentOnUnits()) { 
    			price = catchAllPrice.multiply(BigDecimal.valueOf(priceUnits));
    		} else {
    			price = catchAllPrice;
    		}
			if (nullBandPriceBasis.isDependentOnTermMonths()) {
				price = price.multiply(BigDecimal.valueOf(termMonths));
			} else
			if (nullBandPriceBasis.isDependentOnTermYears()) {
				price = price.multiply(BigDecimal.valueOf(termMonths)).divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
			}
    	}
        
        return price.setScale(2, RoundingMode.HALF_UP);
    }    
    
    private BigDecimal getUnitPrice(List<TmpSolutionPriceBand> coll, int numberOf) {
    	BigDecimal price = BigDecimal.valueOf(0L);
    	BigDecimal catchAllPrice = null;
        boolean matchesNonNullBand = false;
    	for (TmpSolutionPriceBand band : coll) {
    		if (band.getLowerLimitIncl() == null && band.getUpperLimitIncl() == null) {
    			catchAllPrice = band.getPrice();
    		}
    		if ((band.getLowerLimitIncl() == null || band.getLowerLimitIncl() <= numberOf)
    		 && (band.getUpperLimitIncl() == null || band.getUpperLimitIncl() >= numberOf)
       		 && (band.getLowerLimitIncl() != null || band.getUpperLimitIncl() != null)) {
    			matchesNonNullBand = true;
    			if (band.getPriceBasis().isTiered()) {
    				price = price.add(band.getPrice());
    			} else {
    				price = band.getPrice();
    			}
    		}
    	}
        
    	if (!matchesNonNullBand) {
    		price = catchAllPrice;
    	}
        
        return price;
    }    
    
    private TmpPriceBasis getPriceBasis(List<TmpSolutionPriceBand> coll) {
    	TmpPriceBasis priceBasis = null;
    	for (TmpSolutionPriceBand band : coll) {
    		priceBasis = band.getPriceBasis();
    		break;
    	}        
        return priceBasis;
    }    
    
    private TmpUnitType getBandingUnit(List<TmpSolutionPriceBand> coll) {
    	TmpUnitType unitType = null;
    	for (TmpSolutionPriceBand band : coll) {
    		unitType = band.getBandingUnitType();
    		break;
    	}        
        return unitType;
    }    
    
    private String getUnitText(List<TmpSolutionPriceBand> coll) {
    	TmpPriceBasis priceBasis = getPriceBasis(coll);
    	if (priceBasis != null) {
    		long unit1Id = 0;
    		if (priceBasis.getUnit1() != null) {
    			unit1Id = priceBasis.getUnit1().getId();
    		}
    		if (unit1Id == TmpUnitType.PATIENT || unit1Id == TmpUnitType.SERVICE_RECIPIENT) {
    			return priceBasis.getName();
    		} else {
    			return coll.get(0).getUnitName();
    		}
    	}
    	return "";
    }    

}
