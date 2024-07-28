//package com.deyvisonborges.eshop.oauth_client;
//
//import java.util.Collections;
//import java.util.Map;
//
//import jakarta.servlet.http.HttpSession;
//import org.springframework.cloud.context.config.annotation.RefreshScope;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
//import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
//import org.springframework.security.oauth2.core.oidc.user.OidcUser;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.client.RestTemplate;
//
//@RestController
//public class ClientController {
//  private final RestTemplate restTemplate;
//
//  public ClientController(RestTemplate restTemplate) {
//    this.restTemplate = restTemplate;
//  }
//
//  @GetMapping
//  public ResponseEntity<String> getClient(
//          final @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient client,
//          final @AuthenticationPrincipal OidcUser oidcUser) {
//    return ResponseEntity.ok(String.format("""
//        <h2>Access token: %s</h2>
//        <h2>Refresh token: %s</h2>
//        <h2>ID token: %s</h2>
//        <h2>Claims: %s</h2>
//        """,
//            client.getAccessToken().getTokenValue(),
//            client.getRefreshToken() != null ? client.getRefreshToken().getTokenValue() : "N/A",
//            oidcUser.getIdToken().getTokenValue(),
//            oidcUser.getClaims()
//    ));
//  }
//
//  @GetMapping("/authorized")
//  public Map<String, String> authorize(@RequestParam String code, HttpSession session) {
//    session.setAttribute("authorizationCode", code);
//    return Collections.singletonMap("authorizationCode", code);
//  }
//
//  @GetMapping("/orders")
//  @RefreshScope
//  public ResponseEntity<?> getResourceServer(
//          @RegisteredOAuth2AuthorizedClient("client-server-oidc") OAuth2AuthorizedClient oauthClient,
//          HttpSession session) {
//    String url = "http://127.0.0.1:8084/orders";
//    HttpHeaders headers = new HttpHeaders();
//    System.out.println(oauthClient.getAccessToken().getTokenValue());
//    headers.set("Authorization", "Bearer " + oauthClient.getAccessToken().getTokenValue());
//
//    HttpEntity<?> entity = new HttpEntity<>(headers);
//
//    ResponseEntity<String> response = restTemplate.exchange(
//            url,
//            HttpMethod.GET,
//            entity,
//            String.class);
//
//    session.setAttribute("ordersData", response.getBody());
//    return ResponseEntity.ok(response.getBody());
//  }
//
//  @GetMapping("/fetch-orders")
//  public ResponseEntity<?> fetchOrders(HttpSession session) {
//    String ordersData = (String) session.getAttribute("ordersData");
//    if (ordersData == null) {
//      return ResponseEntity.status(403).body("Access denied or no data available.");
//    }
//    return ResponseEntity.ok(ordersData);
//  }
//}
//


package com.deyvisonborges.eshop.oauth_client;

import java.util.Map;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ClientController {
  private final RestTemplate restTemplate;

  @Value("${spring.security.oauth2.client.registration.client-server-oidc.client-id}")
  private String clientId;

  @Value("${spring.security.oauth2.client.registration.client-server-oidc.client-secret}")
  private String clientSecret;

  @Value("${spring.security.oauth2.client.registration.client-server-oidc.redirect-uri}")
  private String redirectUri;

  public ClientController(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @GetMapping
  public ResponseEntity<String> getClient(
          final @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient client,
          final @AuthenticationPrincipal OidcUser oidcUser) {
    return ResponseEntity.ok(String.format("""
        <h2>Access token: %s</h2>
        <h2>Refresh token: %s</h2>
        <h2>ID token: %s</h2>
        <h2>Claims: %s</h2>
        """,
            client.getAccessToken().getTokenValue(),
            client.getRefreshToken() != null ? client.getRefreshToken().getTokenValue() : "N/A",
            oidcUser.getIdToken().getTokenValue(),
            oidcUser.getClaims()
    ));
  }

  @GetMapping("/authorized")
  public ResponseEntity<?> authorize(@RequestParam String code, HttpSession session) {
    // Trocar código de autorização por token de acesso
    String tokenUrl = "http://localhost:9000/oauth2/token";
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED);

    String body = "grant_type=authorization_code"
            + "&code=" + code
            + "&redirect_uri=" + redirectUri
            + "&client_id=" + clientId
            + "&client_secret=" + clientSecret;

    HttpEntity<String> entity = new HttpEntity<>(body, headers);

    ResponseEntity<Map> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, entity, Map.class);
    Map<String, String> tokenResponse = response.getBody();

    if (tokenResponse != null && tokenResponse.containsKey("access_token")) {
      session.setAttribute("accessToken", tokenResponse.get("access_token"));
      return ResponseEntity.ok("Authorized");
    }

    return ResponseEntity.status(403).body("Failed to authorize");
  }

  @GetMapping("/orders")
  public ResponseEntity<?> getResourceServer(final @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient client, HttpSession session) {
    String url = "http://127.0.0.1:8084/orders";
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<?> entity = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

    return ResponseEntity.ok(response.getBody());
  }
}
