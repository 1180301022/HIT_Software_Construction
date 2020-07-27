package P1;

import org.junit.Test;
import static org.junit.Assert.*;

public class PlaneTest {
    /**
     * 测试Plane类的方法
     * 考察输出是否等于设置值
     */
    @Test
    public void testPlaneMethods(){
        Plane plane = new Plane("1", "C919", 100, 1.5);
        assertEquals("1", plane.getCode());
        assertEquals("C919", plane.getModel());
        assertEquals(100, plane.getSeatNumber());
        assert plane.getYear()-1.5 < 1e-5;

        Plane plane1 = new Plane("1", "C919", 100, 1.5);
        assertTrue(plane1.equals(plane));
    }
}
