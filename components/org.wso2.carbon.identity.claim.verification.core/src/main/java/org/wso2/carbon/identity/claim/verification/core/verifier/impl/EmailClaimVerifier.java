/*
 *  Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.wso2.carbon.identity.claim.verification.core.verifier.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.claim.verification.core.exception.ClaimVerificationException;
import org.wso2.carbon.identity.claim.verification.core.internal.ClaimVerificationServiceDataHolder;
import org.wso2.carbon.identity.claim.verification.core.model.Claim;
import org.wso2.carbon.identity.claim.verification.core.model.ConfirmationCodeData;
import org.wso2.carbon.identity.claim.verification.core.model.User;
import org.wso2.carbon.identity.claim.verification.core.store.ClaimVerificationStore;
import org.wso2.carbon.identity.claim.verification.core.util.ClaimVerificationCoreConstants;
import org.wso2.carbon.identity.claim.verification.core.util.ClaimVerificationCoreUtils;
import org.wso2.carbon.identity.claim.verification.core.verifier.ClaimVerifier;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.identity.event.IdentityEventConstants;
import org.wso2.carbon.identity.event.IdentityEventException;
import org.wso2.carbon.identity.event.event.Event;
import org.wso2.carbon.user.api.UserStoreException;
import org.wso2.carbon.user.core.UserStoreManager;
import org.wso2.carbon.user.core.service.RealmService;

import java.util.HashMap;
import java.util.Map;

public class EmailClaimVerifier implements ClaimVerifier {

    private static final Log LOG = LogFactory.getLog(EmailClaimVerifier.class);
    private final String CLAIM_URI_USER_GIVEN_NAME="http://wso2.org/claims/givenname";
    private final String PROPERTY_SEND_TO="send-to";
    private final String PROPERTY_CLAIM_NAME="claim-name";
    private final String PROPERTY_CLAIM_VALUE="claim-value";
    private final String PROPERTY_VALIDATION_URL="validation-url";
    private final String PROPERTY_CONFIRMATION_CODE="confirmation-code";
    private final String PROPERTY_TEMPLATE_TYPE="TEMPLATE_TYPE";
    private final String PROPERTY_TEMPLATE_TYPE_VALUE= "emailVerification";

    private final String identifier = "EmailClaimVerifier";



    @Override
    public void sendNotification(User user, Claim claim, Map<String, String> properties) throws ClaimVerificationException {

        ClaimVerificationStore claimVerificationStore = ClaimVerificationCoreUtils.getClaimVerificationStore();

        if (properties.containsKey(ClaimVerificationCoreConstants.PROP_IS_RETRY_ATTEMPT)){

            ConfirmationCodeData codeData = new ConfirmationCodeData(user, null, claim.getClaimUri(),
                    ClaimVerificationCoreConstants.Scenarios.EMAIL_VERIFICATION );

            claimVerificationStore.invalidateConfirmationCode(codeData);
        }

        String confirmationCode = ClaimVerificationCoreUtils.getConfirmationCode();

        ConfirmationCodeData codeData = new ConfirmationCodeData(user, confirmationCode, claim.getClaimUri(),
                ClaimVerificationCoreConstants.Scenarios.EMAIL_VERIFICATION );

        claimVerificationStore.storeConfirmationCode(codeData);

        // add confirmation code to db-- user common util

        Map <String, Object> notificationProps = new HashMap<>();
        notificationProps.put(IdentityEventConstants.EventProperty.USER_STORE_MANAGER,
                getUserStoreManager(IdentityTenantUtil.getTenantId(user.getTenantDomain())));
        notificationProps.put(IdentityEventConstants.EventProperty.USER_NAME, user.getUsername());
        notificationProps.put(IdentityEventConstants.EventProperty.USER_STORE_DOMAIN, user.getRealm());
        notificationProps.put(IdentityEventConstants.EventProperty.TENANT_DOMAIN, user.getTenantDomain());
        notificationProps.put(PROPERTY_SEND_TO, claim.getClaimValue());
        notificationProps.put(PROPERTY_CLAIM_NAME, claim.getClaimDisplayTag());
        notificationProps.put(PROPERTY_CLAIM_VALUE, claim.getClaimValue());
        notificationProps.put(PROPERTY_VALIDATION_URL, properties.get(PROPERTY_VALIDATION_URL));
        notificationProps.put(PROPERTY_CONFIRMATION_CODE, confirmationCode);
        notificationProps.put(PROPERTY_TEMPLATE_TYPE, PROPERTY_TEMPLATE_TYPE_VALUE);


        Event identityMgtEvent = new Event(IdentityEventConstants.Event.TRIGGER_NOTIFICATION, notificationProps);
        try {
            ClaimVerificationServiceDataHolder.getInstance().getIdentityEventService().handleEvent(identityMgtEvent);
        } catch (IdentityEventException e) {
            String msg = "Error occurred while sending claim verification email to: " + user.getUsername();
            LOG.error(msg, e);
            throw new ClaimVerificationException(msg, e);
        }

    }

    @Override
    public boolean isVerified(Map<String, String> properties) throws ClaimVerificationException {

        //validate confirmation code
        return false;
    }

    @Override
    public boolean canHandle(String requestedVerifier) throws ClaimVerificationException {

        return identifier.equalsIgnoreCase(requestedVerifier);

    }

    @Override
    public String getConfirmationCodeValidityPeriod() throws ClaimVerificationException {
        //universal config builder for any verifier
        return null;
    }


    /*private void sendEmail() {

        HashMap<String, Object> properties = new HashMap<>();
        properties.put(IdentityEventConstants.EventProperty.USER_NAME, receiver.getUsername());
        properties.put(IdentityEventConstants.EventProperty.USER_STORE_DOMAIN, receiver.getUserStoreDomain());
        properties.put(IdentityEventConstants.EventProperty.TENANT_DOMAIN,
                CarbonContext.getThreadLocalCarbonContext().getTenantDomain());
        try {
            UserStoreManager userStoreManager;
            if(IdentityUtil.getPrimaryDomainName().equals(receiver.getUserStoreDomain())) {
                userStoreManager = (UserStoreManager) CarbonContext.getThreadLocalCarbonContext().getUserRealm()
                        .getUserStoreManager();
            } else {
                userStoreManager = ((UserStoreManager) CarbonContext.getThreadLocalCarbonContext().getUserRealm()
                        .getUserStoreManager()).getSecondaryUserStoreManager(receiver.getUserStoreDomain());
            }
            properties.put(IdentityEventConstants.EventProperty.USER_STORE_MANAGER,userStoreManager);
        } catch (UserStoreException e) {
            log.error("Error while getting user store manager", e);
            return;
        }

        properties.put("first-name", receiver.getFirstName());
        properties.put("suspension-date", receiver.getExpireDate());
        properties.put("TEMPLATE_TYPE", "idleAccountReminder");

        Event identityMgtEvent = new Event(IdentityEventConstants.Event.TRIGGER_NOTIFICATION, properties);
        try {
            NotificationTaskDataHolder.getInstance().getIdentityEventService().handleEvent(identityMgtEvent);
        } catch (IdentityEventException e) {
            log.error("Error occurred while sending email to: " + receiver.getUsername(), e);
        }

    }*/

    private UserStoreManager getUserStoreManager(int tenantId) throws ClaimVerificationException {

        RealmService realmService = ClaimVerificationServiceDataHolder.getInstance().getRealmService();
        try {
            return (UserStoreManager) realmService.getTenantUserRealm(tenantId).getUserStoreManager();
        } catch (UserStoreException e) {
            String msg = "Error while retrieving givenname claim value for user.";
            LOG.error(msg,e);
            throw new ClaimVerificationException(msg, e);
        }


    }
}
