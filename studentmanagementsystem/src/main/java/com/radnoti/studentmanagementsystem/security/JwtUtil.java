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

    public String generateJwt(User user) {

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(Secret);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        Integer userId = user.getId();
        String role = user.getRoleId().getRoleType();
        String email = user.getEmail();


        Instant now = Instant.now();

        String token = Jwts.builder()
                .setIssuer("Mate")
                .claim("id", userId)
                .claim("email", email )
                .claim("role", role)
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



    public String getRoleFromJwt(String token) {
        String cleanToken = token.split(" ")[1];
        String[] parts = cleanToken.split("\\.");
        JSONObject data = new JSONObject(decode(parts[1]));
        return data.getString("role");

    }

    public Integer getIdFromJwt(String token) {
        String cleanToken = token.split(" ")[1];
        String[] parts = cleanToken.split("\\.");
        JSONObject data = new JSONObject(decode(parts[1]));
        return data.getInt("id");

    }

    public String getEmailFromJwt(String token) {
        String cleanToken = token.split(" ")[1];
        String[] parts = cleanToken.split("\\.");
        JSONObject data = new JSONObject(decode(parts[1]));
        return data.getString("email");

    }

    private static String decode(String encodedString) {
        return new String(Base64.getUrlDecoder().decode(encodedString));
    }

}
