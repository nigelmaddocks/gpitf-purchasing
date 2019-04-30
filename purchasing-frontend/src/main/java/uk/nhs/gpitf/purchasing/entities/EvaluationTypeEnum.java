package uk.nhs.gpitf.purchasing.entities;

import java.util.stream.Stream;
import lombok.Getter;

@Getter
public enum EvaluationTypeEnum {

  PRICE_ONLY(1, "Price"),
  PRICE_AND_QUALITY(2, "Price & Quality");

  private final Long id;
  private final String name;

  private EvaluationTypeEnum(int id, String name) {
    this.id = (long) id;
    this.name = name;
  }

  public static EvaluationTypeEnum getById(Long id) {
    return Stream.of(EvaluationTypeEnum.values())
              .filter(e -> e.getId() == id)
              .findFirst()
              .orElse(null);
  }

}
