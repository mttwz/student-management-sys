/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.util;

import com.radnoti.studentmanagementsystem.dto.UserDTO;
import com.radnoti.studentmanagementsystem.model.User;
import com.radnoti.studentmanagementsystem.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;
import static javax.crypto.Cipher.SECRET_KEY;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.ParameterMode;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.transaction.Transactional;
import javax.xml.bind.DatatypeConverter;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

/**
 *
 * @author matevoros
 */
@Repository
@Transactional
public class JwtUtil {

   

    private static String Secret = "EktNSOvg5bmDXExYEgavT4HmateDXcZIZSXYv5oZWVCE3GxDvCiPuQUis35R0Ly=";

    public String generateJwt(UserDTO userDTO) {

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(Secret);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        Integer userId = userDTO.getId();
        String role = userDTO.getRoleName();

        //System.out.println(u);
        Instant now = Instant.now();

        String token = Jwts.builder()
                .setIssuer("Mate")
                .claim("id", userId)
                .claim("scope", role)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(7, ChronoUnit.DAYS)))
                .signWith(
                        signatureAlgorithm,
                        signingKey
                )
                .compact();
        return token;

    }
    //enum

    public boolean validateJwt(String token) {
        if(token == null){
            return false;
        }

        String[] parts = token.split("\\.");
        JSONObject header = new JSONObject(decode(parts[0]));
        JSONObject data = new JSONObject(decode(parts[1]));
        String signature = decode(parts[2]);
        if (data.getLong("exp") > (System.currentTimeMillis() / 1000)) {
            return true;
        };
        return false;

    }

    public boolean roleCheck(String roleName, String token) {
        String[] parts = token.split("\\.");
        JSONObject header = new JSONObject(decode(parts[0]));
        JSONObject data = new JSONObject(decode(parts[1]));
        String signature = decode(parts[2]);
        if (data.getString("scope").equals(roleName)) {
            return true;
        };
        return false;

    }

    public String getRoleFromJwt(String token) {
        String[] parts = token.split("\\.");
        JSONObject data = new JSONObject(decode(parts[1]));
        return data.getString("scope");

    }

    public Integer getIdFromJwt(String token) {
        String[] parts = token.split("\\.");
        JSONObject data = new JSONObject(decode(parts[1]));
        return data.getInt("id");

    }

    private static String decode(String encodedString) {
        return new String(Base64.getUrlDecoder().decode(encodedString));
    }

}
