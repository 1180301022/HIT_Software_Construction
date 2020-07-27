package P1;

import org.junit.Test;
import static org.junit.Assert.*;

public class MagicSquareTest
{
    /*���������N���в���
    N���ݵȼ��໮��Ϊ��������0��������
                      ������ż�����������������ϲ������ͣ���
                      ���֡������������ϲ������ͣ���
    ʹ�õѿ��������ǵķ�������Ҫ���� ����������ż����0������������ż��
    */
    @Test
    public void generateMagicSquareTest()
    {
        assertEquals(true,MagicSquare.generateMagicSquare(3));//����������������
        assertEquals(false,MagicSquare.generateMagicSquare(0));//0����������
        assertEquals(false,MagicSquare.generateMagicSquare(-5));//����������������
        assertEquals(false,MagicSquare.generateMagicSquare(4));//��ż������������
        assertEquals(false,MagicSquare.generateMagicSquare(-4));//��ż������������
    }
}
