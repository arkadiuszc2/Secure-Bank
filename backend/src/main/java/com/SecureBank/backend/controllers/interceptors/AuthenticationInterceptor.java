package com.SecureBank.backend.controllers.interceptors;

import com.SecureBank.backend.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {


  private final AuthenticationService authenticationService;
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{

    boolean isAuthenticated = authenticationService.checkIfUserAuthenticated(request);

    if ( !isAuthenticated ) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      throw new RuntimeException("you have to login to see this content!");
    }

    return isAuthenticated;
  }

}
