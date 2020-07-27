package P3;

import static org.junit.Assert.*;
import org.junit.Test;

public class BoardTest {
    /**
     * 测试策略
     * 针对棋盘上的各种行为分别进行测试
     * 具体测试方法标注在各方法头
     */

    /**
     * 测试向棋盘中添加棋子 和 判断棋盘上某位置是否满足放置棋子的条件
     * 添加棋子分类：棋子不在棋盘上、棋子已在棋盘上
     * 输入位置分类：位置在棋盘上且无棋子、位置在棋盘上且有棋子、位置不在棋盘上
     */
    @Test
    public void testAddPieceToBoardAndIsLegalPositionToPutPiece(){
        Board board = new Board(8);
        Piece piece11 = new Piece("piece11",true,new Position(1,1));
        assertTrue(board.isLegalPositionToPutPiece(new Position(1,1)));
        board.addPieceToBoard(piece11);
        assertFalse(board.isLegalPositionToPutPiece(new Position(1,1)));
        assertFalse(board.isLegalPositionToPutPiece(new Position(9,5)));
        assertTrue(board.isLegalPositionToPutPiece(new Position(0,0)));
    }

    /**
     * 测试从棋盘上移除棋子
     * 输入分类：待移除棋子在棋盘上、待移除棋子不在棋盘上
     */
    @Test
    public void testRemovePieceFromBoard(){
        Board board = new Board(8);
        Piece piece11 = new Piece("piece11",true,new Position(1,1));
        board.addPieceToBoard(piece11);
        board.removePieceFromBoard(piece11);
        assertTrue(board.isLegalPositionToPutPiece(piece11.getPiecePosition()));
    }

    /**
     * 测试检测棋盘上某位置的情况
     * 输入分类：该位置有棋子、该位置无棋子
     */
    @Test
    public void testGetPiece(){
        Board board = new Board(8);
        Piece piece11 = new Piece("piece11",true,new Position(1,1));
        board.addPieceToBoard(piece11);
        assertEquals(piece11, board.getPiece(new Position(1, 1)));
        assertEquals(null, board.getPiece(new Position(2,1)));
    }

    /**
     * 测试计算棋盘上指定颜色的棋子数目
     * 输入划分：黑色（false）、白色（true）
     */
    @Test
    public void testComputePieceNumberOnBoard(){
        Board board = new Board(8);
        Piece piece11 = new Piece("piece11",true,new Position(1,1));
        Piece piece12 = new Piece("piece12",false,new Position(1,2));
        board.addPieceToBoard(piece11);
        board.addPieceToBoard(piece12);
        assertEquals(1,board.computePieceNumberOnBoard(true));
        assertEquals(1,board.computePieceNumberOnBoard(false));
    }
}
