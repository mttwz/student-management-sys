/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.util;

import com.radnoti.studentmanagementsystem.model.User;
import io.jsonwebtoken.*;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import io.jsonwebtoken.security.Keys;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

/**
 *
 * @author matevoros
 */
@Service
public class JwtUtil {

   

    private static String Secret = "EktNSOvg5bmDXExYEgavT4HmateDXcZIZSXYv5oZWVCE3GxDvCiPuQUis35R0Ly=";

    public String generateJwt(Optional<User> optionalUser) {

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(Secret);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        Integer userId = optionalUser.get().getId();
        String role = optionalUser.get().getRoleId().getRoleType();


        Instant now = Instant.now();

        String token = Jwts.builder()
                .setIssuer("Mate")
                .claim("id", userId)
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
            };
            return false;
        }catch (Exception e){
            return  false;
        }
        //lehetséges ikszepsönök de lehet hogy van még
        /*catch (SignatureException | IllegalArgumentException | MalformedJwtException e){
            return  false;
        }*/



    }

    public boolean roleCheck(String roleName, String token) {
        try {
            String cleanToken = token.split(" ")[1];
            String[] parts = cleanToken.split("\\.");
            JSONObject header = new JSONObject(decode(parts[0]));
            JSONObject data = new JSONObject(decode(parts[1]));
            String signature = decode(parts[2]);
            if (data.getString("role").equals(roleName)) {
                return true;
            };
            return false;
        }catch (Exception ex){
            return false;
        }


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

    private static String decode(String encodedString) {
        return new String(Base64.getUrlDecoder().decode(encodedString));
    }

}
