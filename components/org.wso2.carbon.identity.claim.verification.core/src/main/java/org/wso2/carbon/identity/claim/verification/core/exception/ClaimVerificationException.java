/*
 *  Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.claim.verification.core.exception;

/**
 * Base exception for claim verification feature.
 */
public class ClaimVerificationException extends Exception {

    private String errorCode;


    public ClaimVerificationException() {

        super();
    }


    public ClaimVerificationException(Throwable cause) {

        super(cause);
    }

    /**
     * Constructs a new exception with the specified message.
     *
     * @param message the detailed message
     */
    public ClaimVerificationException(String message) {

        super(message);
    }

    /**
     * Constructs a new exception with the specified message and cause.
     *
     * @param message the detailed message
     * @param cause the cause
     */
    public ClaimVerificationException(String message, Throwable cause) {

        super(message, cause);
    }

    /**
     * Constructs a new exception with the specified message and the error code.
     *
     * @param message the detailed message
     * @param errorCode error code
     */
    public ClaimVerificationException(String message, String errorCode) {

        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Constructs a new exception with the specified message, cause and the error code.
     *
     * @param message the detailed message
     * @param cause the cause
     * @param errorCode error code
     */
    public ClaimVerificationException(String message, Throwable cause, String errorCode) {

        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * Returns the error code specified at the construct of the exception.
     *
     * @return error code specified
     */
    public String getErrorCode() {

        return errorCode;
    }
}
