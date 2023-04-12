package com.radnoti.studentmanagementsystem.util;

import liquibase.pro.packaged.Z;
import org.hibernate.type.ZonedDateTimeType;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class DateUtil {
    public ZonedDateTime dateConverter(ZonedDateTime zonedDateTime){
        if (zonedDateTime == null){
            return null;
        }
        return  ZonedDateTime.of(zonedDateTime.getYear(),zonedDateTime.getMonthValue(), zonedDateTime.getDayOfMonth(), zonedDateTime.getHour(), zonedDateTime.getMinute(), zonedDateTime.getSecond(), 0, ZonedDateTime.now().getZone());

    }
    public boolean isSameDay(ZonedDateTime date1, ZonedDateTime date2) {

        LocalDate localDate1 = date1.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate localDate2 = date2.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        return localDate1.isEqual(localDate2);
    }



}
