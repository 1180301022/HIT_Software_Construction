package P3;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 棋类游戏的棋盘
 * 属性：棋盘大小、棋盘上棋子的信息
 * 方法：构造方法、获取所有棋子信息、加入棋子、删除棋子、检测位置合法性、计算棋子数目等
 * 可变类
 */
public class Board {
    private final int boardSize;
    private final Map<Position, Piece> boardInfo = new HashMap<>();

    // Abstraction function:
    //   AF(boardSize, boardInfo) = 棋盘大小为boardSize，棋盘上的棋子信息记录在boardInfo中
    // Representation invariant:
    //   记录的每个棋子都在棋盘的合法位置
    // Safety from rep exposure:
    //   所有成员域都用private修饰
    //   棋子是不可变类，可以直接返回

    private void checkRep(){
        Iterator iterator = boardInfo.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<Position, Piece> tempEntry = (Map.Entry)iterator.next();
            if(tempEntry.getKey().getX()<0 || tempEntry.getKey().getX()>=boardSize ||
               tempEntry.getKey().getY()<0 || tempEntry.getKey().getY()>=boardSize){
                assert false;
            }
        }
    }

    /**
     * 构造方法，确定棋盘大小
     * @param size 指定的棋盘大小
     */
    public Board(int size){
        checkRep();
        boardSize = size;
    }

    /**
     * 获得棋盘大小
     * @return 棋盘大小
     */
    public int getBoardSize(){
        checkRep();
        return boardSize;
    }

    /**
     * 向棋盘中加入棋子
     * @param pieceToAdd 需要加入棋盘的棋子，在加入前已保证加入棋子的合法性
     */
    public void addPieceToBoard(Piece pieceToAdd){
        checkRep();
        boardInfo.put(pieceToAdd.getPiecePosition(), pieceToAdd);
    }

    /**
     * 从棋盘中删除棋子
     * @param pieceToRemove 需要删除的棋子，合法性已验证
     */
    public void removePieceFromBoard(Piece pieceToRemove){
        checkRep();
        boardInfo.remove(pieceToRemove.getPiecePosition());
    }

    /**
     * 计算棋盘上指定颜色的棋子数目
     * @param color 指定棋子的颜色
     * @return 指定棋子的数目
     */
    public int computePieceNumberOnBoard(boolean color){
        int sum = 0;
        Iterator iterator = boardInfo.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<Position, Piece> tempEntry = (Map.Entry<Position, Piece>) iterator.next();
            if(tempEntry.getValue().getPieceColor() == color){
                sum++;
            }
        }
        checkRep();
        return sum;
    }

    /**
     * 检测某位置是否是合法的可以放置棋子的位置
     * @param inputPosition 需要检测的位置
     * @return 该位置是否可以放置棋子
     */
    public boolean isLegalPositionToPutPiece(Position inputPosition){
        if(inputPosition.getX()<0 || inputPosition.getX()>=boardSize || inputPosition.getY()<0 ||
            inputPosition.getY()>=boardSize || getPiece(inputPosition) != null){
            checkRep();
            return false;
        }
        checkRep();
        return true;
    }

    /**
     * 检测棋盘上某位置的情况
     * @param inputPosition 需要检测的位置
     * @return 如果存在棋子，则返回棋子信息；如果不存在棋子返回null
     */
    public Piece getPiece(Position inputPosition){
        Piece pieceToReturn = boardInfo.get(inputPosition);
        checkRep();
        return pieceToReturn;
    }

}
