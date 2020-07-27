package P2;

import HelperClasses.Location;
import HelperClasses.TimePair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TrainBoardIteratorTest {
    /**
     * 由于TrainBoard的可视化，所以在此只测试迭代器
     * 查看迭代器是否能实现根据 Board上项目的 起始时间 从早到晚完成遍历
     */
    @Test
    public void testBoard(){
        Location beijing = new Location("北京");
        Location zhengzhou = new Location("郑州");
        Location harbin = new Location("哈尔滨");
        List<Location> location1 = new ArrayList<>();
        List<Location> location2 = new ArrayList<>();
        List<Location> location3 = new ArrayList<>();
        location1.add(beijing);
        location1.add(harbin);
        location1.add(zhengzhou);
        location2.add(harbin);
        location2.add(beijing);
        location2.add(zhengzhou);
        location3.add(beijing);
        location3.add(zhengzhou);
        location3.add(harbin);


        TimePair timePair11 = new TimePair("2020-04-29 15:00", "2020-04-29 16:00");//北京-哈尔滨
        TimePair timePair12 = new TimePair("2020-04-29 16:10","2020-04-29 17:00");//哈尔滨-郑州
        TimePair timePair21 = new TimePair("2020-04-29 16:00", "2020-04-29 17:00");//哈尔滨-北京
        TimePair timePair22 = new TimePair("2020-04-29 17:10","2020-04-29 18:00");//北京-郑州
        TimePair timePair31 = new TimePair("2020-04-29 13:00", "2020-04-29 14:00");//北京-郑州
        TimePair timePair32 = new TimePair("2020-04-29 14:10","2020-04-29 15:50");//郑州-哈尔滨
        List<TimePair> time1 = new ArrayList<>();
        List<TimePair> time2 = new ArrayList<>();
        List<TimePair> time3 = new ArrayList<>();
        time1.add(timePair11);
        time1.add(timePair12);
        time2.add(timePair21);
        time2.add(timePair22);
        time3.add(timePair31);
        time3.add(timePair32);

        TrainEntry entry1 = new TrainEntry("1");
        entry1.presetLocation(location1);
        entry1.presetTime(time1);
        TrainEntry entry2 = new TrainEntry("2");
        entry2.presetLocation(location2);
        entry2.presetTime(time2);
        TrainEntry entry3 = new TrainEntry("3");
        entry3.presetLocation(location3);
        entry3.presetTime(time3);

        List<TrainEntry> entries = new ArrayList<>();
        entries.add(entry1);
        entries.add(entry2);
        entries.add(entry3);
        Iterator<TrainEntry> iterator = new TrainBoard("哈尔滨", entries).iterator();
        while (iterator.hasNext()){
            TrainEntry tempEntry = iterator.next();
            System.out.println(tempEntry.getTime().get(0).getStartTime());
        }
    }

}
