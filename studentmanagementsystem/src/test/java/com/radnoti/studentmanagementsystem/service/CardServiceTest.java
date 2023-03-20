package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.exception.card.CardNotAssignedException;
import com.radnoti.studentmanagementsystem.exception.card.CardNotCreatedException;
import com.radnoti.studentmanagementsystem.exception.card.CardNotExistException;
import com.radnoti.studentmanagementsystem.exception.form.InvalidFormValueException;
import com.radnoti.studentmanagementsystem.exception.form.NullFormValueException;
import com.radnoti.studentmanagementsystem.exception.student.StudentNotExistException;
import com.radnoti.studentmanagementsystem.exception.user.UserNotExistException;
import com.radnoti.studentmanagementsystem.mapper.CardMapper;
import com.radnoti.studentmanagementsystem.model.dto.CardDto;
import com.radnoti.studentmanagementsystem.model.dto.StudentDto;
import com.radnoti.studentmanagementsystem.model.dto.UserDto;
import com.radnoti.studentmanagementsystem.model.entity.Card;
import com.radnoti.studentmanagementsystem.model.entity.Student;
import com.radnoti.studentmanagementsystem.model.entity.User;
import com.radnoti.studentmanagementsystem.repository.CardRepository;
import com.radnoti.studentmanagementsystem.repository.StudentRepository;
import com.radnoti.studentmanagementsystem.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;
import static org.mockito.Mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
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


    @Test
    public void testCreateCardValidCase() {
        // Arrange
        CardDto cardDto = new CardDto();
        cardDto.setHash("valid-hash");
        Card card = new Card();
        card.setId(1);
        card.setHash("valid-hash");
        when(cardRepository.save(any())).thenReturn(card);
        when(cardMapper.fromDtoToEntity(any())).thenReturn(card);



        // Act
        Integer cardId = cardService.createCard(cardDto);

        // Assert
        assertNotNull(cardId);
    }

    @Test
    public void testCreateCardWithNullDto() {
        // Arrange
        CardDto cardDto = null;

        // Act and Assert
        assertThrows(InvalidFormValueException.class, () -> cardService.createCard(cardDto));
        verifyNoInteractions(cardRepository);
    }

    @Test
    public void testCreateCardWithNullHash() {
        // Arrange
        CardDto cardDto = new CardDto();
        cardDto.setHash(null);

        // Act and Assert
        assertThrows(InvalidFormValueException.class, () -> cardService.createCard(cardDto));
        verifyNoInteractions(cardRepository);
    }

    @Test
    public void testCreateCardWithEmptyHash() {
        // Arrange
        CardDto cardDto = new CardDto();
        cardDto.setHash("");

        // Act and Assert
        assertThrows(InvalidFormValueException.class, () -> cardService.createCard(cardDto));
        verifyNoInteractions(cardRepository);
    }























    @Test
    public void testConnectCardToStudentWithValidData() {
        // arrange
        StudentDto studentDto = new StudentDto();
        studentDto.setId(1);
        studentDto.setCardId(2);

        // mock the behavior of your repositories
        when(cardRepository.findById(studentDto.getCardId())).thenReturn(Optional.of(new Card()));
        when(studentRepository.findById(studentDto.getId())).thenReturn(Optional.of(new Student()));

        // act and assert
        assertDoesNotThrow(() -> cardService.connectCardToStudent(studentDto));
    }

    @Test
    public void testConnectCardToStudentWithNullId() {
        // arrange
        StudentDto studentDto = new StudentDto();
        studentDto.setId(null);
        studentDto.setCardId(2);

        // act and assert
        assertThrows(InvalidFormValueException.class, () -> cardService.connectCardToStudent(studentDto));
    }

    @Test
    public void testConnectCardToStudentWithNullCardId() {
        // arrange
        StudentDto studentDto = new StudentDto();
        studentDto.setId(1);
        studentDto.setCardId(null);

        // act and assert
        assertThrows(InvalidFormValueException.class, () -> cardService.connectCardToStudent(studentDto));
    }

    @Test
    public void testConnectCardToStudentWithNonExistentStudent() {
        // arrange
        StudentDto studentDto = new StudentDto();
        studentDto.setId(1);
        studentDto.setCardId(2);

        // mock the behavior of your repositories
        when(studentRepository.findById(studentDto.getId())).thenReturn(Optional.empty());

        // act and assert
        assertThrows(StudentNotExistException.class, () -> cardService.connectCardToStudent(studentDto));
    }

    @Test
    public void testConnectCardToStudentWithNonExistentCard() {
        // arrange
        StudentDto studentDto = new StudentDto();
        studentDto.setId(1);
        studentDto.setCardId(2);

        // mock the behavior of your repositories
        when(cardRepository.findById(studentDto.getCardId())).thenReturn(Optional.empty());
        when(studentRepository.findById(studentDto.getId())).thenReturn(Optional.of(new Student()));

        // act and assert
        assertThrows(CardNotExistException.class, () -> cardService.connectCardToStudent(studentDto));
    }



    @Test
    public void getCardByUserIdTest_valid() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setId(1);

        User user = new User();
        user.setId(userDto.getId());
        when(userRepository.findById(userDto.getId())).thenReturn(Optional.of(user));

        Integer cardId = 123;
        when(cardRepository.getCardByUserId(userDto.getId())).thenReturn(cardId);

        // Act
        Integer result = cardService.getCardByUserId(userDto);

        // Assert
        assertEquals(cardId, result);
    }

    @Test
    public void getCardByUserIdTest_withNullId() {
        // Arrange
        UserDto userDto = new UserDto();

        // Act & Assert
        assertThrows(NullFormValueException.class, () -> cardService.getCardByUserId(userDto));
    }

    @Test
    public void getCardByUserIdTest_withNonexistentUser() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setId(1);

        when(userRepository.findById(userDto.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotExistException.class, () -> cardService.getCardByUserId(userDto));
    }

    @Test
    public void getCardByUserIdTest_withNonexistentCard() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setId(1);

        User user = new User();
        user.setId(userDto.getId());
        when(userRepository.findById(userDto.getId())).thenReturn(Optional.of(user));

        when(cardRepository.getCardByUserId(userDto.getId())).thenReturn(null);

        // Act & Assert
        assertThrows(CardNotAssignedException.class, () -> cardService.getCardByUserId(userDto));

    }

    @Test
    public void testGetCardByStudentId_NullFormValueException() {
        StudentDto studentDto = new StudentDto();

        assertThrows(
                NullFormValueException.class,
                () -> cardService.getCardByStudentId(studentDto)
        );
    }

    @Test
    public void testGetCardByStudentId_StudentNotExistException() {
        StudentDto studentDto = new StudentDto();
        studentDto.setId(1);

        assertThrows(
                StudentNotExistException.class,
                () -> cardService.getCardByStudentId(studentDto)
        );
    }

    @Test
    public void testGetCardByUserId() {
        // Setup
        Integer userId = 1;
        Integer cardId = 123;
        UserDto userDto = new UserDto();
        userDto.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(cardRepository.getCardByUserId(userId)).thenReturn(cardId);

        // Verify
        assertEquals(cardId, cardService.getCardByUserId(userDto));

        // Check if userRepository.findById was called once with userId as parameter
        verify(userRepository, times(1)).findById(userId);
        // Check if cardRepository.getCardByUserId was called once with userId as parameter
        verify(cardRepository, times(1)).getCardByUserId(userId);
    }

    @Test
    public void testGetCardByUserIdNullFormValueException() {
        // Setup
        UserDto userDto = new UserDto();
        userDto.setId(null);

        // Verify
        assertThrows(NullFormValueException.class, () -> cardService.getCardByUserId(userDto));

        // Check if userRepository.findById was not called
        verify(userRepository, times(0)).findById(any());
        // Check if cardRepository.getCardByUserId was not called
        verify(cardRepository, times(0)).getCardByUserId(any());
    }

    @Test
    public void testGetCardByUserIdUserNotExistException() {
        // Setup
        Integer userId = 1;
        UserDto userDto = new UserDto();
        userDto.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Verify
        assertThrows(UserNotExistException.class, () -> cardService.getCardByUserId(userDto));

        // Check if userRepository.findById was called once with userId as parameter
        verify(userRepository, times(1)).findById(userId);
        // Check if cardRepository.getCardByUserId was not called
        verify(cardRepository, times(0)).getCardByUserId(any());
    }

    @Test
    public void testGetCardByUserIdCardNotAssigned() {
        // Setup
        Integer userId = 1;
        UserDto userDto = new UserDto();
        userDto.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(cardRepository.getCardByUserId(userId)).thenReturn(null);

        // Verify
        assertThrows(CardNotAssignedException.class, () -> cardService.getCardByUserId(userDto));

        // Check if userRepository.findById was called once with userId as parameter
        verify(userRepository, times(1)).findById(userId);
        // Check if cardRepository.getCardByUserId was called once with userId as parameter
        verify(cardRepository, times(1)).getCardByUserId(userId);
    }


    @Test
    public void testGetCardByStudentId() {
        // Arrange
        StudentDto studentDto = new StudentDto();
        studentDto.setId(1);
        Card card = new Card();
        card.setId(2);
        Student student = new Student();
        student.setId(1);
        student.setCardId(card);
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));


        // Act
        Integer result = cardService.getCardByStudentId(studentDto);

        // Assert
        assertEquals(2, result);
        verify(studentRepository, times(1)).findById(any());
    }

    @Test
    public void testGetCardByStudentId_withNullId() {
        // Arrange
        StudentDto studentDto = new StudentDto();
        studentDto.setId(null);

        // Act and Assert
        assertThrows(NullFormValueException.class, () -> {
            cardService.getCardByStudentId(studentDto);
        });
        verify(studentRepository, times(0)).findById(any());
        verify(cardRepository, times(0)).findById(any());
    }

    @Test
    public void testGetCardByStudentId_withStudentNotExist() {
        // Arrange
        StudentDto studentDto = new StudentDto();
        studentDto.setId(1);
        when(studentRepository.findById(1)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(StudentNotExistException.class, () -> {
            cardService.getCardByStudentId(studentDto);
        });
        verify(studentRepository, times(1)).findById(1);
        verify(cardRepository, times(0)).findById(any());
    }

    @Test
    public void testGetCardByStudentId_withCardNotExist() {
        // Arrange
        StudentDto studentDto = new StudentDto();
        studentDto.setId(1);
        Card card = new Card();
        card.setId(null);
        Student student = new Student();
        student.setId(1);
        student.setCardId(card);
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));

        // Act and Assert
        assertThrows(CardNotAssignedException.class, () -> {
            cardService.getCardByStudentId(studentDto);
        });
        verify(studentRepository, times(1)).findById(1);
    }







}
