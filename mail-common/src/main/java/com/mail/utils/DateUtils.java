package com.mail.utils;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * 日期处理工具
 * <p/>
 */
public final class DateUtils {

    public static final String DATE_STR = "yyyy-MM-dd";
    public static final String TIME_STR = "HH:mm:ss";
    public static final String DATETIME_STR = "yyyy-MM-dd HH:mm:ss";
    public static final String DATETIME_STR_SLASH = "yyyy/MM/dd HH:mm:ss";
    public static final String DATETIME_STAMP_SLASH = "yyyy/MM/dd HH:mm";
    public static final String DATETIME_STR_SLASH_YMD = "yyyy/MM/dd";
    public static final String DATETIME_STAMP = "yyyy-MM-dd HH:mm";
    public static final String DATEHOUR_STR = "yyyy-MM-dd HH";
    public static final long DAY_MSEC = 24l * 60 * 60 * 1000;

    public static final String DATE_STR_NOSYMBOL = "yyyyMMdd";//date字符串格式，无符号
    public static final String DAYTIME_INT_START_NO = "000000";//日时间整数开始，无符号
    public static final String DAYTIME_INT_END_NO = "235959";//日时间整数结束，无符号
    public static final String DAYTIME_STR_START = "00:00:00";//日时间整数开始
    public static final String DAYTIME_STR_END = "23:59:59";//日时间整数结束

    private final static String ZH_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final static String EN_TIME_FORMAT = "MM/dd/yyyy HH:mm:ss";
    private final static String EN_118N_FORMAT_HOUR = "MMMMM dd,yyyy hh:mm aaa";
    private final static String EN_118N_FORMAT_NO_HOUR = "MMMMM dd,yyyy";

    private DateUtils() {
    }

    /**
     * 获取当前日期
     * 格式: <code>yyyy-MM-dd</code>
     *
     * @return
     */
    public static final String getDateStr() {
        return DateFormatUtils.ISO_DATE_FORMAT.format(new Date());
    }

    public static final String getDateStr(String pattern) {
        return DateFormatUtils.format(new Date(), pattern);
    }

    /**
     * 获取当前日期和时间
     * 格式: <code>yyyy-MM-dd'T'HH:mm:ss</code>
     *
     * @return
     */
    public static final String getDateTimeStr() {
        return DateFormatUtils.ISO_DATETIME_FORMAT.format(new Date());
    }

    /**
     * 获取日期
     * 说明: n=0为当日, 1为明天, -1为昨天
     */
    public static final Date getDate(int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, n);
        return calendar.getTime();
    }

    /**
     * 格式化当前日期
     * 格式: <code>yyyy-MM-dd</code>
     *
     * @return
     */
    public static final String formatDate(Date date) {
        return DateFormatUtils.ISO_DATE_FORMAT.format(date);
    }

    /**
     * 格式化当前日期
     *
     * @return
     */
    public static final String formatDate(Date date, String pattern) {
        return DateFormatUtils.format(date, pattern);
    }

    /**
     * 解析日期
     *
     * @param dateStr
     * @param pattern
     * @return
     */
    public static final Date parseDate(String dateStr, String pattern) {
        try {
            return org.apache.commons.lang.time.DateUtils.parseDate(dateStr, new String[]{pattern});
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 日期操作
     * 说明: n=0为当前日, 1为后一天, -1为前一天
     *
     * @param n
     * @return
     */
    public static final Date getDate(Date date, int n) {
        return org.apache.commons.lang.time.DateUtils.addDays(date, n);
    }

    /**
     * 获取日期的小时位
     *
     * @param date
     * @return
     */
    public static final int getHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 从日期对象得到该日期零点的时间戳(单位秒)
     *
     * @param day 当前日期对象
     * @return 该日期零点的时间戳(单位秒)
     */
    public static Long getZeroTimeByDate(Date day) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(day);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 字符串的日期格式的计算
     */
    public static int daysBetween(String smdate, String bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(smdate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(bdate));
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    public static String getChinaStr(Date date, Locale locale, String pattern) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(StringUtils.isNotBlank(pattern) ? pattern : ZH_TIME_FORMAT, locale);
        //sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return sdf.format(date);
    }

    public static String getEnglishStr(Date date, Locale locale, String pattern) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(StringUtils.isNotBlank(pattern) ? pattern : EN_118N_FORMAT_HOUR, locale);
        //sdf.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        return sdf.format(date);
    }

    public static String getLocal(Locale locale, Date date) {
        if (locale.ENGLISH.equals(locale)) {
            return getEnglishStr(date, locale, null);
        } else {
            return getChinaStr(date, locale, null);
        }
    }

    public static String getLocal(Locale locale, Date date, String pattern) {
        if (locale.ENGLISH.equals(locale)) {
            return getEnglishStr(date, locale, pattern);
        } else {
            return getChinaStr(date, locale, pattern);
        }
    }

    /**
     * 获取time时间的年份中的最后一天
     *
     * @param time
     * @return
     */
    public static String getDateThisYearLastDay(Date time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        //设置月份
        cal.set(Calendar.MONTH, 11);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }

    /***
     * 获取时间date的年信息
     * @param date：时间
     * @return date的年
     */
    public static int getDateYear(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String year = date == null ? sdf.format(new Date()) : sdf.format(date);
        return Integer.parseInt(year);
    }

    /***
     * 获取时间date的年~月的信息
     * @param date：时间
     * @return date的年~月
     */
    public static String getMonthBeginToEnd(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        String first = sdf.format(c.getTime());

        //获取当前月最后一天
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        String last = sdf.format(c.getTime());
        return first + "~" + last;
    }

    /***
     * 获取date在一年中的第几周
     * @param date：时间
     * @return week
     */
    public static int getWeekOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    public static String getYearWeekFirstDayToLastDay(Date date) {
        int year = getYear(date);
        int week = getWeekOfYear(date);
        return getYearWeekFirstDay(year, week - 1) + "~" + getYearWeekEndDay(year, week - 1);
    }

    /**
     * 计算某年某周的开始日期(新需求，周按照周五到周四来计算)
     *
     * @param yearNum 格式 yyyy  ，必须大于1900年度 小于9999年
     * @param weekNum 1到52或者53
     * @return 日期，格式为yyyy-MM-dd
     */
    public static String getYearWeekFirstDay(int yearNum, int weekNum) {
        if (yearNum < 1900 || yearNum > 9999) {
            throw new NullPointerException("年度必须大于等于1900年小于等于9999年");
        }
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY); //设置每周的第一天为星期一
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);//每周从周一开始
//       上面两句代码配合，才能实现，每年度的第一个周，是包含第一个星期一的那个周。
        cal.setMinimalDaysInFirstWeek(7);  //设置每周最少为7天
        cal.set(Calendar.YEAR, yearNum);
        cal.set(Calendar.WEEK_OF_YEAR, weekNum);
        cal.add(Calendar.DATE, -3);

        //分别取得当前日期的年、月、日
        return getFormatDate(cal.getTime());
    }


    private static String getFormatDate(Date time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(time);
    }

    /**
     * 计算某年某周的结束日期(新需求，周按照周五到周四来计算)
     *
     * @param yearNum 格式 yyyy  ，必须大于1900年度 小于9999年
     * @param weekNum 1到52或者53
     * @return 日期，格式为yyyy-MM-dd
     */
    public static String getYearWeekEndDay(int yearNum, int weekNum) {
        if (yearNum < 1900 || yearNum > 9999) {
            throw new NullPointerException("年度必须大于等于1900年小于等于9999年");
        }
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY); //设置每周的第一天为星期一
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);//每周从周一开始
//       上面两句代码配合，才能实现，每年度的第一个周，是包含第一个星期一的那个周。
        cal.setMinimalDaysInFirstWeek(7);  //设置每周最少为7天
        cal.set(Calendar.YEAR, yearNum);
        cal.set(Calendar.WEEK_OF_YEAR, weekNum);
        cal.add(Calendar.DATE, -3);

        return getFormatDate(cal.getTime());
    }

    /**
     * 获取日期的当天的开始时间
     *
     * @param date：日期
     * @return Date
     */
    public static Date getDateStart(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取日期的当天的结束时间
     *
     * @param date：日期
     * @return Date
     */
    public static Date getDateEnd(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }


    /**
     * 获取上个月的最后一天
     *
     * @return
     */
    public static final Date getLastMothFinalyDate() {
        Date date = getDate(0);
        date.setDate(1);

        return getDate(date, -1);
    }

    /**
     * 日期加减
     *
     * @param date：日期
     * @param days：天数
     * @return Date
     */
    public static Date addOrSubtractDate(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, days);
        return cal.getTime();
    }


    public static void main(String[] args) throws ParseException {
//        System.out.println(getMonthBeginToEnd(new Timestamp(System.currentTimeMillis())));
//        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.DATE,3);
//        System.out.println(daysBetween(cal.getTime(),new Date()));
        System.out.println(getDateStart(new Date()));
        System.out.println(getDateEnd(new Date()));

    }


}
