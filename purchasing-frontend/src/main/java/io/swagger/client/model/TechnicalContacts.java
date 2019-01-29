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
 * A means of communicating with an Organisation, typically a person, email address, telephone number etc,  in the context of a Solution
 */
@ApiModel(description = "A means of communicating with an Organisation, typically a person, email address, telephone number etc,  in the context of a Solution")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2019-01-29T15:45:36.951Z")
public class TechnicalContacts {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("solutionId")
  private String solutionId = null;

  @JsonProperty("contactType")
  private String contactType = null;

  @JsonProperty("firstName")
  private String firstName = null;

  @JsonProperty("lastName")
  private String lastName = null;

  @JsonProperty("emailAddress")
  private String emailAddress = null;

  @JsonProperty("phoneNumber")
  private String phoneNumber = null;

  public TechnicalContacts id(String id) {
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

  public TechnicalContacts solutionId(String solutionId) {
    this.solutionId = solutionId;
    return this;
  }

   /**
   * Unique identifier of Solution
   * @return solutionId
  **/
  @ApiModelProperty(required = true, value = "Unique identifier of Solution")
  public String getSolutionId() {
    return solutionId;
  }

  public void setSolutionId(String solutionId) {
    this.solutionId = solutionId;
  }

  public TechnicalContacts contactType(String contactType) {
    this.contactType = contactType;
    return this;
  }

   /**
   * Description of type of TechnicalContact eg  &lt;list type&#x3D;\&quot;bullet\&quot;&gt;  Lead Contact  Technical Contact  Executive Sponsor  Clinical Safety Officer  Connection Agreement Signatory  Other...  &lt;/list&gt;
   * @return contactType
  **/
  @ApiModelProperty(required = true, value = "Description of type of TechnicalContact eg  <list type=\"bullet\">  Lead Contact  Technical Contact  Executive Sponsor  Clinical Safety Officer  Connection Agreement Signatory  Other...  </list>")
  public String getContactType() {
    return contactType;
  }

  public void setContactType(String contactType) {
    this.contactType = contactType;
  }

  public TechnicalContacts firstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

   /**
   * First name of contact
   * @return firstName
  **/
  @ApiModelProperty(value = "First name of contact")
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public TechnicalContacts lastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

   /**
   * Last name of contact
   * @return lastName
  **/
  @ApiModelProperty(value = "Last name of contact")
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public TechnicalContacts emailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
    return this;
  }

   /**
   * Primary email address of contact
   * @return emailAddress
  **/
  @ApiModelProperty(required = true, value = "Primary email address of contact")
  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public TechnicalContacts phoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
    return this;
  }

   /**
   * Primary phone number of contact
   * @return phoneNumber
  **/
  @ApiModelProperty(value = "Primary phone number of contact")
  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TechnicalContacts technicalContacts = (TechnicalContacts) o;
    return Objects.equals(this.id, technicalContacts.id) &&
        Objects.equals(this.solutionId, technicalContacts.solutionId) &&
        Objects.equals(this.contactType, technicalContacts.contactType) &&
        Objects.equals(this.firstName, technicalContacts.firstName) &&
        Objects.equals(this.lastName, technicalContacts.lastName) &&
        Objects.equals(this.emailAddress, technicalContacts.emailAddress) &&
        Objects.equals(this.phoneNumber, technicalContacts.phoneNumber);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, solutionId, contactType, firstName, lastName, emailAddress, phoneNumber);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TechnicalContacts {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    solutionId: ").append(toIndentedString(solutionId)).append("\n");
    sb.append("    contactType: ").append(toIndentedString(contactType)).append("\n");
    sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
    sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
    sb.append("    emailAddress: ").append(toIndentedString(emailAddress)).append("\n");
    sb.append("    phoneNumber: ").append(toIndentedString(phoneNumber)).append("\n");
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

