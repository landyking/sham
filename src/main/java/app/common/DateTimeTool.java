package app.common;

import com.google.common.primitives.Longs;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by landy on 2017/12/22.
 */
public class DateTimeTool {

    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String YYYY_MM = "yyyy-MM";
    public static final String HH_MM_SS = "HH:mm:ss";
    public static final String HH_MM = "HH:mm";
    public static final long ONE_HOUR = 60 * 60 * 1000;
    public static final long ONE_DAY = 24 * ONE_HOUR;

    public static String toFullString(Date date) {
        if (date == null) {
            return null;
        }
        return toFullString(date.getTime());
    }

    public static String toFullString(Long date) {
        if (date == null) {
            return null;
        }
        return DateTimeFormat.forPattern(YYYY_MM_DD_HH_MM_SS).print(date);
    }

    public static Long djsj2TimeStamp(String date_str) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(YYYYMMDDHHMMSS);
            return sdf.parse(date_str).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Long queryDate2TimeStamp(String date_str) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
            return sdf.parse(date_str).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toYMDHMString(Long date) {
        if (date == null) {
            return null;
        }
        return DateTimeFormat.forPattern(YYYY_MM_DD_HH_MM).print(date);
    }

    public static String parseDJSJfor8(Long date) {
        if (date == null) {
            return null;
        }
        return DateTimeFormat.forPattern(YYYYMMDD).print(date);
    }

    public static String parseDJSJfor14(Long date) {
        if (date == null) {
            return null;
        }
        return DateTimeFormat.forPattern(YYYYMMDDHHMMSS).print(date);
    }

    public static String parseDJSJfor17(Long date) {
        if (date == null) {
            return null;
        }
        return DateTimeFormat.forPattern(YYYYMMDDHHMMSSSSS).print(date);
    }

    public static String toYMDString(Long date) {
        if (date == null) {
            return null;
        }
        return DateTimeFormat.forPattern(YYYY_MM_DD).print(date);
    }

    public static String toHMSString(Long date) {
        if (date == null) {
            return null;
        }
        return DateTimeFormat.forPattern(HH_MM_SS).print(date);
    }

    public static String toHMString(Long date) {
        if (date == null) {
            return null;
        }
        return DateTimeFormat.forPattern(HH_MM).print(date);
    }

    public static String toYMString(Long date) {
        if (date == null) {
            return null;
        }
        return DateTimeFormat.forPattern(YYYY_MM).print(date);
    }

    public static Date parseYM(String txt) {
        DateTime dateTime = DateTimeFormat.forPattern("yyyy-MM").parseDateTime(txt);
        return dateTime.toDate();
    }

    public static String consumingFmt(Long timeConsuming) {
        if (timeConsuming == null) {
            return null;
        } else {
            long hourUnit = 60 * 60 * 1000;
            long minuteUnit = 60 * 1000;
            long secondUnit = 1000;
            int hour = (int) (timeConsuming / hourUnit);
            long remain = timeConsuming - hourUnit * hour;
            int minute = (int) (remain / minuteUnit);
            remain = remain - minute * minuteUnit;
            int second = (int) (remain / secondUnit);

            remain = remain - second * secondUnit;
            StringBuilder sb = new StringBuilder();
            if (hour > 0) {
                sb.append(hour + "时");
            }
            if (minute > 0) {
                sb.append(minute + "分");
            }
            if (second > 0) {
                sb.append(second + "秒");
            }
            if (remain > 0) {
                sb.append(remain + "毫秒");
            }
            return sb.toString();
        }
    }

    public static Date currentMonth() {
        return DateTime.now().monthOfYear().roundFloorCopy().toDate();
    }

    public static Date previousMonth() {
        return DateTime.now().monthOfYear().roundFloorCopy().plusMonths(-1).toDate();
    }

    public static Date previousMonth(Long time) {
        return new DateTime(time).monthOfYear().roundFloorCopy().plusMonths(-1).toDate();
    }

    public static Date nextMonth(long time) {
        return new DateTime(time).monthOfYear().roundFloorCopy().plusMonths(1).toDate();
    }

    public static Date nextDay(long time) {
        return new DateTime(time).dayOfMonth().roundFloorCopy().plusDays(1).toDate();
    }

    public static Date parseDJSJ(String djsj) {
        return DateTimeFormat.forPattern("yyyyMMddHHmmss").parseDateTime(djsj).toDate();
    }

    public static Date parseByFMT(String data, String fmt) {
        return DateTimeFormat.forPattern(fmt).parseDateTime(data).toDate();
    }

    public static Date parseYMD(String txt) {
        DateTime dateTime = DateTimeFormat.forPattern(YYYY_MM_DD).parseDateTime(txt);
        return dateTime.toDate();
    }

    public static Date parseFull(String txt) {
        DateTime dateTime = DateTimeFormat.forPattern(YYYY_MM_DD_HH_MM_SS).parseDateTime(txt);
        return dateTime.toDate();
    }

    public static Long nowLong() {
        return System.currentTimeMillis();
    }

    public static Date nowDay() {
        return DateTime.now().dayOfMonth().roundFloorCopy().toDate();
    }

    public static Date now() {
        return DateTime.now().toDate();
    }

    public static Long toDay(Long startTime) {
        return new DateTime(startTime).dayOfMonth().roundFloorCopy().getMillis();
    }

    public static String getShortTime(long dateline) {
        String shortstring = null;
        Date date = new Date(dateline);

        long now = Calendar.getInstance().getTimeInMillis();
        long deltime = (now - date.getTime()) / 1000;
        if (deltime > 365 * 24 * 60 * 60) {
            shortstring = (int) (deltime / (365 * 24 * 60 * 60)) + "年前";
        } else if (deltime > 24 * 60 * 60) {
            shortstring = (int) (deltime / (24 * 60 * 60)) + "天前";
        } else if (deltime > 60 * 60) {
            shortstring = (int) (deltime / (60 * 60)) + "小时前";
        } else if (deltime > 60) {
            shortstring = (int) (deltime / (60)) + "分前";
        } else if (deltime > 1) {
            shortstring = deltime + "秒前";
        } else {
            shortstring = "1秒前";
        }
        return shortstring;
    }

    public static String getShortTime2(long dateline) {
        String shortstring = null;
        Date date = new Date(dateline);

        long now = Calendar.getInstance().getTimeInMillis();
        long deltime = (now - date.getTime()) / 1000;
        if (deltime > 365 * 24 * 60 * 60) {
            shortstring = (int) (deltime / (365 * 24 * 60 * 60)) + "年前";
        } else if (deltime > 24 * 60 * 60) {
            shortstring = (int) (deltime / (24 * 60 * 60)) + "天前";
        } else {
            shortstring = toHMString(dateline);
        }
        return shortstring;
    }

    public static Date tryParse(String txt) {

        List<String> pts = Arrays.asList(YYYY_MM_DD_HH_MM, YYYY_MM_DD_HH_MM_SS, YYYY_MM_DD, YYYY_MM, HH_MM_SS, HH_MM);
        for (String pt : pts) {
            try {
                Date parse = new SimpleDateFormat(pt).parse(txt);
                return parse;
            } catch (ParseException e) {
            }
        }
        Long aLong = Longs.tryParse(txt);
        if (aLong != null) {
            return new Date(aLong);
        }
        return null;
    }

    public static String toFtmString(Long date, String fmt) {
        return DateTimeFormat.forPattern(fmt).print(date);
    }
}
