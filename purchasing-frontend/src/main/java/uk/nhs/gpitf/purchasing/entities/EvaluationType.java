package uk.nhs.gpitf.purchasing.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="evaluation_type", schema="purchasing")
@Getter @Setter
@NoArgsConstructor
public class EvaluationType {

	@Id
	private long id;
	@Size(max = 255)
	private String name;

}
