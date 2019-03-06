package org.wso2.carbon.identity.claim.verification.endpoint.factories;

import org.wso2.carbon.identity.claim.verification.endpoint.InitVerificationApiService;
import org.wso2.carbon.identity.claim.verification.endpoint.impl.InitVerificationApiServiceImpl;

public class InitVerificationApiServiceFactory {

   private final static InitVerificationApiService service = new InitVerificationApiServiceImpl();

   public static InitVerificationApiService getInitVerificationApi()
   {
      return service;
   }
}
