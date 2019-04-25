package uk.nhs.gpitf.purchasing.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.util.StringUtils;
import uk.nhs.gpitf.purchasing.entities.*;
import uk.nhs.gpitf.purchasing.repositories.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProcSolutionBundleService {

    @Autowired
    private ProcSolutionBundleRepository thisRepository;

    @Autowired
    private TmpAdditionalServiceService additionalServiceService;

    @Autowired
    private TmpAssociatedServiceService associatedServiceService;

    public List<TmpAssociatedService>getAssociatedServicesForBundle(ProcSolutionBundle bundle) {
    	List<TmpAssociatedService> associatedServices = new ArrayList<>();
    	for (ProcSolutionBundleItem item : bundle.getBundleItems()) {
    		if (StringUtils.isNotEmpty(item.getSolutionId())) {
    			List<TmpAssociatedService> itemAssociatedServices = associatedServiceService.getAllBySolutionIdOrderByName(item.getSolutionId());
    			associatedServices.addAll(itemAssociatedServices);
    		}
    	}
    	
    	associatedServices.sort((object1, object2) -> (object1.getName()).compareToIgnoreCase(object2.getName()));
    	return associatedServices;
    }

    public List<TmpAdditionalService>getAdditionalServicesForBundle(ProcSolutionBundle bundle) {
    	List<TmpAdditionalService> additionalServices = new ArrayList<>();
    	for (ProcSolutionBundleItem item : bundle.getBundleItems()) {
    		if (StringUtils.isNotEmpty(item.getSolutionId())) {
    			List<TmpAdditionalService> itemAdditionalServices = additionalServiceService.getAllBySolutionIdOrderByName(item.getSolutionId());
    			additionalServices.addAll(itemAdditionalServices);
    		}
    	}
    	
    	additionalServices.sort((object1, object2) -> (object1.getName()).compareToIgnoreCase(object2.getName()));
    	return additionalServices;
    }

}
