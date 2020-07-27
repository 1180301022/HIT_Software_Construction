package HelperClasses;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * 表示一个起止时间对
 * 时间格式 yyyy-MM-dd HH:mm
 * 不可变类
 */
public class TimePair {
    private String startTime;
    private String endTime;

    //AF：起始时间为startTime，结束时间为endTime的时间对
    //RI：起始时间要在终止时间之前
    //Safety from rep exposure：所有成员域都用private修饰
    //                          不在方法中直接返回可变类型的对象

    private void checkRep(){
        assert compareTime(startTime, endTime)==1;
    }

    public TimePair(String inputStartTime, String inputEndTime){
        startTime = inputStartTime;
        endTime = inputEndTime;
        checkRep();
    }

    /**
     * 获取起始时间
     * @return 起始时间
     */
    public String getStartTime(){
        checkRep();
        return startTime;
    }

    /**
     * 获取结束时间
     * @return 结束时间
     */
    public String getEndTime(){
        checkRep();
        return endTime;
    }

    /**
     * 提取输入年份
     * @param timeToChar 转换成字符数组的单个时间
     * @return 年份
     */
    public static int getYear(char[] timeToChar){
        int k = (int)timeToChar[0] - (int)'0';
        int h = (int)timeToChar[1] - (int)'0';
        int t = (int)timeToChar[2] - (int)'0';
        int s = (int)timeToChar[3] - (int)'0';
        return k*1000 + h*100 + t*10 + s;
    }

    /**
     * 提取输入月份
     * @param timeToChar 转换成字符数组的单个时间
     * @return 月份
     */
    public static int getMonth(char[] timeToChar){
        int t = (int)timeToChar[5] - (int)'0';
        int s = (int)timeToChar[6] - (int)'0';
        return t*10 + s;
    }

    /**
     * 提取输入日期
     * @param timeToChar 转换成字符数组的单个时间
     * @return 日期
     */
    public static int getDay(char[] timeToChar){
        int t = (int)timeToChar[8] - (int)'0';
        int s = (int)timeToChar[9] - (int)'0';
        return t*10 + s;
    }

    /**
     * 提取输入小时
     * @param timeToChar 转换成字符数组的单个时间
     * @return 小时
     */
    public static int getHour(char[] timeToChar){
        int t = (int)timeToChar[11] - (int)'0';
        int s = (int)timeToChar[12] - (int)'0';
        return t*10 + s;
    }

    /**
     * 提取输入分钟
     * @param timeToChar 转换成字符数组的单个时间
     * @return 分钟
     */
    public static int getMinute(char[] timeToChar){
        int t = (int)timeToChar[14] - (int)'0';
        int s = (int)timeToChar[15] - (int)'0';
        return t*10 + s;
    }


    /**
     * 比较两个时间的关系
     * @param time1 输入时间1
     * @param time2 输入时间2
     * @return 1在2之前返回整数1； 1在2之后返回整数2； 1和2相等返回整数3
     */
    public static int compareTime(String time1, String time2){
        if(time1.equals(time2)){
            return 3;
        }
/*        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.ENGLISH);
        LocalDateTime localDateTime1 = LocalDateTime.parse(time1, dateTimeFormatter);
        LocalDateTime localDateTime2 = LocalDateTime.parse(time2, dateTimeFormatter);
        Duration duration = Duration.between(localDateTime1, localDateTime2);
        if(duration.toMinutes()<0){
            return 2;
        }
        else {
            return 1;
        }*/

        char[] time1ToArray = time1.toCharArray();
        char[] time2ToArray = time2.toCharArray();
        int year1 = getYear(time1ToArray);
        int year2 = getYear(time2ToArray);
        int month1 = getMonth(time1ToArray);
        int month2 = getMonth(time2ToArray);
        int day1 = getDay(time1ToArray);
        int day2 = getDay(time2ToArray);
        int hour1 = getHour(time1ToArray);
        int hour2 = getHour(time2ToArray);
        int minute1 = getMinute(time1ToArray);
        int minute2 = getMinute(time2ToArray);
        //年份不同
        if(year1 < year2){
            return 1;
        }
        else if(year1 > year2){
            return 2;
        }
        //年份相同
        else {
            //月份不同
            if(month1 < month2){
                return 1;
            }
            else if(month1 > month2){
                return 2;
            }
            //月份相同
            else {
                //日期不同
                if(day1 < day2){
                    return 1;
                }
                else if(day1 > day2){
                    return 2;
                }
                //日期相同
                else {
                    //小时不同
                    if(hour1 < hour2){
                        return 1;
                    }
                    else if(hour1 > hour2){
                        return 2;
                    }
                    //小时相同
                    else {
                        //分钟不同
                        if(minute1 < minute2){
                            return 1;
                        }
                        else if(minute1 > minute2){
                            return 2;
                        }
                        //分钟相同，则两时间完全相同
                        else {
                            return 3;
                        }
                    }
                }
            }
        }
    }

    /**
     * 比较两个TimePair的关系
     * @param inputTime 输入TimePair
     * @return 如果输入TimePair在该TimePair之前，返回1；在之后返回2；有交叉返回3
     */
    public int compareTimePair(TimePair inputTime){
        //首先处理特殊情况1：两个TimePair的起始时间或终止时间相等，一定存在交叉
        if((compareTime(startTime, inputTime.getStartTime())==3) ||
                (compareTime(endTime, inputTime.getEndTime())==3)){
            checkRep();
            return 3;
        }
        //特殊情况2：其中一个起始时间和另一个终止时间相等
        if(compareTime(startTime, inputTime.getEndTime())==3){
            checkRep();
            return 1;
        }
        if(compareTime(endTime, inputTime.getStartTime())==3){
            checkRep();
            return 2;
        }
        //一般情况：两TimePair的起始、结束（共四个时间点）均不相等
        //1.两个TimePair不相交
        if(compareTime(endTime, inputTime.getStartTime())==1){
            checkRep();
            return 2;
        }
        if(compareTime(startTime, inputTime.getEndTime())==2){
            checkRep();
            return 1;
        }
        //2.两个TimePair相交
        checkRep();
        return 3;
    }

    @Override
    public String toString(){
        checkRep();
        return "(" + startTime + ", " + endTime + ")";
    }

    @Override
    public int hashCode(){
        return startTime.hashCode() + endTime.hashCode();
    }

    @Override
    public boolean equals(Object obj){
        if(obj instanceof TimePair){
            TimePair temp = (TimePair) obj;
            if((compareTime(startTime, temp.startTime)==3) && (compareTime(endTime, temp.getEndTime())==3)){
                return true;
            }
        }
        return false;
    }
}

