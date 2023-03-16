package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.model.dto.CardDto;
import com.radnoti.studentmanagementsystem.model.dto.StudentDto;
import com.radnoti.studentmanagementsystem.model.entity.Card;
import com.radnoti.studentmanagementsystem.model.entity.Student;
import com.radnoti.studentmanagementsystem.repository.CardRepository;
import com.radnoti.studentmanagementsystem.repository.StudentRepository;
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
        CardDto cardDto = new CardDto();
        cardDto.setHash(hash);
        Card card = new Card();
        card.setHash(hash);

        when(cardRepository.createCard(any())).thenReturn(1);
        when(cardRepository.findById(any())).thenReturn(Optional.of(card));
        //act
        int actual = cardService.createCard(cardDto);
        //assert
        assertEquals(1,actual);

    }

    @Test
    public void createCardTest_notCreatedBecauseCardIdIsNull(){
        String hash = "hashashahash";
        CardDto cardDto = new CardDto();
        when(cardRepository.createCard(any())).thenReturn(null);

        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, ()-> cardService.createCard(cardDto));

        Integer actuaStatusCode = responseStatusException.getRawStatusCode();
        String actualMessage = responseStatusException.getReason();
        String actualStatusCodeName = responseStatusException.getStatus().name();


        assertEquals(409, actuaStatusCode);
        assertEquals("CONFLICT", actualStatusCodeName);
        assertEquals("Card not created", actualMessage);
    }


    @Test
    public void createCardTest_not_exist(){
        //arrange
        String hash = "hashPipe";
        CardDto cardDto = new CardDto();
        cardDto.setHash(hash);
        when(cardRepository.createCard(any())).thenReturn(1);
        when(cardRepository.findById(any())).thenReturn(Optional.empty());

        //act
        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, ()-> cardService.createCard(cardDto));

        Integer actuaStatusCode = responseStatusException.getRawStatusCode();
        String actualMessage = responseStatusException.getReason();
        String actualStatusCodeName = responseStatusException.getStatus().name();

        //assert
        assertEquals(409, actuaStatusCode);
        assertEquals("CONFLICT", actualStatusCodeName);
        assertEquals("Card not exist", actualMessage);


    }


    @Test
    public void createCardTest_notCreatedBecauseNotEquals(){
        String hash = "hashPipe";
        CardDto cardDto = new CardDto();
        cardDto.setHash(hash);
        Card card = new Card();
        card.setHash(hash+"randomString");

        when(cardRepository.createCard(any())).thenReturn(1);
        when(cardRepository.findById(any())).thenReturn(Optional.of(card));

        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, ()-> cardService.createCard(cardDto));

        Integer actuaStatusCode = responseStatusException.getRawStatusCode();
        String actualMessage = responseStatusException.getReason();
        String actualStatusCodeName = responseStatusException.getStatus().name();


        assertEquals(409, actuaStatusCode);
        assertEquals("CONFLICT", actualStatusCodeName);
        assertEquals("Card not created", actualMessage);
    }




    @Test
    public void connectCardToStudentTest_valid(){
        //arrange
        StudentDto studentDto = new StudentDto();
        studentDto.setId(1);
        studentDto.setCardId(1);
        Student student = new Student(1);
        student.setCardId(new Card(1));
        Card card = new Card();
        card.setHash("asdasdasdead");
        card.setId(1);

        when(studentRepository.findById(any())).thenReturn(Optional.of(student));
        when(cardRepository.findById(any())).thenReturn(Optional.of(card));

        //act
        int actual = cardService.connectCardToStudent(studentDto);
        //assert
        assertEquals(1,actual);
    }


    @Test
    public void connectCardToStudentTest_userNotFound(){
        //arrange
        StudentDto studentDto = new StudentDto();

        when(studentRepository.findById(any())).thenReturn(Optional.empty());
        //act & assert
        Exception ex = assertThrows(ResponseStatusException.class,()->cardService.connectCardToStudent(studentDto));

        String expectedMessage = "409 CONFLICT \"User not found\"";
        String actualMessage = ex.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void connectCardToStudentTest_card_not_found(){
        //arrange
        StudentDto studentDto = new StudentDto();

        when(cardRepository.findById(any())).thenReturn(Optional.empty());

        //act & assert
        Exception ex = assertThrows(ResponseStatusException.class,()->cardService.connectCardToStudent(studentDto));

        String expectedMessage = "409 CONFLICT \"User not found\"";
        String actualMessage = ex.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }


}
