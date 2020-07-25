package com.gk4u.rss.backend.util;

import com.gk4u.rss.CommaFeedConfiguration;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


public class DateUtil {

    @Autowired
    private CommaFeedConfiguration config;


    public static LocalDate addLocaltime(Date date, int amount) {

        LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        LocalDate localDate = localDateTime.toLocalDate();
        return localDate;
    }


    public static LocalDate addDays(Date date, int amount) {

        Date date1 = DateUtils.addDays(date, amount);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(date1.toInstant(), ZoneId.systemDefault());
        LocalDate localDate = localDateTime.toLocalDate();
        return localDate;
    }

}
