package etc.cloud.commons.tool;

import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TurnTimeStamp {

    private TurnTimeStamp() {}

    public static Timestamp TurnStamp(String timestamp)
    {
        if(!StringUtils.isEmpty(timestamp)) {
            String temp = timestamp.substring(0,10) + " 00:00:00";
            return Timestamp.valueOf(temp);
        }
        else
            return null;
    }

    public static Timestamp TurnStampEnd(String timestamp)
    {
        if(!StringUtils.isEmpty(timestamp)) {
            String temp = timestamp.substring(0,10) + " 24:00:00";
            return Timestamp.valueOf(temp);
        }
        else
            return null;
    }

    public static String TurnString(Timestamp timestamp)
    {
        if(!StringUtils.isEmpty(timestamp)) {
            //定义转换格式
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            //时间戳转换String类型
            String temp = simpleDateFormat.format(timestamp);
            return temp;
        }
        else
            return null;
    }

    public static String TurnStringSecond(Timestamp timestamp)
    {
        if(!StringUtils.isEmpty(timestamp)) {
            //定义转换格式
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //时间戳转换String类型
            String temp = simpleDateFormat.format(timestamp);
            return temp;
        }
        else
            return null;
    }

    public static String CalendarTurnString(Calendar calendar) {
        if (!StringUtils.isEmpty(calendar)) {

            //创建日历类
            Calendar calendat = Calendar.getInstance();
            //定义转换格式
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //转换
            String dateStr = sdf.format(calendar.getTime());
            return dateStr;
        } else
            return null;
    }

    public static Calendar StringTurnCalendar(String str) throws ParseException {
        if (!StringUtils.isEmpty(str)) {


            //定义转换格式
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //转换成date
            Date date = sdf.parse(str);
            //设置日历
            Calendar calendar = Calendar.getInstance();
            //设置时间
            calendar.setTime(date);
            return calendar;
        } else
            return null;
    }

    public static Date StringTurnDate(String str) throws ParseException {
        if (!StringUtils.isEmpty(str)) {

            //定义转换格式
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //转换成date
            Date date = sdf.parse(str);
            return date;
        } else
            return null;
    }


    //两日期相隔多少天

    public static int daysBetween(Date date1, Date date2) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        long time1 = cal.getTimeInMillis();
        cal.setTime(date2);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    //计算两日期相差的difftype 数
    public static int dateDiff(Date sDate, Date eDate, int diffType) {
        java.util.Calendar calst = java.util.Calendar.getInstance();
        java.util.Calendar caled = java.util.Calendar.getInstance();

        calst.setTime(sDate);
        caled.setTime(eDate);

        long diffMill = caled.getTime().getTime() - calst.getTime().getTime();
        long rtn = 0;
        switch (diffType) {
            case Calendar.MILLISECOND:
                rtn = diffMill;
                break;
            case Calendar.SECOND:
                rtn = diffMill / 1000;
                break;
            case Calendar.MINUTE:
                rtn = diffMill / 1000 / 60;
                break;
            case Calendar.HOUR:
                rtn = diffMill / 1000 / 3600;
                break;
            case Calendar.DATE:
                rtn = diffMill / 1000 / 60 / 60 / 24;
                break;
            case Calendar.MONTH:
                rtn = diffMill / 1000 / 60 / 60 / 24 / 12;
                break;
            case Calendar.YEAR:
                rtn = diffMill / 1000 / 60 / 60 / 24 / 365;
                break;
        }
        return Integer.parseInt(String.valueOf(rtn));

    }
}
