package uk.nhs.gpitf.purchasing.entities;

import java.lang.invoke.MethodHandles;
import java.util.NoSuchElementException;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.Getter;

@Getter
public enum EvaluationTypeEnum {

  PRICE_ONLY(1, "Price"),
  PRICE_AND_QUALITY(2, "Price & Quality");

  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final long id;
  private final String name;

  private EvaluationTypeEnum(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public static EvaluationTypeEnum getById(Long id) {
    return Stream.of(EvaluationTypeEnum.values())
              .filter(e -> e.getId() == id)
              .findFirst()
              .orElseThrow(() -> {
                LOGGER.warn("An attempt to get an EvaluationTypeEnum by id of \"{}\" occurred. But could not be found.", id);
                return new NoSuchElementException("EvaluationTypeEnum " + id + " not found");
              });
  }
}
