package com.personalProject.reddit.security;

import com.personalProject.reddit.exceptionHandler.SpringRedditException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.time.Instant;
import java.util.Date;

import static io.jsonwebtoken.Jwts.parserBuilder;

@Service
@Slf4j
public class JwtProvider {
    private KeyStore keyStore;

    @Value("${jwt.expiration.time}")
    private Long jwtExpirationInMillis;

    @PostConstruct
    public void init(){
        try{
            keyStore = KeyStore.getInstance("JKS");
            InputStream resourceAsStream = getClass().getResourceAsStream("/springblog.jks");
            keyStore.load(resourceAsStream, "secret".toCharArray());
        }catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            throw new SpringRedditException("Exception occurred while loading keystore {}", e);
        }
    }


    public String generateToken(Authentication authentication){
        User principal = (User) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(principal.getUsername())
                .signWith(getPrivateKey())
                .setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationInMillis)))
                .compact();
    }

    public String generateTokenWithUserName(String userName){
        return Jwts.builder()
                .setSubject(userName)
                .setIssuedAt(Date.from(Instant.now()))
                .signWith(getPrivateKey())
                .setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationInMillis)))
                .compact();
    }
    private PrivateKey getPrivateKey() {
        try{
            return (PrivateKey) keyStore.getKey("springblog", "secret".toCharArray());
        }catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException ex){
            throw new SpringRedditException("Exception while retrieving private key from key store");
        }
    }

    public boolean validateToken(String jwt){
        parserBuilder().setSigningKey(getPublicKey()).build().parseClaimsJws(jwt);
        return true;
    }

    private PublicKey getPublicKey() {
        try{
            return keyStore.getCertificate("springblog").getPublicKey();
        }catch (KeyStoreException ex){
            throw new SpringRedditException("Exception occurred while retrieving public key");
        }
    }

    public String getUserNameFromJwt(String jwt){

        Claims  claims = parserBuilder()
                .setSigningKey(getPublicKey()).build()
                .parseClaimsJws(jwt)
                .getBody();

        return claims.getSubject();
    }

    public Long getExpirationTime(){
        return jwtExpirationInMillis;
    }

}
