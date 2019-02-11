package org.wso2.carbon.identity.claim.verification.endpoint.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.claim.verification.core.exception.ClaimVerificationException;
import org.wso2.carbon.identity.claim.verification.core.exception.ClaimVerificationFailureException;
import org.wso2.carbon.identity.claim.verification.endpoint.ConfirmApiService;
import org.wso2.carbon.identity.claim.verification.endpoint.dto.ConfirmationRequestDTO;
import org.wso2.carbon.identity.claim.verification.endpoint.impl.util.ClaimVerificationEndpointConstants;
import org.wso2.carbon.identity.claim.verification.endpoint.impl.util.ClaimVerificationEndpointUtils;

import javax.ws.rs.core.Response;

public class ConfirmApiServiceImpl extends ConfirmApiService {

    private static final Log LOG = LogFactory.getLog(ConfirmApiServiceImpl.class);

    @Override
    public Response confirmPost(ConfirmationRequestDTO confirmationRequest) {

        boolean isValidationSuccess = false;
        if (ClaimVerificationEndpointConstants.CLAIM_VALIDATION_SUCCESS.equalsIgnoreCase(
                confirmationRequest.getStatus())) {
            isValidationSuccess = true;
        } else if (ClaimVerificationEndpointConstants.CLAIM_VALIDATION_FAILURE.equalsIgnoreCase(
                confirmationRequest.getStatus())) {
            isValidationSuccess = false;
        } else {
            String msg = String.format("Sent validation status: %s is not a acceptable status. Use %s or %s .",
                    confirmationRequest.getStatus(), ClaimVerificationEndpointConstants.CLAIM_VALIDATION_SUCCESS,
                    ClaimVerificationEndpointConstants.CLAIM_VALIDATION_FAILURE);
            if (LOG.isDebugEnabled()) {
                LOG.debug(msg);
            }
            ClaimVerificationEndpointUtils.handleBadRequest(
                    ClaimVerificationEndpointConstants.ERROR_CODE_NO_MATCHING_VALIDATION_STATUS_FOUND, msg);
        }

        try {
            ClaimVerificationEndpointUtils.getClaimVerificationHandler().confirmVerification(
                    confirmationRequest.getCode(), isValidationSuccess);
        } catch (ClaimVerificationException e) {

            if (e instanceof ClaimVerificationFailureException) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(e.getErrorCode() + ":" + e.getMessage(), e);
                }
                ClaimVerificationEndpointUtils.handleBadRequest(e.getErrorCode(), e.getMessage());
            } else {
                String msg = "Error while finalizing claim verification.";
                LOG.error(msg, e);
                ClaimVerificationEndpointUtils.handleInternalServerError(
                        ClaimVerificationEndpointConstants.ERROR_CODE_UNEXPECTED_ERROR, msg);
            }
        }

        return Response.ok().build();
    }

}
