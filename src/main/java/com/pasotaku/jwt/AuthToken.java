package com.pasotaku.jwt;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.apache.log4j.Logger;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Date;

import static com.nimbusds.jwt.JWTClaimsSet.*;

/**
 * Created by johnlight on 12/5/15.
 */
public class AuthToken {
    private static final Logger logger = Logger.getLogger(AuthToken.class);

    private SecretKey secretKey;

    public AuthToken() {
        this.secretKey = generateSecretKey();
    }

    private SecretKey generateSecretKey() {
        // Generate 256-bit AES key for HMAC as well as encryption
        KeyGenerator keyGen;
        try {
            logger.info("Generating key");
            keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(256);
            return keyGen.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String createJwt(final String valueEnc) {
        // Create HMAC signer
        JWSSigner signer = null;
        try {
            signer = new MACSigner(this.secretKey.getEncoded());
        } catch (KeyLengthException e) {
            e.printStackTrace();
        }
        SignedJWT signedJWT = getSignedJWT(valueEnc);
        try {
            signedJWT.sign(signer); //Sign the JWT

        // Create JWE object with signed JWT as payload
        JWEObject jweObject = new JWEObject(
                new JWEHeader.Builder(JWEAlgorithm.DIR, EncryptionMethod.A256GCM)
                        .contentType("JWT") // required to signal nested JWT
                        .build(),
                new Payload(signedJWT));
            // Perform encryption
            jweObject.encrypt(new DirectEncrypter(this.secretKey.getEncoded()));
            // Serialise to JWE compact form
            return jweObject.serialize();
        }
        catch (JOSEException e) {
        logger.debug("Failed to sign JWT" + e);
        }
        return null;
    }

    private SignedJWT getSignedJWT(String valueEnc) {
        Builder builder = getBuilderForJWT(valueEnc);
        JWTClaimsSet claimsSet = builder.build();
        return new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
    }

    private Builder getBuilderForJWT(String valueEnc) {
        Builder builder = new Builder();
        builder.subject(valueEnc);
        builder.issueTime(new Date());
        builder.issuer("PasotakuBackend");
        return builder;
    }

    public String decodeJwt(final String encryptedValue) {

        String decryptedValue = null;

        try {

            // Parse the JWE string
            JWEObject jweObject = JWEObject.parse(encryptedValue);
            // Decrypt with shared key
            jweObject.decrypt(new DirectDecrypter(this.secretKey.getEncoded()));
            // Extract payload
            SignedJWT signedJWT = jweObject.getPayload().toSignedJWT();
            boolean goodJwt = signedJWT.verify(new MACVerifier(this.secretKey.getEncoded()));

            if(signedJWT!= null && goodJwt) {
                return signedJWT.getJWTClaimsSet().getSubject();
            }
            // If it isn't true, then we don't need to do anything, one of the exceptions will be thrown instead.
        } catch(JOSEException e) {
            logger.debug("Not a valid JWT. Error: " + e);
        } catch (ParseException e) {
            logger.debug("Exception thrown. Error: " + e);
        }
        return decryptedValue;
    }


//    Test code for this project
//    public static void main(String[] args) throws JOSEException {
//        System.out.println("testing authToken creation");
//        AuthToken authToken = new AuthToken();
//
//        String token = authToken.createJwt("Bob the builder");
//        System.out.println(token);
//        String decoded = authToken.decodeJwt(token);
//        System.out.println(decoded);
//
//        String decodedBad = authToken.decodeJwt("eyJjdHkiOiJKV1QiLCJlbmMiOiJBMjU2R0NNIiwiYWxnIjoiZGlyIn0..8aUnIZz13qFk1ziV.t8F8KWrHYmQWEjyp9U04Qbe52QN0_QRtehMC57HvycYZD19Zp-_cZDz2ZrnseWxzcnBouRpc43u0gxJPZAzOKlt_WFYW8jjRlJSxHKODE-VZK_7vZzgXhaMGqYACQMBxu03f5KCZ08fBeQ5x5QIqCt52cgdWDHaZFM7nJnA7Kj2U4oavcDSh2O5DEg.2IXwG9SsZJL9IFR33TqX6B");
//        System.out.println(decodedBad);
//
//    }


}
