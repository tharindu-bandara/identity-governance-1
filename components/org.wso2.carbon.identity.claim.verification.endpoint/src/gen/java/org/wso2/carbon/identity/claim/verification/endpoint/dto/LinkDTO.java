package org.wso2.carbon.identity.claim.verification.endpoint.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.StringUtils;





@ApiModel(description = "")
public class LinkDTO  {
  
  
  
  private String rel = StringUtils.EMPTY;
  
  
  private String uri = StringUtils.EMPTY;

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("rel")
  public String getRel() {
    return rel;
  }
  public void setRel(String rel) {
    this.rel = rel;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("uri")
  public String getUri() {
    return uri;
  }
  public void setUri(String uri) {
    this.uri = uri;
  }

  

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class LinkDTO {\n");
    
    sb.append("  rel: ").append(rel).append("\n");
    sb.append("  uri: ").append(uri).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
