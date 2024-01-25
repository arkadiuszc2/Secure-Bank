package com.SecureBank.backend.services;

import com.SecureBank.backend.entities.RequestCounter;
import com.SecureBank.backend.repositiories.RequestCounterRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginRequestLimiter {

  private final RequestCounterRepository requestCounterRepository;

  public final int MAX_REQUEST_NUMBER = 10;
  public final int REQUEST_PERIOD_TIME = 60;

  public boolean isIpAllowedToMakeRequest(String ipAddress){
    boolean isAllowed = true;
    RequestCounter requestCounter = requestCounterRepository.findByIpAddress(ipAddress);

    if(requestCounter==null) {
      throw new RuntimeException("Counting requests from remote address error");
    }

    LocalDateTime reqPeriodStartDate = requestCounter.getReqPeriodStartDate();
    int requestNumber = requestCounter.getRequestNumber();

    if(Duration.between(LocalDateTime.now(), reqPeriodStartDate).toSeconds() < REQUEST_PERIOD_TIME && requestNumber > MAX_REQUEST_NUMBER){
        isAllowed = false;
    }

    return isAllowed;

  }

  public void saveRequestFromIp(String ipAddress){
    RequestCounter requestCounter = requestCounterRepository.findByIpAddress(ipAddress);

    if(requestCounter != null) {
      requestCounter.setRequestNumber(requestCounter.getRequestNumber()+1);

      if(Duration.between(LocalDateTime.now(), requestCounter.getReqPeriodStartDate()).toSeconds() > REQUEST_PERIOD_TIME){
        requestCounter.setReqPeriodStartDate(LocalDateTime.now());
        requestCounter.setRequestNumber(1);
      }

    } else {
      requestCounter = new RequestCounter(0, ipAddress, 1, LocalDateTime.now());
    }

    requestCounterRepository.save(requestCounter);

  }

}
