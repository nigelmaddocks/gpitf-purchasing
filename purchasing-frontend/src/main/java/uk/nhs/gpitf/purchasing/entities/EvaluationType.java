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
@Table(name="evaluation_type", schema="purchasing")
@Getter @Setter
@NoArgsConstructor
public class EvaluationType {
	
	public static final long PRICE_ONLY = 1;
	public static final long PRICE_AND_QUALITY = 2;

	@Id
	private long id;
	@Size(max = 255)
	private String name;
	
}
