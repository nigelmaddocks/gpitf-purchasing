package uk.nhs.gpitf.purchasing.models;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.validation.constraints.Size;

import io.swagger.client.model.Capabilities;
import io.swagger.client.model.Solutions;
import lombok.Builder;
import lombok.Data;
import uk.nhs.gpitf.purchasing.entities.OrgContact;
import uk.nhs.gpitf.purchasing.entities.ProcStatus;
import uk.nhs.gpitf.purchasing.entities.Procurement;

@Data
public class ListProcurementsModel {

	long orgContactId;
	OrgContact orgContact;
	
	List <ProcStatus> statusFilter = new ArrayList<>();
	List <Procurement> openProcurements = new ArrayList<>();
	List <Procurement> completedProcurements = new ArrayList<>();

}
