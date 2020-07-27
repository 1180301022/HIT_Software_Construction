package P1;

import org.junit.Test;
import static org.junit.Assert.*;

public class MagicSquareTest
{
    /*对于输入的N进行测试
    N根据等价类划分为：正数、0、负数；
                      奇数、偶数、浮点数（不符合参数类型）；
                      数字、非数（不符合参数类型）；
    使用笛卡儿积覆盖的方法，需要输入 正奇数、正偶数、0、负奇数、负偶数
    */
    @Test
    public void generateMagicSquareTest()
    {
        assertEquals(true,MagicSquare.generateMagicSquare(3));//正奇数，满足条件
        assertEquals(false,MagicSquare.generateMagicSquare(0));//0不满足条件
        assertEquals(false,MagicSquare.generateMagicSquare(-5));//负奇数不满足条件
        assertEquals(false,MagicSquare.generateMagicSquare(4));//正偶数不满足条件
        assertEquals(false,MagicSquare.generateMagicSquare(-4));//负偶数不满足条件
    }
}
