package uk.nhs.gpitf.purchasing.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="org_type", schema="purchasing")
@Getter @Setter
@NoArgsConstructor
public class OrgType {
	
	public static final long GPPRACTICE = 1;
	public static final long CCG = 2;
	public static final long CSU = 3;
	public static final long SUPPLIER = 4;
	
	@Id
	private long id;
	@Size(max = 255)
	private String name;
	
}
