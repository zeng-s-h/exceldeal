package com.zsh.excel.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * @author 小白i
 * @date 2020/9/7
 */
public class DateUtils {

    private static final String Y_M_D_H_M_SFORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String getStringDateymdhms(LocalDateTime time){
        if (time == null) {
            return "";
        }
        return DateTimeFormatter.ofPattern(Y_M_D_H_M_SFORMAT).format(time);
    }

    public static String getStringymdhmsDateByStringLocalDate(String localDateTimeType){
        if (Objects.isNull(localDateTimeType)) {
            return "";
        }
        LocalDateTime parse = LocalDateTime.parse(localDateTimeType);
        return DateTimeFormatter.ofPattern(Y_M_D_H_M_SFORMAT).format(parse);
    }

    public static boolean isLocalDateTime(String localDate){
        try {
            LocalDateTime.parse(localDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean isLocalDateTimeType(String localDate){
        try {
            LocalDateTime.parse(localDate);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
