package uk.nhs.gpitf.purchasing.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name="evaluation_off_cat_criterion", schema="purchasing")
@Data
public class OffCatCriterion {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
	private long id;

    @NotBlank
	@Size(max = 255)
    private String name;

	private int tolerance;
	
}
