package uk.nhs.gpitf.purchasing.entities;

import java.lang.invoke.MethodHandles;
import java.util.NoSuchElementException;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.Getter;

@Getter
public enum SingleSiteContinuityEnum {
  NOT_SINGLE_SITE_CONTINUITY(1, "Regular Procurement"),
  SINGLE_SITE_CONTINUITY(2, "Single Site Continuity");

  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final long id;
  private final String name;

  private SingleSiteContinuityEnum(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public static SingleSiteContinuityEnum getById(Long id) {
    return Stream.of(SingleSiteContinuityEnum.values())
              .filter(e -> e.getId() == id)
              .findFirst()
              .orElseThrow(() -> {
                LOGGER.warn("An attempt to get a SingleSiteContinuityEnum by id of \"{}\" occurred. But could not be found.", id);
                return new NoSuchElementException("SingleSiteContinuityEnum " + id + " not found");
              });
  }
}
