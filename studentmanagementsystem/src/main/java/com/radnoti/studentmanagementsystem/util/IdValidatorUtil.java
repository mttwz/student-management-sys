package com.radnoti.studentmanagementsystem.util;

import com.radnoti.studentmanagementsystem.exception.form.InvalidIdException;
import org.springframework.stereotype.Component;

@Component
public class IdValidatorUtil {

    public Integer idValidator(String idAsString){
        Integer id;
        try {
            id = Integer.parseInt(idAsString);
        }catch (NumberFormatException e){
            throw new InvalidIdException();
        }
        return id;
    }
}
