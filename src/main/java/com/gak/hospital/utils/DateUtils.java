package com.gak.hospital.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    private static final SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * date转String
     * @param date date
     * @return string
     */
    public static String formatYMD(Date date) {
        return df.format(date);
    }

    public static String formatYMDHMS(Date date) {
        return df2.format(date);
    }

    private DateUtils(){}
}
