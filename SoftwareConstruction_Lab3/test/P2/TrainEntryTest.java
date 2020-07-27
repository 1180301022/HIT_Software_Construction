package P2;

import HelperClasses.Location;
import HelperClasses.TimePair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TrainEntryTest {
    /**
     * 测试TrainEntry的getter、setter方法
     */
    @Test
    public void testTrainEntryMethods(){
        TrainEntry entry = new TrainEntry("test");

        Location beijing = new Location("北京");
        Location zhengzhou = new Location("郑州");
        Location harbin = new Location("哈尔滨");
        List<Location> location1 = new ArrayList<>();
        location1.add(beijing);
        location1.add(harbin);
        location1.add(zhengzhou);

        TimePair timePair11 = new TimePair("2020-04-29 15:00", "2020-04-29 16:00");//北京-哈尔滨
        TimePair timePair12 = new TimePair("2020-04-29 16:10","2020-04-29 17:00");//哈尔滨-郑州
        List<TimePair> time1 = new ArrayList<>();
        time1.add(timePair11);
        time1.add(timePair12);

        Train train1 = new Train(1, "engine", 100, 1);
        Train train2 = new Train(2, "canteen", 10, 5);
        List<Train> trains = new ArrayList<>();
        trains.add(train1);
        trains.add(train2);

        entry.presetLocation(location1);
        entry.presetTime(time1);
        entry.setSource(trains);

        assert entry.getLocationToString().equals("北京-郑州");

        for(Location temp : entry.getLocation()){
            System.out.println(temp.getLocationName());
        }

        for(TimePair temp : entry.getTime()){
            System.out.println(temp.toString());
        }

        for(Train temp : entry.getSource()){
            System.out.println(temp.toString());
        }
    }
}
