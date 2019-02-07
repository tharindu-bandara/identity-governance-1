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

package org.wso2.carbon.identity.claim.verification.endpoint.impl.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.claim.verification.core.ClaimVerificationHandler;
import org.wso2.carbon.identity.claim.verification.core.model.Claim;
import org.wso2.carbon.identity.claim.verification.core.model.User;
import org.wso2.carbon.identity.claim.verification.endpoint.dto.ClaimDTO;
import org.wso2.carbon.identity.claim.verification.endpoint.dto.ErrorDTO;
import org.wso2.carbon.identity.claim.verification.endpoint.dto.LinkDTO;
import org.wso2.carbon.identity.claim.verification.endpoint.dto.PropertyDTO;
import org.wso2.carbon.identity.claim.verification.endpoint.dto.UserDTO;
import org.wso2.carbon.identity.claim.verification.endpoint.dto.ValidationResponseDTO;
import org.wso2.carbon.identity.claim.verification.endpoint.dto.VerificationInitiatingResponseDTO;
import org.wso2.carbon.identity.claim.verification.endpoint.impl.exception.BadRequestException;
import org.wso2.carbon.identity.claim.verification.endpoint.impl.exception.InternalServerErrorException;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.recovery.internal.IdentityRecoveryServiceDataHolder;
import org.wso2.carbon.user.api.UserStoreException;
import org.wso2.carbon.user.core.UserStoreManager;
import org.wso2.carbon.user.core.service.RealmService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClaimVerificationEndpointUtils {
    private static final Log LOG = LogFactory.getLog(ClaimVerificationEndpointUtils.class);

    public static ClaimVerificationHandler getClaimVerificationHandler() {

        return (ClaimVerificationHandler) PrivilegedCarbonContext.getThreadLocalCarbonContext()
                .getOSGiService(ClaimVerificationHandler.class, null);
    }

    public static User getUser(UserDTO userDTO) {
        User user = new User();
        user.setTenantDomain(userDTO.getTenantDomain());
        if (StringUtils.isNotBlank(userDTO.getRealm())) {
            user.setRealm(userDTO.getRealm());
        } else {
            user.setRealm(IdentityUtil.getPrimaryDomainName());
        }

        user.setUsername(userDTO.getUsername());
        return user;
    }

    public static Claim getClaim(ClaimDTO claimDTO){
        Claim claim = new Claim();
        claim.setClaimUri(claimDTO.getClaimUri());
        claim.setClaimValue(claimDTO.getValue());
        return claim;
    }

    public static Map<String, String> getPropertiesToMap(List<PropertyDTO> propertyDTOList) {

        Map<String, String> properties = new HashMap<>();

        for (PropertyDTO propertyDTO :
                propertyDTOList) {
            properties.put(propertyDTO.getKey(), propertyDTO.getValue());
        }

        return properties;
    }

    public static ValidationResponseDTO getValidationResponse(boolean isValid, boolean isAdditionalValidationRequired, String code){
        ValidationResponseDTO validationResponseDTO = new ValidationResponseDTO();

        if (!isValid){
            validationResponseDTO.setStatus(ClaimVerificationEndpointConstants.CLAIM_VALIDATION_FAILURE);
            validationResponseDTO.setProperties(new ArrayList<>());
            validationResponseDTO.setLink(getLink(ClaimVerificationEndpointConstants.API_URI_EP_INIT_VERIFICATION));
        } else if (!isAdditionalValidationRequired){
            validationResponseDTO.setStatus(ClaimVerificationEndpointConstants.CLAIM_VALIDATION_SUCCESS);
            validationResponseDTO.setProperties(new ArrayList<>());
            validationResponseDTO.setLink(getEmptyLink());
        } else {
            validationResponseDTO.setStatus(ClaimVerificationEndpointConstants.CLAIM_VALIDATION_SUCCESS);

            List<PropertyDTO> propertyList = new ArrayList<>();
            propertyList.add(getProperty("code", code));

            validationResponseDTO.setProperties(propertyList);
            validationResponseDTO.setLink(getLink(ClaimVerificationEndpointConstants.API_URI_EP_INIT_VERIFICATION));
        }

        return validationResponseDTO;
    }

    public static VerificationInitiatingResponseDTO getInitVerificationResponse(String code){
        VerificationInitiatingResponseDTO verificationInitiatingResponseDTO = new VerificationInitiatingResponseDTO();
        verificationInitiatingResponseDTO.setCode(code);
        verificationInitiatingResponseDTO.setLink(getLink(ClaimVerificationEndpointConstants.API_URI_EP_VALIDATE));

        return verificationInitiatingResponseDTO;
    }

    private static LinkDTO getLink (String rel){

        LinkDTO linkDTO = new LinkDTO();
        linkDTO.setRel(rel);
        linkDTO.setUri(ClaimVerificationEndpointConstants.API_URI);

        return linkDTO;
    }

    private static LinkDTO getEmptyLink (){

        LinkDTO linkDTO = new LinkDTO();
        linkDTO.setRel(StringUtils.EMPTY);
        linkDTO.setUri(StringUtils.EMPTY);

        return linkDTO;
    }

    private static PropertyDTO getProperty(String key, String value){
        PropertyDTO property = new PropertyDTO();
        property.setKey(key);
        property.setValue(value);

        return property;
    }


//    public static boolean isUserExisting(int tenantId, String username){
//        boolean isUserExisting = false;
//
//        try {
//            UserStoreManager userStoreManager = getUserStoreManager(tenantId);
//            userList = userStoreManager.isExistingUser(username);
//            return userList;
//        } catch (UserStoreException e) {
//            String msg = "Error retrieving the user-list for the tenant : " + tenantId;
//            LOG.error(msg, e);
//            handleInternalServerError(ClaimVerificationCoreConstants
//                    .ErrorMessages.ERROR_CODE_UNEXPECTED.getCode(), msg);
//        }
//
//        return isUserExisting;
//    }

    public static String[] getUserList(int tenantId, String username) {

        String[] userList = null;

        try {
            UserStoreManager userStoreManager = getUserStoreManager(tenantId);
            userList = userStoreManager.listUsers(username , 2);
            return userList;
        } catch (UserStoreException e) {
            String msg = "Error retrieving the user-list for the tenant : " + tenantId;
            LOG.error(msg, e);
            handleInternalServerError(ClaimVerificationEndpointConstants.ERROR_CODE_UNEXPECTED_ERROR, msg);
        }

        return userList;
    }

    private static UserStoreManager getUserStoreManager(int tenantId){
        UserStoreManager userStoreManager = null;
        RealmService realmService = IdentityRecoveryServiceDataHolder.getInstance().getRealmService();
        final String userStoreRetrievalExceptionMsg = "Error retrieving UserStoreManager for tenant : " + tenantId;
        try {
            if (realmService.getTenantUserRealm(tenantId) != null) {
                userStoreManager = (UserStoreManager) realmService.getTenantUserRealm(tenantId).getUserStoreManager();
                if (userStoreManager == null){
                    throw new UserStoreException(userStoreRetrievalExceptionMsg);
                }
                return userStoreManager;
            }else {
                throw new UserStoreException(userStoreRetrievalExceptionMsg);
            }
        } catch (UserStoreException e) {
            LOG.error(userStoreRetrievalExceptionMsg, e);
            handleInternalServerError(ClaimVerificationEndpointConstants.ERROR_CODE_UNEXPECTED_ERROR,
                    userStoreRetrievalExceptionMsg);
        }

        return userStoreManager;
    }

    public static ErrorDTO getErrorResponse(String code, String message, String description) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setCode(code);
        errorDTO.setMessage(message);
        errorDTO.setDescription(description);

        return errorDTO;
    }

    public static void handleBadRequest(String code, String description){
        throw new BadRequestException(
                ClaimVerificationEndpointUtils.getErrorResponse(code,
                        ClaimVerificationEndpointConstants.STATUS_BAD_REQUEST_MESSAGE_DEFAULT, description)
        );
    }

    public static void handleInternalServerError(String code, String description) {
//        InternalServerErrorException internalServerErrorException = buildInternalServerErrorException(code);
//        if (throwable == null) {
//            log.error(msg);
//        } else {
//            log.error(msg, throwable);
//        }
//        throw internalServerErrorException;

        throw new InternalServerErrorException(
                ClaimVerificationEndpointUtils.getErrorResponse(code,
                        ClaimVerificationEndpointConstants.STATUS_INTERNAL_SERVER_ERROR_MESSAGE_DEFAULT, description)
        );
    }

//    public static List<PropertyDTO> getPropertyListFromMap(Map<String, String> propertyMap){
//
//        List<PropertyDTO> propertyList = new ArrayList<>();
//
//        propertyMap.forEach((k,v)-> propertyList.add(new PropertyDTO(k,v)));
//
//        return propertyList;
//    }

}

