package uk.nhs.gpitf.purchasing.services;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.nhs.gpitf.purchasing.entities.*;
import uk.nhs.gpitf.purchasing.repositories.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class EvaluationService {

    @Autowired
    private EvaluationProcCriterionRepository evaluationProcCriterionRepository;
    
    @Autowired
    private OffCatCriterionRepository offCatCriterionRepository;

    public boolean containsCriteria(Procurement procurement) {
    	return evaluationProcCriterionRepository.findByProcurement(procurement.getId()).size() > 0;
    }

    public boolean containsCriteria(long procurementId) {
    	return evaluationProcCriterionRepository.findByProcurement(procurementId).size() > 0;
    }
    
    public void convertOnToOffCatalogCriteria(Procurement procurement) {
    	convertOnToOffCatalogCriteria(procurement.getId());
    }
    
    /**
     * Convert the set of On-Catalogue evaluation criteria to Off-Catalogue evaluation criteria.
     * This involves matching the free-format On-Catalogue criteria names to fixed Off-Catalogue values.
     * This uses a fuzzy-match on words in each - it's not going to be 100% right all the time.
     * Therefore the user should be directed towards a screen that enables checking/editing them.
     * @param procurementId
     */
    public void convertOnToOffCatalogCriteria(long procurementId) {
    	int THRESHOLD = 3;
    	List<EvaluationProcCriterion> criteria = evaluationProcCriterionRepository.findByProcurement(procurementId);
    	List<OffCatCriterion> offCats = new ArrayList<>();
    	offCatCriterionRepository.findAll().forEach(offCats::add);
    	for (EvaluationProcCriterion criterion : criteria) {
    		int minDistance = Integer.MAX_VALUE;
    		long minDistanceOffCatId = -1;
    		for (OffCatCriterion offCat : offCats) {
    			String[] words1 = criterion.getName().split(" ");
    			String[] words2 = offCat.getName().split(" ");
    			for (int idx1=0; idx1<words1.length; idx1++) {
    				if (words1[idx1].trim().length() > 4) {
    	    			for (int idx2=0; idx2<words2.length; idx2++) {
    	    				if (words2[idx2].trim().length() > 4) {
    	    	    			int distance = StringUtils.getLevenshteinDistance(words1[idx1], words2[idx2]);
    	    	    			if (distance < minDistance && distance <= THRESHOLD) {
    	    	    				minDistance = distance;
    	    	    				minDistanceOffCatId = offCat.getId();
    	    	    			}

    	    				}
    	    			}
    				}    				
    			}
    		}
    		
    		List<OffCatCriterion> offCatsToRemove = new ArrayList<>();
    		if (minDistanceOffCatId > -1) {
        		for (OffCatCriterion offCat : offCats) {
        			if (offCat.getId() == minDistanceOffCatId) {
	        			criterion.setName(null);
	        			criterion.setTolerance(null);
	        			criterion.setOffCatCriterion(Integer.valueOf((int)minDistanceOffCatId));
	        			offCatsToRemove.add(offCat);
        			}
        		}    			
    		}
    		offCats.removeAll(offCatsToRemove);
    	}

    	evaluationProcCriterionRepository.saveAll(criteria);

    	List<EvaluationProcCriterion> criteriaToDelete = new ArrayList<>();
    	for (EvaluationProcCriterion criterion : criteria) {
    		if (criterion.getName() != null) {
    			criteriaToDelete.add(criterion);
    		}
    	}
		evaluationProcCriterionRepository.deleteAll(criteriaToDelete);
    }
}
