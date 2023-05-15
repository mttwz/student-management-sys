package com.radnoti.studentmanagementsystem.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.radnoti.studentmanagementsystem.model.dto.AttendanceDto;
import com.radnoti.studentmanagementsystem.model.dto.ResponseDto;
import com.radnoti.studentmanagementsystem.service.AttendanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.MediaType;

import java.time.ZonedDateTime;

import static org.mockito.Mockito.when;

import static org.mockito.Mockito.*;


//@WebMvcTest(AttendanceController.class)
public class AttendanceControllerTest {

    private MockMvc mockMvc;


    @Mock
    private AttendanceService attendanceService;

    @InjectMocks
    private AttendanceController attendanceController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(attendanceController).build();
    }

    @Test
    public void testLogStudent() throws Exception {
        String cardHash = "1234567890";
        ResponseDto expectedResponse = new ResponseDto(1);

        when(attendanceService.logStudent(cardHash)).thenReturn(expectedResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/attendance/log-student")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(cardHash))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(expectedResponse.getId()));
    }






}
