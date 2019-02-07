package org.wso2.carbon.identity.claim.verification.endpoint.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;



/**
 * User that the claim belongs to.
 **/


@ApiModel(description = "User that the claim belongs to.")
public class UserDTO  {
  
  
  
  private String username = null;
  
  
  private String tenantDomain = null;


  private String realm = null;

  
  /**
   * Username of the user.
   **/
  @ApiModelProperty(value = "Username of the user.")
  @JsonProperty("username")
  public String getUsername() {
    return username;
  }
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Tenant domain of the user.
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("tenant-domain")
  public String getTenantDomain() {
    return tenantDomain;
  }
  public void setTenantDomain(String tenantDomain) {
    this.tenantDomain = tenantDomain;
  }
  
  /**
   * User store that the user resides in.
   **/
  @ApiModelProperty(value = "User store that the user resides in.")
  @JsonProperty("realm")
  public String getRealm() {
    return realm;
  }
  public void setRealm(String realm) {
    this.realm = realm;
  }

  

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserDTO {\n");
    
    sb.append("  username: ").append(username).append("\n");
    sb.append("  tenantDomain: ").append(tenantDomain).append("\n");
    sb.append("  realm: ").append(realm).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
