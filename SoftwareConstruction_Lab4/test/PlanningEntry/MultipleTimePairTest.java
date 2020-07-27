package PlanningEntry;

import HelperClasses.TimePair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MultipleTimePairTest {
    /**
     * 测试MultipleTimePair类的方法
     * 考察输出是否等于设置值
     * 输入划分：和设置值相等，和设置值不等
     */
    @Test
    public void testMultipleTimePairMethods(){
        TimePair timePair1 = new TimePair("2020-04-22 14:46", "2020-04-22 14:50");
        TimePair timePair2 = new TimePair("2020-04-22 14:51", "2020-04-22 14:58");
        TimePair timePair3 = new TimePair("2020-04-22 15:50", "2020-04-22 15:58");
        List<TimePair> list = new ArrayList<>();
        list.add(timePair1);
        list.add(timePair2);
        list.add(timePair3);

        TimeNumber time = new MultipleTimePair();
        time.presetTime(list);

        assert (time.getTime().size()==3);
        assert timePair1.equals(time.getTime().get(0));
        assert timePair2.equals(time.getTime().get(1));
        assert timePair3.equals(time.getTime().get(2));
    }
}
