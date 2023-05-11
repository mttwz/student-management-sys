package com.radnoti.studentmanagementsystem.util;

import com.radnoti.studentmanagementsystem.exception.form.InvalidIdException;
import org.springframework.stereotype.Component;

@Component
public class IdValidatorUtil {


    /**
     * Validates and converts the given ID string to an Integer.
     *
     * @param idAsString The ID value as a string.
     * @return The validated and converted ID as an Integer.
     * @throws InvalidIdException If the ID string is invalid or cannot be parsed as an Integer.
     */
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
