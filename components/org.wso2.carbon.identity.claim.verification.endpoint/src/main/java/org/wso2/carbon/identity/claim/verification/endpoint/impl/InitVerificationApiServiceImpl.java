package org.wso2.carbon.identity.claim.verification.endpoint.impl;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.claim.verification.core.exception.ClaimVerificationException;
import org.wso2.carbon.identity.claim.verification.core.model.Claim;
import org.wso2.carbon.identity.claim.verification.core.model.User;
import org.wso2.carbon.identity.claim.verification.endpoint.InitVerificationApiService;
import org.wso2.carbon.identity.claim.verification.endpoint.dto.VerificationInitiatingRequestDTO;
import org.wso2.carbon.identity.claim.verification.endpoint.impl.util.ClaimVerificationEndpointConstants;
import org.wso2.carbon.identity.claim.verification.endpoint.impl.util.ClaimVerificationEndpointUtils;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.identity.core.util.IdentityUtil;

import javax.ws.rs.core.Response;

public class InitVerificationApiServiceImpl extends InitVerificationApiService {

    private static final Log LOG = LogFactory.getLog(InitVerificationApiServiceImpl.class);

    @Override
    public Response initVerificationPost(VerificationInitiatingRequestDTO verificationInitiatingRequest) {

        String tenantDomainFromContext =
                (String) IdentityUtil.threadLocalProperties.get().get(ClaimVerificationEndpointConstants
                .TENANT_NAME_FROM_CONTEXT);

        User user = ClaimVerificationEndpointUtils.getUser(verificationInitiatingRequest.getUser(),
                tenantDomainFromContext);

        int tenantIdFromContext = IdentityTenantUtil.getTenantId(user.getTenantDomain());

        String[] userList = ClaimVerificationEndpointUtils.getUserList(tenantIdFromContext, user.getUsername());

        // Validate incoming user date.
        if (ArrayUtils.isEmpty(userList)) {
            String msg = "Unable to find an user with username: " + user.getUsername() + " in the system.";
            if (LOG.isDebugEnabled()) {
                LOG.debug(msg);
            }
            ClaimVerificationEndpointUtils.handleBadRequest(
                    ClaimVerificationEndpointConstants.ERROR_CODE_NO_MATCHING_USER_FOUND, msg);
        } else if (userList.length == 1) {
            user.setRealm(IdentityUtil.extractDomainFromName(userList[0]));
        } else {
            String msg = "There are multiple users with username: " + user.getUsername() + " in the system, " +
                    "please send the correct user-store domain along with the username.";
            if (LOG.isDebugEnabled()) {
                LOG.debug(msg);
            }
            ClaimVerificationEndpointUtils.handleBadRequest(
                    ClaimVerificationEndpointConstants.ERROR_CODE_MULTIPLE_MATCHING_USERS_FOUND, msg);
        }

        Claim claim = ClaimVerificationEndpointUtils.getClaim(verificationInitiatingRequest.getClaim());
        org.wso2.carbon.user.api.Claim claimMetaData =
                ClaimVerificationEndpointUtils.getClaimMetaData(tenantIdFromContext, claim.getClaimUri());

        // Validate incoming claim date.
        if (claimMetaData == null){
            String msg = "Unable to find a claim with claim uri: " + claim.getClaimUri() + " in the system.";
            if (LOG.isDebugEnabled()) {
                LOG.debug(msg);
            }
            ClaimVerificationEndpointUtils.handleBadRequest(
                    ClaimVerificationEndpointConstants.ERROR_CODE_NO_MATCHING_USER_FOUND, msg);
        }
        claim.setClaimDisplayTag(claimMetaData.getDisplayTag());

        String confirmationCode = "";
        try {
            confirmationCode = ClaimVerificationEndpointUtils.getClaimVerificationHandler().initVerification(
                    user, claim, verificationInitiatingRequest.getVerificationMethod(),
                    ClaimVerificationEndpointUtils.getPropertiesToMap(verificationInitiatingRequest.getProperties()));
        } catch (ClaimVerificationException e) {
            String msg = "Error while initiating claim verification.";
            LOG.error(msg, e);
            ClaimVerificationEndpointUtils.handleInternalServerError(
                    ClaimVerificationEndpointConstants.ERROR_CODE_UNEXPECTED_ERROR, msg);
        }

        return Response.ok().entity(ClaimVerificationEndpointUtils.getInitVerificationResponse(confirmationCode)).build();
    }
}
