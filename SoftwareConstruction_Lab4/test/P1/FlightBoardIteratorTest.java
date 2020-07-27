package P1;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import HelperClasses.*;

public class FlightBoardIteratorTest {
    /**
     * 由于FlightBoard的可视化，所有在此只测试迭代器
     * 查看迭代器是否能实现根据 Board上项目的 起始时间 从早到晚完成遍历
     */
    @Test
    public void testBoard() {
        FlightEntry entry1 = new FlightEntry("A");
        FlightEntry entry2 = new FlightEntry("B");
        FlightEntry entry3 = new FlightEntry("C");

        Plane plane1 = new Plane("XX", "C919", 100, 1.5);
        Plane plane2 = new Plane("YY", "XXY7", 110, 13);
        Plane plane3 = new Plane("ZZ", "PO45", 70, 2);
        List<Plane> source1 = new ArrayList<>();
        List<Plane> source2 = new ArrayList<>();
        List<Plane> source3 = new ArrayList<>();
        source1.add(plane1);
        source2.add(plane2);
        source3.add(plane3);

        Location location1 = new Location("哈尔滨");
        Location location2 = new Location("北京");
        Location location3 = new Location("郑州");
        List<Location> locations1 = new ArrayList<>();
        List<Location> locations2 = new ArrayList<>();
        List<Location> locations3 = new ArrayList<>();
        locations1.add(location1);
        locations1.add(location2);
        locations2.add(location3);
        locations2.add(location1);
        locations3.add(location2);
        locations3.add(location1);

        String startTime1 = "2020-04-29 09:00";
        String endTime1 = "2020-04-29 22:33";
        String startTime2 = "2020-04-29 09:20";
        String endTime2 = "2020-04-29 09:29";
        String startTime3 = "2020-04-29 08:00";
        String endTime3 = "2020-04-29 09:39";
        TimePair timePair1 = new TimePair(startTime1, endTime1);
        TimePair timePair2 = new TimePair(startTime2, endTime2);
        TimePair timePair3 = new TimePair(startTime3, endTime3);
        List<TimePair> list1 = new ArrayList<>();
        List<TimePair> list2 = new ArrayList<>();
        List<TimePair> list3 = new ArrayList<>();
        list1.add(timePair1);
        list2.add(timePair2);
        list3.add(timePair3);

        entry1.presetLocation(locations1);
        entry2.presetLocation(locations2);
        entry3.presetLocation(locations3);

        entry1.presetTime(list1);
        entry2.presetTime(list2);
        entry3.presetTime(list3);

        entry1.setSource(source1);
        entry2.setSource(source2);
        entry3.setSource(source3);

        entry1.run();

        List<FlightEntry> entries = new ArrayList<>();
        entries.add(entry1);
        entries.add(entry2);
        entries.add(entry3);

        FlightBoard board = new FlightBoard("哈尔滨", entries);
        Iterator iterator = board.iterator();
        while (iterator.hasNext()) {
            FlightEntry temp = (FlightEntry) iterator.next();
            System.out.println(temp.getTime().get(0).getStartTime());
        }
    }
}
