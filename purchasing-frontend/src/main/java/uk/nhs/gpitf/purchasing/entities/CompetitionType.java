package uk.nhs.gpitf.purchasing.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import lombok.Data;

@Entity
@Table(name="competition_type", schema="purchasing")
@Data
public class CompetitionType {
	public static final long ON_CATALOGUE = 1;
	public static final long OFF_CATALOGUE = 2;	
	
    @Id
	private long id;
    
    @Size(max = 255)
    private String name;
}
