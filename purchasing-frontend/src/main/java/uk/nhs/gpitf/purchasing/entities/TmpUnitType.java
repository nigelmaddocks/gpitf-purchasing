package uk.nhs.gpitf.purchasing.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="tmp_unit_type", schema="purchasing")
@Data
public class TmpUnitType {
	public static final long PATIENT = 2;
	public static final long SERVICE_RECIPIENT = 3;
	
    @Id
	private long id;
 
    private String name;
}
