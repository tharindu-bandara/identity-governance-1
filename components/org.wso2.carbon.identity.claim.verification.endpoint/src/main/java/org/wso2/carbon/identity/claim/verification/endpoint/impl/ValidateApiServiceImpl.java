package org.wso2.carbon.identity.claim.verification.endpoint.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.claim.verification.core.exception.ClaimVerificationException;
import org.wso2.carbon.identity.claim.verification.core.exception.ClaimVerificationFailureException;
import org.wso2.carbon.identity.claim.verification.core.model.ValidationResponse;
import org.wso2.carbon.identity.claim.verification.endpoint.ValidateApiService;
import org.wso2.carbon.identity.claim.verification.endpoint.dto.ValidationRequestDTO;
import org.wso2.carbon.identity.claim.verification.endpoint.dto.ValidationResponseDTO;
import org.wso2.carbon.identity.claim.verification.endpoint.impl.util.ClaimVerificationEndpointConstants;
import org.wso2.carbon.identity.claim.verification.endpoint.impl.util.ClaimVerificationEndpointUtils;

import javax.ws.rs.core.Response;

public class ValidateApiServiceImpl extends ValidateApiService {

    private static final Log LOG = LogFactory.getLog(ValidateApiServiceImpl.class);


    @Override
    public Response validatePost(ValidationRequestDTO validationRequest){
        // do some magic!

        ValidationResponse validationResponse = null;
        try {
            validationResponse = ClaimVerificationEndpointUtils.getClaimVerificationHandler().validateClaim(
                    validationRequest.getCode(),
                    ClaimVerificationEndpointUtils.getPropertiesToMap(validationRequest.getProperties()),
                    validationRequest.getRequireAdditionalValidation()
            );
        } catch (ClaimVerificationException e) {

            if (e instanceof ClaimVerificationFailureException){
                if (LOG.isDebugEnabled()){
                    LOG.debug(e.getErrorCode()+":"+e.getMessage(), e);
                }
                ClaimVerificationEndpointUtils.handleBadRequest(e.getErrorCode(), e.getMessage());
            } else {
                String msg = "Error while validating the claims";
                LOG.error(msg, e);
                ClaimVerificationEndpointUtils.handleInternalServerError(
                        ClaimVerificationEndpointConstants.ERROR_CODE_UNEXPECTED_ERROR, msg);

            }
        }
        ValidationResponseDTO validationResponseDTO = ClaimVerificationEndpointUtils.getValidationResponse(
                validationResponse.isValid(), validationRequest.getRequireAdditionalValidation(),
                validationResponse.getCode());

        return Response.ok().entity(validationResponseDTO).build();
    }
}
