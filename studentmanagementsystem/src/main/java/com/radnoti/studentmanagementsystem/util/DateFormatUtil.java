package com.radnoti.studentmanagementsystem.util;

import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
@Service
public class DateFormatUtil {

    public Date dateFormatter(String dateString){
        try {
            String[] splitDate = dateString.substring(0,dateString.length()-2).split(" ");
            SimpleDateFormat formatter =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date =formatter.parse(splitDate[0]+ " " + splitDate[1]);
            return date;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
