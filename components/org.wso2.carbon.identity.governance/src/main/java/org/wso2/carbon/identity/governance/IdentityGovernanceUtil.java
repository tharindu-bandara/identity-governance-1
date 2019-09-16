/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.identity.governance;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.application.common.model.FederatedAuthenticatorConfig;
import org.wso2.carbon.identity.application.common.model.IdentityProvider;
import org.wso2.carbon.identity.application.common.model.IdentityProviderProperty;
import org.wso2.carbon.identity.application.common.util.IdentityApplicationConstants;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.event.IdentityEventConstants;
import org.wso2.carbon.identity.governance.common.IdentityConnectorConfig;
import org.wso2.carbon.identity.governance.internal.IdentityMgtServiceDataHolder;
import org.wso2.carbon.idp.mgt.IdentityProviderManagementException;
import org.wso2.carbon.idp.mgt.IdpManager;
import org.wso2.carbon.user.api.UserStoreManager;
import org.wso2.carbon.user.core.UserCoreConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class IdentityGovernanceUtil {

    private static final Log log = LogFactory.getLog(IdentityGovernanceUtil.class);

    public static void saveConnectorDefaultProperties(IdentityConnectorConfig identityConnectorConfig,
                                                      String tenantDomain) throws IdentityGovernanceException {

        IdpManager identityProviderManager = IdentityMgtServiceDataHolder.getInstance().getIdpManager();

        try {
            IdentityProvider residentIdp = identityProviderManager.getResidentIdP(tenantDomain);
            IdentityProviderProperty[] idpProperties = residentIdp.getIdpProperties();
            String[] connectorPropertiesNames = identityConnectorConfig.getPropertyNames();
            List<IdentityProviderProperty> propertiesToAdd = new ArrayList<>();
            for (String connectorPropertyName : connectorPropertiesNames) {
                boolean propertyExists = false;
                for (IdentityProviderProperty property : idpProperties) {
                    if (connectorPropertyName.equals(property.getName())) {
                        propertyExists = true;
                        break;
                    }
                }

                if (!propertyExists) {
                    IdentityProviderProperty newProperty = new IdentityProviderProperty();
                    newProperty.setName(connectorPropertyName);
                    newProperty.setDisplayName(
                            identityConnectorConfig.getPropertyNameMapping().get(connectorPropertyName));
                    Properties defaultPropertyValues = identityConnectorConfig.getDefaultPropertyValues(tenantDomain);
                    newProperty.setValue(String.valueOf(defaultPropertyValues.get(connectorPropertyName)));
                    propertiesToAdd.add(newProperty);
                }
            }

            // If the property list size is greater than 0, add the new properties to the database.
            if (propertiesToAdd.size() > 0) {
                IdentityProviderProperty property = new IdentityProviderProperty();
                property.setName(identityConnectorConfig.getName() + "." + IdentityEventConstants.PropertyConfig
                        .ALREADY_WRITTEN_PROPERTY_KEY);
                property.setValue(IdentityEventConstants.PropertyConfig.ALREADY_WRITTEN_PROPERTY_VALUE);
                propertiesToAdd.add(property);
                propertiesToAdd.addAll(Arrays.asList(idpProperties));
                residentIdp.setIdpProperties(propertiesToAdd.toArray(new IdentityProviderProperty[0]));
                FederatedAuthenticatorConfig[] authenticatorConfigs = residentIdp.getFederatedAuthenticatorConfigs();
                List<FederatedAuthenticatorConfig> configsToSave = new ArrayList<>();
                for (FederatedAuthenticatorConfig authenticatorConfig : authenticatorConfigs) {
                    if (IdentityApplicationConstants.Authenticator.PassiveSTS.NAME.equals(authenticatorConfig.getName
                            ()) || IdentityApplicationConstants.Authenticator.SAML2SSO.NAME.equals(authenticatorConfig
                            .getName())) {
                        configsToSave.add(authenticatorConfig);
                    }
                }
                residentIdp.setFederatedAuthenticatorConfigs(configsToSave.toArray(new FederatedAuthenticatorConfig[0]));

                identityProviderManager.updateResidentIdP(residentIdp, tenantDomain);
                if (log.isDebugEnabled()) {
                    log.debug("New resident IDP properties for tenant : " + tenantDomain + " written to database");
                }
            }

        } catch (IdentityProviderManagementException e) {
            log.error("Error while adding identity management properties to resident Idp.", e);
        }

    }

    public static String getUserStoreDomainName(UserStoreManager userStoreManager) {
        String domainNameProperty = null;
        if(userStoreManager instanceof org.wso2.carbon.user.core.UserStoreManager) {
            domainNameProperty = ((org.wso2.carbon.user.core.UserStoreManager)
                    userStoreManager).getRealmConfiguration()
                    .getUserStoreProperty(UserCoreConstants.RealmConfig.PROPERTY_DOMAIN_NAME);
            if(StringUtils.isBlank(domainNameProperty)) {
                domainNameProperty = IdentityUtil.getPrimaryDomainName();
            }
        }
        return domainNameProperty;
    }

}
