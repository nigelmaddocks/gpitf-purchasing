package uk.nhs.gpitf.purchasing.services;

import org.springframework.stereotype.Component;
import uk.nhs.gpitf.purchasing.entities.ProcSolutionBundle;

import java.util.ArrayList;
import java.util.List;

@Component("OnboardingServiceStub")
public class OnboardingServiceStub implements IOnboardingService {

    @Override
    public List<OnboardingService.RankedBundle> findRankedSolutionsHavingCapabilitiesInList(String csvCapabilityList, String csvInteroperables, boolean foundation) {
        OnboardingService.RankedBundle rankedBundle = new OnboardingService.RankedBundle();
      //  rankedBundle.bundle.setId(1L);
//        rankedBundle.bundle = new ProcSolutionBundle();
        List<OnboardingService.RankedBundle> list = new ArrayList<>();
      //  list.add(rankedBundle);
        return list;
    }

}
