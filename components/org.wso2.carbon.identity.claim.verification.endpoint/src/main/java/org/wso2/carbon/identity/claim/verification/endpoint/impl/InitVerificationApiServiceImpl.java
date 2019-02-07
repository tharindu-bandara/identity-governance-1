package org.wso2.carbon.identity.claim.verification.endpoint.impl;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.base.MultitenantConstants;
import org.wso2.carbon.identity.claim.verification.core.exception.ClaimVerificationException;
import org.wso2.carbon.identity.claim.verification.endpoint.InitVerificationApiService;
import org.wso2.carbon.identity.claim.verification.endpoint.dto.UserDTO;
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
        // do some magic!

        String tenantDomainFromContext =
                (String) IdentityUtil.threadLocalProperties.get().get(ClaimVerificationEndpointConstants
                .TENANT_NAME_FROM_CONTEXT);

        if (StringUtils.isNotBlank(tenantDomainFromContext)) {
            verificationInitiatingRequest.getUser().setTenantDomain(tenantDomainFromContext);
        } else {
            verificationInitiatingRequest.getUser().setTenantDomain(MultitenantConstants.SUPER_TENANT_DOMAIN_NAME);
        }

        UserDTO user = verificationInitiatingRequest.getUser();
        int tenantIdFromContext = IdentityTenantUtil.getTenantId(user.getTenantDomain());

        String[] userList = ClaimVerificationEndpointUtils.getUserList(tenantIdFromContext, user.getUsername());

        if (ArrayUtils.isEmpty(userList)) {
            String msg = "Unable to find an user with username: " + user.getUsername() + " in the system.";
            LOG.error(msg);
            ClaimVerificationEndpointUtils.handleBadRequest(
                    ClaimVerificationEndpointConstants.ERROR_CODE_NO_MATCHING_USER_FOUND, msg);
        } else if (userList.length == 1) {
            user.setRealm(IdentityUtil.extractDomainFromName(userList[0]));
        } else {
            String msg = "There are multiple users with username: " + user.getUsername() + " in the system, " +
                    "please send the correct user-store domain along with the username.";
            LOG.error(msg);
            ClaimVerificationEndpointUtils.handleBadRequest(
                    ClaimVerificationEndpointConstants.ERROR_CODE_MULTIPLE_MATCHING_USERS_FOUND, msg);
        }

        String confirmationCode = "";
        try {
            confirmationCode = ClaimVerificationEndpointUtils.getClaimVerificationHandler().initVerification(
                    ClaimVerificationEndpointUtils.getUser(user),
                    ClaimVerificationEndpointUtils.getClaim(verificationInitiatingRequest.getClaim()),
                    verificationInitiatingRequest.getVerificationMethod(),
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
