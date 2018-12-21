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
 * A piece of &#39;evidence&#39; which supports a claim to a ‘standard’.  This is then assessed by NHS to verify the ‘solution’ complies with the ‘standard’ it has claimed.
 */
@ApiModel(description = "A piece of 'evidence' which supports a claim to a ‘standard’.  This is then assessed by NHS to verify the ‘solution’ complies with the ‘standard’ it has claimed.")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-12-11T12:47:15.885Z")
public class StandardsApplicableEvidence {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("previousId")
  private String previousId = null;

  @JsonProperty("claimId")
  private String claimId = null;

  @JsonProperty("createdById")
  private String createdById = null;

  @JsonProperty("createdOn")
  private OffsetDateTime createdOn = null;

  @JsonProperty("evidence")
  private String evidence = null;

  @JsonProperty("hasRequestedLiveDemo")
  private Boolean hasRequestedLiveDemo = null;

  @JsonProperty("blobId")
  private String blobId = null;

  public StandardsApplicableEvidence id(String id) {
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

  public StandardsApplicableEvidence previousId(String previousId) {
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

  public StandardsApplicableEvidence claimId(String claimId) {
    this.claimId = claimId;
    return this;
  }

   /**
   * Unique identifier of Claim
   * @return claimId
  **/
  @ApiModelProperty(required = true, value = "Unique identifier of Claim")
  public String getClaimId() {
    return claimId;
  }

  public void setClaimId(String claimId) {
    this.claimId = claimId;
  }

  public StandardsApplicableEvidence createdById(String createdById) {
    this.createdById = createdById;
    return this;
  }

   /**
   * Unique identifier of Contact who created record  Derived from calling context  SET ON SERVER
   * @return createdById
  **/
  @ApiModelProperty(required = true, value = "Unique identifier of Contact who created record  Derived from calling context  SET ON SERVER")
  public String getCreatedById() {
    return createdById;
  }

  public void setCreatedById(String createdById) {
    this.createdById = createdById;
  }

  public StandardsApplicableEvidence createdOn(OffsetDateTime createdOn) {
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

  public StandardsApplicableEvidence evidence(String evidence) {
    this.evidence = evidence;
    return this;
  }

   /**
   * Serialised evidence data
   * @return evidence
  **/
  @ApiModelProperty(value = "Serialised evidence data")
  public String getEvidence() {
    return evidence;
  }

  public void setEvidence(String evidence) {
    this.evidence = evidence;
  }

  public StandardsApplicableEvidence hasRequestedLiveDemo(Boolean hasRequestedLiveDemo) {
    this.hasRequestedLiveDemo = hasRequestedLiveDemo;
    return this;
  }

   /**
   * true if supplier has requested to do a &#39;live demo&#39;  instead of submitting a file
   * @return hasRequestedLiveDemo
  **/
  @ApiModelProperty(value = "true if supplier has requested to do a 'live demo'  instead of submitting a file")
  public Boolean isHasRequestedLiveDemo() {
    return hasRequestedLiveDemo;
  }

  public void setHasRequestedLiveDemo(Boolean hasRequestedLiveDemo) {
    this.hasRequestedLiveDemo = hasRequestedLiveDemo;
  }

  public StandardsApplicableEvidence blobId(String blobId) {
    this.blobId = blobId;
    return this;
  }

   /**
   * unique identifier of binary file in blob storage system  NOTE:  this may not be a GUID eg it may be a URL  NOTE:  this is a GUID for SharePoint
   * @return blobId
  **/
  @ApiModelProperty(value = "unique identifier of binary file in blob storage system  NOTE:  this may not be a GUID eg it may be a URL  NOTE:  this is a GUID for SharePoint")
  public String getBlobId() {
    return blobId;
  }

  public void setBlobId(String blobId) {
    this.blobId = blobId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StandardsApplicableEvidence standardsApplicableEvidence = (StandardsApplicableEvidence) o;
    return Objects.equals(this.id, standardsApplicableEvidence.id) &&
        Objects.equals(this.previousId, standardsApplicableEvidence.previousId) &&
        Objects.equals(this.claimId, standardsApplicableEvidence.claimId) &&
        Objects.equals(this.createdById, standardsApplicableEvidence.createdById) &&
        Objects.equals(this.createdOn, standardsApplicableEvidence.createdOn) &&
        Objects.equals(this.evidence, standardsApplicableEvidence.evidence) &&
        Objects.equals(this.hasRequestedLiveDemo, standardsApplicableEvidence.hasRequestedLiveDemo) &&
        Objects.equals(this.blobId, standardsApplicableEvidence.blobId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, previousId, claimId, createdById, createdOn, evidence, hasRequestedLiveDemo, blobId);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StandardsApplicableEvidence {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    previousId: ").append(toIndentedString(previousId)).append("\n");
    sb.append("    claimId: ").append(toIndentedString(claimId)).append("\n");
    sb.append("    createdById: ").append(toIndentedString(createdById)).append("\n");
    sb.append("    createdOn: ").append(toIndentedString(createdOn)).append("\n");
    sb.append("    evidence: ").append(toIndentedString(evidence)).append("\n");
    sb.append("    hasRequestedLiveDemo: ").append(toIndentedString(hasRequestedLiveDemo)).append("\n");
    sb.append("    blobId: ").append(toIndentedString(blobId)).append("\n");
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
