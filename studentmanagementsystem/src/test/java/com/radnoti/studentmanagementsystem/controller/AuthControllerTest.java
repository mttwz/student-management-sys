package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.StudentmanagementsystemApplication;
import com.radnoti.studentmanagementsystem.model.dto.UserDTO;
import com.radnoti.studentmanagementsystem.model.dto.UserLoginDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;


import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(classes = StudentmanagementsystemApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthControllerTest {

    @Value("${server.servlet.context-path}")
    private String contextPath;

    private String mainPath = "/auth";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;


    @Sql({ "AuthLogin.sql" })
    @Sql(value = {"classpath:sqls/clearDb.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void loginTest_validCredentials(){




        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("testEmail");
        userDTO.setPassword("testPw");

//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer ");
//        HttpEntity< UserDTO > userDTOtest = new HttpEntity<>(userDTO, httpHeaders);

        ResponseEntity<UserLoginDTO> responseEntity = this.restTemplate
                .postForEntity("http://localhost:" + port + contextPath + mainPath + "/login", userDTO, UserLoginDTO.class);



        assertEquals("testEmail", responseEntity.getBody().getEmail());
        assertEquals("testLastname", responseEntity.getBody().getLastName());
        assertEquals("testFirstname", responseEntity.getBody().getFirstName());
        assertTrue(responseEntity.getBody().getJwt().startsWith("Bearer"));
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Sql({ "AuthLogin.sql" })
    @Sql(value = {"classpath:sqls/clearDb.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void loginTest_invalidCredentials(){
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("thisEmailNotExist");
        userDTO.setPassword("thisPwNotExist");
        ResponseEntity<UserLoginDTO> responseEntity = this.restTemplate
                .postForEntity("http://localhost:" + port + contextPath + mainPath + "/login", userDTO, UserLoginDTO.class);

        assertEquals(403, responseEntity.getStatusCodeValue());
    }



}
