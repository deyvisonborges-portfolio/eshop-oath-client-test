//package com.deyvisonborges.eshop.oauth_client;
//
//import java.security.KeyFactory;
//import java.security.spec.X509EncodedKeySpec;
//import java.security.interfaces.RSAPublicKey;
//import java.util.Base64;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.oauth2.jwt.JwtDecoder;
//import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
//
//@Configuration
//public class TokenStoreConfig {
//  @Value("${rsa.public}")
//  private String publicKey;
//
//  @Bean
//  RSAPublicKey rsaPublicKey() throws Exception {
//    String publicKeyPEM = publicKey.replaceAll("-----BEGIN PUBLIC KEY-----", "").replaceAll("-----END PUBLIC KEY-----", "").replaceAll("\\s", "");
//    byte[] decoded = Base64.getDecoder().decode(publicKeyPEM);
//    X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
//    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//    RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(spec);
//    System.out.println("Client Service - Public Key: " + this.publicKey);
//    return publicKey;
//  }
//
//  @Bean
//  JwtDecoder jwtDecoder() throws Exception {
//    RSAPublicKey publicKey = rsaPublicKey();
//
//    System.out.println(publicKey);
//    return NimbusJwtDecoder.withPublicKey(publicKey).build();
//  }
//}
