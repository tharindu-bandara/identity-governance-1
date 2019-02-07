package org.wso2.carbon.identity.claim.verification.endpoint.dto;

import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.claim.verification.endpoint.dto.ClaimDTO;
import org.wso2.carbon.identity.claim.verification.endpoint.dto.PropertyDTO;
import org.wso2.carbon.identity.claim.verification.endpoint.dto.UserDTO;

import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.*;

import javax.validation.constraints.NotNull;





@ApiModel(description = "")
public class VerificationInitiatingRequestDTO  {
  
  
  
  private UserDTO user = null;
  
  
  private ClaimDTO claim = null;
  
  
  private String verificationMethod = null;
  
  
  private List<PropertyDTO> properties = new ArrayList<PropertyDTO>();

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("user")
  public UserDTO getUser() {
    return user;
  }
  public void setUser(UserDTO user) {
    this.user = user;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("claim")
  public ClaimDTO getClaim() {
    return claim;
  }
  public void setClaim(ClaimDTO claim) {
    this.claim = claim;
  }

  
  /**
   * Verifier that should be used to verify the claim.
   **/
  @ApiModelProperty(value = "Verifier that should be used to verify the claim.")
  @JsonProperty("verificationMethod")
  public String getVerificationMethod() {
    return verificationMethod;
  }
  public void setVerificationMethod(String verificationMethod) {
    this.verificationMethod = verificationMethod;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("properties")
  public List<PropertyDTO> getProperties() {
    return properties;
  }
  public void setProperties(List<PropertyDTO> properties) {
    this.properties = properties;
  }

  

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class VerificationInitiatingRequestDTO {\n");
    
    sb.append("  user: ").append(user).append("\n");
    sb.append("  claim: ").append(claim).append("\n");
    sb.append("  verificationMethod: ").append(verificationMethod).append("\n");
    sb.append("  properties: ").append(properties).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
