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
import org.wso2.carbon.identity.claim.verification.core.verifier.impl.EmailClaimVerifier;
import org.wso2.carbon.identity.event.services.IdentityEventService;
import org.wso2.carbon.user.core.service.RealmService;

/**
 * OSGi declarative services component which handles registration and un-registration of configuration management
 * service.
 */
@Component(
        name = "org.wso2.carbon.identity.claim.verification.service",
        immediate = true
)
public class ClaimVerificationServiceComponent {

    private static final Log LOG = LogFactory.getLog(ClaimVerificationServiceComponent.class);


    /**
     * Register ClaimVerificationHandler as an OSGI service.
     *
     * @param componentContext OSGI service component context.
     */
    @Activate
    protected void activate(ComponentContext componentContext) {

        try {
            BundleContext bundleContext = componentContext.getBundleContext();

            bundleContext.registerService(ClaimVerifier.class.getName(), new EmailClaimVerifier(),
                    null);

            bundleContext.registerService(ClaimVerificationHandler.class.getName(),
                    new ClaimVerificationHandlerImpl() {
                    }, null);

        } catch (Throwable e) {
            LOG.error("Error while activating ClaimVerificationServiceComponent.", e);
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
            if (LOG.isDebugEnabled()) {
                LOG.debug("Claim verifier:" + claimVerifier.getClass().getSimpleName() + " is registered in claim " +
                        "verification service.");
            }

            ClaimVerificationServiceDataHolder.getInstance().getClaimVerifiers().add(claimVerifier);
        }
    }

    protected void unsetClaimVerifier(ClaimVerifier claimVerifier) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Claim verifier:" + claimVerifier.getClass().getSimpleName() + " is unregistered in claim " +
                    "verification service.");
        }
        ClaimVerificationServiceDataHolder.getInstance().getClaimVerifiers().remove(claimVerifier);
    }

     @Reference(
             name = "IdentityEventService",
             service = org.wso2.carbon.identity.event.services.IdentityEventService.class,
             cardinality = ReferenceCardinality.MANDATORY,
             policy = ReferencePolicy.DYNAMIC,
             unbind = "unsetIdentityEventService"
     )
    protected void setIdentityEventService(IdentityEventService identityEventService) {
        ClaimVerificationServiceDataHolder.getInstance().setIdentityEventService(identityEventService);
    }

    protected void unsetIdentityEventService(IdentityEventService identityEventService) {
        ClaimVerificationServiceDataHolder.getInstance().setIdentityEventService(null);
    }

    @Reference(
            name = "realm.service",
            service = org.wso2.carbon.user.core.service.RealmService.class,
            cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unsetRealmService"
    )
    protected void setRealmService(RealmService realmService) {

        ClaimVerificationServiceDataHolder.getInstance().setRealmService(realmService);
//        if (log.isDebugEnabled()) {
//            log.debug("RealmService is set in the User Store Count bundle");
//        }
    }

    protected void unsetRealmService(RealmService realmService) {

        ClaimVerificationServiceDataHolder.getInstance().setRealmService(null);
//        if (log.isDebugEnabled()) {
//            log.debug("RealmService is unset in the Application Authentication Framework bundle");
//        }
    }

}
