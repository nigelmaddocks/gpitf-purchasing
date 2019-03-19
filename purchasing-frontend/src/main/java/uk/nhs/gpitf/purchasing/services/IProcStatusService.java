package uk.nhs.gpitf.purchasing.services;

import uk.nhs.gpitf.purchasing.entities.ProcStatus;

import java.util.List;

public interface IProcStatusService {
    List<ProcStatus> getAll();
}
