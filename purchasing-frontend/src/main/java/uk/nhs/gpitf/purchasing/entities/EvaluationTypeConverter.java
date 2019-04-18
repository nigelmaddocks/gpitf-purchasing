package uk.nhs.gpitf.purchasing.entities;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class EvaluationTypeConverter implements AttributeConverter<EvaluationTypeEnum, Long> {

  @Override
  public Long convertToDatabaseColumn(EvaluationTypeEnum attribute) {
    return attribute == null ? null : attribute.getId();
  }

  @Override
  public EvaluationTypeEnum convertToEntityAttribute(Long dbData) {
    return EvaluationTypeEnum.getById(dbData);
  }

}
