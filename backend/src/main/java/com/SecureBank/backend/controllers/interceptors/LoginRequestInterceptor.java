package com.SecureBank.backend.controllers.interceptors;

import com.SecureBank.backend.services.LoginRequestLimiter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class LoginRequestInterceptor implements HandlerInterceptor {

  private final LoginRequestLimiter loginRequestLimiter;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String ipAddress = request.getRemoteAddr();
    System.out.println(ipAddress);
    loginRequestLimiter.saveRequestFromIp(ipAddress);
    boolean isIpAllowedToMakeRequest = loginRequestLimiter.isIpAllowedToMakeRequest(ipAddress);

    if(!isIpAllowedToMakeRequest){
      throw new RuntimeException("Max login requests number (" + loginRequestLimiter.MAX_REQUEST_NUMBER + ") in " + loginRequestLimiter.REQUEST_PERIOD_TIME
          + "seconds exceeded! ");
    }

    return isIpAllowedToMakeRequest;
  }
}
