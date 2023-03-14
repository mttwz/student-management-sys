package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.model.dto.StudentDto;
import com.radnoti.studentmanagementsystem.model.entity.Student;
import com.radnoti.studentmanagementsystem.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AttendanceService {


    private final StudentRepository studentRepository;


    @Transactional
    public Integer logStudent(StudentDto studentDto){
        // TODO: 2023. 03. 14.  ezt nemtudom meg hogyan :(
        Integer studentId = studentDto.getId();
        if(studentId == null){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Value is null");
        }
        Optional<Student> optionalStudent = studentRepository.findById(studentId);

        if (optionalStudent.isEmpty()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Student not exist");
        }

        Integer connectionId = studentRepository.logStudent(studentId);

        if(connectionId == null){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Log not saved");
        }

        return connectionId;
    }
}
