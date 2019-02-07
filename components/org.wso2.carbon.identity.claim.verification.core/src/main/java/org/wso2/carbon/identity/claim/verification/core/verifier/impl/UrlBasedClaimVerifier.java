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

import org.wso2.carbon.identity.claim.verification.core.exception.ClaimVerificationException;
import org.wso2.carbon.identity.claim.verification.core.model.Claim;
import org.wso2.carbon.identity.claim.verification.core.verifier.ClaimVerifier;

import javax.ws.rs.core.MultivaluedMap;

public class UrlBasedClaimVerifier implements ClaimVerifier {

    @Override
    public void sendNotification(Claim claim) throws ClaimVerificationException {

    }

    @Override
    public boolean isVerified(MultivaluedMap properties) throws ClaimVerificationException {

        return false;
    }

    @Override
    public boolean canHandle(String requestedVerifier) throws ClaimVerificationException {

        return false;
    }

    @Override
    public String getConfirmationCodeExpiryTime() throws ClaimVerificationException {

        return null;
    }
}
