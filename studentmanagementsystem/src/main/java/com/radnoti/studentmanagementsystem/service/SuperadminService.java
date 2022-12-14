package com.radnoti.studentmanagementsystem.service;


import com.radnoti.studentmanagementsystem.dto.CardDTO;
import com.radnoti.studentmanagementsystem.dto.StudentDTO;
import com.radnoti.studentmanagementsystem.dto.UserDTO;
import com.radnoti.studentmanagementsystem.model.User;
import com.radnoti.studentmanagementsystem.repository.CardRepository;
import com.radnoti.studentmanagementsystem.repository.UserRepository;
import com.radnoti.studentmanagementsystem.util.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class SuperadminService {


    private final CardRepository cardRepository;



    private final UserRepository userRepository;



    private final JwtUtil jwtUtil;

    public SuperadminService(CardRepository cardRepository, UserRepository userRepository, JwtUtil jwtUtil) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public void createCard(String jwt, CardDTO cardDTO) {
        if (jwtUtil.roleCheck("Superadmin", jwt) && jwtUtil.validateJwt(jwt)) {
            cardRepository.createCard(cardDTO.getHash());

        }
    }


    @Transactional
    public void connectCardToStudent(String jwt, StudentDTO studentDTO) {
        if (jwtUtil.roleCheck("Superadmin", jwt) && jwtUtil.validateJwt(jwt)) {
/*            Optional<Student> student = studentRepository.findById(studentDTO.getId());
            if (student.isPresent()){
                student.get().setCardId(new Card(studentDTO.getCardId()));
                studentRepository.save(student.get());
            }*/
            cardRepository.connectCardToStudent(studentDTO.getId(), studentDTO.getCardId());
        }
    }

    @Transactional
    public void connectStudentToUser(String jwt, StudentDTO studentDTO) {
        if (jwtUtil.roleCheck("Superadmin", jwt) && jwtUtil.validateJwt(jwt)) {
            cardRepository.connectStudentToUser(studentDTO.getId(), studentDTO.getUserId());
        }
    }

    @Transactional
    public Optional<User> getUserData(UserDTO userDTO){
        Optional<User> user = userRepository.findById(userDTO.getId());
        return user;
    }

    @Transactional
    public void setUserIsActivated(String jwt, UserDTO userDTO) {
        if (jwtUtil.roleCheck("Superadmin", jwt) && jwtUtil.validateJwt(jwt)) {
            userRepository.setUserIsActivated(userDTO.getId());
        }
    }
    @Transactional
    public void deleteUser(String jwt, UserDTO userDTO) {
        if (jwtUtil.roleCheck("Superadmin", jwt) && jwtUtil.validateJwt(jwt)) {
            userRepository.setUserIsDeleted(userDTO.getId());
        }
    }
    @Transactional
    public void setUserRole(String jwt, UserDTO userDTO) {
        if (jwtUtil.roleCheck("Superadmin", jwt) && jwtUtil.validateJwt(jwt)) {
            userRepository.setUserRole(userDTO.getId(), userDTO.getRoleName());
        }
    }

    @Transactional
    public void addUserToWorkgroup(String jwt, UserDTO userDTO) {
        if (jwtUtil.roleCheck("Superadmin", jwt) && jwtUtil.validateJwt(jwt)) {
            userRepository.addUserToWorkgroup(userDTO.getId(), userDTO.getWorkgroupId());
        }
    }
}
