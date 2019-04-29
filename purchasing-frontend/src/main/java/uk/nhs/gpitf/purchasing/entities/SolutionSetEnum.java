package uk.nhs.gpitf.purchasing.entities;

import java.lang.invoke.MethodHandles;
import java.util.NoSuchElementException;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.Getter;

@Getter
public enum SolutionSetEnum {
  FOUNDATION(1, "Foundation"),
  NON_FOUNDATION(2, "Non-foundation");

  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final long id;
  private final String name;

  private SolutionSetEnum(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public static SolutionSetEnum getById(Long id) {
    return Stream.of(SolutionSetEnum.values())
              .filter(e -> e.getId() == id)
              .findFirst()
              .orElseThrow(() -> {
                LOGGER.warn("An attempt to get a SolutionSetEnum by id of \"{}\" occurred. But could not be found.", id);
                return new NoSuchElementException("SolutionSetEnum " + id + " not found");
              });
  }
}
