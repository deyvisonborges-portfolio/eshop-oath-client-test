package com.deyvisonborges.eshop.oauth_client;

import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class TokenStoreConfig {

  private static final Logger logger = LoggerFactory.getLogger(TokenStoreConfig.class);

  @Value("${rsa.public}")
  private String publicRsaKey;

  @Bean
  SecurityFilterChain chain(final HttpSecurity http) throws Exception {
    http
      .authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated())
      .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {
        try {
          jwt.decoder(jwtDecoder());
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }));
    return http.build();
  }

  @Bean
  public JwtDecoder jwtDecoder() throws Exception {
    byte[] decoded = Base64.getDecoder().decode(publicRsaKey);
    X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(spec);
    return NimbusJwtDecoder.withPublicKey(publicKey).build();
  }
}

