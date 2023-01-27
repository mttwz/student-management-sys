package com.radnoti.studentmanagementsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role")
@CrossOrigin(origins = "http://localhost:4200/")
@RequiredArgsConstructor
public class RoleController {
}
