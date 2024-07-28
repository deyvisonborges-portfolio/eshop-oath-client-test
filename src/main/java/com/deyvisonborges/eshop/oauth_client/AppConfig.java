package com.deyvisonborges.eshop.oauth_client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

  @Value("${rsa.public}")
  private String publicKey;

  @Bean
  RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
