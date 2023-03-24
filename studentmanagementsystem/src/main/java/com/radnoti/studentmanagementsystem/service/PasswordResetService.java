package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.exception.user.UserDeletedException;
import com.radnoti.studentmanagementsystem.exception.user.UserNotActivatedException;
import com.radnoti.studentmanagementsystem.exception.user.UserNotExistException;
import com.radnoti.studentmanagementsystem.model.entity.Passwordreset;
import com.radnoti.studentmanagementsystem.model.entity.User;
import com.radnoti.studentmanagementsystem.repository.PasswordResetRepository;
import com.radnoti.studentmanagementsystem.repository.UserRepository;
import com.radnoti.studentmanagementsystem.security.HashUtil;
import com.radnoti.studentmanagementsystem.util.IdValidatorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private static final int ACTIVATION_CODE_LENGTH = 8;

    private static final int ACTIVATION_CODE_VALID_IN_HOURS = 2;

    private final HashUtil hashUtil;

    private final UserRepository userRepository;
    private final PasswordResetRepository passwordResetRepository;



    @Transactional
    public void resetPassword(String userName){
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
}
