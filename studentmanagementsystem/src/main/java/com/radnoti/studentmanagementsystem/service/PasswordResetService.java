package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.exception.passwordReset.ResetCodeExpiredException;
import com.radnoti.studentmanagementsystem.exception.passwordReset.ResetCodeInvalidException;
import com.radnoti.studentmanagementsystem.exception.passwordReset.ResetCodeNotExistException;
import com.radnoti.studentmanagementsystem.exception.user.InvalidCredentialsException;
import com.radnoti.studentmanagementsystem.exception.user.UserDeletedException;
import com.radnoti.studentmanagementsystem.exception.user.UserNotActivatedException;
import com.radnoti.studentmanagementsystem.exception.user.UserNotExistException;
import com.radnoti.studentmanagementsystem.mapper.UserMapper;
import com.radnoti.studentmanagementsystem.model.dto.ResponseDto;
import com.radnoti.studentmanagementsystem.model.dto.UserDto;
import com.radnoti.studentmanagementsystem.model.entity.Passwordreset;
import com.radnoti.studentmanagementsystem.model.entity.User;
import com.radnoti.studentmanagementsystem.repository.PasswordResetRepository;
import com.radnoti.studentmanagementsystem.repository.UserRepository;
import com.radnoti.studentmanagementsystem.security.HashUtil;
import com.radnoti.studentmanagementsystem.util.IdValidatorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private static final int ACTIVATION_CODE_LENGTH = 8;

    private static final int ACTIVATION_CODE_VALID_IN_HOURS = 2;

    private final HashUtil hashUtil;

    private final UserRepository userRepository;
    private final PasswordResetRepository passwordResetRepository;

    private final UserMapper userMapper;




    /**
     * Generates a password reset code for the user with the provided username.
     *
     * @param userName The username of the user.
     * @throws UserNotExistException If the user with the provided username does not exist.
     * @throws UserDeletedException If the user has been deleted.
     * @throws UserNotActivatedException If the user has not activated their account.
     */
    @Transactional
    public void generatePasswordResetCode(String userName){
        Integer userId = userRepository.findByUsername(userName)
                .orElseThrow(UserNotExistException::new)
                .getId();

        String resetCode = hashUtil.generateRandomString(ACTIVATION_CODE_LENGTH);
        User user = userRepository.findById(userId).orElseThrow(UserNotExistException::new);
        if (user.getIsDeleted()){
            throw new UserDeletedException();
        }

        if (!user.getIsActivated()){
            throw new UserNotActivatedException();
        }
        ZonedDateTime currDate = java.time.ZonedDateTime.now();

        Passwordreset passwordreset = new Passwordreset();
        passwordreset.setUserId(user);
        passwordreset.setResetCode(resetCode);
        passwordreset.setExpireDate(currDate.plusHours(ACTIVATION_CODE_VALID_IN_HOURS));
        passwordreset.setIsUsed(false);
        passwordResetRepository.save(passwordreset);
    }



    /**
     * Resets the password of the user using the provided UserDto object.
     *
     * @param userDto The UserDto object containing the user's email, reset code, and new password.
     * @throws NoSuchAlgorithmException If the SHA256 algorithm is not available.
     * @throws ResetCodeNotExistException If the provided reset code does not exist.
     * @throws InvalidCredentialsException If the email in the UserDto object does not match the user's email.
     * @throws ResetCodeInvalidException If the provided reset code is invalid.
     */
    @Transactional
    public void resetPassword(UserDto userDto) throws NoSuchAlgorithmException {
        User userByCode = userRepository.findByResetCode(userDto.getResetCode())
                .orElseThrow(ResetCodeNotExistException::new);

        if (!Objects.equals(userByCode.getEmail(), userDto.getEmail())){
            throw new InvalidCredentialsException();
        }

        Passwordreset passwordreset = getLastValidResetCode(userByCode.getId());


        if (!Objects.equals(passwordreset.getResetCode(), userDto.getResetCode())){
            throw new ResetCodeInvalidException();

        }
        userByCode.setPassword( hashUtil.getSHA256Hash(userDto.getPassword()));
        passwordreset.setIsUsed(true);

    }


    /**
     * Retrieves the last valid password reset code for the user with the provided user ID.
     *
     * @param userId The ID of the user.
     * @return The Passwordreset object representing the last valid reset code.
     * @throws ResetCodeNotExistException If there is no valid reset code for the user.
     * @throws ResetCodeExpiredException If the last valid reset code has expired.
     */
    public Passwordreset getLastValidResetCode(Integer userId){

        Pageable pageable = PageRequest.of(0, 1);
        List<Passwordreset> passwordresetList = passwordResetRepository.getLastValidResetCode(userId,pageable);

        ZonedDateTime currDate = java.time.ZonedDateTime.now();

        if (passwordresetList.size() == 0){
            throw new ResetCodeNotExistException();
        }
        if (passwordresetList.get(0).getExpireDate().isBefore(currDate)){
            throw new ResetCodeExpiredException();
        }
        return passwordresetList.get(0);

    }
}
