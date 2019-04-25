package uk.nhs.gpitf.purchasing.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Entity
@Table(name="service_type", schema="purchasing")
@Data
public class ServiceType {
	
	public static final long BASE_SOLUTION = 1;
	public static final long ASSOCIATED_SERVICE = 2;
	public static final long ADDITIONAL_SERVICE = 3;
	public static final long ASSOCIATED_SERVICE_OF_ADDITIONAL_SERVICE = 4;

	
    @Id
	private long id;
	
    @NotBlank
	@Size(max = 255)
	private String name;
    
}
