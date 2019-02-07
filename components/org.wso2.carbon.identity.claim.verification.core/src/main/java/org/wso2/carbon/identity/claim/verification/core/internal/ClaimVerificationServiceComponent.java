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

package org.wso2.carbon.identity.claim.verification.core.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.wso2.carbon.identity.claim.verification.core.ClaimVerificationHandler;
import org.wso2.carbon.identity.claim.verification.core.ClaimVerificationHandlerImpl;
import org.wso2.carbon.identity.claim.verification.core.verifier.ClaimVerifier;
import org.wso2.carbon.identity.claim.verification.core.verifier.impl.UrlBasedClaimVerifier;

import java.util.ArrayList;
import java.util.List;

/**
 * OSGi declarative services component which handles registration and un-registration of configuration management
 * service.
 */
@Component(
        name = "org.wso2.carbon.identity.claim.verification.service",
        immediate = true
)
public class ClaimVerificationServiceComponent {

    private static final Log log = LogFactory.getLog(ClaimVerificationServiceComponent.class);
    private List<ClaimVerifier> claimVerifiers = new ArrayList<>();

    /**
     * Register ClaimVerificationHandler as an OSGI service.
     *
     * @param componentContext OSGI service component context.
     */
    @Activate
    protected void activate(ComponentContext componentContext) {

        try {
            BundleContext bundleContext = componentContext.getBundleContext();

            bundleContext.registerService(ClaimVerifier.class.getName(), new UrlBasedClaimVerifier(),
                    null);

            ClaimVerificationServiceDataHolder claimVerificationServiceDataHolder =
                    new ClaimVerificationServiceDataHolder();
            claimVerificationServiceDataHolder.setClaimVerifiers(claimVerifiers);

            bundleContext.registerService(ClaimVerificationHandler.class.getName(),
                    new ClaimVerificationHandlerImpl(claimVerificationServiceDataHolder) {
                    }, null);

        } catch (Throwable e) {
            log.error("Error while activating ClaimVerificationServiceComponent.", e);
        }
    }

    @Reference(
            name = "claim.verifier",
            service = org.wso2.carbon.identity.claim.verification.core.verifier.ClaimVerifier.class,
            cardinality = ReferenceCardinality.MULTIPLE,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unsetClaimVerifier"
    )
    protected void setClaimVerifier(ClaimVerifier claimVerifier) {

        if (claimVerifier != null) {
            if (log.isDebugEnabled()) {
                log.debug("Claim verifier:" + claimVerifier.getClass().getSimpleName() + " is registered in claim " +
                        "verification service.");
            }

            this.claimVerifiers.add(claimVerifier);
        }
    }

    protected void unsetClaimVerifier(ClaimVerifier claimVerifier) {

        if (log.isDebugEnabled()) {
            log.debug("Claim verifier:" + claimVerifier.getClass().getSimpleName() + " is unregistered in claim " +
                    "verification service.");
        }
        this.claimVerifiers.remove(claimVerifier);
    }
}
