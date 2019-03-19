package uk.nhs.gpitf.purchasing.services.buying.process;

import org.apache.tomcat.jni.Proc;
import org.springframework.stereotype.Component;
import uk.nhs.gpitf.purchasing.entities.ProcStatus;
import uk.nhs.gpitf.purchasing.services.IProcStatusService;

import java.util.ArrayList;
import java.util.List;

@Component("stubProcStatusService")
public class ProcStatusServiceStub implements IProcStatusService {

    @Override
    public List<ProcStatus> getAll() {
        List<ProcStatus> list = new ArrayList<>();
        ProcStatus draft = new ProcStatus();
        ProcStatus longList = new ProcStatus();
        ProcStatus shortList = new ProcStatus();
        ProcStatus internalCompetition = new ProcStatus();
        ProcStatus externalTender = new ProcStatus();
        ProcStatus contractOffered = new ProcStatus();
        ProcStatus completed = new ProcStatus();
        ProcStatus deleted = new ProcStatus();

        draft.setId(1L);
        longList.setId(2L);
        shortList.setId(3L);
        internalCompetition.setId(4L);
        externalTender.setId(5L);
        contractOffered.setId(6L);
        completed.setId(80L);
        deleted.setId(99L);

        draft.setName("Draft");
        longList.setName("Longlist");
        shortList.setName("shortlist");
        internalCompetition.setName("Internal competition");
        externalTender.setName("External tender");
        contractOffered.setName("Contract offered");
        completed.setName("Completed");
        deleted.setName("Deleted");

        list.add(draft);
        list.add(longList);
        list.add(shortList);
        list.add(internalCompetition);
        list.add(externalTender);
        list.add(contractOffered);
        list.add(completed);
        list.add(deleted);

        return list;
    }

}