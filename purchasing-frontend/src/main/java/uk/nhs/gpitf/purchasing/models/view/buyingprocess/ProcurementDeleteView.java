package uk.nhs.gpitf.purchasing.models.view.buyingprocess;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ProcurementDeleteView {

  private Long id;
  private String name;
  private LocalDateTime startedDate;
  private LocalDateTime lastUpdated;

}
