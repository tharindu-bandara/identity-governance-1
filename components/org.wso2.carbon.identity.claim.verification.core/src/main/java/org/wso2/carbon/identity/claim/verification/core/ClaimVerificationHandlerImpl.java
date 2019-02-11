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

package org.wso2.carbon.identity.claim.verification.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.claim.verification.core.exception.ClaimVerificationException;
import org.wso2.carbon.identity.claim.verification.core.internal.ClaimVerificationServiceDataHolder;
import org.wso2.carbon.identity.claim.verification.core.model.Claim;
import org.wso2.carbon.identity.claim.verification.core.model.User;
import org.wso2.carbon.identity.claim.verification.core.model.ValidationResponse;
import org.wso2.carbon.identity.claim.verification.core.verifier.ClaimVerifier;

import java.util.Map;

public class ClaimVerificationHandlerImpl implements ClaimVerificationHandler {

    private static final Log LOG = LogFactory.getLog(ClaimVerificationHandlerImpl.class);


    @Override
    public String initVerification(User user, Claim claim, String verificationMethod, Map<String, String> properties) throws ClaimVerificationException {
        //check is verifiable claim?

        //check if its a retry
        // if retry update confirmation code

        //use getverifier


        //get code expiry time
        //create db entry

        //notify method

        //send back code



        return null;
    }


    @Override
    public ValidationResponse validateClaim (String code, Map<String, String> properties) throws ClaimVerificationException{

        return validateClaim(code, properties, false);
    }

    @Override
    public ValidationResponse validateClaim(String code, Map<String, String> properties, boolean isAdditionalValidationRequired) throws ClaimVerificationException {

        //send the confirmation code if validation success
        // throw error exception if verification fails


        return null;
    }

    @Override
    public void confirmVerification (String code, boolean isValidationSuccess) throws ClaimVerificationException {

        // verify code
        // call finalizer
    }

    private ClaimVerifier getClaimVerifier(String verificationMethod){
        return ClaimVerificationServiceDataHolder.getInstance().getClaimVerifiers().get(0);
    }

}
