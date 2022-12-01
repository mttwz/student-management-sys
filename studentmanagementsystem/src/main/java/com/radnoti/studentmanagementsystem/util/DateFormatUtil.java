package com.radnoti.studentmanagementsystem.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatUtil {

    public Date dateFormatter(String dateString){
        try {
            String[] splittedDate = dateString.substring(0,dateString.length()-2).split(" ");
            SimpleDateFormat formatter =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date =formatter.parse(splittedDate[0]+ " " + splittedDate[1]);
            return date;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
