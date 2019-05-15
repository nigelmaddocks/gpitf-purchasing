package uk.nhs.gpitf.purchasing.services.evaluations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.nhs.gpitf.purchasing.entities.*;
import uk.nhs.gpitf.purchasing.exceptions.InvalidCriterionException;
import uk.nhs.gpitf.purchasing.models.BundleScoring;
import uk.nhs.gpitf.purchasing.models.CriterionScore;
import uk.nhs.gpitf.purchasing.models.Evaluation;
import uk.nhs.gpitf.purchasing.models.EvaluationsModel;
import uk.nhs.gpitf.purchasing.repositories.*;
import uk.nhs.gpitf.purchasing.services.IOnboardingService;
import uk.nhs.gpitf.purchasing.services.OnboardingService;
import uk.nhs.gpitf.purchasing.utils.GUtils;
import uk.nhs.gpitf.purchasing.utils.SecurityInfo;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.Math.toIntExact;
import static uk.nhs.gpitf.purchasing.utils.SecurityInfo.getSecurityInfo;

@Component("evaluationService")
public class EvaluationService implements IEvaluationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EvaluationService.class);
    private static final String SCREEN_1 = "redirect:/buyingprocess/evaluations/{procurementId}";
    private static final String SCREEN_2 = "redirect:/buyingprocess/evaluations/solutionsReview/{procurementId}";
    private static final String SCREEN_2_ERROR = "buying-process/evaluationsScreen2";
    private static final String SEARCH_SOLUTIONS_SCREEN = "redirect:/buyingprocess/{procurementId}/solutionByCapability";
    private final IRepositoryFetcher repositoryFetcher;
    private final IOnboardingService onboardingService;
    private final EvaluationsScreen1SetUpHelper screen1SetUpHelper = new EvaluationsScreen1SetUpHelper();
    private final EvaluationsScreen2SetUpHelper screen2SetUpHelper = new EvaluationsScreen2SetUpHelper();

    @Value("${sysparam.shortlist.max}")
    private String SHORTLIST_MAX;

    @Autowired
    public EvaluationService(IRepositoryFetcher repositoryFetcher, IOnboardingService onboardingService) {
        this.repositoryFetcher = repositoryFetcher;
        this.onboardingService = onboardingService;
    }

    @Override
    public String setUpEvaluationsScreen1(EvaluationsServiceParameterObject espo) throws InvalidCriterionException {

        boolean procurementValidationPassed = screen1SetUpHelper.validateProcurement(espo.getAttr(), espo.getRequest(), espo.getProcurementId()).isSecurityPassed();

        if(procurementValidationPassed){
            screen1SetUpHelper.setUpHelperBox(espo.getModel());
            screen1SetUpHelper.populateDropDownWithOffCatalogCriterion(espo.getEvaluationsModel(), espo.getModel(), espo.getProcurementId());
            return screen1SetUpHelper.getScreen1Path(espo.getProcurementId());
        } else {
            return SecurityInfo.SECURITY_ERROR_REDIRECT;
        }

    }

    @Override
    public String submitScreen1Form(EvaluationsServiceParameterObject espo) {
        try {
            screen2SetUpHelper.persistEvaluationCriteriaFromScreen1(espo.getEvaluationsModel());
        } catch (InvalidCriterionException ice) {
        	espo.getBindingResult().addError(new ObjectError("Evaluation criteria error",  ice.getUserMessage()));
        	try {
                screen1SetUpHelper.setUpHelperBox(espo.getModel());
        		screen1SetUpHelper.populateDropDownWithOffCatalogCriterion(espo.getEvaluationsModel(), espo.getModel(), espo.getProcurementId());
        		return screen1SetUpHelper.getScreen1Path(espo.getProcurementId());
        	} catch (Exception e) {
        		return "path unknown";
        	}
        }
        return SEARCH_SOLUTIONS_SCREEN;
    }

    @Override
    public String setUpEvaluationsScreen2(EvaluationsServiceParameterObject espo) {
        Procurement p = repositoryFetcher.getProcurementRepository().findById(espo.getProcurementId()).get();

        RankedBundlesValidator rankedBundlesValidator = screen2SetUpHelper.validateAndGetBundles(p, espo.getRequest(), espo.getAttr());
        if(rankedBundlesValidator.isValidationFailed()) {
            return SecurityInfo.SECURITY_ERROR_REDIRECT;
        } else {
            screen2SetUpHelper.setUpBundleNamesCriterionNamesAndDropDowns(rankedBundlesValidator, p, espo);
            return "buying-process/evaluationsScreen2";
        }
    }

    @Override
    public String saveScoresForEachBundle(EvaluationsServiceParameterObject espo) {
        Procurement p = repositoryFetcher.getProcurementRepository().findById(espo.getProcurementId()).get();
        RankedBundlesValidator rankedBundlesValidator = screen2SetUpHelper.validateAndGetBundles(p, espo.getRequest(), espo.getAttr());
        if(rankedBundlesValidator.isValidationFailed()) {
            return SecurityInfo.SECURITY_ERROR_REDIRECT;
        }
        try {
            screen2SetUpHelper.persistScoresForEachBundle(espo, rankedBundlesValidator.getBundles());
        } catch(InvalidCriterionException ice) {
            screen2SetUpHelper.setUpBundleNamesCriterionNamesAndDropDowns(rankedBundlesValidator, p, espo);
            String errorMessage = "InvalidCriterionException::" + ice.getStackTrace()[0] +  " ::" + ice.getMessage();
            espo.getBindingResult().addError(new ObjectError("Evaluation scoring error",  ice.getUserMessage()));
            LOGGER.error(errorMessage);
            return SCREEN_2_ERROR;
        }
        return SCREEN_2; //TODO: Place order with winning supplier
    }

    private class EvaluationsScreen2SetUpHelper {

        private Integer getBundleId(ProcSolutionBundle procSolutionBundle) {
            return toIntExact(procSolutionBundle.getId());
        }

        private boolean scoringAlreadyDone(EvaluationsServiceParameterObject espo) throws InvalidCriterionException {
            ProcurementRepository procurementRepository = repositoryFetcher.getProcurementRepository();
            Optional<Procurement> optionalProcurement = procurementRepository.findById(espo.getProcurementId());
            if(optionalProcurement.isEmpty()) {
                throw new InvalidCriterionException("Procurement not found for procurement Id:: " + espo.getProcurementId());
            }
            Procurement procurement = optionalProcurement.get();
            ProcSolutionBundleRepository procSolutionBundleRepository = repositoryFetcher.getProcSolutionBundleRepository();
            List<ProcSolutionBundle> procSolutionBundles= procSolutionBundleRepository.findAllByProcurement(procurement);
            boolean scoringAlreadyDone = procSolutionBundles.get(0).getEvaluationScorePercent()!=null;
            return scoringAlreadyDone;
        }

        private void persistScoresForEachBundle(EvaluationsServiceParameterObject espo, List<ProcSolutionBundle> bundles)
                throws InvalidCriterionException {

            if(scoringAlreadyDone(espo)) {
                LOGGER.info("Scoring already done for procurement:: " + espo.getProcurementId() + " using previous scores.");
                return;
            }

            List<EvaluationBundleScore> evaluationBundleScores = new ArrayList<>();
            List<Integer> bundleIds = bundles.stream().map(this::getBundleId).collect(Collectors.toList());

            try {
	            for(int i=0; i<espo.getEvaluationsModel().getBundleScorings().size(); i++) {
	                BundleScoring bundleScoring = espo.getEvaluationsModel().getBundleScorings().get(i);
	                Integer bundleId = bundleIds.get(i);
	
	                for(CriterionScore criterionScore : bundleScoring.getCriterionScores()) {
	                    EvaluationBundleScore evaluationBundleScore = new EvaluationBundleScore();
	                    evaluationBundleScore.setBundle((ProcSolutionBundle) GUtils.makeObjectForId(ProcSolutionBundle.class, bundleId.longValue()));
	                    evaluationBundleScore.setScoredDate(new Date());
	                    evaluationBundleScore.setScore(criterionScore.getScore());
	                    evaluationBundleScore.setProcCriterion(criterionScore.getCriterionId());
	                    evaluationBundleScores.add(evaluationBundleScore);
	                    evaluationBundleScore.setScoredBy(getScoredBy(espo));
	                    updateScoreInProcSolutionBundleRepo(getEvaluationProcCriterion(criterionScore, espo), criterionScore, bundleId);
	                }
	            }

                 repositoryFetcher.getEvaluationBundleScoreRepository().saveAll(evaluationBundleScores);
            } catch (DataIntegrityViolationException dive) {
                throw new InvalidCriterionException("No score selected from dropdown:: " + dive, "No score selected from dropdown");
            } catch (Exception dive) {
                throw new InvalidCriterionException("No score selected from dropdown:: " + dive, "No score selected from dropdown");
            }
        }
        
        private int getScoredBy(EvaluationsServiceParameterObject espo) {
            SecurityInfo securityInfo = getSecurityInfo(espo.getRequest());
            int orgContactId = toIntExact(securityInfo.getOrgContactId());
            return orgContactId;
        }

        private EvaluationProcCriterion getEvaluationProcCriterion(CriterionScore criterionScore, EvaluationsServiceParameterObject espo) throws InvalidCriterionException {
            long criterionId = criterionScore.getCriterionId();
            EvaluationProcCriterionRepository repo = repositoryFetcher.getEvaluationProcCriterionRepository();
            Optional<EvaluationProcCriterion> optEvaluationProcCriterion = repo.findById(criterionId);
            EvaluationProcCriterion evaluationProcCriterion;
            if(optEvaluationProcCriterion.isPresent()) {
                evaluationProcCriterion = optEvaluationProcCriterion.get();
            } else {
                throw new InvalidCriterionException("Criteria for procurement:: " + espo.getProcurementId() + " not avaliable.");
            }
            return evaluationProcCriterion;
        }

        private void updateScoreInProcSolutionBundleRepo(EvaluationProcCriterion evaluationProcCriterion, CriterionScore criterionScore, Integer bundleId) throws InvalidCriterionException {
            long weighting = evaluationProcCriterion.getWeightingPercent();
            if (criterionScore == null || criterionScore.getScore() == null) { // nima
            	throw new InvalidCriterionException("No score selected from dropdown", "No score selected from dropdown");
            }
            long score = criterionScore.getScore();
            ProcSolutionBundle procSolutionBundle = getProcSolutionBundle(bundleId);
            BigDecimal value = procSolutionBundle.getEvaluationScorePercent();
            if (value == null) {
            	value = BigDecimal.valueOf(0.0);
            }
            procSolutionBundle.setEvaluationScorePercent(value.add(new BigDecimal(weighting*score/5)));
            try {
                repositoryFetcher.getProcSolutionBundleRepository().save(procSolutionBundle);
            } catch (DataIntegrityViolationException dive) {
                throw new InvalidCriterionException("No score selected from dropdown:: " + dive, "No score selected from dropdown");
            }
        }

        private ProcSolutionBundle getProcSolutionBundle(int bundleId) throws InvalidCriterionException {
            ProcSolutionBundleRepository repo = repositoryFetcher.getProcSolutionBundleRepository();
            Optional<ProcSolutionBundle> optProcSolutionBundle = repo.findById(Long.valueOf(bundleId));
            ProcSolutionBundle procSolutionBundle;

            if(optProcSolutionBundle.isPresent()) {
                procSolutionBundle = optProcSolutionBundle.get();
            } else {
                throw new InvalidCriterionException("ProcSolutionBundle object not available for bundleId:: " + bundleId);
            }
            
            return procSolutionBundle;
        }
        
        
        private void setUpBundleNamesCriterionNamesAndDropDowns(RankedBundlesValidator rankedBundlesValidator, 
                                                                Procurement p, 
                                                                EvaluationsServiceParameterObject espo) {
            
            List<ProcSolutionBundle> bundles = rankedBundlesValidator.getBundles();
            List<CriterionScore> criterionScores = screen2SetUpHelper.getCriterionScores(p.getId());

            espo.getEvaluationsModel().setScores(screen2SetUpHelper.getDropDownEntries());

            List<BundleScoring> bundleScorings = screen2SetUpHelper.getBundleScorings(bundles,criterionScores);
            
            // Load scores from user's input
        	if (espo.getRequest().getMethod().equalsIgnoreCase("POST")) { // nima
        		for (int idxBundleScoring = 0; idxBundleScoring < bundleScorings.size(); idxBundleScoring++) {
        			BundleScoring bundleScoring = bundleScorings.get(idxBundleScoring);
        			List<CriterionScore> newCriterionScores = new ArrayList<>();
        			for (int idxCriterionScore = 0; idxCriterionScore < bundleScoring.getCriterionScores().size(); idxCriterionScore++) {
        				CriterionScore criterionScore = bundleScoring.getCriterionScores().get(idxCriterionScore);
        				CriterionScore newCriterionScore = new CriterionScore();
        				Integer userScore = espo.getEvaluationsModel().getBundleScorings().get(idxBundleScoring).getCriterionScores().get(idxCriterionScore).getScore();
        				newCriterionScore.setCriterion(criterionScore.getCriterion());
        				newCriterionScore.setCriterionId(criterionScore.getCriterionId());
    					newCriterionScore.setScore(userScore.intValue());
        				newCriterionScores.add(newCriterionScore);
        			}
        			bundleScoring.setCriterionScores(newCriterionScores);
        			bundleScorings.set(idxBundleScoring, bundleScoring);
        		}
        	} else {
        	
        	// Load scores from database	
        		for (int idxBundleScoring = 0; idxBundleScoring < bundleScorings.size(); idxBundleScoring++) {
        			BundleScoring bundleScoring = bundleScorings.get(idxBundleScoring);
        			List<CriterionScore> newCriterionScores = new ArrayList<>();
        			Iterable<EvaluationBundleScore> iterEbs = repositoryFetcher.getEvaluationBundleScoreRepository().findAllByBundle(bundleScoring.getBundle());
        			boolean bAddedScores = false;
        			for (EvaluationBundleScore ebs : iterEbs) {
            			for (int idxCriterionScore = 0; idxCriterionScore < bundleScoring.getCriterionScores().size(); idxCriterionScore++) {
            				CriterionScore criterionScore = bundleScoring.getCriterionScores().get(idxCriterionScore);
            				if (ebs.getProcCriterion().intValue() == criterionScore.getCriterionId().intValue()) {
	            				CriterionScore newCriterionScore = new CriterionScore();
	            				Integer dbScore = ebs.getScore();
	            				newCriterionScore.setCriterion(criterionScore.getCriterion());
	            				newCriterionScore.setCriterionId(criterionScore.getCriterionId());
            					newCriterionScore.setScore(dbScore);
	            				newCriterionScores.add(newCriterionScore);
	            				bAddedScores = true;
	            				break;
            				}
            			}
        			}
        			if (bAddedScores) {
        				bundleScoring.setCriterionScores(newCriterionScores);
        			}
        			bundleScorings.set(idxBundleScoring, bundleScoring);
        		}        		
        	}
        	espo.getEvaluationsModel().setBundleScorings(bundleScorings);

            espo.getModel().addAttribute("evaluations", espo.getEvaluationsModel());

            Iterable<EvaluationScoreValue> evaluationScoreValues = repositoryFetcher.getEvaluationScoreRepository().findAll();
            List<EvaluationScoreValue> evaluationScoreValueList = new ArrayList<>();
            evaluationScoreValues.iterator().forEachRemaining(evaluationScoreValueList::add);

            espo.getModel().addAttribute("evaluationScoreValues", evaluationScoreValues);
            
        }

        private RankedBundlesValidator validateAndGetBundles(Procurement procurement, HttpServletRequest request, RedirectAttributes attr) {
            
            List<OnboardingService.RankedBundle> rankedBundles = onboardingService.findRankedSolutionsHavingCapabilitiesInList(
                    procurement.getCsvCapabilities(), 
                    procurement.getCsvInteroperables(), 
                    procurement.getFoundation().booleanValue()
            );
            
            if (rankedBundles.size() > Integer.valueOf(SHORTLIST_MAX)) {
                String message = getInvalidShortlistSizeMessage(rankedBundles.size(), procurement.getId());
                LOGGER.warn(getSecurityInfo(request).loggerSecurityMessage(message));
                attr.addFlashAttribute("security_message", message);
                return new RankedBundlesValidator(true, SecurityInfo.SECURITY_ERROR_REDIRECT);
            }

            ProcSolutionBundleRepository repo = repositoryFetcher.getProcSolutionBundleRepository();
            List<ProcSolutionBundle> procSolutionBundleList = repo.findAllByProcurement(procurement);
            return new RankedBundlesValidator(false, procSolutionBundleList);
        }
        
        private String getInvalidShortlistSizeMessage(int shortListSize, long procId) {
            return "Shortlist size for procurement " + procId + " is " + shortListSize +
                    " but the maximum allowed size is " + SHORTLIST_MAX + " in order to proceed to shortlisting. " +
                    "Please re-visit your procurement to reduce the solution results or go through the long-listing process.";
        }

        private void persistEvaluationCriteriaFromScreen1(EvaluationsModel evaluationsModel) throws InvalidCriterionException {
            List<EvaluationProcCriterion> evaluationProcCriterionList;

            boolean procurementCriterrionAlreadyWeighted = repositoryFetcher.getEvaluationProcCriterionRepository()
                    .findByProcurement(evaluationsModel.getProcurementId()).size()>0;

            if(procurementCriterrionAlreadyWeighted) {
                LOGGER.info("Procurement:: " + evaluationsModel.getProcurementId()
                        + " has already been weighted, continuing with previous weightings.");
                return;
            }

            try {
                evaluationProcCriterionList = getCriterionFromModel(evaluationsModel);
                validateCriterionList(evaluationProcCriterionList);
            } catch (InvalidCriterionException ice) {
                String errorMessage = "InvalidCriterionException:: " + ice.getStackTrace()[0] +  " ::" + ice.getMessage();
                LOGGER.error(errorMessage);
                throw new InvalidCriterionException(errorMessage, ice.getMessage());
            }
            repositoryFetcher.getEvaluationProcCriterionRepository().saveAll(evaluationProcCriterionList);
        }

        private List<BundleScoring> getBundleScorings(List<ProcSolutionBundle> bundles, List<CriterionScore> criterionScores) {
            List<BundleScoring> bundleScorings = new ArrayList<>();
            for(ProcSolutionBundle bundle : bundles) {
                BundleScoring bundleScoring = new BundleScoring();
                bundleScoring.setBundle(bundle);
                bundleScoring.setCriterionScores(criterionScores);
                bundleScorings.add(bundleScoring);
            }
            return bundleScorings;
        }

        private List<Integer> getDropDownEntries() {
            Iterable<EvaluationScoreValue> evaluationScoreValues  = repositoryFetcher.getEvaluationScoreRepository().findAll();
            List<EvaluationScoreValue> target = new ArrayList<>();
            evaluationScoreValues.forEach(target::add);
            List<Integer> dropDownEntries = new ArrayList<>();
            for(EvaluationScoreValue c : target) {
                dropDownEntries.add(c.getScore());
            }
            return dropDownEntries;
        }

        private void logNumOfCriterionAvailableForProcurement(List<EvaluationProcCriterion> evaluationProcCriterionList, Long procurementId) {
            int numOfCriterionForProcurement = 0;
            if(evaluationProcCriterionList!=null) {
                numOfCriterionForProcurement = evaluationProcCriterionList.size();
            }
            LOGGER.info(numOfCriterionForProcurement + " criterion found for procurement:: " + procurementId);
        }

        private List<CriterionScore> getCriterionScores(Long procurementId) {
            List<EvaluationProcCriterion> evaluationProcCriterionList = repositoryFetcher.getEvaluationProcCriterionRepository()
                    .findByProcurement(procurementId);

            logNumOfCriterionAvailableForProcurement(evaluationProcCriterionList, procurementId);

            List<CriterionScore> criterionScores = new ArrayList<>();
            for(EvaluationProcCriterion e : evaluationProcCriterionList) {
                CriterionScore criterionScore = new CriterionScore();
                criterionScore.setCriterion(e.getName());
                criterionScore.setCriterionId(toIntExact(e.getId()));
                criterionScores.add(criterionScore);
            }
            return criterionScores;
        }

        private void validateCriterionList(List<EvaluationProcCriterion> evaluationProcCriterionList) throws InvalidCriterionException {
            boolean modelValid = false;
            boolean numberOfCriterionValid = false;
            boolean weightingValid= true;
            boolean noDupNames = true;

            long weightingTotal = 0;
            List<String> names = new ArrayList<>();

            for(EvaluationProcCriterion evaluationProcCriterion : evaluationProcCriterionList) {
                weightingTotal = weightingTotal + evaluationProcCriterion.getWeightingPercent();

                numberOfCriterionValid = true;
                weightingValid = weightingTotal==100;
                noDupNames = noDupNames && !names.contains(evaluationProcCriterion.getName());

                names.add(evaluationProcCriterion.getName());
                if(noDupNames && weightingValid) {
                    modelValid = true;
                }
            }
            if(modelValid) {
                return;
            }
            throw new InvalidCriterionException(getInvalidCriterionExceptionMessage(numberOfCriterionValid, weightingValid, noDupNames));
        }

        private String getInvalidCriterionExceptionMessage(boolean numberOfCriterionValid, boolean weightingValid, boolean noDupNames) {
            StringBuilder messageBuilder = new StringBuilder();
            if(!numberOfCriterionValid) {
                messageBuilder.append("You must add least 1 criterion.\n");
            }
            if(!weightingValid) {
                messageBuilder.append("Your weightings do not add up to 100%.\n");
            }
            if(!noDupNames) {
                messageBuilder.append("You have duplicate names in your criterion.");
            }
            return messageBuilder.toString();
        }

        private List<EvaluationProcCriterion> getCriterionFromModel(EvaluationsModel evaluationsModel) throws InvalidCriterionException {
            List<EvaluationProcCriterion> evaluationProcCriterionList = new ArrayList<>();
            List<Evaluation> evaluationsFromModel = evaluationsModel.getEvaluations();
            if(evaluationsFromModel==null) {
                throw new InvalidCriterionException("You haven't added any criteria for evaluation.");
            }
            for(Evaluation ev : evaluationsFromModel) {
                EvaluationProcCriterion evaluationProcCriterion = makeEvaluationProcCriterion(ev, evaluationsModel);
                if(isFormComplete(ev)) {
                    evaluationProcCriterionList.add(evaluationProcCriterion);
                }
            }
            return evaluationProcCriterionList;
        }

        private boolean isFormComplete(Evaluation ev) {
            return !(ev.getWeighting().isEmpty() && ev.getCriteria().isEmpty() && ev.getSeq().isEmpty());
        }

        private EvaluationProcCriterion makeEvaluationProcCriterion(Evaluation ev, EvaluationsModel evaluationsModel) throws InvalidCriterionException {
            EvaluationProcCriterion evaluationProcCriterion = new EvaluationProcCriterion();
            evaluationProcCriterion.setName(ev.getCriteria());
            if (isFormComplete(ev)) {
                try {
                    evaluationProcCriterion.setWeightingPercent(Long.valueOf(ev.getWeighting()));
                    evaluationProcCriterion.setSeq(Integer.valueOf(ev.getSeq()));
                    evaluationProcCriterion.setProcurement(evaluationsModel.getProcurementId());
                    evaluationProcCriterion = addTolerances(evaluationProcCriterion, evaluationsModel, ev);
                    checkIfWeightingIsWithinToleranceLimits(evaluationProcCriterion);
                } catch (NumberFormatException nfex) {
                    throw new InvalidCriterionException(nfex.getMessage());
                }
            }
            return evaluationProcCriterion;
        }

        private void checkIfWeightingIsWithinToleranceLimits(EvaluationProcCriterion evaluationProcCriterion) throws InvalidCriterionException {
            Optional<EvaluationTolerance> optionalEvaluationTolerance = repositoryFetcher.getEvaluationToleranceRepository().findById(evaluationProcCriterion.getTolerance());
            EvaluationTolerance tol;
            if(optionalEvaluationTolerance.isPresent()) {
                tol = optionalEvaluationTolerance.get();
            } else {
                throw new InvalidCriterionException("Tolerances not available for criterion:: " + evaluationProcCriterion.getName());
            }
            int weighting = Long.valueOf(evaluationProcCriterion.getWeightingPercent()).intValue();
            boolean weightingOutsideTolerance = weighting < tol.getLowerInclusivePercent() || weighting > tol.getUpperInclusivePercent();
            if (weightingOutsideTolerance) {
                String errorMessage = "Weighting: " + weighting + " for criteria: " + evaluationProcCriterion.getName()
                        + " is outside tolerance range of lower bound: " + tol.getLowerInclusivePercent()
                        + " and upper bound: " + tol.getUpperInclusivePercent();
                throw new InvalidCriterionException(errorMessage);
            }
        }

        private EvaluationProcCriterion addTolerances(EvaluationProcCriterion evaluationProcCriterion,
                                                      EvaluationsModel evaluationsModel,
                                                      Evaluation ev) throws InvalidCriterionException {

            EvaluationProcCriterion evaluationProcCriterionCopy = new EvaluationProcCriterion(evaluationProcCriterion);

            Optional<Procurement> optionalProcurement = repositoryFetcher.getProcurementRepository().findById(evaluationsModel.getProcurementId());
            Procurement p;
            if(optionalProcurement.isPresent()) {
                p = optionalProcurement.get();
            } else {
                throw new InvalidCriterionException("Procurement:: " + evaluationsModel.getProcurementId() + " not available.");
            }

            int competType = toIntExact(p.getCompetitionType().getId());
            boolean offCatalogCompetitionType = competType==2;
            int toleranceId;

            if(offCatalogCompetitionType) {
                toleranceId = getToleranceForOffCatCompetitionType(evaluationProcCriterionCopy,ev);
            } else {
                toleranceId = getToleranceForOnCatCriterionType(competType, ev);
            }
            evaluationProcCriterionCopy.setTolerance(toleranceId);

            return evaluationProcCriterionCopy;
        }

        private int getToleranceForOffCatCompetitionType(EvaluationProcCriterion evaluationProcCriterionCopy, Evaluation ev)
                throws InvalidCriterionException {

            if(ev.getCriteria().isEmpty()) {
                throw new InvalidCriterionException("No criteria selected from dropdown.");
            }
            Optional<OffCatCriterion> optionalOffCatCriterion = repositoryFetcher.getOffCatCriterionRepository().findByName(ev.getCriteria());
            OffCatCriterion offCatCriterion;
            if(optionalOffCatCriterion.isPresent()) {
                offCatCriterion = optionalOffCatCriterion.get();
            } else {
                throw new InvalidCriterionException("No OffCatCriterion available for criterion:: " + ev.getCriteria());
            }

            int offCatCriterionId = toIntExact(offCatCriterion.getId());
            evaluationProcCriterionCopy.setOffCatCriterion(offCatCriterionId); //cheeky setting of offCatCriterion
            return offCatCriterion.getTolerance();
        }

        private int getToleranceForOnCatCriterionType(int competitionType, Evaluation ev) throws InvalidCriterionException {
            Optional<EvaluationCriterionType> criterionTypeOpt = repositoryFetcher.getEvaluationCriterionTypeRepository().findByName(ev.getCriteria());
            boolean priceCriteriumType = criterionTypeOpt.isPresent();
            EvaluationTolerance evaluationTolerance;
            if(priceCriteriumType)  {
                EvaluationCriterionType criterionType  = criterionTypeOpt.get();
                int priceCriterionType = toIntExact(criterionType.getId());
                evaluationTolerance = getEvaluationTolerance(priceCriterionType, competitionType, ev);
            } else {
                int nonPriceCriteriumType = 2;
                evaluationTolerance = getEvaluationTolerance(nonPriceCriteriumType, competitionType, ev);
            }
            return evaluationTolerance.getId();
        }

        private EvaluationTolerance getEvaluationTolerance(int criterionTypeId, int competitionType, Evaluation ev) throws InvalidCriterionException {
            EvaluationToleranceRepository repo = repositoryFetcher.getEvaluationToleranceRepository();
            Optional<EvaluationTolerance> optEvalTol = repo.findByCompetitionTypeAndCriteriumType(competitionType, criterionTypeId);
            if(optEvalTol.isPresent()) {
                return optEvalTol.get();
            } else {
                throw new InvalidCriterionException("No tolerance available for criterion:: " + ev.getCriteria());
            }
        }

    }

    private class EvaluationsScreen1SetUpHelper {

        private ProcurementValidator validateProcurement(RedirectAttributes attr, HttpServletRequest request, long procurementId) throws InvalidCriterionException {
            boolean passed = !(checkProcurementIdValid(procurementId, attr, request).isSecurityFailed()
                    || checkUserAuthorisedToAccessProcurement(procurementId, attr,  request).isSecurityFailed()
                    || checkProcurementStatusIsDraft(procurementId,  attr, request).isSecurityFailed());
            if(passed) {
                return new ProcurementValidator(passed, getProcurement(procurementId));
            }
            return new ProcurementValidator(passed);
        }


        private Procurement getProcurement(long procurementId) throws InvalidCriterionException {
            ProcurementRepository repo = repositoryFetcher.getProcurementRepository();
            Optional<Procurement> optionalProcurement = repo.findById(procurementId);
            if(optionalProcurement.isPresent()) {
                return optionalProcurement.get();
            } else {
                throw new InvalidCriterionException("No procurement available for procurementId:: " + procurementId);
            }
        }

        private void setUpHelperBox(Model model) {
            Iterable<OffCatCriterion> offCatCriterionList = repositoryFetcher.getOffCatCriterionRepository().findAll();
            List<OffCatCriterion> target = new ArrayList<>();
            offCatCriterionList.forEach(target::add);
            List<String> hints = new ArrayList<>();
            for(OffCatCriterion c : target) {
                hints.add(c.getName());
            }
            model.addAttribute("criterionHints", hints);
        }

        private void populateDropDownWithOffCatalogCriterion(EvaluationsModel evaluationsModel, Model model, long procurementId) {
            Iterable<OffCatCriterion> offCatCriterionList = repositoryFetcher.getOffCatCriterionRepository().findAll();
            List<OffCatCriterion> target = new ArrayList<>();
            offCatCriterionList.forEach(target::add);
            List<String> dropDownEntries = new ArrayList<>();
            for(OffCatCriterion c : target) {
                dropDownEntries.add(c.getName());
            }
            evaluationsModel.setProcurementId(procurementId);
            evaluationsModel.setOffCatCriterion(dropDownEntries);
            model.addAttribute("evaluations", evaluationsModel);
        }

        private String getScreen1Path(long procurementId) throws InvalidCriterionException {
            int competitionType = toIntExact(getProcurement(procurementId).getCompetitionType().getId());
            boolean onCatalogCompetitionType = competitionType==1;
            if(onCatalogCompetitionType) {
                return "buying-process/evaluationsOnCatScreen1";
            } else {
                return "buying-process/evaluationsOffCatScreen1";
            }
        }

        private ProcurementValidator checkProcurementIdValid(long procurementId, RedirectAttributes attr, HttpServletRequest request) {
            if (procurementId == 0) {
                String message = "Unidentified route to shortlist";
                LOGGER.warn(getSecurityInfo(request).loggerSecurityMessage(message));
                attr.addFlashAttribute("security_message", message);
                return new ProcurementValidator(true);
            }
            return new ProcurementValidator(false);
        }

        private ProcurementValidator checkUserAuthorisedToAccessProcurement(long procurementId, RedirectAttributes attr, HttpServletRequest request) {
            Optional<Procurement> optProcurement = repositoryFetcher.getProcurementRepository().findById(procurementId);
            if (optProcurement.isPresent()) {
                SecurityInfo secInfo = getSecurityInfo(request);
                Procurement procurement = optProcurement.get();
                if (procurement.getOrgContact().getOrganisation().getId() != secInfo.getOrganisationId()
                        && !secInfo.isAdministrator()) {
                    String message = "view procurement " + procurement.getId();
                    LOGGER.warn(getSecurityInfo(request).loggerSecurityMessage(message));
                    attr.addFlashAttribute("security_message", "You attempted to " + message + " but you are not authorised");
                    return new ProcurementValidator(true);
                }
            }
            return new ProcurementValidator(false);
        }

        private ProcurementValidator checkProcurementStatusIsDraft(long procurementId, RedirectAttributes attr, HttpServletRequest request) { //TODO assumption wrong not shortlist!!
            Optional<Procurement> optProcurement = repositoryFetcher.getProcurementRepository().findById(procurementId);
            if (optProcurement.isPresent()) {
                Procurement procurement = optProcurement.get();
                if (procurement.getStatus().getId() != ProcStatus.DRAFT) {
                    String message = "procurement " + procurement.getId() +
                            " is at the wrong status. It's status is " + procurement.getStatus().getName() + ".";
                    LOGGER.warn(getSecurityInfo(request).loggerSecurityMessage(message));
                    attr.addFlashAttribute("security_message", message);
                    return new ProcurementValidator(true);
                }
            }
            return new ProcurementValidator(false);
        }

    }

} 