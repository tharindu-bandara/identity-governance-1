package org.wso2.carbon.identity.claim.verification.endpoint;

import org.wso2.carbon.identity.claim.verification.endpoint.*;
import org.wso2.carbon.identity.claim.verification.endpoint.dto.*;

import org.wso2.carbon.identity.claim.verification.endpoint.dto.ErrorDTO;
import org.wso2.carbon.identity.claim.verification.endpoint.dto.VerificationInitiatingRequestDTO;
import org.wso2.carbon.identity.claim.verification.endpoint.dto.VerificationInitiatingResponseDTO;

import java.util.List;

import java.io.InputStream;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;

import javax.ws.rs.core.Response;

public abstract class InitApiService {
    public abstract Response initPost(VerificationInitiatingRequestDTO verificationInitiatingRequest);
}

