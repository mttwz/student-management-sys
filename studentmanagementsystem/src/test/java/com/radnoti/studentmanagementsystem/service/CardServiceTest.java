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


    /**
     * Test case to verify that the createCard() method successfully creates a new card when provided with a unique card hash.
     * The test sets up the necessary objects and mocks, including a card hash and a mock behavior for the cardRepository.
     * It configures the mock behavior for the cardRepository to return an empty Optional when searching for the card hash and to return the specified card when saving a new card.
     * The createCard() method is then called with the card hash.
     * Finally, it asserts that the response ID matches the ID of the created card.
     */
    @Test
    public void testCreateCard_Success() {

        String cardHash = "cardHash";
        Mockito.when(cardRepository.findByHash(cardHash)).thenReturn(Optional.empty());

        Card card = new Card();
        card.setHash(cardHash);
        card.setCreatedAt(ZonedDateTime.now());
        card.setIsDeleted(false);
        card.setIsAssigned(false);

        Mockito.when(cardRepository.save(Mockito.any(Card.class))).thenReturn(card);


        ResponseDto response = cardService.createCard(cardHash);


        assertEquals(card.getId(), response.getId());
    }


    /**
     * Test case to verify that the createCard() method throws a FormValueInvalidException when the card hash is missing or null.
     * The test does not require any setup or mocks.
     * The createCard() method is then called with a null value for the card hash using an assertThrows statement to catch the expected FormValueInvalidException.
     */
    @Test
    public void testCreateCard_MissingCardHash_ThrowsFormValueInvalidException() {



        assertThrows(FormValueInvalidException.class, () -> {
            cardService.createCard(null);
        });
    }

    /**
     * Test case to verify that the createCard() method throws a CardAlreadyAssignedException when the card hash is already assigned to a card.
     * The test sets up the necessary objects and mocks, including a card hash and a mock behavior for the cardRepository.
     * It configures the mock behavior for the cardRepository to return an Optional containing a card when searching for the card hash.
     * The createCard() method is then called with the card hash using an assertThrows statement to catch the expected CardAlreadyAssignedException.
     */
    @Test
    public void testCreateCard_CardAlreadyAssigned_ThrowsCardAlreadyAssignedException() {

        String cardHash = "cardHash";
        Optional<Card> optionalCard = Optional.of(new Card());
        Mockito.when(cardRepository.findByHash(cardHash)).thenReturn(optionalCard);


        assertThrows(CardAlreadyAssignedException.class, () -> {
            cardService.createCard(cardHash);
        });
    }


    /**
     * Test case to verify that the deleteCard() method successfully deletes a card when provided with a valid card ID.
     * The test sets up the necessary objects and mocks, including a card ID, a card object, and mock behaviors for the idValidatorUtil, cardRepository, and studentRepository.
     * It configures the mock behavior for the idValidatorUtil to return the card ID when validating the ID string.
     * It configures the mock behavior for the cardRepository to return the specified card when searching for the card by ID.
     * It configures the mock behavior for the studentRepository to return an Optional containing a student when searching for the last assigned student.
     * The deleteCard() method is then called with the card ID.
     * Finally, it asserts that the card is marked as deleted, not assigned, and the deletedAt field is not null.
     */
    @Test
    public void testDeleteCard() {

        String cardIdString = "1";
        Integer cardId = 1;
        Card card = new Card();
        card.setId(cardId);
        card.setIsDeleted(false);
        card.setLastAssignedTo(123);

        when(idValidatorUtil.idValidator(cardIdString)).thenReturn(cardId);
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
        when(studentRepository.findById(card.getLastAssignedTo())).thenReturn(Optional.of(new Student()));


        cardService.deleteCard(cardIdString);


        assertTrue(card.getIsDeleted());
        assertFalse(card.getIsAssigned());
        assertNotNull(card.getDeletedAt());
    }

    /**
     * Test case to verify that the deleteCard() method throws a CardNotExistException when provided with an invalid card ID.
     * The test sets up the necessary objects and mocks, including an invalid card ID and a mock behavior for the idValidatorUtil.
     * It configures the mock behavior for the idValidatorUtil to throw a CardNotExistException when validating the ID string.
     * The deleteCard() method is then called with the invalid card ID using an assertThrows statement to catch the expected CardNotExistException.
     */
    @Test
    public void testDeleteCard_InvalidCardId() {

        String cardIdString = "invalid";

        when(idValidatorUtil.idValidator(cardIdString)).thenThrow(new CardNotExistException());


        assertThrows(CardNotExistException.class, () -> {
            cardService.deleteCard(cardIdString);
        });

    }

    /**
     * Test case to verify that the deleteCard() method throws a CardAlreadyDeletedException when attempting to delete an already deleted card.
     * The test sets up the necessary objects and mocks, including a card ID and a mock behavior for the cardRepository.
     * It configures the mock behavior for the cardRepository to return the specified card with the isDeleted field set to true when searching for the card by ID.
     * The deleteCard() method is then called with the card ID using an assertThrows statement to catch the expected CardAlreadyDeletedException.
     */
    @Test
    public void testDeleteCard_CardAlreadyDeleted() {

        String cardIdString = "deleted";
        Card card = new Card();
        card.setId(1);
        card.setIsDeleted(true);

        when(idValidatorUtil.idValidator(cardIdString)).thenReturn(1);
        when(cardRepository.findById(1)).thenReturn(Optional.of(card));


        assertThrows(CardAlreadyDeletedException.class, () -> {
            cardService.deleteCard(cardIdString);
        });

    }

    /**
     * Test case for restoring a deleted card with a valid card ID.
     * It mocks the dependencies and test data necessary for the test.
     * Calls the method to be tested, which restores the deleted card.
     * Verifies the student and card state after the method call.
     */
    @Test
    public void testRestoreDeletedCard_ValidCardId() {

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


        cardService.restoreDeletedCard(cardIdString);


        assertEquals(card, student.getCardId());
        assertTrue(student.getCardId() != null && student.getCardId().equals(card));
        assertTrue(card.getIsAssigned());
        assertFalse(card.getIsDeleted());
    }


    /**
     * Test case for restoring a deleted card with an invalid card ID.
     * It mocks the dependencies and test data necessary for the test.
     * Calls the method to be tested and asserts the thrown exception.
     */
    @Test
    public void testRestoreDeletedCard_InvalidCardId() {

        String cardIdString = "invalid";

        when(idValidatorUtil.idValidator(cardIdString)).thenThrow(new InvalidIdException());


        assertThrows(InvalidIdException.class, () -> {
            cardService.restoreDeletedCard(cardIdString);
        });
    }

    /**
     * Test case for restoring a deleted card when the card does not exist.
     * It mocks the dependencies and test data necessary for the test.
     * Calls the method to be tested and asserts the thrown exception.
     */
    @Test
    public void testRestoreDeletedCard_CardNotExist() {

        String cardIdString = "1";
        Integer cardId = 1;

        when(idValidatorUtil.idValidator(cardIdString)).thenReturn(cardId);
        when(cardRepository.findById(cardId)).thenReturn(Optional.empty());


        assertThrows(CardNotExistException.class, () -> {
            cardService.restoreDeletedCard(cardIdString);
        });
    }

    /**
     * Test case for restoring a deleted card when the card is not deleted.
     * It mocks the dependencies and test data necessary for the test.
     * Calls the method to be tested and asserts the thrown exception.
     */
    @Test
    public void testRestoreDeletedCard_CardNotDeleted() {

        String cardIdString = "1";
        Integer cardId = 1;
        Card card = new Card();
        card.setId(cardId);
        card.setIsDeleted(false);

        when(idValidatorUtil.idValidator(cardIdString)).thenReturn(cardId);
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));


        assertThrows(CardNotDeletedException.class, () -> {
            cardService.restoreDeletedCard(cardIdString);
        });
    }

    /**
     * Test case for restoring a deleted card when the student's card is not assigned.
     * It mocks the dependencies and test data necessary for the test.
     * Calls the method to be tested.
     * Verifies the student and card state after the method call.
     */
    @Test
    public void testRestoreDeletedCard_StudentCardNotAssigned() {

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


        cardService.restoreDeletedCard(cardIdString);


        assertEquals(card, student.getCardId());
        assertTrue(card.getIsAssigned());
        assertFalse(card.getIsDeleted());

    }


    /**
     * Test case for assigning a card to a student with an invalid StudentDto object.
     * It mocks the dependencies and test data necessary for the test.
     * Calls the method to be tested and asserts the thrown exception.
     */
    @Test
    public void testAssignCardToStudent_InvalidStudentDto() {

        StudentDto studentDto = new StudentDto();


        assertThrows(FormValueInvalidException.class, () -> {
            cardService.assignCardToStudent(studentDto);
        });

    }


    /**
     * Test case for retrieving a card by user ID.
     * It mocks the dependencies and test data necessary for the test.
     * Calls the method to be tested and verifies the returned response.
     */
    @Test
    public void testGetCardByUserId() {

        String userIdString = "123";

        User user = new User();
        user.setId(123);

        Card card = new Card();
        card.setId(456);
        card.setIsDeleted(false);

        when(idValidatorUtil.idValidator(userIdString)).thenReturn(123);
        when(userRepository.findById(123)).thenReturn(Optional.of(user));
        when(cardRepository.getCardByUserId(123)).thenReturn(Optional.of(card));


        ResponseDto responseDto = cardService.getCardByUserId(userIdString);


        assertNotNull(responseDto);
        assertEquals(456, responseDto.getId());
    }


    /**
     * Test case for retrieving a card by user ID when the user does not exist.
     * It mocks the dependencies and test data necessary for the test.
     * Calls the method to be tested and asserts the thrown exception.
     */
    @Test
    public void testGetCardByUserId_UserNotExist() {

        String userIdString = "123";

        when(idValidatorUtil.idValidator(userIdString)).thenReturn(123);
        when(userRepository.findById(123)).thenReturn(Optional.empty());


        assertThrows(UserNotExistException.class, () -> {
            cardService.getCardByUserId(userIdString);
        });
    }

    /**
     * Test case for retrieving a card by user ID when the card does not exist.
     * It mocks the dependencies and test data necessary for the test.
     * Calls the method to be tested and asserts the thrown exception.
     */
    @Test
    public void testGetCardByUserId_CardNotExist() {

        String userIdString = "123";

        User user = new User();
        user.setId(123);

        when(idValidatorUtil.idValidator(userIdString)).thenReturn(123);
        when(userRepository.findById(123)).thenReturn(Optional.of(user));
        when(cardRepository.getCardByUserId(123)).thenReturn(Optional.empty());


        assertThrows(CardNotExistException.class, () -> {
            cardService.getCardByUserId(userIdString);
        });
    }

    /**
     * Test case for retrieving a card by user ID when the card is already deleted.
     * It mocks the dependencies and test data necessary for the test.
     * Calls the method to be tested and asserts the thrown exception.
     */
    @Test
    public void testGetCardByUserId_CardAlreadyDeleted() {

        String userIdString = "123";

        User user = new User();
        user.setId(123);

        Card card = new Card();
        card.setId(456);
        card.setIsDeleted(true);

        when(idValidatorUtil.idValidator(userIdString)).thenReturn(123);
        when(userRepository.findById(123)).thenReturn(Optional.of(user));
        when(cardRepository.getCardByUserId(123)).thenReturn(Optional.of(card));


        assertThrows(CardAlreadyDeletedException.class, () -> {
            cardService.getCardByUserId(userIdString);
        });
    }


    /**
     * Test case for retrieving a card by student ID.
     * It mocks the dependencies and test data necessary for the test.
     * Calls the method to be tested and verifies the returned response.
     */
    @Test
    public void testGetCardByStudentId() {

        String studentIdString = "123";

        Card card = new Card();
        card.setId(456);

        Student student = new Student();
        student.setId(123);
        student.setCardId(card);

        when(idValidatorUtil.idValidator(studentIdString)).thenReturn(123);
        when(studentRepository.findById(123)).thenReturn(Optional.of(student));



        ResponseDto responseDto = cardService.getCardByStudentId(studentIdString);


        assertNotNull(responseDto);
        assertEquals(456, responseDto.getId());

    }

    /**
     * Test case for retrieving a card by student ID when the student does not exist.
     * It mocks the dependencies and test data necessary for the test.
     * Calls the method to be tested and asserts the thrown exception.
     */
    @Test
    public void testGetCardByStudentId_StudentNotExist() {
        String studentIdString = "123";

        when(idValidatorUtil.idValidator(studentIdString)).thenReturn(123);
        when(studentRepository.findById(123)).thenReturn(Optional.empty());

        assertThrows(StudentNotExistException.class, () -> {
            cardService.getCardByStudentId(studentIdString);
        });
    }

    /**
     * Test case for retrieving a card by student ID when the card is not assigned.
     * It mocks the dependencies and test data necessary for the test.
     * Calls the method to be tested and asserts the thrown exception.
     */
    @Test
    public void testGetCardByStudentId_CardNotAssigned() {
        String studentIdString = "123";

        Student student = new Student();
        student.setId(123);
        student.setCardId(null);

        when(idValidatorUtil.idValidator(studentIdString)).thenReturn(123);
        when(studentRepository.findById(123)).thenReturn(Optional.of(student));

        assertThrows(CardNotAssignedException.class, () -> {
            cardService.getCardByStudentId(studentIdString);
        });
    }


    /**
     *
     * Test case for retrieving all cards with pagination.
     * It mocks the dependencies and test data necessary for the test.
     * Calls the method to be tested and verifies the returned response.
     * Additional assertions can be added as needed.
     */
    @Test
    public void testGetAllCard() {

        Pageable pageable = PageRequest.of(0, 10);

        List<Card> cardList = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            Card card = new Card();
            card.setId(i);
            cardList.add(card);
        }
        Page<Card> cardPage = new PageImpl<>(cardList, pageable, 20);

        when(cardRepository.findAll(pageable)).thenReturn(cardPage);


        PagingDto pagingDto = cardService.getAllCard(pageable);


        assertNotNull(pagingDto);
        assertEquals(2, pagingDto.getAllPages());
        assertEquals(20, pagingDto.getCardDtoList().size());

    }






}












