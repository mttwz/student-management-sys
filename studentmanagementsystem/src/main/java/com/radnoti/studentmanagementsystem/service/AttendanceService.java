package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.exception.form.NullFormValueException;
import com.radnoti.studentmanagementsystem.exception.student.StudentNotExistException;
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

        if(studentDto.getId() == null){
            throw new NullFormValueException();
        }
        studentRepository.findById(studentDto.getId()).orElseThrow(StudentNotExistException::new);
        Integer connectionId = studentRepository.logStudent(studentDto.getId());


        if(connectionId == null){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Log not saved");
        }

        return connectionId;
    }
}
