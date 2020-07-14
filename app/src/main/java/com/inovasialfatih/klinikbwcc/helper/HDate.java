package com.inovasialfatih.klinikbwcc.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HDate {
    public static String toIndonesianTime(String date){
        SimpleDateFormat fromFmt = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat toFmt = new SimpleDateFormat("hh:mm");

        String result = "";
        try {
            Date dt = fromFmt.parse(date);
            result = toFmt.format(dt);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String toIndonesianFullDate(String date){
        return toIndonesianFullDate(date, "yyyy-MM-dd hh:mm:ss");
    }

    public static String toIndonesianFullDate(String date, String pattern){
        SimpleDateFormat fromFmt = new SimpleDateFormat(pattern);

        String result = "";
        try {
            Date dt = fromFmt.parse(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(dt);

            int month = cal.get(Calendar.MONTH);
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

            result = getIndonesianDay(dayOfWeek) + ", " + cal.get(Calendar.DATE) + " " + getIndonesianMonth(month) + " " + cal.get(Calendar.YEAR);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String toIndonesianFullDateTime(String date){
        return toIndonesianFullDateTime(date, "yyyy-MM-dd hh:mm:ss");
    }

    public static String toIndonesianFullDateTime(String date, String pattern){
        SimpleDateFormat fromFmt = new SimpleDateFormat(pattern);

        String result = "";
        try {
            Date dt = fromFmt.parse(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(dt);

            int month = cal.get(Calendar.MONTH);
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);

            result = getIndonesianDay(dayOfWeek) + ", " + cal.get(Calendar.DATE) + " " +
                    getIndonesianMonth(month) + " " + cal.get(Calendar.YEAR) + " " + (hour <= 9 ? "0" + hour : hour) + ":" + (minute <= 9 ? "0" + minute : minute);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

    private static String getIndonesianMonth(int month){
        String result = "";
        switch (month){
            case 0: result = "Januari"; break;
            case 1: result = "Februari"; break;
            case 2: result = "Maret"; break;
            case 3: result = "April"; break;
            case 4: result = "Mei"; break;
            case 5: result = "Juni"; break;
            case 6: result = "Juli"; break;
            case 7: result = "Agustus"; break;
            case 8: result = "September"; break;
            case 9: result = "Oktober"; break;
            case 10: result = "November"; break;
            case 11: result = "Desember"; break;
        }

        return result;
    }

    private static String getIndonesianDay(int day){
        String result = "";
        switch (day){
            case 1: result = "Minggu"; break;
            case 2: result = "Senin"; break;
            case 3: result = "Selasa"; break;
            case 4: result = "Rabu"; break;
            case 5: result = "Kamis"; break;
            case 6: result = "Jumat"; break;
            case 7: result = "Sabtu"; break;
        }

        return result;
    }

    public static String[] getDateOfDays(){
        String[] result = new String[31];

        for(int i = 1; i <= 31; i++){
            result[i-1] =  ((i<10) ? "0" : "") + i;
        }

        return result;
    }

    public static String[] getMonths(){
        String[] result = new String[12];

        for(int i = 1; i <= 12; i++){
            result[i-1] =  ((i<10) ? "0" : "") + i;
        }

        return result;
    }

    public static String[] getYears(){
        Calendar cal = Calendar.getInstance();

        String[] result = new String[(cal.get(Calendar.YEAR) - 1940) + 1];

        int counter = 0;
        for(int i = 1940; i <= cal.get(Calendar.YEAR); i++){
            result[counter] = "" + i;
            counter++;
        }

        return result;
    }

    public static String[] getYearsFuture(){
        Calendar cal = Calendar.getInstance();

        String[] result = new String[(2040 - cal.get(Calendar.YEAR)) + 1];

        int counter = 0;
        for(int i = cal.get(Calendar.YEAR); i <= 2040; i++){
            result[counter] = "" + i;
            counter++;
        }

        return result;
    }

    public static String generateExpiredDate(){
        Calendar cal = Calendar.getInstance();

        int randomYear = 1 + (int)(Math.random() * 4);
        int randomMonth = 1 + (int)(Math.random() * 8);
        int randomDay = 1 + (int)(Math.random() * 20);
        cal.add(Calendar.YEAR, randomYear);
        cal.add(Calendar.MONTH, randomMonth);
        cal.add(Calendar.DAY_OF_MONTH, randomDay);

        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

        return fmt.format(cal.getTime());
    }

    public static String secondFromNow(int second){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, second);

        SimpleDateFormat fm = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return fm.format(cal.getTime());
    }

    public static boolean isStillValidDate(String date){
        if(date.isEmpty())
            return false;

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        boolean isValid = false;
        try {
            Date nowDate = fm.parse(fm.format(cal.getTime()));
            Date expireDate = fm.parse(date);

            isValid = nowDate.before(expireDate);
            //Log.d("VALID DATE : ", date + " " + expireDate + " " + nowDate + " " + isValid);
            return isValid;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return isValid;
    }

    public static String getTimeElapsed(String departDate, String departTime,
                                        String arrivalDate, String arrivalTime, boolean ignoreDay){
        SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        try {
            Date depart = fm.parse(departDate + " " + departTime);
            Date arrival = fm.parse(arrivalDate + " " + arrivalTime);

            return calculateDifference(depart, arrival, ignoreDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String getTimeElapsed(String departDate, String departTime, int departLocal,
                                        String arrivalDate, String arrivalTime, int arrivalLocal, boolean ignoreDay){
        SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        try {
            Date depart = fm.parse(departDate + " " + departTime);
            Date arrival = fm.parse(arrivalDate + " " + arrivalTime);

            Calendar departCal = Calendar.getInstance();
            departCal.setTime(depart);
            departCal.add(Calendar.HOUR_OF_DAY, -departLocal); 
            depart = departCal.getTime();

            Calendar arrivalCal = Calendar.getInstance();
            arrivalCal.setTime(arrival);
            arrivalCal.add(Calendar.HOUR_OF_DAY, -arrivalLocal);
            arrival = arrivalCal.getTime();

            return calculateDifference(depart, arrival, ignoreDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

    private static String calculateDifference(Date startDate, Date endDate, boolean ignoreDay){
        long different = endDate.getTime() - startDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        if(!ignoreDay)
            return String.format(
                "%s%dh and %dm",
                elapsedDays > 0 ? elapsedDays + "d, " : "",
                elapsedHours, elapsedMinutes);
        else return String.format(
                "%dh and %dm", elapsedHours, elapsedMinutes);

    }
}
