package uk.nhs.gpitf.purchasing.services.buying.process;

import com.palantir.docker.compose.DockerComposeRule;
import com.palantir.docker.compose.configuration.DockerComposeFiles;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import uk.nhs.gpitf.purchasing.models.ListProcurementsModel;
import uk.nhs.gpitf.purchasing.models.SearchListProcurementsModel;
import uk.nhs.gpitf.purchasing.repositories.OrgContactRepository;
import uk.nhs.gpitf.purchasing.services.IProcurementService;
import uk.nhs.gpitf.purchasing.services.ProcStatusService;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class ProcurementsFilteringServiceTest {

    @Autowired
    private IProcurementService stubProcurementService;

    @Autowired
    private OrgContactRepository orgContactRepositoryStub;

    @Autowired
    private ProcurementsFilteringService procurementsFilteringService;

    @Autowired
    private ProcStatusService stubProcStatusService;

    private static SearchListProcurementsModel searchListProcurementsModel;

    @Before
    public void injectStubs() {
        setField(procurementsFilteringService, "procurementService", stubProcurementService);
        setField(procurementsFilteringService, "orgContactRepository", orgContactRepositoryStub);
        setField(procurementsFilteringService, "procStatusService", stubProcStatusService);
    }

    @Before
    public void setUpInitialConditionsOfFilter() {
        searchListProcurementsModel = new SearchListProcurementsModel();
        searchListProcurementsModel.setOpenProcStatusSearchField("ALL");
    }

    @After
    public void restoreInitialConmditionsOfFilter() {
        searchListProcurementsModel = new SearchListProcurementsModel();
        searchListProcurementsModel.setOpenProcStatusSearchField("ALL");
    }

    @ClassRule
    public static DockerComposeRule docker() {
        return DockerComposeRule.builder()
                .files(DockerComposeFiles.from("src/test/resources/docker-compose-mockserver.yml",
                        "src/test/resources/docker-compose-postgres.yml"))
                .build();
    }

    @Test
    public void testFilterProcurements_whenFilteringOnCompletedList_andMatchFound() {
        // given
        String expected = "stubbyMcCompletedStubFace";

        // and
        searchListProcurementsModel.setCompletedProcNameSearchField("stu");

        // when
        ListProcurementsModel filteredStuff =  procurementsFilteringService.filterProcurements(1L, searchListProcurementsModel);

        // then
        String actual = filteredStuff.getCompletedProcurements().get(0).getName();
        assertEquals(expected, actual);
    }

    @Test
    public void testFilterProcurements_whenFilteringOnOpenList_andMatchFound() {
        // given
        String expected = "stubbyMcUncompletedStubFace";

        // and
        searchListProcurementsModel.setOpenProcNameSearchField("stu");

        // when
        ListProcurementsModel filteredStuff =  procurementsFilteringService.filterProcurements(1L, searchListProcurementsModel);

        // then
        String actual = filteredStuff.getOpenProcurements().get(0).getName();
        assertEquals(expected, actual);
    }

    @Test
    public void testFilterProcurements_whenFilteringOnOpenList_andndNoMatchFound() {
        // given
        int expected = 0;

        // and
        searchListProcurementsModel.setOpenProcNameSearchField("there is no way a procurement has this name!");

        // when
        ListProcurementsModel filteredStuff =  procurementsFilteringService.filterProcurements(1L, searchListProcurementsModel);

        // then
        int actual = filteredStuff.getOpenProcurements().size();
        assertEquals(expected, actual);
    }

    @Test
    public void testFilterProcurements_whenFilteringOnOpenList_andNoMatchFoundOnStatusFilter() {
        // given
        int expected = 0;

        // and
        searchListProcurementsModel.setOpenProcNameSearchField("stub");
        searchListProcurementsModel.setOpenProcStatusSearchField("pretty sure there is nothing for this status!!");

        // when
        ListProcurementsModel filteredStuff =  procurementsFilteringService.filterProcurements(1L, searchListProcurementsModel);

        // then
        int actual = filteredStuff.getOpenProcurements().size();
        assertEquals(expected, actual);
    }

    @Test
    public void testFilterProcurements_whenFilteringOnCompletedList_andNoMatchFound() {
        // given
        int expected = 0;

        // and
        searchListProcurementsModel.setCompletedProcNameSearchField("there is no way a procurement has this name!");

        // when
        ListProcurementsModel filteredStuff =  procurementsFilteringService.filterProcurements(1L, searchListProcurementsModel);

        // then
        int actual = filteredStuff.getCompletedProcurements().size();
        assertEquals(expected, actual);
    }

    @Test
    public void testFilterProcurementsIsCaseInsensitive() {
        // given
        String expected = "stubbyMcUncompletedStubFace";

        // and
        String uppercasedSearchStringUsedToLookForLowerCasedRecord = "STU";
        searchListProcurementsModel.setOpenProcNameSearchField(uppercasedSearchStringUsedToLookForLowerCasedRecord);

        // when
        ListProcurementsModel filteredStuff =  procurementsFilteringService.filterProcurements(1L, searchListProcurementsModel);

        // then
        String actual = filteredStuff.getOpenProcurements().get(0).getName();
        assertEquals(expected, actual);
    }

}