package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.model.dto.CardDTO;
import com.radnoti.studentmanagementsystem.model.dto.StudentDTO;
import com.radnoti.studentmanagementsystem.model.dto.UserDTO;
import com.radnoti.studentmanagementsystem.model.dto.UserLoginDTO;
import com.radnoti.studentmanagementsystem.model.entity.Card;
import com.radnoti.studentmanagementsystem.model.entity.Student;
import com.radnoti.studentmanagementsystem.model.entity.User;
import com.radnoti.studentmanagementsystem.repository.CardRepository;
import com.radnoti.studentmanagementsystem.repository.StudentRepository;
import com.radnoti.studentmanagementsystem.repository.UserRepository;
import com.radnoti.studentmanagementsystem.security.JwtConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class CardServiceTest {

    @InjectMocks
    CardService cardService;
    @Mock
    CardRepository cardRepository;
    @Mock
    StudentRepository studentRepository;

    @Test
    public void createCardTest_valid(){
        //arrange
        String hash = "hashPipe";
        CardDTO cardDTO = new CardDTO();
        cardDTO.setHash(hash);
        Card card = new Card();
        card.setHash(hash);

        when(cardRepository.createCard(any())).thenReturn(1);
        when(cardRepository.findById(any())).thenReturn(Optional.of(card));
        //act
        int actual = cardService.createCard(cardDTO);
        //assert
        assertEquals(1,actual);

    }


    @Test
    public void createCardTest_empty_card(){
        //arrange
        String hash = "hashPipe";
        CardDTO cardDTO = new CardDTO();
        cardDTO.setHash(hash);
        Card card = new Card();
        card.setHash(hash);
        when(cardRepository.createCard(any())).thenReturn(1);
        when(cardRepository.findById(any())).thenReturn(Optional.empty());

        //act & assert
        assertThrows(ResponseStatusException.class,()-> cardService.createCard(cardDTO));


    }

    @Test
    public void connectCardToStudentTest_valid(){
        //arrange
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(1);
        studentDTO.setCardId(1);
        Student student = new Student(1);
        student.setCardId(new Card(1));
        Card card = new Card();
        card.setHash("asdasdasdead");
        card.setId(1);

        when(studentRepository.findById(any())).thenReturn(Optional.of(student));
        when(cardRepository.findById(any())).thenReturn(Optional.of(card));

        //act
        int actual = cardService.connectCardToStudent(studentDTO);
        //assert
        assertEquals(1,actual);
    }

    @Test
    public void connectCardToStudentTest_card_not_found(){
        //arrange
        StudentDTO studentDTO = new StudentDTO();

        when(cardRepository.findById(any())).thenReturn(Optional.empty());


        //act & assert
        Exception ex = assertThrows(ResponseStatusException.class,()->cardService.connectCardToStudent(studentDTO));

        String expectedMessage = "409 CONFLICT \"User not found\"";
        String actualMessage = ex.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void connectCardToStudentTest_user_not_found(){
        //arrange
        StudentDTO studentDTO = new StudentDTO();

        when(studentRepository.findById(any())).thenReturn(Optional.empty());
        //act & assert
        Exception ex = assertThrows(ResponseStatusException.class,()->cardService.connectCardToStudent(studentDTO));

        String expectedMessage = "409 CONFLICT \"User not found\"";
        String actualMessage = ex.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
}
