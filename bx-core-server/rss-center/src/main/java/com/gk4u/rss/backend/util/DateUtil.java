package com.gk4u.rss.backend.util;

import java.time.*;
import java.util.Date;



public class DateUtil {


    /**
     * Date转换为LocalDateTime
     * @param date
     */

    public static LocalDateTime date2LocalDate(Date date)
    {
        Instant instant = date.toInstant();//An instantaneous point on the time-line.(时间线上的一个瞬时点。)
        ZoneId zoneId = ZoneId.systemDefault();//A time-zone ID, such as {@code Europe/Paris}.(时区)
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();

//        System.out.println(localDateTime.toString());//2018-03-27T14:07:32.668
//        System.out.println(localDateTime.toLocalDate() + " " +localDateTime.toLocalTime());//2018-03-27 14:48:57.453
//
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");//This class is immutable and thread-safe.@since 1.8
//        System.out.println(dateTimeFormatter.format(localDateTime));//2018-03-27 14:52:57


        return localDateTime;
    }


    /**
     * LocalDateTime转换为Date
     * @param localDateTime
     */
    public static Date localDateTime2Date( LocalDateTime localDateTime){
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);//Combines this date-time with a time-zone to create a  ZonedDateTime.
        Date date = Date.from(zdt.toInstant());
        System.out.println(date.toString());//Tue Mar 27 14:17:17 CST 2018
        return date;
    }




}
