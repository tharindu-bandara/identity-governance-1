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

package org.wso2.carbon.identity.claim.verification.core.store;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.claim.verification.core.exception.ClaimVerificationException;
import org.wso2.carbon.identity.claim.verification.core.model.ConfirmationCodeData;
import org.wso2.carbon.identity.claim.verification.core.model.User;
import org.wso2.carbon.identity.claim.verification.core.util.ClaimVerificationCoreConstants;
import org.wso2.carbon.identity.core.util.IdentityDatabaseUtil;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.identity.core.util.IdentityUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class JDBCClaimVerificationStore implements ClaimVerificationStore {

    private static final Log LOG = LogFactory.getLog(JDBCClaimVerificationStore.class);

    private static ClaimVerificationStore jdbcClaimVerificationStore = new JDBCClaimVerificationStore();


    private JDBCClaimVerificationStore() {

    }

    public static ClaimVerificationStore getInstance() {
        return jdbcClaimVerificationStore;
    }

    @Override
    public void storeConfirmationCode (ConfirmationCodeData codeData) throws ClaimVerificationException {
        Connection connection = IdentityDatabaseUtil.getDBConnection();
        PreparedStatement prepStmt = null;
        try {
            prepStmt = connection.prepareStatement(ClaimVerificationCoreConstants.SQLQueries.STORE_RECOVERY_DATA);
            prepStmt.setString(1, codeData.getUser().getUsername());
            prepStmt.setString(2, codeData.getUser().getRealm().toUpperCase());
            prepStmt.setInt(3, IdentityTenantUtil.getTenantId(codeData.getUser().getTenantDomain()));
            prepStmt.setString(4, codeData.getCode());
            prepStmt.setString(5, codeData.getScenario());
            prepStmt.setString(6, String.valueOf(codeData.getStep()));
            prepStmt.setTimestamp(7, new Timestamp(new Date().getTime()));
//            prepStmt.setString(8, recoveryDataDO.getRemainingSetIds());
            prepStmt.execute();
            connection.setAutoCommit(false);
            connection.commit();
        } catch (SQLException e) {
            LOG.error(ClaimVerificationCoreConstants.ErrorMessages.ERROR_CODE_STORING_DATA.getMessage(),
                    e);
            throw new ClaimVerificationException(
                    ClaimVerificationCoreConstants.ErrorMessages.ERROR_CODE_STORING_DATA.getCode(),
                    ClaimVerificationCoreConstants.ErrorMessages.ERROR_CODE_STORING_DATA.getMessage(), e);
        } finally {
            IdentityDatabaseUtil.closeStatement(prepStmt);
            IdentityDatabaseUtil.closeConnection(connection);
        }
    }

    ConfirmationCodeData load (String code) {
        return null;
    }

    ConfirmationCodeData load (User user) {
        return null;
    }

    @Override
    public void invalidateConfirmationCode (ConfirmationCodeData codeData) throws ClaimVerificationException {
        PreparedStatement prepStmt = null;
        Connection connection = IdentityDatabaseUtil.getDBConnection();
        try {
            String sql;
            if (IdentityUtil.isUserStoreCaseSensitive(codeData.getUser().getRealm(),
                    IdentityTenantUtil.getTenantId(codeData.getUser().getTenantDomain()))) {
                sql = ClaimVerificationCoreConstants.SQLQueries.INVALIDATE_CODE;
            } else {
                sql = ClaimVerificationCoreConstants.SQLQueries.INVALIDATE_CODE_CASE_INSENSITIVE;
            }

            prepStmt = connection.prepareStatement(sql);
            prepStmt.setString(1, codeData.getUser().getUsername());
            prepStmt.setString(2, codeData.getUser().getRealm());
            prepStmt.setInt(3, IdentityTenantUtil.getTenantId(codeData.getUser().getTenantDomain()));
            prepStmt.setString(4, codeData.getScenario());
            prepStmt.setString(5, String.valueOf(codeData.getStep()));
            prepStmt.execute();
            connection.commit();
        } catch (SQLException e) {
            LOG.error(ClaimVerificationCoreConstants.ErrorMessages.ERROR_CODE_INVALIDATING_CODE.getMessage(),
                    e);
            throw new ClaimVerificationException(
                    ClaimVerificationCoreConstants.ErrorMessages.ERROR_CODE_INVALIDATING_CODE.getCode(),
                    ClaimVerificationCoreConstants.ErrorMessages.ERROR_CODE_INVALIDATING_CODE.getMessage(), e);
        } finally {
            IdentityDatabaseUtil.closeStatement(prepStmt);
            IdentityDatabaseUtil.closeConnection(connection);
        }
    }

    boolean isConfirmationCodeExisting (ConfirmationCodeData codeData) {
        return false;
    }

    boolean isClaimDataExisting (/*claim data*/) {
        return false;
    }
    //store claim details - calls store confirmation code
}
