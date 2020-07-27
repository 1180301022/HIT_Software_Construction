package PlanningEntry;

import HelperClasses.Location;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class SingleLocationTest {
    /**
     * 测试SingleLocation类的方法
     * 考察输出是否等于设置值
     * 输入划分：和设置值相等，和设置值不等
     */
    @Test
    public void testSingleLocationMethods(){
        List<Location> location = new ArrayList<>();
        location.add(new Location("1"));

        LocationNumber singleLocation = new SingleLocation();
        singleLocation.presetLocation(location);
        assertEquals(location.get(0), singleLocation.getLocation().get(0));

        location.remove(0);
        location.add(new Location("2"));
        singleLocation.presetLocation(location);
        assert singleLocation.getLocation().get(0).equals(new Location("1")) == false;
    }
}
