package org.wso2.carbon.identity.claim.verification.endpoint.dto;

import io.swagger.annotations.ApiModel;

import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.*;

import javax.validation.constraints.NotNull;



/**
 * Property map to pass additional properties that may be required.
 **/


@ApiModel(description = "Property map to pass additional properties that may be required.")
public class PropertyDTO  {
  
  
  
  private String key = null;
  
  
  private String value = null;

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("key")
  public String getKey() {
    return key;
  }
  public void setKey(String key) {
    this.key = key;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("value")
  public String getValue() {
    return value;
  }
  public void setValue(String value) {
    this.value = value;
  }

  

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class PropertyDTO {\n");
    
    sb.append("  key: ").append(key).append("\n");
    sb.append("  value: ").append(value).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
