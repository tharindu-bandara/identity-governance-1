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

package org.wso2.carbon.identity.claim.verification.core.util;

public class ClaimVerificationCoreConstants {

    private ClaimVerificationCoreConstants(){}

    public static final String SCENARIO_CLAIM_VALIDATION = "CLAIM_VALIDATION";
    public static final String SCENARIO_CLAIM_CONFIRMATION = "CLAIM_CONFIRMATION";
    public static final String SCENARIO_EMAIL_CLAIM_VERIFICATION = "EMAIL_CLAIM_VERIFICATION";
    public static final String PROP_IS_RETRY_ATTEMPT = "IS_RETRY_ATTEMPT";

    public enum ErrorMessages {

        ERROR_CODE_UNEXPECTED("18013", "Unexpected error"),
        ERROR_CODE_INVALID_CONFIRMATION_CODE("18013", "Unexpected error"),
        ERROR_CODE_EXPIRED_CONFIRMATION_CODE("18013", "Unexpected error"),
        ERROR_CODE_STORING_DATA("18013", "Error when storing data."),
        ERROR_CODE_INVALIDATING_CODE("18013", "Error when invalidating confirmation code.");

        private final String code;
        private final String message;

        ErrorMessages(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public String toString() {
            return code + " - " + message;
        }

    }

    public enum Scenarios {

        CLAIM_VALIDATION,
        CLAIM_CONFIRMATION,
        EMAIL_VERIFICATION

    }

    public static class SQLQueries {

        public static final String STORE_RECOVERY_DATA = "INSERT INTO IDN_RECOVERY_DATA "
                + "(USER_NAME, USER_DOMAIN, TENANT_ID, CODE, SCENARIO,STEP, TIME_CREATED)"
                + "VALUES (?,?,?,?,?,?,?)";
        public static final String LOAD_RECOVERY_DATA = "SELECT "
                + "* FROM IDN_RECOVERY_DATA WHERE USER_NAME = ? AND USER_DOMAIN = ? AND TENANT_ID = ? AND CODE = ? AND " +
                "SCENARIO = ? AND STEP = ?";

        public static final String LOAD_RECOVERY_DATA_CASE_INSENSITIVE = "SELECT * FROM IDN_RECOVERY_DATA WHERE" +
                " LOWER(USER_NAME)=LOWER(?) AND USER_DOMAIN = ? AND TENANT_ID = ? AND CODE= ? AND SCENARIO = ? AND " +
                "STEP = ?";

        public static final String LOAD_RECOVERY_DATA_FROM_CODE = "SELECT * FROM IDN_RECOVERY_DATA WHERE CODE = ?";


        public static final String INVALIDATE_CODE_USING_CODE = "DELETE FROM IDN_RECOVERY_DATA WHERE CODE = ?";

        public static final String INVALIDATE_CODE = "DELETE FROM IDN_RECOVERY_DATA WHERE USER_NAME = ? AND " +
                "USER_DOMAIN = ? AND TENANT_ID =? AND SCENARIO =? AND STEP =?";

        public static final String INVALIDATE_CODE_CASE_INSENSITIVE = "DELETE FROM IDN_RECOVERY_DATA WHERE " +
                "LOWER(USER_NAME)=LOWER(?) AND USER_DOMAIN = ? AND TENANT_ID =? AND SCENARIO =? AND STEP =?";

        public static final String LOAD_RECOVERY_DATA_OF_USER = "SELECT "
                + "* FROM IDN_RECOVERY_DATA WHERE USER_NAME = ? AND USER_DOMAIN = ? AND TENANT_ID = ?";

        public static final String LOAD_RECOVERY_DATA_OF_USER_CASE_INSENSITIVE = "SELECT "
                + "* FROM IDN_RECOVERY_DATA WHERE LOWER(USER_NAME)=LOWER(?) AND USER_DOMAIN = ? AND TENANT_ID = ?";

    }
}
