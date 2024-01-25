package com.SecureBank.backend.configurations;


import com.SecureBank.backend.controllers.interceptors.AuthenticationInterceptor;
import com.SecureBank.backend.controllers.interceptors.UnauthenticatedRequestInterceptor;
import com.SecureBank.backend.services.AuthenticationService;
import com.SecureBank.backend.services.LoginRequestLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

  private final AuthenticationService authenticationService;
  private final LoginRequestLimiter loginRequestLimiter;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new AuthenticationInterceptor(authenticationService)).addPathPatterns("/api/authenticated/**");
    registry.addInterceptor(new UnauthenticatedRequestInterceptor(loginRequestLimiter)).addPathPatterns("/api/unauthenticated/**");
  }
}

