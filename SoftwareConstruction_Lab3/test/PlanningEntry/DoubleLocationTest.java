package PlanningEntry;

import HelperClasses.Location;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DoubleLocationTest {
    /**
     * 测试DoubleLocation类的方法
     * 考察输出是否等于设置值
     * 输入划分：和设置值相等，和设置值不等
     */
    @Test
    public void testDoubleLocationMethods(){
        Location location1 = new Location("1");
        Location location2 = new Location("2");
        Location location3 = new Location("3");
        List<Location> list1 = new ArrayList<>();
        List<Location> list2 = new ArrayList<>();
        list1.add(location1);
        list1.add(location2);
        list2.add(location1);
        list2.add(location3);

        LocationNumber locations = new MultipleLocation();
        locations.presetLocation(list1);

        for(int i=0 ; i<locations.getLocation().size() ; i++){
            if(!list1.get(i).equals(locations.getLocation().get(i))){
                assert false;
            }
        }

        boolean flag = false;
        for(int i=0 ; i<locations.getLocation().size() ; i++){
            if(!list2.get(i).equals(locations.getLocation().get(i))){
                flag = true;
            }
        }
        assert flag;
    }
}
