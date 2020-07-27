package P3;

/**
 * 棋类游戏的棋子类型
 * 属性：棋子名、棋子颜色、棋子位置
 * 方法：构造方法、获得棋子名、获得棋子颜色、获得棋子位置
 * 不可变类
 */
public class Piece {
    private final String pieceName;
    private final boolean pieceColor;
    private Position piecePosition;

    // Abstraction function:
    //   AF(pieceName, pieceColor, piecePosition) = 以pieceName为棋子名，颜色为pieceColor，位置在piecePosition的棋子
    // Representation invariant:
    //   由于piecePosition的正确性得到保证，所以该类保持不变量的结果恒为true
    // Safety from rep exposure:
    //   所有成员域都用private修饰
    //   棋子名、棋子颜色、棋子位置是不可变类，可以直接返回

    /**
     * 创建以pieceName为棋子名，颜色为pieceColor，位置在piecePosition的棋子
     * @param inputName 棋子名
     * @param inputColor 棋子颜色
     * @param inputPosition 棋子位置
     */
    public Piece(String inputName, boolean inputColor, Position inputPosition){
        pieceName = inputName;
        pieceColor = inputColor;
        piecePosition = new Position(inputPosition.getX(), inputPosition.getY());
    }

    /**
     * 获取棋子名
     * @return 棋子名
     */
    public String getPieceName(){
        return pieceName;
    }

    /**
     * 获取棋子颜色
     * @return 棋子颜色
     */
    public boolean getPieceColor(){
        return pieceColor;
    }

    /**
     * 获取棋子位置
     * @return 棋子位置
     */
    public Position getPiecePosition(){
        return piecePosition;
    }

    @Override
    public int hashCode(){
        return piecePosition.getX()<<piecePosition.getY();
    }

    @Override
    public boolean equals(Object input){
        Piece inputPiece = (Piece)input;
        if(piecePosition.getX()==inputPiece.piecePosition.getX() &&
                piecePosition.getY()==inputPiece.piecePosition.getY() ){
            return true;
        }
        return false;
    }

    @Override
    public String toString(){
        if(pieceColor == true){
            return "棋子名："+pieceName+" 棋子颜色：白色"+" 棋子位置："+piecePosition.toString();
        }
        else{
            return "棋子名："+pieceName+" 棋子颜色：黑色"+" 棋子位置："+piecePosition.toString();
        }
    }
}
