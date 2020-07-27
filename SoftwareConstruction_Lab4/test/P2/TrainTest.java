package P2;

import org.junit.Test;
import static org.junit.Assert.*;

public class TrainTest {
    /**
     * 测试Carriage类的方法
     * 考察输出是否等于设置值
     */
    @Test
    public void testCarriageMethods(){
        Train train = new Train(1, "Canteen", 10, 10);
        assertEquals(1, train.getNumber());
        assertEquals("Canteen", train.getType());
        assertEquals(10, train.getCapacity());
        assert train.getYear()-10 < 1e-5;

        Train train1 = new Train(1, "Canteen", 10, 10);
        assertTrue(train.equals(train1));
    }
}
