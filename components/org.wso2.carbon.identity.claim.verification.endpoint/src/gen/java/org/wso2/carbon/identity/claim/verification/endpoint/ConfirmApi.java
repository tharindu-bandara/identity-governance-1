package org.wso2.carbon.identity.claim.verification.endpoint;

import org.wso2.carbon.identity.claim.verification.endpoint.dto.*;
import org.wso2.carbon.identity.claim.verification.endpoint.ConfirmApiService;
import org.wso2.carbon.identity.claim.verification.endpoint.factories.ConfirmApiServiceFactory;

import io.swagger.annotations.ApiParam;

import org.wso2.carbon.identity.claim.verification.endpoint.dto.ErrorDTO;
import org.wso2.carbon.identity.claim.verification.endpoint.dto.ConfirmationRequestDTO;

import java.util.List;

import java.io.InputStream;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;

import javax.ws.rs.core.Response;
import javax.ws.rs.*;

@Path("/confirm")
@Consumes({ "application/json" })
@Produces({ "application/json" })
@io.swagger.annotations.Api(value = "/confirm", description = "the confirm API")
public class ConfirmApi  {

   private final ConfirmApiService delegate = ConfirmApiServiceFactory.getConfirmApi();

    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "This API is used to confirm the new claim value after validating it.\n", response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Ok"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error") })

    public Response confirmPost(@ApiParam(value = "Request object to finalize the process which states whether the verification was successful or not." ,required=true ) ConfirmationRequestDTO confirmationRequest)
    {
    return delegate.confirmPost(confirmationRequest);
    }
}

