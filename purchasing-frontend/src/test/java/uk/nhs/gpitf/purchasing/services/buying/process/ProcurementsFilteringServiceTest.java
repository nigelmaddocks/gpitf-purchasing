package uk.nhs.gpitf.purchasing.services.buying.process;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import uk.nhs.gpitf.purchasing.models.ListProcurementsModel;
import uk.nhs.gpitf.purchasing.models.SearchListProcurementsModel;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes={ProcurementServiceStub.class,OrgContactRepositoryStub.class,ProcurementsFilteringService.class,ProcStatusServiceStub.class})
public class ProcurementsFilteringServiceTest {

    @Autowired
    private ProcurementsFilteringService procurementsFilteringService;

    private static SearchListProcurementsModel searchListProcurementsModel;

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