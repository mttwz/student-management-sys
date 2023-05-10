/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.security;

import com.radnoti.studentmanagementsystem.model.entity.User;
import io.jsonwebtoken.*;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author matevoros
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String Secret;


    /**
     * Generates a JWT for the given user.
     *
     * @param user The user for whom the JWT is generated.
     * @return The generated JWT.
     */
    public String generateJwt(User user) {

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(Secret);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        Integer userId = user.getId();
        String role = user.getRoleId().getRoleType();
        String email = user.getEmail();
        String fullName = user.getFirstName() + " " + user.getLastName();


        Instant now = Instant.now();

        String token = Jwts.builder()
                .setIssuer("Mate")
                .claim("id", userId)
                .claim("email", email )
                .claim("role", role)
                .claim("fullName", fullName)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(7, ChronoUnit.DAYS)))
                .signWith(
                        signatureAlgorithm,
                        signingKey
                )
                .compact();
        return "Bearer " +token;

    }
    //enum


    /**
     * Validates a JWT.
     *
     * @param token The JWT to validate.
     * @return true if the JWT is valid, false otherwise.
     */
    public boolean validateJwt(String token) {

        try{

            String cleanToken = token.split(" ")[1];
            Jwts.parser().setSigningKey(Secret).parseClaimsJws(cleanToken);

            String[] parts = cleanToken.split("\\.");
            JSONObject data = new JSONObject(decode(parts[1]));

            if (data.getLong("exp") > (System.currentTimeMillis() / 1000)) {
                return true;
            }
            return false;
        }catch (SignatureException | IllegalArgumentException | MalformedJwtException | NullPointerException | ExpiredJwtException e){
            return  false;
        }
        //lehetséges ikszepsönök de lehet hogy van még
        /*catch (SignatureException | IllegalArgumentException | MalformedJwtException e){
            return  false;
        }*/



    }


    /**
     * Retrieves the role from the authentication header of a JWT.
     *
     * @param header The authentication header containing the JWT.
     * @return The role extracted from the JWT.
     */
    public String getRoleFromAuthHeader(String header) {
        String cleanToken = header.split(" ")[1];
        String[] parts = cleanToken.split("\\.");
        JSONObject data = new JSONObject(decode(parts[1]));
        return data.getString("role");

    }

    /**
     * Retrieves the ID from the authentication header of a JWT.
     *
     * @param header The authentication header containing the JWT.
     * @return The ID extracted from the JWT.
     */
    public Integer getIdFromAuthHeader(String header) {
        String cleanToken = header.split(" ")[1];
        String[] parts = cleanToken.split("\\.");
        JSONObject data = new JSONObject(decode(parts[1]));
        return data.getInt("id");

    }


    /**
     * Retrieves the email from the authentication header of a JWT.
     *
     * @param header The authentication header containing the JWT.
     * @return The email extracted from the JWT.
     */
    public String getEmailFromAuthHeader(String header) {
        String cleanToken = header.split(" ")[1];
        String[] parts = cleanToken.split("\\.");
        JSONObject data = new JSONObject(decode(parts[1]));
        return data.getString("email");

    }

    /**
     * Decodes a Base64 encoded string.
     *
     * @param encodedString The Base64 encoded string to decode.
     * @return The decoded string.
     */
    private static String decode(String encodedString) {
        return new String(Base64.getUrlDecoder().decode(encodedString));
    }

}
