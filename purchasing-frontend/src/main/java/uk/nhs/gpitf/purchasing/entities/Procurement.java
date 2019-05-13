package uk.nhs.gpitf.purchasing.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import lombok.Getter;
import lombok.Setter;
import uk.nhs.gpitf.purchasing.repositories.PatientCountRepository;
import uk.nhs.gpitf.purchasing.repositories.PatientCountRunRepository;

@Entity
@Table(name="procurement", schema="purchasing")
@Data
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Procurement implements Serializable {

	@Transient
	public static final String SESSION_ATTR_NAME = "temp_procurement";

	@Transient
	@JsonIgnore
	PatientCountRunRepository patientCountRunRepository;

	@Transient
	@JsonIgnore
	PatientCountRepository patientCountRepository;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

    @NotNull
	@Size(min = 5, max = 255, message = "Name needs to be at least 5-255 characters long")
	private String name;

	private LocalDateTime startedDate;

    @ManyToOne(optional=false)
    @JoinColumn(name = "org_contact")
	private OrgContact orgContact;

    @ManyToOne(optional=false)
    @JoinColumn(name = "status")
	private ProcStatus status;

	private LocalDateTime statusLastChangedDate;

	private LocalDateTime lastUpdated;

	private LocalDateTime completedDate;

	private String searchKeyword;

//    @ManyToOne(optional=true)
//    @JoinColumn(name = "evaluation_type")
    @Convert(converter = EvaluationTypeConverter.class)
	private EvaluationTypeEnum evaluationType;
    
    @Getter(AccessLevel.NONE)
    private Boolean singleSiteContinuity;
    public Boolean getSingleSiteContinuity() {
    	if (this.singleSiteContinuity == null) {
    		return false;
    	}
    	return this.singleSiteContinuity.booleanValue();
    }

    private Boolean foundation;

    @ManyToOne(optional=true)
    @JoinColumn(name = "competition_type")
	private CompetitionType competitionType;

    
	private String csvCapabilities;

	private String csvInteroperables;

	private String csvPractices;
/*
    @OneToMany(
    	fetch = FetchType.LAZY
        //cascade = CascadeType.ALL,
        //orphanRemoval = true
    )
    @JoinTable(name = "proc_shortXXlist", schema="purchasing", joinColumns = @JoinColumn(name = "procurement"), inverseJoinColumns = @JoinColumn(name = "id"))
    @JsonIgnore
    private List<ProcShortlist> shortXXlistItems = new ArrayList<>();
*/
    @OneToMany(
    	fetch = FetchType.LAZY
    )
    @JoinTable(name = "proc_solution_bundle", schema="purchasing", joinColumns = @JoinColumn(name = "procurement"), inverseJoinColumns = @JoinColumn(name = "id"))
    @OrderBy(value="id asc")
    @JsonIgnore
    private List<ProcSolutionBundle> bundles = new ArrayList<>();

	private Integer initialPatientCount;

	private LocalDate plannedContractStart;

	private Integer contractMonths;

	private Integer patientCount;
	
	@JsonIgnore
	public String getSummaryAttributes() {
		String s = "";
		if (singleSiteContinuity != null && singleSiteContinuity.booleanValue()) {
			s = "Single Site Continuity";
		}
		if (evaluationType != null) {
			if (StringUtils.isNotEmpty(s)) {
				s += " | ";
			}
			s += evaluationType.getName();
		}
		if (foundation != null) {
			if (StringUtils.isNotEmpty(s)) {
				s += " | ";
			}
			s += (foundation?"":"Non-") + "Foundation";
		}	
		if (competitionType != null) {
			if (StringUtils.isNotEmpty(s)) {
				s += " | ";
			}
			s += competitionType.getName();
		}
		
		return s;
	}

	@Data
	public static class PrimitiveProcurement implements Serializable {
		private static final long serialVersionUID = 7165183178416691678L;
		private String name;
		private long orgContactId;
		private EvaluationTypeEnum evaluationType;
		private Boolean singleSiteContinuity;
		private Boolean foundation;
		private String csvCapabilities;
		private String csvInteroperables;
		private String csvPractices;
		private Integer initialPatientCount;
		
		@JsonIgnore
		public String getSummaryAttributes() {
			String s = "";
			if (singleSiteContinuity != null && singleSiteContinuity.booleanValue()) {
				s = "Single Site Continuity";
			}
			if (evaluationType != null) {
				if (StringUtils.isNotEmpty(s)) {
					s += " | ";
				}
				s += evaluationType.getName();
			}
			if (foundation != null) {
				if (StringUtils.isNotEmpty(s)) {
					s += " | ";
				}
				s += (foundation?"":"Non-") + "Foundation";
			}			
			
			return s;
		}
	}
/*
	public String serializeToString() throws IOException {
		PrimitiveProcurement prim = new PrimitiveProcurement();
		BeanUtils.copyProperties(this, prim);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(baos);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(bufferedOutputStream);
		objectOutputStream.writeObject(prim);
		objectOutputStream.close();
		String s = baos.toString();
		return s;

	}
*/
}
