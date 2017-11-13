package com.example.renan.cliente.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Hermanos 04 on 10/11/2017.
 */

public class DataValidador {
    public static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public static Calendar isThisDateValid(String dateToValidate){
        Calendar calendar = Calendar.getInstance();

        sdf.setLenient(false);
        try {
            //if not valid, it will throw ParseException
            calendar.setTime(sdf.parse(dateToValidate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }

    public static boolean isValidBirthday(String dateToValidate) {
        Calendar calendar = isThisDateValid(dateToValidate);

        if(dateToValidate == null){
            return false;
        }

        int year = calendar.get(Calendar.YEAR);
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        return year >= 1900 && year < thisYear-15;
    }
}
