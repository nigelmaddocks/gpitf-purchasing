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
import org.threeten.bp.OffsetDateTime;

/**
 * A product and/or service provided by an ‘organisation’.  Note that a ‘solution’ has a link to zero or one previous ‘solution’  Generally, only interested in current ‘solution’  Standard MS Dynamics CRM entity
 */
@ApiModel(description = "A product and/or service provided by an ‘organisation’.  Note that a ‘solution’ has a link to zero or one previous ‘solution’  Generally, only interested in current ‘solution’  Standard MS Dynamics CRM entity")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-12-11T12:47:15.885Z")
public class Solutions {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("previousId")
  private String previousId = null;

  @JsonProperty("organisationId")
  private String organisationId = null;

  @JsonProperty("version")
  private String version = null;

  /**
   * Current status of this solution
   */
  public enum StatusEnum {
    FAILED("Failed"),
    
    DRAFT("Draft"),
    
    REGISTERED("Registered"),
    
    CAPABILITIESASSESSMENT("CapabilitiesAssessment"),
    
    STANDARDSCOMPLIANCE("StandardsCompliance"),
    
    FINALAPPROVAL("FinalApproval"),
    
    SOLUTIONPAGE("SolutionPage"),
    
    APPROVED("Approved");

    private String value;

    StatusEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static StatusEnum fromValue(String text) {
      for (StatusEnum b : StatusEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("status")
  private StatusEnum status = null;

  @JsonProperty("createdById")
  private String createdById = null;

  @JsonProperty("createdOn")
  private OffsetDateTime createdOn = null;

  @JsonProperty("modifiedById")
  private String modifiedById = null;

  @JsonProperty("modifiedOn")
  private OffsetDateTime modifiedOn = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("productPage")
  private String productPage = null;

  public Solutions id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique identifier of entity
   * @return id
  **/
  @ApiModelProperty(required = true, value = "Unique identifier of entity")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Solutions previousId(String previousId) {
    this.previousId = previousId;
    return this;
  }

   /**
   * Unique identifier of previous version of entity
   * @return previousId
  **/
  @ApiModelProperty(value = "Unique identifier of previous version of entity")
  public String getPreviousId() {
    return previousId;
  }

  public void setPreviousId(String previousId) {
    this.previousId = previousId;
  }

  public Solutions organisationId(String organisationId) {
    this.organisationId = organisationId;
    return this;
  }

   /**
   * Unique identifier of organisation
   * @return organisationId
  **/
  @ApiModelProperty(required = true, value = "Unique identifier of organisation")
  public String getOrganisationId() {
    return organisationId;
  }

  public void setOrganisationId(String organisationId) {
    this.organisationId = organisationId;
  }

  public Solutions version(String version) {
    this.version = version;
    return this;
  }

   /**
   * Version of solution
   * @return version
  **/
  @ApiModelProperty(value = "Version of solution")
  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public Solutions status(StatusEnum status) {
    this.status = status;
    return this;
  }

   /**
   * Current status of this solution
   * @return status
  **/
  @ApiModelProperty(value = "Current status of this solution")
  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public Solutions createdById(String createdById) {
    this.createdById = createdById;
    return this;
  }

   /**
   * Unique identifier of Contact who created this entity  Derived from calling context  SET ON SERVER
   * @return createdById
  **/
  @ApiModelProperty(required = true, value = "Unique identifier of Contact who created this entity  Derived from calling context  SET ON SERVER")
  public String getCreatedById() {
    return createdById;
  }

  public void setCreatedById(String createdById) {
    this.createdById = createdById;
  }

  public Solutions createdOn(OffsetDateTime createdOn) {
    this.createdOn = createdOn;
    return this;
  }

   /**
   * UTC date and time at which record created  Set by server when creating record  SET ON SERVER
   * @return createdOn
  **/
  @ApiModelProperty(value = "UTC date and time at which record created  Set by server when creating record  SET ON SERVER")
  public OffsetDateTime getCreatedOn() {
    return createdOn;
  }

  public void setCreatedOn(OffsetDateTime createdOn) {
    this.createdOn = createdOn;
  }

  public Solutions modifiedById(String modifiedById) {
    this.modifiedById = modifiedById;
    return this;
  }

   /**
   * Unique identifier of Contact who last modified this entity  Derived from calling context  SET ON SERVER
   * @return modifiedById
  **/
  @ApiModelProperty(required = true, value = "Unique identifier of Contact who last modified this entity  Derived from calling context  SET ON SERVER")
  public String getModifiedById() {
    return modifiedById;
  }

  public void setModifiedById(String modifiedById) {
    this.modifiedById = modifiedById;
  }

  public Solutions modifiedOn(OffsetDateTime modifiedOn) {
    this.modifiedOn = modifiedOn;
    return this;
  }

   /**
   * UTC date and time at which record last modified  Set by server when creating record  SET ON SERVER
   * @return modifiedOn
  **/
  @ApiModelProperty(value = "UTC date and time at which record last modified  Set by server when creating record  SET ON SERVER")
  public OffsetDateTime getModifiedOn() {
    return modifiedOn;
  }

  public void setModifiedOn(OffsetDateTime modifiedOn) {
    this.modifiedOn = modifiedOn;
  }

  public Solutions name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Name of solution, as displayed to a user
   * @return name
  **/
  @ApiModelProperty(value = "Name of solution, as displayed to a user")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Solutions description(String description) {
    this.description = description;
    return this;
  }

   /**
   * Description of solution, as displayed to a user
   * @return description
  **/
  @ApiModelProperty(value = "Description of solution, as displayed to a user")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Solutions productPage(String productPage) {
    this.productPage = productPage;
    return this;
  }

   /**
   * Serialised product page
   * @return productPage
  **/
  @ApiModelProperty(value = "Serialised product page")
  public String getProductPage() {
    return productPage;
  }

  public void setProductPage(String productPage) {
    this.productPage = productPage;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Solutions solutions = (Solutions) o;
    return Objects.equals(this.id, solutions.id) &&
        Objects.equals(this.previousId, solutions.previousId) &&
        Objects.equals(this.organisationId, solutions.organisationId) &&
        Objects.equals(this.version, solutions.version) &&
        Objects.equals(this.status, solutions.status) &&
        Objects.equals(this.createdById, solutions.createdById) &&
        Objects.equals(this.createdOn, solutions.createdOn) &&
        Objects.equals(this.modifiedById, solutions.modifiedById) &&
        Objects.equals(this.modifiedOn, solutions.modifiedOn) &&
        Objects.equals(this.name, solutions.name) &&
        Objects.equals(this.description, solutions.description) &&
        Objects.equals(this.productPage, solutions.productPage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, previousId, organisationId, version, status, createdById, createdOn, modifiedById, modifiedOn, name, description, productPage);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Solutions {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    previousId: ").append(toIndentedString(previousId)).append("\n");
    sb.append("    organisationId: ").append(toIndentedString(organisationId)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    createdById: ").append(toIndentedString(createdById)).append("\n");
    sb.append("    createdOn: ").append(toIndentedString(createdOn)).append("\n");
    sb.append("    modifiedById: ").append(toIndentedString(modifiedById)).append("\n");
    sb.append("    modifiedOn: ").append(toIndentedString(modifiedOn)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    productPage: ").append(toIndentedString(productPage)).append("\n");
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
