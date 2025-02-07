package org.learn.utils;

import java.util.Date;

public class DateUtils {

    public static Date getDate() {
        return new Date();
    }

    public static Date getDate(long dateTime) {
        return new Date(dateTime);
    }

}
