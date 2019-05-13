package uk.nhs.gpitf.purchasing.services;

import java.util.List;

public interface IOnboardingService {

    List<OnboardingService.RankedBundle> findRankedSolutionsHavingCapabilitiesInList(String csvCapabilityList, String csvInteroperables, boolean foundation);

}
