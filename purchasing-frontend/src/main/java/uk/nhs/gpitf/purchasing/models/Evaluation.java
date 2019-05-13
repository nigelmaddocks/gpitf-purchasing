package uk.nhs.gpitf.purchasing.models;

import lombok.Data;

import java.util.List;

@Data
public class Evaluation {

    private Long procurementId;
    private String criteria;
    private String weighting;
    private String seq;
    private String score;

}
