package uk.nhs.gpitf.purchasing.entities;

import lombok.Data;

import javax.persistence.*;


@Entity
@Table(name="proc_srv_recipient", schema="purchasing")
@Data
public class ProcSrvRecipient {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
	private long id;
    
    @ManyToOne(optional=false)
    @JoinColumn(name = "procurement")
	private Procurement procurement;
    
    @ManyToOne(optional=false)
    @JoinColumn(name = "organisation")
	private Organisation organisation;

    private Integer term;

    private int patientCount;
    
}
