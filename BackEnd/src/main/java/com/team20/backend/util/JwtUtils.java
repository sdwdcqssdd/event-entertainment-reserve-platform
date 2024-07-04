package com.team20.backend.util;

import com.team20.backend.model.user.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Date;

@Component
public class JwtUtils {

    private static final String KEYSTORE_PATH = "key/keystore.jks";
    private static final String KEYSTORE_PASSWORD = "123456";

    private static final String KEY_PASSWORD = "123456";

    private static final String KEY_ALIAS = "mykey";


    private static final PrivateKey PRIVATE_KEY = loadPrivateKeyFromKeyStore();
    private static final PublicKey PUBLIC_KEY = loadPublicKeyFromKeyStore();

    private static final Key SIGNING_KEY = PUBLIC_KEY;
    private static PrivateKey loadPrivateKeyFromKeyStore() {
        try {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            InputStream inputStream = JwtUtils.class.getClassLoader().getResourceAsStream(KEYSTORE_PATH);
            keyStore.load(inputStream, KEYSTORE_PASSWORD.toCharArray());
            return (PrivateKey) keyStore.getKey(KEY_ALIAS, KEY_PASSWORD.toCharArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static PublicKey loadPublicKeyFromKeyStore() {
        try {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            InputStream inputStream = JwtUtils.class.getClassLoader().getResourceAsStream(KEYSTORE_PATH);
            keyStore.load(inputStream, KEYSTORE_PASSWORD.toCharArray());
            Certificate cert = keyStore.getCertificate(KEY_ALIAS);
            return cert.getPublicKey();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 生成JWT令牌
    public String generateToken(Users user) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + 86400000); // Token有效期为1天
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("identity", user.getIdentity())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(PRIVATE_KEY, SignatureAlgorithm.RS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SIGNING_KEY)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            e.printStackTrace();
            return false;
        }
    }
    public String getUsername(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SIGNING_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (JwtException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 从JWT令牌中获取用户权限信息
    public String getIdentity(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SIGNING_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return (String) claims.get("identity");
        } catch (JwtException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean validateSuperUser(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SIGNING_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return "SUPERUSER".equals(claims.get("identity"));
        } catch (JwtException e) {
            e.printStackTrace();
            return false;
        }
    }
}
