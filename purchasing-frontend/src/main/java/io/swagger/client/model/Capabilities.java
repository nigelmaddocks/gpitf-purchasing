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
 * A list of potential competencies a ‘solution’ can perform or provide eg  * Mobile working  * Training  * Prescribing  * Installation  Note that a ‘capability’ has a link to zero or one previous ‘capability’  Generally, only interested in current ‘capability’
 */
@ApiModel(description = "A list of potential competencies a ‘solution’ can perform or provide eg  * Mobile working  * Training  * Prescribing  * Installation  Note that a ‘capability’ has a link to zero or one previous ‘capability’  Generally, only interested in current ‘capability’")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2019-02-27T11:46:50.314Z")
public class Capabilities {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("previousId")
  private String previousId = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("url")
  private String url = null;

  @JsonProperty("type")
  private String type = null;

  public Capabilities id(String id) {
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

  public Capabilities previousId(String previousId) {
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

  public Capabilities name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Name of Capability/Standard, as displayed to a user
   * @return name
  **/
  @ApiModelProperty(value = "Name of Capability/Standard, as displayed to a user")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Capabilities description(String description) {
    this.description = description;
    return this;
  }

   /**
   * Description of Capability/Standard, as displayed to a user
   * @return description
  **/
  @ApiModelProperty(value = "Description of Capability/Standard, as displayed to a user")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Capabilities url(String url) {
    this.url = url;
    return this;
  }

   /**
   * URL with further information
   * @return url
  **/
  @ApiModelProperty(value = "URL with further information")
  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public Capabilities type(String type) {
    this.type = type;
    return this;
  }

   /**
   * Category of Capability/Standard
   * @return type
  **/
  @ApiModelProperty(value = "Category of Capability/Standard")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Capabilities capabilities = (Capabilities) o;
    return Objects.equals(this.id, capabilities.id) &&
        Objects.equals(this.previousId, capabilities.previousId) &&
        Objects.equals(this.name, capabilities.name) &&
        Objects.equals(this.description, capabilities.description) &&
        Objects.equals(this.url, capabilities.url) &&
        Objects.equals(this.type, capabilities.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, previousId, name, description, url, type);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Capabilities {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    previousId: ").append(toIndentedString(previousId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
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

