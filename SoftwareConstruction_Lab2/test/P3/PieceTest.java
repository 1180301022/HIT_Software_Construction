package P3;

import static org.junit.Assert.*;
import org.junit.Test;

public class PieceTest {
    /**
     * 测试策略
     * 创建棋子实例，由于Piece类只有observer成员方法，则查看输出是否等于输入信息即可
     */
    @Test
    public void testPieceMethods(){
        Piece testPiece = new Piece("sample",true, new Position(2,2));
        assertEquals("sample", testPiece.getPieceName());
        assertEquals(true,testPiece.getPieceColor());
        assertEquals(new Position(2,2),testPiece.getPiecePosition());
    }
}
