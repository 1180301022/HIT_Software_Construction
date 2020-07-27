package HelperClasses;

import org.junit.Test;


public class TimePairTest {
    /**
     * 创建TimePair对象并调用get方法，验证输出是否符合实际结果
     */
    @Test
    public void testGetYearMonthDayHourMinute(){
        String startTime = "2020-04-20 22:32";
        String endTime = "2020-04-20 22:33";
        TimePair timePair = new TimePair(startTime, endTime);
        char[] startTimeToArray = startTime.toCharArray();
        assert timePair.getYear(startTimeToArray) == 2020;
        assert timePair.getMonth(startTimeToArray) == 4;
        assert timePair.getDay(startTimeToArray) == 20;
        assert timePair.getHour(startTimeToArray) == 22;
        assert timePair.getMinute(startTimeToArray) == 32;
    }

    /**
     * 测试比较单个时间的方法
     * 输入划分：参数1在参数2之前，参数1在参数2之后，参数1和参数2相等
     */
    @Test
    public void testCompareTime(){
        String startTime = "2020-04-20 22:32";
        String endTime = "2021-04-20 22:33";
        String time = "2020-04-20 22:32";
        TimePair timePair = new TimePair(startTime, endTime);
        assert timePair.compareTime(startTime, endTime)==1;
        assert timePair.compareTime(endTime, startTime)==2;
        assert timePair.compareTime(startTime, time)==3;
    }

    /**
     * 测试比较时间序对的结果
     * 输入划分：两个TimePair的起始时间或终止时间相等，其中一个起始时间和另一个终止时间相等，两TimePair的起始、结束（共四个时间点）均不相等
     */
    @Test
    public void testCompareTimePair(){
        String startTime1 = "2020-04-20 22:32";
        String endTime1 = "2020-04-20 22:33";
        String startTime2 = "2020-04-21 22:32";
        String endTime2 = "2021-04-21 22:33";
        String startTime3 = "2020-04-20 22:32";
        String endTime3 = "2020-04-20 23:10";
        String startTime4 = "2020-04-20 22:33";
        String endTime4 = "2020-04-21 10:10";
        TimePair timePair1 = new TimePair(startTime1, endTime1);
        TimePair timePair2 = new TimePair(startTime2, endTime2);
        TimePair timePair3 = new TimePair(startTime3, endTime3);
        TimePair timePair4 = new TimePair(startTime4, endTime4);
        assert timePair1.compareTimePair(timePair2)==2;
        assert timePair2.compareTimePair(timePair1)==1;
        assert timePair1.compareTimePair(timePair3)==3;
        assert timePair1.compareTimePair(timePair4)==2;
    }
}
