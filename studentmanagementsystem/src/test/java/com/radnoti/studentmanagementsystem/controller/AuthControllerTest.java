package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.StudentmanagementsystemApplication;
import com.radnoti.studentmanagementsystem.model.dto.UserDto;
import com.radnoti.studentmanagementsystem.model.dto.UserLoginDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;


import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("testH2")
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





}
