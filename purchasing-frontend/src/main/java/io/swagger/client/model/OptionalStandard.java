/*
 * catalogue-api
 * NHS Digital GP IT Futures Buying Catalog API
 *
 * OpenAPI spec version: 1.0.0-private-beta
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package io.swagger.client.model;

import java.util.Objects;
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * A Standard and a flag associated with a Capability through a CapabilityMapping
 */
@ApiModel(description = "A Standard and a flag associated with a Capability through a CapabilityMapping")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-12-11T12:47:15.885Z")
public class OptionalStandard {
  @JsonProperty("standardId")
  private String standardId = null;

  @JsonProperty("isOptional")
  private Boolean isOptional = null;

  public OptionalStandard standardId(String standardId) {
    this.standardId = standardId;
    return this;
  }

   /**
   * Unique identifier of Standard
   * @return standardId
  **/
  @ApiModelProperty(required = true, value = "Unique identifier of Standard")
  public String getStandardId() {
    return standardId;
  }

  public void setStandardId(String standardId) {
    this.standardId = standardId;
  }

  public OptionalStandard isOptional(Boolean isOptional) {
    this.isOptional = isOptional;
    return this;
  }

   /**
   * True if the Standard does not have to be supported in order to support the Capability
   * @return isOptional
  **/
  @ApiModelProperty(value = "True if the Standard does not have to be supported in order to support the Capability")
  public Boolean isIsOptional() {
    return isOptional;
  }

  public void setIsOptional(Boolean isOptional) {
    this.isOptional = isOptional;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OptionalStandard optionalStandard = (OptionalStandard) o;
    return Objects.equals(this.standardId, optionalStandard.standardId) &&
        Objects.equals(this.isOptional, optionalStandard.isOptional);
  }

  @Override
  public int hashCode() {
    return Objects.hash(standardId, isOptional);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OptionalStandard {\n");
    
    sb.append("    standardId: ").append(toIndentedString(standardId)).append("\n");
    sb.append("    isOptional: ").append(toIndentedString(isOptional)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}
