package P3;

/**
 * 棋类游戏的动作类
 * 属性：棋盘、操作历史记录
 * 方法：放子、移动、吃子、提子、查询位置占用情况、计算棋盘上的棋子数、获得历史记录
 * 可变类型
 */
public class Action {
    private Board board;
    private final StringBuilder operationLog = new StringBuilder("");

    // Abstraction function:
    //   AF(board, operationLog) = 在棋盘board上进行的操作，每步操作记录在operationLog中
    // Representation invariant:
    //   true
    // Safety from rep exposure:
    //   所有成员域都用private修饰

    public Action(Board inputBoard) {
        board = inputBoard;
    }

    /**
     * 将一枚棋子从场外放到指定位置，该操作仅针对围棋
     *
     * @param player 操作玩家
     * @param piece  操作棋子
     * @return 提示操作是否成功
     */
    public boolean put(Player player, Piece piece) {
        Position inputPosition = piece.getPiecePosition();

        //如果操作非己方棋子
        if (!(player.getPlayerColor() == piece.getPieceColor())) {
            System.out.println("操作非己方棋子");
            return false;
        }

        //如果操作非合法位置
        if (!board.isLegalPositionToPutPiece(inputPosition)) {
            System.out.println("操作位置非法");
            return false;
        }

        operationLog.append(player.getPlayerName() + "将棋子放在位置" + piece.getPiecePosition().toString() + "\n");
        board.addPieceToBoard(piece);
        return true;
    }

    /**
     * 将一枚棋子从一个位置移动到另一个位置，该操作仅针对象棋
     *
     * @param player         操作玩家
     * @param sourcePosition 棋子起始位置
     * @param targetPosition 棋子终止位置
     * @return 提示操作是否成功
     */
    public boolean move(Player player, Position sourcePosition, Position targetPosition) {
        Piece sourcePiece = board.getPiece(sourcePosition);
        //想要操作的位置无棋子
        if (sourcePiece == null) {
            System.out.println("输入位置无棋子");
            return false;
        }
        //两位置相同
        if (sourcePiece.getPiecePosition().equals(targetPosition)) {
            System.out.println("两位置相同");
            return false;
        }
        //操作对方棋子
        if (sourcePiece.getPieceColor() != player.getPlayerColor()) {
            System.out.println("操作非己方棋子");
            return false;
        }
        //目标位置已有棋子 或 超出范围
        if (!board.isLegalPositionToPutPiece(targetPosition)) {
            System.out.println("目标位置已有棋子，或者目标位置超出棋盘范围");
            return false;
        }

        //输入合法，移动棋子
        board.removePieceFromBoard(sourcePiece);
        Piece pieceToAdd = new Piece(sourcePiece.getPieceName(), sourcePiece.getPieceColor(), targetPosition);
        board.addPieceToBoard(pieceToAdd);
        operationLog.append(player.getPlayerName() + "将棋子" + sourcePiece.getPieceName() + "从" + sourcePosition.toString()
                + "移动到" + targetPosition.toString() + "\n");
        return true;
    }

    /**
     * 将指定坐标的对手棋子移除，该操作仅针对围棋
     *
     * @param player           操作方
     * @param toRemovePosition 想要移除的棋子的位置
     * @return 提示是否操作成功
     */
    public boolean remove(Player player, Position toRemovePosition) {
        //输入位置超出棋盘
        if (toRemovePosition.getX() < 0 || toRemovePosition.getX() >= 19 || toRemovePosition.getY() < 0 ||
                toRemovePosition.getY() >= 19) {
            System.out.println("输入位置超出棋盘");
            return false;
        }

        //目标位置无棋子
        if (board.getPiece(toRemovePosition) == null) {
            System.out.println("目标位置无棋子");
            return false;
        }
        //目标位置是己方棋子，无法移除
        if (board.getPiece(toRemovePosition).getPieceColor() == player.getPlayerColor()) {
            System.out.println("目标位置是己方棋子，无法移除");
            return false;
        }
        //符合要求，可以提子
        board.removePieceFromBoard(board.getPiece(toRemovePosition));
        operationLog.append(player.getPlayerName() + "将对手位于" + toRemovePosition.toString() + "的棋子移除\n");
        return true;
    }

    /**
     * 吃子（移动己方棋子到对方棋子位置，同时移除对方棋子），该操作仅针对象棋
     *
     * @param player         操作方
     * @param sourcePosition 操作己方棋子
     * @param targetPosition 被吃的对方棋子
     * @return 提示是否操作成功
     */
    public boolean eat(Player player, Position sourcePosition, Position targetPosition) {
        if (sourcePosition.getX() < 0 || sourcePosition.getX() >= 8 || sourcePosition.getY() < 0 ||
                sourcePosition.getY() >= 8 || targetPosition.getX() < 0 || targetPosition.getX() >= 8 ||
                targetPosition.getY() < 0 || targetPosition.getY() >= 8) {
            System.out.println("输入位置超出棋盘");
            return false;
        }

        Piece sourcePiece = board.getPiece(sourcePosition);
        Piece targetPiece = board.getPiece(targetPosition);

        if (sourcePiece == null) {
            System.out.println("操作位置没有棋子");
            return false;
        }
        if (targetPiece == null) {
            System.out.println("目标位置没有棋子");
            return false;
        }
        if (sourcePiece.getPieceColor() != player.getPlayerColor()) {
            System.out.println("操作位置非己方棋子");
            return false;
        }
        if (targetPiece.getPieceColor() == player.getPlayerColor()) {
            System.out.println("目标棋子是己方棋子");
            return false;
        }

        //满足条件，可以吃子
        board.removePieceFromBoard(targetPiece);
        board.removePieceFromBoard(sourcePiece);
        Piece newPiece = new Piece(sourcePiece.getPieceName(), sourcePiece.getPieceColor(), targetPosition);
        board.addPieceToBoard(newPiece);
        operationLog.append(player.getPlayerName() + "用己方位于" + sourcePosition.toString() + "的棋子吃掉了对方位于" +
                targetPosition.toString() + "的棋子\n");
        return true;
    }

    /**
     * 查询输入位置的占用情况
     *
     * @param player        操作方
     * @param inputPosition 查询位置
     * @return 如果有棋子则返回棋子信息，如果没有则返回提示信息
     */
    public boolean checkPosition(Player player, Position inputPosition) {
        if (inputPosition.getX() < 0 || inputPosition.getX() >= board.getBoardSize() || inputPosition.getY() < 0 ||
                inputPosition.getY() >= board.getBoardSize()) {
            System.out.println("输入位置超出棋盘");
            return false;
        }

        Piece piece = board.getPiece(inputPosition);

        if (piece == null) {
            System.out.println("该位置无棋子");
        } else {
            System.out.println(piece.toString());
        }
        operationLog.append(player.getPlayerName() + "查询了位置" + inputPosition.toString() + "的占用情况\n");
        return true;
    }

    /**
     * 计算棋盘上两种颜色的棋子数
     *
     * @param player 操作方
     * @return 一定是操作成功的，为了和上述操作保持一致采用的返回值
     */
    public boolean computePieceNumber(Player player) {
        System.out.println("白色棋子数：" + board.computePieceNumberOnBoard(true));
        System.out.println("黑色棋子数：" + board.computePieceNumberOnBoard(false));
        operationLog.append(player.getPlayerName() + "查询了棋盘上的棋子数目\n");
        return true;
    }

    /**
     * 跳过回合
     *
     * @param player 操作方
     * @return 一定是操作成功的，为了和上述操作保持一致采用的返回值
     */
    public boolean skip(Player player) {
        operationLog.append(player.getPlayerName() + "跳过了回合\n");
        return true;
    }

    /**
     * 游戏结束后，获得对局步骤历史
     * @return 对局步骤历史
     */
    public String getGameLog(){
        return operationLog.toString();
    }
}

