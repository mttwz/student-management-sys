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
