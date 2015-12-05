package com.pasotaku.jwt;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;

import io.jsonwebtoken.*;
import org.apache.log4j.Logger;

import java.util.Date;

/**
 * Created by johnlight on 12/5/15.
 */
public class JWT {
    private static final Logger logger = Logger.getLogger(JWT.class);

    public static void main (String [] args) {
        System.out.println("testing jwt creation");
        JWT jwt = new JWT ("asdfj");
        String token = jwt.createJWT("James Ben.");
        System.out.println(token);
        String decoded = jwt.decodeJWT(token);
        System.out.println(decoded);

        String decodedBad = jwt.decodeJWT("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKYW1lcyBCZW4uIiwiZXhwIjoxNDQ5MzQ5MjQ0fQ.2KbQjcznu4xDTfpHkTq0a7aWDAKTblXspaSU4NRs3IU");
        System.out.println(decodedBad);

    }

    private Key signingKey;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    public JWT (String secretKey){
        this.signingKey = generateSigningKey(secretKey);
    }

    private Key generateSigningKey(String secretKey) {
        //We will sign our JWT with our secretKey
        //The JWT signature algorithm we will be using to sign the token
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
        return new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
    }

    public String createJWT(String subject) {
    //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder()
                .setSubject(subject)
                .signWith(signatureAlgorithm, this.signingKey);

    long nowMillis = System.currentTimeMillis();
    long ttlMillis = 300000;

    //if it has been specified, let's add the expiration

    long expMillis = nowMillis + ttlMillis;
    Date exp = new Date(expMillis);
    builder.setExpiration(exp);

    //Builds the JWT and serializes it to a compact, URL-safe string
    return builder.compact();
    }

    public String decodeJWT(String jwtString){
        try {
            return Jwts.parser().setSigningKey(this.signingKey).parseClaimsJws(jwtString).getBody().getSubject();

        } catch (SignatureException e) {
            logger.info("We got a bad JWT Token");
            return null;
        }

    }

}
