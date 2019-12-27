package com.zk.timerpicker.util.time;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public abstract class TimeUtil {
    public static long getNowTime(){
        return new java.util.Date().getTime();
    }

    public static long getMounthStartTime(String year, String mounth) {
        try{
            String date_str = year + mounth + "01-00:00:00";
            System.out.println(date_str);
            return new SimpleDateFormat("yyyyMMdd-hh:mm:ss").parse(date_str).getTime();
        } catch (java.text.ParseException e) {
            return 0;
        }
    }


    public static long getMounthEndTime (String year, String mounth) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(getMounthStartTime(year, mounth)));
        calendar.add(Calendar.MONTH, 1);
        return calendar.getTime().getTime();
    }

    public static long getDayStartTime(String year, String mounth, String day) {
        try{
            String date_str = year + mounth + day + "-00:00:00";
            return new SimpleDateFormat("yyyyMMdd-hh:mm:ss").parse(date_str).getTime();
        } catch (java.text.ParseException e) {
            return 0;
        }
    }

    public static long getDayEndTime (String year, String mounth, String day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(getDayStartTime(year, mounth, day)));
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime().getTime();
    }

    public static String DayToString(int i) {
        if(i < 10) {
            return ("0" + String.valueOf(i));
        } else {
            return String.valueOf(i);
        }
    }

    public static int getDaysByYearMonth(String year, String month) {
        return getDaysByYearMonth(Integer.parseInt(year), Integer.parseInt(month));
    }

    public static int getDaysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    public static String timeToString (String formatStr, long time) {
        return new SimpleDateFormat(formatStr).format(time);
    }

    public static String dateToString (String formatStr, Date date) {
        return new SimpleDateFormat(formatStr).format(date);
    }

    public static String timeToWeek (long time) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(new Date(time));

        int week  = cd.get(Calendar.DAY_OF_WEEK);
        String weekString;
        switch (week) {
            case Calendar.SUNDAY:
                weekString = "星期日";
                break;
            case Calendar.MONDAY:
                weekString = "星期一";
                break;
            case Calendar.TUESDAY:
                weekString = "星期二";
                break;
            case Calendar.WEDNESDAY:
                weekString = "星期三";
                break;
            case Calendar.THURSDAY:
                weekString = "星期四";
                break;
            case Calendar.FRIDAY:
                weekString = "星期五";
                break;
            default:
                weekString = "星期六";
                break;
        }
        return weekString;
    }

    public static Calendar newCalendarByYearMounthDay(int year, int mounth, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, mounth, day);
        return calendar;
    }

    public static Calendar newCalendarByYearMounthDayHourMinuteSecond
            (int year, int mounth, int day, int h, int m, int s) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, mounth, day, h, m, s);
        return calendar;
    }

}
