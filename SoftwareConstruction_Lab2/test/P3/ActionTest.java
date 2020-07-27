package P3;

import static org.junit.Assert.*;
import org.junit.Test;

public class ActionTest {
    /**
     * 测试策略
     * 针对每种方法，对输入操作进行等价类划分
     * 在尽可能简洁的情况下，提高代码覆盖率
     */

    /**
     * 对放置棋子的操作进行测试
     * 按操作棋子颜色划分：操作棋子属于己方、操作棋子属于对方
     * 按棋子的位置划分：位置在棋盘上且该位置无棋子、位置在棋盘上且该位置有棋子、位置不在棋盘上
     */
    @Test
    public void testPut(){
        Board board = new Board(8);
        Action action = new Action(board);
        Player player = new Player("sample", true);
        Piece whitePiece1 = new Piece("white", true, new Position(0,0));
        Piece whitePiece2 = new Piece("white", true, new Position(0,0));
        Piece whitePiece3 = new Piece("white", true, new Position(9,0));
        Piece blackPiece = new Piece("black", false, new Position(1,1));

        assertFalse(action.put(player, blackPiece));
        assertFalse(action.put(player,whitePiece3));
        assertTrue(action.put(player,whitePiece1));
        assertFalse(action.put(player,whitePiece2));
    }

    /**
     * 对移动棋子的操作进行测试
     * 按操作棋子类型划分：操作棋子属于己方、操作棋子属于对方、输入位置无棋子
     * 按目标位置划分：位置在棋盘上且该位置无棋子、位置在棋盘上且该位置有棋子、位置不在棋盘上
     */
    @Test
    public void testMove(){
        Board board = new Board(8);
        Action action = new Action(board);
        Player player = new Player("sample", true);
        Piece whitePiece = new Piece("white", true, new Position(0,0));
        Piece blackPiece = new Piece("black", false, new Position(1,1));
        board.addPieceToBoard(whitePiece);
        board.addPieceToBoard(blackPiece);
        assertFalse(action.move(player, new Position(1,1), new Position(5,5)));
        assertFalse(action.move(player, new Position(4,1), new Position(5,5)));
        assertFalse(action.move(player, new Position(0,0), new Position(1,1)));
        assertFalse(action.move(player, new Position(0,0), new Position(9,5)));
        assertTrue(action.move(player, new Position(0,0), new Position(5,5)));
    }

    /**
     *测试移除棋子的操作
     * 按操作棋子类型划分：操作棋子属于己方、操作棋子属于对方、输入位置无棋子
     */
    @Test
    public void testRemove(){
        Board board = new Board(8);
        Action action = new Action(board);
        Player player = new Player("sample", true);
        Piece whitePiece = new Piece("white", true, new Position(0,0));
        Piece blackPiece = new Piece("black", false, new Position(1,1));
        board.addPieceToBoard(whitePiece);
        board.addPieceToBoard(blackPiece);
        assertTrue(action.remove(player, new Position(1,1)));
        assertFalse(action.remove(player, new Position(1,1)));
        assertFalse(action.remove(player, new Position(0,0)));
    }

    /**
     * 测试吃子操作
     * 按操作棋子类型划分：操作棋子属于己方、操作棋子属于对方、输入位置无棋子
     * 按目标位置划分：目标位置是己方棋子、目标位置是对方棋子、目标位置无棋子
     */
    @Test
    public void testEat(){
        Board board = new Board(8);
        Action action = new Action(board);
        Player player = new Player("sample", true);
        Piece whitePiece1 = new Piece("white", true, new Position(0,0));
        Piece whitePiece2 = new Piece("white", true, new Position(0,1));
        Piece blackPiece = new Piece("black", false, new Position(1,1));
        board.addPieceToBoard(whitePiece1);
        board.addPieceToBoard(blackPiece);
        board.addPieceToBoard(whitePiece2);
        assertFalse(action.eat(player, new Position(1,1), new Position(0,0)));
        assertFalse(action.eat(player, new Position(5,5), new Position(1,1)));
        assertFalse(action.eat(player, new Position(0,0), new Position(0,1)));
        assertFalse(action.eat(player, new Position(0,0), new Position(4,1)));
        assertTrue(action.eat(player, new Position(0,0), new Position(1,1)));
    }

    /**
     * 测试查询操作
     * 按操作棋子类型划分：操作棋子属于己方、操作棋子属于对方、输入位置无棋子
     */
    @Test
    public void testCheckPosition(){
        Board board = new Board(8);
        Action action = new Action(board);
        Player player = new Player("sample", true);
        Piece whitePiece = new Piece("white", true, new Position(0,0));
        Piece blackPiece = new Piece("black", false, new Position(1,1));
        board.addPieceToBoard(whitePiece);
        board.addPieceToBoard(blackPiece);
        assertTrue(action.checkPosition(player, new Position(0,0)));
        assertTrue(action.checkPosition(player, new Position(1,1)));
        assertTrue(action.checkPosition(player, new Position(5,0)));
    }

    /**
     * 测试计算棋盘上的棋子数
     * 按输入玩家的棋子颜色划分：黑色、白色
     */
    @Test
    public void testComputePieceNumber(){
        Board board = new Board(8);
        Action action = new Action(board);
        Player player1 = new Player("sample", true);
        Player player2 = new Player("samplex", false);
        Piece whitePiece1 = new Piece("white", true, new Position(0,0));
        Piece whitePiece2 = new Piece("white", true, new Position(0,1));
        Piece blackPiece = new Piece("black", false, new Position(1,1));
        board.addPieceToBoard(whitePiece1);
        board.addPieceToBoard(blackPiece);
        board.addPieceToBoard(whitePiece2);
        assertTrue(action.computePieceNumber(player1));
        assertTrue(action.computePieceNumber(player2));
    }

    /**
     * 测试跳过回合操作
     * 按输入玩家的棋子颜色划分：黑色、白色
     */
    @Test
    public void testSkip(){
        Board board = new Board(8);
        Action action = new Action(board);
        Player player1 = new Player("sample", true);
        Player player2 = new Player("samplex", false);
        assertTrue(action.skip(player1));
        assertTrue(action.skip(player2));
    }
}
