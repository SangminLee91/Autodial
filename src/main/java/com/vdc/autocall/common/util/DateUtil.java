package com.vdc.autocall.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtil {
    public static String getToday(String type){
        SimpleDateFormat sdf = new SimpleDateFormat(type);
        Calendar c1 = Calendar.getInstance();

        return sdf.format(c1.getTime());
    }

}
