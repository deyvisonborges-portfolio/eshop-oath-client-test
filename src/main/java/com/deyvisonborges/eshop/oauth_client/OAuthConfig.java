//package com.deyvisonborges.eshop.oauth_client;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.oauth2.client.registration.ClientRegistration;
//import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
//import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
//import org.springframework.security.oauth2.core.AuthorizationGrantType;
//
//import java.util.UUID;
//
//@Configuration
//public class OAuthConfig {
//  private static final String AUTHSERVER_URI = "http://localhost:9000";
//
//  @Bean
//  ClientRegistrationRepository clientRegistrationRepository() {
//    final ClientRegistration gatewayClient = ClientRegistration
//      .withRegistrationId(UUID.randomUUID().toString())
//      .clientId("client-server-id")
//      .clientSecret("{noop}secret")
//      .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//      .redirectUri("http://127.0.0.1:8084/login/oauth2/code/{registrationId}")
//      .scope("read", "write")
//      .authorizationUri( AUTHSERVER_URI + "/oauth2/authorize")
//      .tokenUri(AUTHSERVER_URI + "/oauth2/token")
//      .userInfoUri(AUTHSERVER_URI + "/userinfo")
//      .userNameAttributeName("id")
//      .clientName("Eshop Gateway")
//      .build();
//    return new InMemoryClientRegistrationRepository(gatewayClient);
//  }
//}
