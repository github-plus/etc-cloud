package etc.cloud.commons.tool;

import java.sql.Timestamp;

public class TimestampCount {
    private TimestampCount(){};

    public static Integer Month(Timestamp start,Timestamp end)
    {
        double floor = Math.floor((end.getTime() - start.getTime()) / 2592000 / 1000);
        Integer month=(int)floor;
        return month;
    }

    public static Integer Day(Timestamp start,Timestamp end)
    {
        double floor = Math.floor((end.getTime() - start.getTime()) / 86400000);
        Integer day=(int)floor;
        return day;
    }

    public static Integer Hour(Timestamp start,Timestamp end)
    {
        double floor = Math.floor((end.getTime() - start.getTime()) / 3600000);
        Integer hour=(int)floor;
        return hour;
    }

    public static Integer Minute(Timestamp start,Timestamp end)
    {
        double floor = Math.floor((end.getTime() - start.getTime()) / 60000);
        Integer minute=(int)floor;
        return minute;
    }
}
