package PlanningEntry;

import HelperClasses.TimePair;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class SingleTimePairTest {
    /**
     * 测试SingleTimePair类的方法
     * 考察输出是否等于设置值
     * 输入划分：和设置值相等，和设置值不等
     */
    @Test
    public void testSingleTimePair(){
        TimePair timePair1 = new TimePair("2020-04-22 14:46", "2020-04-22 14:59");
        TimePair timePair2 = new TimePair("2020-04-22 14:50", "2020-04-22 14:58");
        List<TimePair> list = new ArrayList<>();
        list.add(timePair1);

        TimeNumber time = new SingleTimePair();
        time.presetTime(list);
        assertEquals(timePair1, time.getTime().get(0));
        assertFalse(timePair2.equals(time.getTime().get(0)));
    }
}
