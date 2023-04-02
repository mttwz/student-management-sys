package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.exception.card.CardAlreadyDeletedException;
import com.radnoti.studentmanagementsystem.exception.card.CardNotAssignedException;
import com.radnoti.studentmanagementsystem.exception.card.CardNotExistException;
import com.radnoti.studentmanagementsystem.exception.form.FormValueInvalidException;
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
import com.radnoti.studentmanagementsystem.util.IdValidatorUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.*;

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
    public void createCardTest_valid() {
        // Arrange
        CardDto cardDto = new CardDto();
        cardDto.setHash("valid-hash");
        Card card = new Card();
        card.setId(1);
        card.setHash("valid-hash");
        when(cardRepository.save(any())).thenReturn(card);
        when(cardMapper.fromDtoToEntity(any())).thenReturn(card);

        // Act
        Integer cardId = cardService.createCard("valid-hash").getId();

        // Assert
        assertNotNull(cardId);
    }

    @Test
    public void createCardTest_cardDto_null() {
        // Arrange
        CardDto cardDto = null;

        // Act and Assert
        assertThrows(FormValueInvalidException.class, () -> cardService.createCard("valid-hash"));
        verifyNoInteractions(cardRepository);
    }

    @Test
    public void createCardTest_card_hash_null() {
        // Arrange
        CardDto cardDto = new CardDto();
        cardDto.setHash(null);

        // Act and Assert
        assertThrows(FormValueInvalidException.class, () -> cardService.createCard("valid-hash"));
        verifyNoInteractions(cardRepository);
    }

    @Test
    public void createCardTest_empty_hash() {
        // Arrange
        CardDto cardDto = new CardDto();
        cardDto.setHash("");

        // Act and Assert
        assertThrows(FormValueInvalidException.class, () -> cardService.createCard("valid-hash"));
        verifyNoInteractions(cardRepository);
    }


    @Test
    public void connectCardToStudentTest_valid() {
        // arrange
        StudentDto studentDto = new StudentDto();
        studentDto.setId(1);
        studentDto.setCardId(1);

        Student mockStudent = mock(Student.class);
        mockStudent.setId(1);

        Card mockCard = mock(Card.class);
        mockCard.setId(1);
        mockCard.setIsDeleted(false);
        mockCard.setIsAssigned(false);
        mockCard.setLastAssignedTo(null);

        // mock the behavior of your repositories
        when(cardRepository.findById(studentDto.getCardId())).thenReturn(Optional.of(mockCard));
        when(studentRepository.findById(studentDto.getId())).thenReturn(Optional.of(mockStudent));

        cardService.assignCardToStudent(studentDto);
        // act and assert
        verify(mockStudent,times(1)).setCardId(mockCard);
        verify(mockCard,times(1)).setIsAssigned(true);
        verify(mockCard,times(1)).setLastAssignedTo(mockStudent.getId());
    }

    @Test
    public void connectCardToStudentTest_studentId_null() {
        // arrange
        StudentDto studentDto = new StudentDto();
        studentDto.setId(null);
        studentDto.setCardId(2);

        // act and assert
        assertThrows(FormValueInvalidException.class, () -> cardService.assignCardToStudent(studentDto));
    }

    @Test
    public void connectCardToStudentTest_student_cardId_null() {
        // arrange
        StudentDto studentDto = new StudentDto();
        studentDto.setId(1);
        studentDto.setCardId(null);

        // act and assert
        assertThrows(FormValueInvalidException.class, () -> cardService.assignCardToStudent(studentDto));
    }

    @Test
    public void connectCardToStudentTest_student_not_exist() {
        // arrange
        StudentDto studentDto = new StudentDto();
        studentDto.setId(1);
        studentDto.setCardId(2);

        // mock the behavior of your repositories
        when(studentRepository.findById(studentDto.getId())).thenReturn(Optional.empty());

        // act and assert
        assertThrows(StudentNotExistException.class, () -> cardService.assignCardToStudent(studentDto));
    }

    @Test
    public void connectCardToStudentTest_card_not_exist() {
        // arrange
        StudentDto studentDto = new StudentDto();
        studentDto.setId(1);
        studentDto.setCardId(2);

        // mock the behavior of your repositories
        when(cardRepository.findById(studentDto.getCardId())).thenReturn(Optional.empty());
        when(studentRepository.findById(studentDto.getId())).thenReturn(Optional.of(new Student()));

        // act and assert
        assertThrows(CardNotExistException.class, () -> cardService.assignCardToStudent(studentDto));
    }



    @Test
    public void getCardByUserIdTest_valid() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setId(1);

        User user = new User();
        user.setId(userDto.getId());

        when(userRepository.findById(userDto.getId())).thenReturn(Optional.of(user));

        Card card = new Card();
        card.setId(1);

        when(cardRepository.getCardByUserId(userDto.getId())).thenReturn(Optional.of(card));
        when(idValidatorUtil.idValidator(userDto.getId().toString())).thenReturn(1);
        // Act
        Integer result = cardService.getCardByUserId(userDto.getId().toString()).getId();

        // Assert
        assertEquals(1, result);
    }



    @Test
    public void getCardByUserIdTest_user_not_exist() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setId(1);


        when(userRepository.findById(any())).thenReturn(Optional.empty());
        when(idValidatorUtil.idValidator(userDto.getId().toString())).thenReturn(null);

        // Act & Assert
        assertThrows(UserNotExistException.class, () -> cardService.getCardByUserId(userDto.getId().toString()));
    }

    @Test
    public void getCardByUserIdTest_card_not_exist() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setId(1);

        User user = new User();
        user.setId(1);

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(cardRepository.getCardByUserId(any())).thenReturn(Optional.empty());
//        when(cardRepository.getCardByUserId(any())).thenThrow(CardNotExistException.class);

        // Act & Assert
        assertThrows(CardNotExistException.class, () -> cardService.getCardByUserId(userDto.getId().toString()));
    }

    @Test
    public void getCardByUserIdTest_card_already_deleted(){
        UserDto userDto = new UserDto();
        userDto.setId(1);

        User user = new User();
        user.setId(1);

        Card card = new Card();
        card.setId(1);
        card.setIsDeleted(true);

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(cardRepository.getCardByUserId(any())).thenReturn(Optional.of(card));

        // Act & Assert
        assertThrows(CardAlreadyDeletedException.class, () -> cardService.getCardByUserId(userDto.getId().toString()));
    }


    @Test
    public void getCardByStudentIdTest_valid() {
        // Arrange
        StudentDto studentDto = new StudentDto();
        studentDto.setId(1);

        Card card = new Card();
        card.setId(1);

        Student student = new Student();
        student.setId(1);
        student.setCardId(card);

        when(studentRepository.findById(any())).thenReturn(Optional.of(student));

        // Act
        Integer actual = cardService.getCardByStudentId(studentDto.getId().toString()).getId();

        // Assert
        assertEquals(1, actual);
    }

    @Test
    public void getCardByStudentIdTest_student_not_exist() {
        StudentDto studentDto = new StudentDto();
        studentDto.setId(1);

        assertThrows(StudentNotExistException.class, () -> cardService.getCardByStudentId(studentDto.getId().toString()));
    }


    @Test
    public void getCardByStudentIdTest_card_not_assigned() {
        // Arrange
        StudentDto studentDto = new StudentDto();
        studentDto.setId(1);

        Student student = new Student();
        student.setId(1);
        student.setCardId(null);

        when(studentRepository.findById(any())).thenReturn(Optional.of(student));

        // Act and Assert
        assertThrows(CardNotAssignedException.class, () -> cardService.getCardByStudentId(studentDto.getId().toString()));
    }



    @Test
    public void deleteCardTest_valid(){
        CardDto cardDto = new CardDto();
        cardDto.setId(1);
        cardDto.setIsDeleted(false);

        Card mockCard = mock(Card.class);
        mockCard.setId(1);

        when(cardRepository.findById(any())).thenReturn(Optional.of(mockCard));

        cardService.deleteCard(cardDto.getId().toString());

        verify(mockCard, times(1)).setIsDeleted(true);
    }

    @Test
    public void deleteCardTest_card_not_exist(){
        CardDto cardDto = new CardDto();
        cardDto.setId(1);
        cardDto.setIsDeleted(false);

        when(cardRepository.findById(any()))
                .thenReturn(Optional.empty());

        assertThrows(CardNotExistException.class, ()-> cardService.deleteCard(cardDto.getId().toString()));

    }

    @Test
    public void deleteCardTest_card_already_deleted(){
        CardDto cardDto = new CardDto();
        cardDto.setId(1);
        cardDto.setIsDeleted(true);

        Card card = new Card();
        card.setIsDeleted(true);

        when(cardRepository.findById(any()))
                .thenReturn(Optional.of(card));

        assertThrows(CardAlreadyDeletedException.class, ()-> cardService.deleteCard(cardDto.getId().toString()));
    }

    @Test
    public void restoreDeletedCardTest_valid(){
        CardDto cardDto = new CardDto();
        cardDto.setId(1);
        cardDto.setIsDeleted(true);

        Card mockCard = mock(Card.class);
        mockCard.setId(1);
        mockCard.setIsDeleted(true);

        when(cardRepository.findById(any())).thenReturn(Optional.of(mockCard));

        cardService.deleteCard(cardDto.getId().toString());

        verify(mockCard, times(1)).setIsDeleted(false);
    }




//    @Test
//    public void testGetCardByUserId() {
//        // Setup
//        Integer userId = 1;
//        Integer cardId = 123;
//        UserDto userDto = new UserDto();
//        userDto.setId(userId);
//        Card card = new Card(cardId);
//
//        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));
//        //when(cardRepository.getCardByUserId(any())).thenReturn(cardId);
//        when(cardRepository.findById(any())).thenReturn(Optional.of(card));
//
//        // Verify
//        assertEquals(cardId, cardService.getCardByUserId(userDto.getId().toString()).getId());
//
//        // Check if userRepository.findById was called once with userId as parameter
//        verify(userRepository, times(1)).findById(userId);
//        // Check if cardRepository.getCardByUserId was called once with userId as parameter
//        verify(cardRepository, times(1)).getCardByUserId(userId);
//    }


//    @Test
//    public void testGetCardByUserIdUserNotExistException() {
//        // Setup
//        Integer userId = 1;
//        UserDto userDto = new UserDto();
//        userDto.setId(userId);
//
//        when(userRepository.findById(userId)).thenReturn(Optional.empty());
//
//        // Verify
//        assertThrows(UserNotExistException.class, () -> cardService.getCardByUserId(userDto.getId().toString()));
//
//        // Check if userRepository.findById was called once with userId as parameter
//        verify(userRepository, times(1)).findById(userId);
//        // Check if cardRepository.getCardByUserId was not called
//        verify(cardRepository, times(0)).getCardByUserId(any());
//    }

//    @Test
//    public void testGetCardByUserIdCardNotExist() {
//        // Setup
//        Integer userId = 1;
//        UserDto userDto = new UserDto();
//        userDto.setId(userId);
//
//        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
//        when(cardRepository.getCardByUserId(userId)).thenReturn(null);
//
//        // Verify
//        assertThrows(CardNotExistException.class, () -> cardService.getCardByUserId(userDto.getId().toString()));
//
//        // Check if userRepository.findById was called once with userId as parameter
//        verify(userRepository, times(1)).findById(userId);
//        // Check if cardRepository.getCardByUserId was called once with userId as parameter
//        verify(cardRepository, times(1)).getCardByUserId(userId);
//    }





//    @Test
//    public void testGetCardByStudentId_withStudentNotExist() {
//        // Arrange
//        StudentDto studentDto = new StudentDto();
//        studentDto.setId(1);
//        when(studentRepository.findById(1)).thenReturn(Optional.empty());
//
//        // Act and Assert
//        assertThrows(StudentNotExistException.class, () -> {
//            cardService.getCardByStudentId(studentDto.getId().toString());
//        });
//        verify(studentRepository, times(1)).findById(1);
//        verify(cardRepository, times(0)).findById(any());
//    }


}
