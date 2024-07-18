package com.deyvisonborges.eshop.oauth_client;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ClientController {
  RestTemplate restTemplate;

  public ClientController(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @SuppressWarnings("null")
  @GetMapping
  public ResponseEntity<String> getClient(
      final @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient client,
      final @AuthenticationPrincipal OidcUser oidcUser) {
    return ResponseEntity.ok("""
        <h2>Access token: %s</h2>
        <h2>Refresh token: %s</h2>
        <h2>ID token: %s</h2>
        <h2>Claims: %s</h2>
        """.formatted(
        client.getAccessToken().getTokenValue(),
        client.getRefreshToken().getTokenValue(),
        oidcUser.getIdToken().getTokenValue(),
        oidcUser.getClaims()

    ));
  }

  @GetMapping("/orders")
  public ResponseEntity<?> getResourceServer(
      @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient oauthClient) {
    String url = "http://localhost:8081/orders";
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + oauthClient.getAccessToken().getTokenValue());

    HttpEntity<String> entity = new HttpEntity<>(headers);

    @SuppressWarnings("rawtypes")
    ResponseEntity<Map> response = restTemplate.exchange(
        url,
        HttpMethod.GET,
        entity,
        Map.class);

    return ResponseEntity.ok(response.getBody());
  }

}
