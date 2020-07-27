package debug;

import org.junit.Test;
import static org.junit.Assert.*;

public class EventManagerTest {
    /**
     * 测试book方法
     * 测试策略：设计时间段测试book方法的正确性；
     *          输入错误数据检测book方法健壮性
     */
    @Test
    public void testBook(){
        //分别对应day<0; start<0; end>24; end<start四种异常输入
        assertEquals(-1, EventManager.book(0, 10, 12));
        assertEquals(-1, EventManager.book(1, -2, 12));
        assertEquals(-1, EventManager.book(1, 10, 25));
        assertEquals(-1, EventManager.book(1, 12, 10));

        //加入不同时间段验证book方法正确性
        assertEquals(1, EventManager.book(1, 10, 12));
        assertEquals(1, EventManager.book(2, 10, 12));
        assertEquals(2, EventManager.book(1, 10, 12));
        assertEquals(3, EventManager.book(1, 11, 12));
        assertEquals(3, EventManager.book(2, 9, 12));
        assertEquals(3, EventManager.book(2, 8, 12));
        assertEquals(4, EventManager.book(2, 7, 12));
    }
}
