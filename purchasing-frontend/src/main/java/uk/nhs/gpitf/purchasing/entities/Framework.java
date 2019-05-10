package uk.nhs.gpitf.purchasing.entities;

import javax.persistence.Column;
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
@Table(name="framework", schema="purchasing")
@Getter @Setter
@NoArgsConstructor
public class Framework {
	
	@Id
	private long id;
	
	@Size(max = 36)
	@Column(name = "frameworkid")
	private String frameworkId;
	
	@Size(max = 255)
	private String name;
	
	private int maxTermMonths;
	
}
