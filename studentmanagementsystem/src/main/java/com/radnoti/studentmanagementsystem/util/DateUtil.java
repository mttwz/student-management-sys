package com.radnoti.studentmanagementsystem.util;

import liquibase.pro.packaged.Z;
import org.hibernate.type.ZonedDateTimeType;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
public class DateUtil {
    public ZonedDateTime dateConverter(ZonedDateTime zonedDateTime){
        return  ZonedDateTime.of(zonedDateTime.getYear(),zonedDateTime.getMonthValue(), zonedDateTime.getDayOfMonth(), zonedDateTime.getHour(), zonedDateTime.getMinute(), zonedDateTime.getSecond(), 0, ZonedDateTime.now().getZone());

    }
}
