package org.wso2.carbon.identity.claim.verification.endpoint.dto;


import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.*;

import javax.validation.constraints.NotNull;





@ApiModel(description = "")
public class ConfirmationRequestDTO  {
  
  
  
  private String code = null;
  
  
  private String status = null;

  
  /**
   * Confirmation code related to the claim verification request.
   **/
  @ApiModelProperty(value = "Confirmation code related to the claim verification request.")
  @JsonProperty("code")
  public String getCode() {
    return code;
  }
  public void setCode(String code) {
    this.code = code;
  }

  
  /**
   * Verification status of the claim(SUCESS/FAILURE).
   **/
  @ApiModelProperty(value = "Verification status of the claim(SUCESS/FAILURE).")
  @JsonProperty("status")
  public String getStatus() {
    return status;
  }
  public void setStatus(String status) {
    this.status = status;
  }

  

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConfirmationRequestDTO {\n");
    
    sb.append("  code: ").append(code).append("\n");
    sb.append("  status: ").append(status).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
