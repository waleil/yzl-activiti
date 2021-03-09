package cn.net.yzl.activiti.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class DateUtils{
 
    /**
     * LocalDate转Date
     * @param localDate
     * @return
     */
    public static Date localDate2Date(LocalDate localDate) {
        if (null == localDate) {
            return null;
        }
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }
 
    /**
     * Date转LocalDate
     * @param date
     */
    public static LocalDate date2LocalDate(Date date) {
        if(null == date) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * 计算商品剩余天数
     * @param nowLocalDate
     * @param productDate
     * @param periodDay
     * @return
     */
    public static int  getSurplusDay(LocalDate nowLocalDate,LocalDate productDate,int periodDay){
        LocalDate localDate = nowLocalDate.plusDays(periodDay);
        int day = (int)(productDate.toEpochDay() - localDate.toEpochDay());
        return day;

    }

}