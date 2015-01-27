package my.lib.time;

/**
 * 출처 : http://blog.bbirec.com/post/51151519159/android-timeago
 *  
 * @author yeojoy
 *
 */

import java.util.Calendar;
import java.util.Date;

import my.lib.UtilConstants;
 
public class TimeAgo implements UtilConstants {
    
    public static String timeAgoString(Date date) {
        Calendar nowCal = Calendar.getInstance();
 
        Calendar targetCal = Calendar.getInstance();
        if(date != null){
            targetCal.setTime(date);
        }
 
        long curTime = nowCal.getTimeInMillis();
        long targetTime = targetCal.getTimeInMillis();
        long diff = curTime - targetTime;
 
        if(targetTime > curTime) {
            return "방금";
        }
        else {
            if(diff < MIN_SECONDS) {
                return "방금";
            }
            else if(diff < ONE_MINUTE) {
                return diff + "초 전";
            }
            else if(diff < ONE_HOUR) {
                return diff / ONE_MINUTE + "분 전";
            }
            else if(diff < ONE_DAY) {
                return diff / ONE_HOUR + "시간 전";
            }
            else if(diff < ONE_MONTH) {
                long diffDay = diff / ONE_DAY;
                if(diffDay == 1) {
                    return "하루 전";
                }
                else {
                    return diffDay + "일 전";
                }
            }
            else if(diff < ONE_YEAR) {
                long diffMonth = diff / ONE_MONTH;
                if(diffMonth == 1) {
                    return "한달 전";
                }
                else {
                    return diffMonth + "달 전";
                }
            }
            else {
                long diffYear = diff / ONE_YEAR;
                if(diffYear == 1) {
                    return "작년";
                }
                else {
                    return diffYear + "년 전";
                }
            }
        }
 
    }
}