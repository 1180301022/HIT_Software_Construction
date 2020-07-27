package P1;

import HelperClasses.Location;
import HelperClasses.TimePair;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class FlightEntryTest {
    /**
     * 测试Flight的setter、getter方法，和查看和另一个计划项是否兼容的方法
     * 输入划分：
     * 计划项名：相同、不同
     * 计划项起止日期：相同、不同
     * 计划项起止时刻：相同、不同
     */
    @Test
    public void testFlightEntryMethods(){
        FlightEntry entry1 = new FlightEntry("test");
        //设置位置
        Location location1 = new Location("Harbin");
        Location location2 = new Location("Zhengzhou");
        List<Location> locationList = new ArrayList<>();
        locationList.add(location1);
        locationList.add(location2);
        entry1.presetLocation(locationList);
        //设置时间
        TimePair time1 = new TimePair("2020-04-29 09:00", "2020-04-29 10:00");
        List<TimePair> timeList1 = new ArrayList<>();
        timeList1.add(time1);
        entry1.presetTime(timeList1);
        //设置资源
        Plane plane = new Plane("A123", "C919", 100, 1);
        List<Plane> planeList = new ArrayList<>();
        planeList.add(plane);
        entry1.setSource(planeList);

        //测试getter方法
        assertEquals("flight", entry1.getEntryType());
        assertEquals("Harbin-Zhengzhou", entry1.getLocationToString());

        //测试是否兼容的方法
        //entry2和entry1计划项名相同，日期相同，不能兼容
        FlightEntry entry2 = new FlightEntry("test");
        TimePair time2 = new TimePair("2020-04-29 21:00", "2020-04-29 22:00");
        List<TimePair> timeList2 = new ArrayList<>();
        timeList2.add(time2);
        entry2.presetTime(timeList2);

        //entry3和entry1计划项名相同，日期不同，时刻不同，不能兼容
        FlightEntry entry3 = new FlightEntry("test");
        TimePair time3 = new TimePair("2020-04-30 21:00", "2020-04-30 22:00");
        List<TimePair> timeList3 = new ArrayList<>();
        timeList3.add(time3);
        entry3.presetTime(timeList3);

        //entry4和entry1计划项名相同，日期不同，时刻相同，可以兼容
        FlightEntry entry4 = new FlightEntry("test");
        TimePair time4 = new TimePair("2020-04-30 09:00", "2020-04-30 10:00");
        List<TimePair> timeList4 = new ArrayList<>();
        timeList4.add(time4);
        entry4.presetTime(timeList4);

        //entry5和entry1计划项名不同，可以兼容
        FlightEntry entry5 = new FlightEntry("test1");
        TimePair time5 = new TimePair("2020-04-29 09:00", "2020-04-29 10:00");
        List<TimePair> timeList5 = new ArrayList<>();
        timeList5.add(time5);
        entry5.presetTime(timeList5);
    }

    /**
     * 测试在格式要求的前提下航班名是否相等
     */
    @Test
    public void testIsNameEqual(){
        FlightEntry entry1 = new FlightEntry("CA01");
        FlightEntry entry2 = new FlightEntry("CA001");
        assertTrue(entry1.isNameEqual(entry2));
        FlightEntry entry3 = new FlightEntry("CA03");
        FlightEntry entry4 = new FlightEntry("CA002");
        assertFalse(entry3.isNameEqual(entry4));
        FlightEntry entry5 = new FlightEntry("CA123");
        FlightEntry entry6 = new FlightEntry("CA12");
        assertFalse(entry5.isNameEqual(entry6));
    }
}
