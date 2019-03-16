package uk.nhs.gpitf.purchasing.controllers.buyingprocess;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.nhs.gpitf.purchasing.entities.OrgContact;
import uk.nhs.gpitf.purchasing.entities.Procurement;
import uk.nhs.gpitf.purchasing.models.ListProcurementsModel;
import uk.nhs.gpitf.purchasing.repositories.OrgContactRepository;
import uk.nhs.gpitf.purchasing.repositories.ProcurementRepository;
import uk.nhs.gpitf.purchasing.services.ProcurementService;
import uk.nhs.gpitf.purchasing.services.buying.process.ProcurementsFilteringService;
import uk.nhs.gpitf.purchasing.services.buying.process.ProcurementsFilteringServiceParameterObject;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class BuyingProcessControllerTest {

  @InjectMocks
  private BuyingProcessController bpc;

  @Mock
  private OrgContactRepository orgContactRepository;

  @Mock
  private ProcurementService procurementService;

  @Mock
  private ProcurementRepository procurementRepository;

  @Mock
  private ProcurementsFilteringService procurementsFilteringService;

  @Test
  public void procurementShouldGoToProcurementView() throws Exception {
    MockMvc mockMvc = standaloneSetup(bpc).build();
    mockMvc.perform(get("/buyingprocess/procurement"))
           .andExpect(view().name("buying-process/procurement"));
  }

  @Test
  public void procurementShouldContainProcurementsInModel() throws Exception {
    List<Procurement> procurementList = new ArrayList<>();
    procurementList.add(new Procurement());
    given(procurementService.getUncompletedByOrgContactOrderByLastUpdated(ArgumentMatchers.anyLong())).willReturn(procurementList);

    MockMvc mockMvc = standaloneSetup(bpc).build();
    mockMvc.perform(get("/buyingprocess/procurement"))
           .andExpect(model().attributeExists("procurements"))
           .andExpect(model().attribute("procurements", procurementList));
  }

  @Test
  public void filterShouldUtiliseFilterService() throws Exception {
    ListProcurementsModel expected = new ListProcurementsModel();
    List<Procurement> procurementList = new ArrayList<>();
    procurementList.add(new Procurement());
    given(orgContactRepository.findById(ArgumentMatchers.anyLong())).willReturn(Optional.of(new OrgContact()));
    given(procurementsFilteringService.filterProcurements(ArgumentMatchers.any(ProcurementsFilteringServiceParameterObject.class))).willReturn(expected);

    MockMvc mockMvc = standaloneSetup(bpc).build();
    mockMvc.perform(post("/buyingprocess/filtered"))
            .andExpect(model().attribute("listProcurementsModel", expected));
  }


}