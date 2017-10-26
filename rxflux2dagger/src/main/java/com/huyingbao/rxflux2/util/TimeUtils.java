package com.huyingbao.rxflux2.util;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by liujunfeng on 2017/1/1.
 */
public class TimeUtils {
    public static String formatTime(long timeLong) {
        return new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(timeLong);
    }

    public static String formatTime(String createTime) {
        return createTime;
    }

    public static String formatTimeForDay(long timeLong) {
        return new SimpleDateFormat("yyyy-MM-dd").format(timeLong);
    }

    /**
     * date类型转换为String类型
     * formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
     * data Date类型的时间
     *
     * @param data
     * @param formatType
     * @return
     */
    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }


    /**
     * long类型转换为String类型
     * currentTime要转换的long类型的时间
     * formatType要转换的string类型的时间格式
     *
     * @param currentTime
     * @param formatType
     * @return
     * @throws ParseException
     */
    public static String longToString(long currentTime, String formatType) {
        Date date = null; // long类型转成Date类型
        try {
            date = longToDate(currentTime, formatType);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateToString(date, formatType); // date类型转成String
    }


    /**
     * string类型转换为date类型
     * strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
     * HH时mm分ss秒，
     * strTime的时间格式必须要与formatType的时间格式相同
     *
     * @param strTime
     * @param formatType
     * @return
     * @throws ParseException
     */
    public static Date stringToDate(String strTime, String formatType) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        return formatter.parse(strTime);
    }

    /**
     * long转换为Date类型
     * currentTime要转换的long类型的时间
     * formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
     *
     * @param currentTime
     * @param formatType
     * @return
     * @throws ParseException
     */
    public static Date longToDate(long currentTime, String formatType) throws ParseException {
        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
        return stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
    }

    /**
     * string类型转换为long类型
     * strTime要转换的String类型的时间
     * formatType时间格式
     * strTime的时间格式和formatType的时间格式必须相同
     *
     * @param strTime
     * @param formatType
     * @return
     * @throws ParseException
     */
    public static long stringToLong(String strTime, String formatType) {
        Date date = null; // String类型转成date类型
        try {
            date = stringToDate(strTime, formatType);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date == null ? System.currentTimeMillis() : dateToLong(date); // date类型转成long类型
    }


    /**
     * date类型转换为long类型
     * date要转换的date类型的时间
     *
     * @param date
     * @return
     */
    public static long dateToLong(Date date) {
        return date.getTime();
    }

    /**
     * weekday=1，当天是周日；weekday=2，当天是周一,weekday=7，当天是周六
     *
     * @return
     */
    public static int getDayWeek() {
        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        return c.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 分钟转小时
     *
     * @param min
     * @return
     */
    public static String minConvertDayHourMin(int min) {
        String format;
        Object[] array;
        Integer days = min / (60 * 24);
        Integer hours = min / (60) - days * 24;
        Integer minutes = min - hours * 60 - days * 24 * 60;
        if (days > 0) {
            format = "%1$,d天%2$,d小时%3$,d分";
            array = new Object[]{days, hours, minutes};
        } else if (hours > 0) {
            format = "%1$,d小时%2$,d分";
            array = new Object[]{hours, minutes};
        } else {
            format = "%1$,d分";
            array = new Object[]{minutes};
        }
        return String.format(format, array);
    }

    /**
     * 分钟转小时
     *
     * @param second
     * @return
     */
    public static String secondConvertDayHourMin(long second) {
        String format;
        Object[] array;
        long days = second / (60 * 60 * 24);
        long hours = second / (60 * 60) - days * 24;
        long minutes = second / 60 - hours * 60 - days * 24 * 60;
        long seconds = second - minutes * 60 - hours * 60 * 60 - days * 24 * 60 * 60;
        if (days > 0) {
            format = "%1$,d天%2$,d小时%3$,d分%4$,d秒";
            array = new Object[]{days, hours, minutes, seconds};
        } else if (hours > 0) {
            format = "%1$,d小时%2$,d分%3$,d秒";
            array = new Object[]{hours, minutes, seconds};
        } else if (minutes > 0) {
            format = "%1$,d分%2$,d秒";
            array = new Object[]{minutes, seconds};
        } else {
            format = "%1$,d秒";
            array = new Object[]{seconds};
        }
        return String.format(format, array);
    }

    /**
     * 时间转分钟
     *
     * @param day
     * @param hour
     * @param min
     * @return min
     */
    public static int dayHourMinConvertMin(int day, int hour, int min) {
        int days = day * 24 * 60;
        int hours = hour * 60;
        return days + hours + min;
    }

    /**
     * 两个时间相差距离多少小时
     *
     * @param str1 时间参数 1 格式：1990-01-01 12:00:00
     * @param str2 时间参数 2 格式：2009-01-01 12:00:00
     * @return long[] 返回值为：{天, 时, 分, 秒}
     */
    public static long getDistanceTimes(String str1, String str2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date one;
        Date two;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return day * 24 + hour;
    }

    /**
     * 起始年月日yyyy-MM-dd与终止年月日yyyy-MM-dd之间的比较。
     *
     * @param DATE1 格式为yyyy-MM-dd
     * @param DATE2 格式为yyyy-MM-dd
     * @return isFirstBig。
     */
    @SuppressLint("SimpleDateFormat")
    public static boolean CompareDate(String DATE1, String DATE2) {
        boolean isFirstBig = false;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.compareTo(dt2) == 1) {
                System.out.println("dt1 在dt2前");
                isFirstBig = true;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1在dt2后");
                isFirstBig = false;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return isFirstBig;
    }

    /**
     * 起始年月yyyy-MM与终止月yyyy-MM之间的比较。
     *
     * @param DATE1 格式为yyyy-MM
     * @param DATE2 格式为yyyy-MM
     * @return isFirstBig。
     */
    @SuppressLint("SimpleDateFormat")
    public static boolean CompareDateMonth(String DATE1, String DATE2) {
        boolean isFirstBig = false;
        DateFormat df = new SimpleDateFormat("yyyy-MM");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.compareTo(dt2) == 1) {
                System.out.println("dt1 在dt2前");
                isFirstBig = true;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1在dt2后");
                isFirstBig = false;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return isFirstBig;
    }

    /**
     * 起始年月yyyy-MM与终止月yyyy-MM之间跨度的月数。
     *
     * @param beginMonth 格式为yyyy-MM
     * @param endMonth   格式为yyyy-MM
     * @return 整数。
     */
    public static int getIntervalMonth(String beginMonth, String endMonth) {
        int intBeginYear = Integer.parseInt(beginMonth.substring(0, 4));
        int intBeginMonth = Integer.parseInt(beginMonth.substring(beginMonth.indexOf("-") + 1));
        int intEndYear = Integer.parseInt(endMonth.substring(0, 4));
        int intEndMonth = Integer.parseInt(endMonth.substring(endMonth.indexOf("-") + 1));

        return ((intEndYear - intBeginYear) * 12) + (intEndMonth - intBeginMonth) + 1;
    }

    /**
     * 求两个日期相差天数
     *
     * @param sd 起始日期，格式yyyy-MM-dd
     * @param ed 终止日期，格式yyyy-MM-dd
     * @return 两个日期相差天数
     */
    public static long getIntervalDays(String sd, String ed) {
        return ((java.sql.Date.valueOf(ed)).getTime() - (java.sql.Date.valueOf(sd)).getTime()) / (3600 * 24 * 1000);
    }

    /**
     * 得到指定月的天数
     */
    public static int getMonthLastDay(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);//把日期设置为当月第一天
        a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }
}
