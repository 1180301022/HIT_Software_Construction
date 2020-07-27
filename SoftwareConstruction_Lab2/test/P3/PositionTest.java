package P3;

import static org.junit.Assert.*;
import org.junit.Test;

public class PositionTest {
    /**
     * 测试策略
     * 由于在Position类中的checkRep方法保证了横纵坐标非负，所有测试只考虑坐标为非负的情况
     * 新建一个位置，调用成员方法查看输出是否等于期望
     */

    @Test
    public void testPositionMethods(){
        Position position = new Position(1,2);
        assertEquals(1,position.getX());
        assertEquals(2,position.getY());
        assertEquals("(1,2)",position.toString());
    }
}
