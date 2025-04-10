/**
 * 
 */
package com.apoollo.commons.util;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author liuyulong
 */
public class DateUtils {

    /**
     * N 分钟之前是否是昨天
     * @param beforeMinutes
     * @return
     */
    public static boolean beforeMinutesIsYestoday(int beforeMinutes) {
        LocalDateTime now = LocalDateTime.now();
        return beforeMinutesIsBeforeDays(now, beforeMinutes, now.toLocalDate(), 1);
    }

    
    public static boolean beforeMinutesIsBeforeDays(LocalDateTime minute, int beforeMinutes, LocalDate day,
            int beforeDays) {
        LocalDateTime beforeMinutesLocalDateTime = minute.minusMinutes(beforeMinutes);
        LocalDate beforeDaysLocalDate = day.minusDays(beforeDays);
        return beforeMinutesLocalDateTime.toLocalDate().equals(beforeDaysLocalDate);
    }
}
