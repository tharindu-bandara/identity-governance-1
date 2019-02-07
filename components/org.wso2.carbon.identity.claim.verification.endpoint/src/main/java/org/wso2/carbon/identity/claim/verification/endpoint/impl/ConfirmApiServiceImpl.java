package org.wso2.carbon.identity.claim.verification.endpoint.impl;

import org.wso2.carbon.identity.claim.verification.endpoint.ApiResponseMessage;
import org.wso2.carbon.identity.claim.verification.endpoint.ConfirmApiService;
import org.wso2.carbon.identity.claim.verification.endpoint.dto.ConfirmationRequestDTO;

import javax.ws.rs.core.Response;

public class ConfirmApiServiceImpl extends ConfirmApiService {
    @Override
    public Response confirmPost(ConfirmationRequestDTO confirmationRequest){
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
