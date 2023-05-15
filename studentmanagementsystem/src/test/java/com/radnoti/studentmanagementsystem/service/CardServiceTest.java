package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.exception.card.*;
import com.radnoti.studentmanagementsystem.exception.form.FormValueInvalidException;
import com.radnoti.studentmanagementsystem.exception.form.InvalidIdException;
import com.radnoti.studentmanagementsystem.exception.student.StudentNotExistException;
import com.radnoti.studentmanagementsystem.exception.user.UserNotExistException;
import com.radnoti.studentmanagementsystem.mapper.CardMapper;
import com.radnoti.studentmanagementsystem.model.dto.*;
import com.radnoti.studentmanagementsystem.model.entity.Card;
import com.radnoti.studentmanagementsystem.model.entity.Student;
import com.radnoti.studentmanagementsystem.model.entity.User;
import com.radnoti.studentmanagementsystem.repository.CardRepository;
import com.radnoti.studentmanagementsystem.repository.StudentRepository;
import com.radnoti.studentmanagementsystem.repository.UserRepository;
import com.radnoti.studentmanagementsystem.util.IdValidatorUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import static org.mockito.Mockito.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    UserRepository userRepository;
    @Mock
    StudentRepository studentRepository;
    @Mock
    private CardMapper cardMapper;

    @Mock
    IdValidatorUtil idValidatorUtil;

    @Test
    public void testCreateCard_Success() {
        // Arrange
        String cardHash = "cardHash";
        Mockito.when(cardRepository.findByHash(cardHash)).thenReturn(Optional.empty());

        Card card = new Card();
        card.setHash(cardHash);
        card.setCreatedAt(ZonedDateTime.now());
        card.setIsDeleted(false);
        card.setIsAssigned(false);

        Mockito.when(cardRepository.save(Mockito.any(Card.class))).thenReturn(card);

        // Act
        ResponseDto response = cardService.createCard(cardHash);

        // Assert
        assertEquals(card.getId(), response.getId());
    }

    @Test
    public void testCreateCard_MissingCardHash_ThrowsFormValueInvalidException() {
        // Arrange

        // Act & Assert
        assertThrows(FormValueInvalidException.class, () -> {
            cardService.createCard(null);
        });
    }

    @Test
    public void testCreateCard_CardAlreadyAssigned_ThrowsCardAlreadyAssignedException() {
        // Arrange
        String cardHash = "cardHash";
        Optional<Card> optionalCard = Optional.of(new Card());
        Mockito.when(cardRepository.findByHash(cardHash)).thenReturn(optionalCard);

        // Act & Assert
        assertThrows(CardAlreadyAssignedException.class, () -> {
            cardService.createCard(cardHash);
        });
    }

    @Test
    public void testDeleteCard() {
        // Mocking dependencies and test data
        String cardIdString = "1";
        Integer cardId = 1;
        Card card = new Card();
        card.setId(cardId);
        card.setIsDeleted(false);
        card.setLastAssignedTo(123);

        when(idValidatorUtil.idValidator(cardIdString)).thenReturn(cardId);
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
        when(studentRepository.findById(card.getLastAssignedTo())).thenReturn(Optional.of(new Student()));

        // Calling the method to be tested
        cardService.deleteCard(cardIdString);


        assertTrue(card.getIsDeleted());
        assertFalse(card.getIsAssigned());
        assertNotNull(card.getDeletedAt());
    }

    @Test
    public void testDeleteCard_InvalidCardId() {
        // Mocking dependencies and test data
        String cardIdString = "invalid";

        when(idValidatorUtil.idValidator(cardIdString)).thenThrow(new CardNotExistException());

        // Calling the method to be tested and asserting the thrown exception
        assertThrows(CardNotExistException.class, () -> {
            cardService.deleteCard(cardIdString);
        });

    }

    @Test
    public void testDeleteCard_CardAlreadyDeleted() {
        // Mocking dependencies and test data
        String cardIdString = "deleted";
        Card card = new Card();
        card.setId(1);
        card.setIsDeleted(true);

        when(idValidatorUtil.idValidator(cardIdString)).thenReturn(1);
        when(cardRepository.findById(1)).thenReturn(Optional.of(card));

        // Calling the method to be tested and asserting the thrown exception
        assertThrows(CardAlreadyDeletedException.class, () -> {
            cardService.deleteCard(cardIdString);
        });

    }


    @Test
    public void testRestoreDeletedCard_ValidCardId() {
        // Mocking dependencies and test data
        String cardIdString = "1";
        Integer cardId = 1;
        Card card = new Card();
        card.setId(cardId);
        card.setIsDeleted(true);

        int studentId = 123;
        Student student = new Student();
        student.setId(studentId);
        student.setCardId(null);
        card.setLastAssignedTo(studentId);

        when(idValidatorUtil.idValidator(cardIdString)).thenReturn(cardId);
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
        when(studentRepository.findById(card.getLastAssignedTo())).thenReturn(Optional.of(student));

        // Calling the method to be tested
        cardService.restoreDeletedCard(cardIdString);

        // Verifying the student and card state after the method call
        assertEquals(card, student.getCardId());
        assertTrue(student.getCardId() != null && student.getCardId().equals(card));
        assertTrue(card.getIsAssigned());
        assertFalse(card.getIsDeleted());
    }


    @Test
    public void testRestoreDeletedCard_InvalidCardId() {
        // Mocking dependencies and test data
        String cardIdString = "invalid";

        when(idValidatorUtil.idValidator(cardIdString)).thenThrow(new InvalidIdException());

        // Calling the method to be tested and asserting the thrown exception
        assertThrows(InvalidIdException.class, () -> {
            cardService.restoreDeletedCard(cardIdString);
        });
    }

    @Test
    public void testRestoreDeletedCard_CardNotExist() {
        // Mocking dependencies and test data
        String cardIdString = "1";
        Integer cardId = 1;

        when(idValidatorUtil.idValidator(cardIdString)).thenReturn(cardId);
        when(cardRepository.findById(cardId)).thenReturn(Optional.empty());

        // Calling the method to be tested and asserting the thrown exception
        assertThrows(CardNotExistException.class, () -> {
            cardService.restoreDeletedCard(cardIdString);
        });
    }

    @Test
    public void testRestoreDeletedCard_CardNotDeleted() {
        // Mocking dependencies and test data
        String cardIdString = "1";
        Integer cardId = 1;
        Card card = new Card();
        card.setId(cardId);
        card.setIsDeleted(false);

        when(idValidatorUtil.idValidator(cardIdString)).thenReturn(cardId);
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        // Calling the method to be tested and asserting the thrown exception
        assertThrows(CardNotDeletedException.class, () -> {
            cardService.restoreDeletedCard(cardIdString);
        });
    }

    @Test
    public void testRestoreDeletedCard_StudentCardNotAssigned() {
        // Mocking dependencies and test data
        String cardIdString = "1";
        Integer cardId = 1;
        Card card = new Card();
        card.setId(cardId);
        card.setIsDeleted(true);
        int studentId = 123;
        Student student = new Student();
        student.setId(studentId);
        student.setCardId(null);

        when(idValidatorUtil.idValidator(cardIdString)).thenReturn(cardId);
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
        when(studentRepository.findById(card.getLastAssignedTo())).thenReturn(Optional.of(student));

        // Calling the method to be tested
        cardService.restoreDeletedCard(cardIdString);

        // Verifying the student and card state after the method call
        assertEquals(card, student.getCardId());
        assertTrue(card.getIsAssigned());
        assertFalse(card.getIsDeleted());

    }


//    @Test
//    public void testAssignCardToStudent_ValidStudentDto() {
//        // Mocking dependencies and test data
//        Integer studentId = 123;
//        Integer cardId = 456;
//
//        StudentDto studentDto = new StudentDto();
//        studentDto.setId(studentId);
//        studentDto.setCardId(cardId);
//
//        Student student = new Student();
//        student.setId(studentId);
//        student.setCardId(null);
//
//        Card card = new Card();
//        card.setId(cardId);
//        card.setIsAssigned(false);
//        card.setIsDeleted(false);
//
//        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
//        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
//
//        // Calling the method to be tested
//        cardService.assignCardToStudent(studentDto);
//
//        // Verifying the student and card state after the method call
//        assertEquals(card, student.getCardId());
//        assertTrue(card.getIsAssigned());
//        assertEquals(studentId, card.getLastAssignedTo());
//
//    }

    @Test
    public void testAssignCardToStudent_InvalidStudentDto() {
        // Mocking dependencies and test data
        StudentDto studentDto = new StudentDto(); // Invalid StudentDto object with null ID and card ID

        // Calling the method to be tested and asserting the thrown exceptions
        assertThrows(FormValueInvalidException.class, () -> {
            cardService.assignCardToStudent(studentDto);
        });

    }
//
//    @Test
//    public void testAssignCardToStudent_StudentNotExist() {
//        // Mocking dependencies and test data
//        StudentDto studentDto = new StudentDto();
//        studentDto.setId(123); // Valid student ID
//        studentDto.setCardId(456); // Valid card ID
//
//        when(studentRepository.findById(123)).thenReturn(Optional.empty()); // Simulating student not found in the database
//
//        // Calling the method to be tested and asserting the thrown exception
//        assertThrows(StudentNotExistException.class, () -> {
//            cardService.assignCardToStudent(studentDto);
//        });
//
//    }
//
//    @Test
//    public void testAssignCardToStudent_CardNotExist() {
//        // Mocking dependencies and test data
//        StudentDto studentDto = new StudentDto();
//        studentDto.setId(123); // Valid student ID
//        studentDto.setCardId(456); // Valid card ID
//
//        when(studentRepository.findById(123)).thenReturn(Optional.of(new Student())); // Simulating existing student
//        when(cardRepository.findById(456)).thenReturn(Optional.empty()); // Simulating card not found in the database
//
//        // Calling the method to be tested and asserting the thrown exception
//        assertThrows(CardNotExistException.class, () -> {
//            cardService.assignCardToStudent(studentDto);
//        });
//
//
//    }
//
//    @Test
//    public void testAssignCardToStudent_CardAlreadyAssigned() {
//        // Mocking dependencies and test data
//        StudentDto studentDto = new StudentDto();
//        studentDto.setId(123); // Valid student ID
//        studentDto.setCardId(456); // Valid card ID
//
//        Student student = new Student();
//        student.setId(123);
//        student.setCardId(new Card());
//
//        Card card = new Card();
//        card.setId(456);
//        card.setIsAssigned(true);
//
//        when(studentRepository.findById(123)).thenReturn(Optional.of(student)); // Simulating existing student
//        when(cardRepository.findById(456)).thenReturn(Optional.of(card)); // Simulating assigned card
//
//        // Calling the method to be tested and asserting the thrown exception
//        assertThrows(CardAlreadyAssignedException.class, () -> {
//            cardService.assignCardToStudent(studentDto);
//        });
//
//    }
//
//    @Test
//    public void testAssignCardToStudent_CardAlreadyDeleted() {
//        // Mocking dependencies and test data
//        StudentDto studentDto = new StudentDto();
//        studentDto.setId(123); // Valid student ID
//        studentDto.setCardId(456); // Valid card ID
//
//        Student student = new Student();
//        student.setId(123);
//        student.setCardId(null);
//
//        Card card = new Card();
//        card.setId(456);
//        card.setIsDeleted(true);
//        card.setIsAssigned(false);
//
//        when(studentRepository.findById(123)).thenReturn(Optional.of(student)); // Simulating existing student
//        when(cardRepository.findById(456)).thenReturn(Optional.of(card)); // Simulating deleted card
//
//        // Calling the method to be tested and asserting the thrown exception
//        assertThrows(CardAlreadyDeletedException.class, () -> {
//            cardService.assignCardToStudent(studentDto);
//        });
//    }
//
//
//
//
//
//    @Test
//    public void testAssignCardToStudent_AnotherCardAlreadyAssigned() {
//        // Mocking dependencies and test data
//        StudentDto studentDto = new StudentDto();
//        studentDto.setId(123); // Valid student ID
//        studentDto.setCardId(456); // Valid card ID
//
//        Student student = new Student();
//        student.setId(123);
//        student.setCardId(new Card());
//
//        Card card = new Card();
//        card.setId(456);
//        card.setIsDeleted(false);
//        card.setIsAssigned(false);
//
//        when(studentRepository.findById(123)).thenReturn(Optional.of(student)); // Simulating existing student
//        when(cardRepository.findById(456)).thenReturn(Optional.of(card)); // Simulating unassigned card
//
//        // Calling the method to be tested and asserting the thrown exception
//        assertThrows(AnotherCardAlreadyAssignedException.class, () -> {
//            cardService.assignCardToStudent(studentDto);
//        });
//    }


    @Test
    public void testGetCardByUserId() {
        // Mocking dependencies and test data
        String userIdString = "123"; // Valid user ID

        User user = new User();
        user.setId(123);

        Card card = new Card();
        card.setId(456);
        card.setIsDeleted(false);

        when(idValidatorUtil.idValidator(userIdString)).thenReturn(123); // Simulating valid user ID
        when(userRepository.findById(123)).thenReturn(Optional.of(user)); // Simulating existing user
        when(cardRepository.getCardByUserId(123)).thenReturn(Optional.of(card)); // Simulating existing card

        // Calling the method to be tested
        ResponseDto responseDto = cardService.getCardByUserId(userIdString);

        // Verifying the returned response
        assertNotNull(responseDto);
        assertEquals(456, responseDto.getId());
    }



    @Test
    public void testGetCardByUserId_UserNotExist() {
        // Mocking dependencies and test data
        String userIdString = "123"; // Valid user ID

        when(idValidatorUtil.idValidator(userIdString)).thenReturn(123); // Simulating valid user ID
        when(userRepository.findById(123)).thenReturn(Optional.empty()); // Simulating non-existent user

        // Calling the method to be tested and asserting the thrown exception
        assertThrows(UserNotExistException.class, () -> {
            cardService.getCardByUserId(userIdString);
        });
    }

    @Test
    public void testGetCardByUserId_CardNotExist() {
        // Mocking dependencies and test data
        String userIdString = "123"; // Valid user ID

        User user = new User();
        user.setId(123);

        when(idValidatorUtil.idValidator(userIdString)).thenReturn(123); // Simulating valid user ID
        when(userRepository.findById(123)).thenReturn(Optional.of(user)); // Simulating existing user
        when(cardRepository.getCardByUserId(123)).thenReturn(Optional.empty()); // Simulating non-existent card

        // Calling the method to be tested and asserting the thrown exception
        assertThrows(CardNotExistException.class, () -> {
            cardService.getCardByUserId(userIdString);
        });
    }

    @Test
    public void testGetCardByUserId_CardAlreadyDeleted() {
        // Mocking dependencies and test data
        String userIdString = "123"; // Valid user ID

        User user = new User();
        user.setId(123);

        Card card = new Card();
        card.setId(456);
        card.setIsDeleted(true);

        when(idValidatorUtil.idValidator(userIdString)).thenReturn(123); // Simulating valid user ID
        when(userRepository.findById(123)).thenReturn(Optional.of(user)); // Simulating existing user
        when(cardRepository.getCardByUserId(123)).thenReturn(Optional.of(card)); // Simulating deleted card

        // Calling the method to be tested and asserting the thrown exception
        assertThrows(CardAlreadyDeletedException.class, () -> {
            cardService.getCardByUserId(userIdString);
        });
    }


    @Test
    public void testGetCardByStudentId() {
        // Mocking dependencies and test data
        String studentIdString = "123"; // Valid student ID

        Card card = new Card();
        card.setId(456);

        Student student = new Student();
        student.setId(123);
        student.setCardId(card);

        when(idValidatorUtil.idValidator(studentIdString)).thenReturn(123); // Simulating valid student ID
        when(studentRepository.findById(123)).thenReturn(Optional.of(student)); // Simulating existing student


        // Calling the method to be tested
        ResponseDto responseDto = cardService.getCardByStudentId(studentIdString);

        // Verifying the returned response
        assertNotNull(responseDto);
        assertEquals(456, responseDto.getId());

    }

    @Test
    public void testGetCardByStudentId_StudentNotExist() {
        // Mocking dependencies and test data
        String studentIdString = "123"; // Valid student ID

        when(idValidatorUtil.idValidator(studentIdString)).thenReturn(123); // Simulating valid student ID
        when(studentRepository.findById(123)).thenReturn(Optional.empty()); // Simulating non-existent student

        // Calling the method to be tested and asserting the thrown exception
        assertThrows(StudentNotExistException.class, () -> {
            cardService.getCardByStudentId(studentIdString);
        });
    }

    @Test
    public void testGetCardByStudentId_CardNotAssigned() {
        // Mocking dependencies and test data
        String studentIdString = "123"; // Valid student ID

        Student student = new Student();
        student.setId(123);
        student.setCardId(null);

        when(idValidatorUtil.idValidator(studentIdString)).thenReturn(123); // Simulating valid student ID
        when(studentRepository.findById(123)).thenReturn(Optional.of(student)); // Simulating existing student

        // Calling the method to be tested and asserting the thrown exception
        assertThrows(CardNotAssignedException.class, () -> {
            cardService.getCardByStudentId(studentIdString);
        });
    }


    @Test
    public void testGetAllCard() {
        // Mocking dependencies and test data
        Pageable pageable = PageRequest.of(0, 10); // Page 1 with 10 items per page

        List<Card> cardList = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            Card card = new Card();
            card.setId(i);
            cardList.add(card);
        }
        Page<Card> cardPage = new PageImpl<>(cardList, pageable, 20);

        when(cardRepository.findAll(pageable)).thenReturn(cardPage); // Simulating cards pagination

        // Calling the method to be tested
        PagingDto pagingDto = cardService.getAllCard(pageable);

        // Verifying the returned response
        assertNotNull(pagingDto);
        assertEquals(2, pagingDto.getAllPages());
        assertEquals(20, pagingDto.getCardDtoList().size());
        // Add more assertions as needed
    }






}












