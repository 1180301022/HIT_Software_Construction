package P3;

import java.util.Scanner;

/**
 * 创建并进行一盘新的棋类游戏
 * 属性：两名玩家、一个棋盘、一个操作类、游戏种类
 * 方法：初始化象棋/围棋游戏、启动游戏、打印菜单、执行操作等
 * 可变类
 */
public class Game {
    private final Player playerA;
    private final Player playerB;
    private Board gameBoard;
    private Action gameAction;
    private String gameKind;

    // Abstraction function:
    //   AF(playerA, playerB, gameBoard, gameAction, gameKind) = 由玩家playerA、playerB、棋盘gameBoard、
    //                                                          操作gameAction、游戏种类gameKind构成的一盘游戏
    // Representation invariant:
    //   playerA!=null && playerB!=null
    // Safety from rep exposure:
    //   所有成员域都用private修饰，所有成员方法都用private修饰

    private void checkRep(){
        assert (playerA!=null)&&(playerB!=null);
    }

    /**
     * 创建并进行一局棋类游戏
     * @param gameKind 游戏种类
     * @param player1 玩家1姓名
     * @param player2 玩家2姓名
     */
    public Game(String gameKind, String player1, String player2){

        this.gameKind = gameKind;
        playerA = new Player(player1, true);
        playerB = new Player(player2, false);
        checkRep();

        if(gameKind.equals("go")){
            initializeGoGame(player1, player2);
        }
        else if(gameKind.equals("chess")){
            initializeChessGame(player1, player2);
        }
        else {
            System.out.println("仅支持输入chess或go");
            System.exit(1);
        }
    }

    /**
     * 初始化围棋游戏
     * @param player1 玩家1姓名
     * @param player2 玩家2姓名
     */
    private void initializeGoGame(String player1, String player2){
        gameBoard = new Board(19);
        gameAction = new Action(gameBoard);
        checkRep();
        startGame();
    }

    /**
     * 初始化象棋游戏
     * @param player1 玩家1姓名
     * @param player2 玩家2姓名
     */
    private void initializeChessGame(String player1, String player2){
        gameBoard = new Board(8);
        gameAction = new Action(gameBoard);
        //按照国际象棋规则，需要将棋子摆放到棋盘上
        Piece whiteKing = new Piece("King",true,new Position(3,0));
        Piece whiteQueen = new Piece("Queen",true,new Position(4,0));
        Piece whiteRook1 = new Piece("Rook",true,new Position(0,0));
        Piece whiteRook2 = new Piece("Rook",true,new Position(7,0));
        Piece whiteBishop1 = new Piece("Bishop",true,new Position(2,0));
        Piece whiteBishop2 = new Piece("Bishop",true,new Position(5,0));
        Piece whiteKnight1 = new Piece("Knight",true,new Position(1,0));
        Piece whiteKnight2 = new Piece("Knight",true,new Position(6,0));
        Piece whitePawn1 = new Piece("Pawn",true,new Position(0,1));
        Piece whitePawn2 = new Piece("Pawn",true,new Position(1,1));
        Piece whitePawn3 = new Piece("Pawn",true,new Position(2,1));
        Piece whitePawn4 = new Piece("Pawn",true,new Position(3,1));
        Piece whitePawn5 = new Piece("Pawn",true,new Position(4,1));
        Piece whitePawn6 = new Piece("Pawn",true,new Position(5,1));
        Piece whitePawn7 = new Piece("Pawn",true,new Position(6,1));
        Piece whitePawn8 = new Piece("Pawn",true,new Position(7,1));

        Piece blackKing = new Piece("King",false,new Position(3,7));
        Piece blackQueen = new Piece("Queen",false,new Position(4,7));
        Piece blackRook1 = new Piece("Rook",false,new Position(0,7));
        Piece blackRook2 = new Piece("Rook",false,new Position(7,7));
        Piece blackBishop1 = new Piece("Bishop",false,new Position(2,7));
        Piece blackBishop2 = new Piece("Bishop",false,new Position(5,7));
        Piece blackKnight1 = new Piece("Knight",false,new Position(1,7));
        Piece blackKnight2 = new Piece("Knight",false,new Position(6,7));
        Piece blackPawn1 = new Piece("Pawn",false,new Position(0,6));
        Piece blackPawn2 = new Piece("Pawn",false,new Position(1,6));
        Piece blackPawn3 = new Piece("Pawn",false,new Position(2,6));
        Piece blackPawn4 = new Piece("Pawn",false,new Position(3,6));
        Piece blackPawn5 = new Piece("Pawn",false,new Position(4,6));
        Piece blackPawn6 = new Piece("Pawn",false,new Position(5,6));
        Piece blackPawn7 = new Piece("Pawn",false,new Position(6,6));
        Piece blackPawn8 = new Piece("Pawn",false,new Position(7,6));

        gameBoard.addPieceToBoard(whiteKing);
        gameBoard.addPieceToBoard(whiteQueen);
        gameBoard.addPieceToBoard(whiteRook1);
        gameBoard.addPieceToBoard(whiteRook2);
        gameBoard.addPieceToBoard(whiteBishop1);
        gameBoard.addPieceToBoard(whiteBishop2);
        gameBoard.addPieceToBoard(whiteKnight1);
        gameBoard.addPieceToBoard(whiteKnight2);
        gameBoard.addPieceToBoard(whitePawn1);
        gameBoard.addPieceToBoard(whitePawn2);
        gameBoard.addPieceToBoard(whitePawn3);
        gameBoard.addPieceToBoard(whitePawn4);
        gameBoard.addPieceToBoard(whitePawn5);
        gameBoard.addPieceToBoard(whitePawn6);
        gameBoard.addPieceToBoard(whitePawn7);
        gameBoard.addPieceToBoard(whitePawn8);

        gameBoard.addPieceToBoard(blackKing);
        gameBoard.addPieceToBoard(blackQueen);
        gameBoard.addPieceToBoard(blackRook1);
        gameBoard.addPieceToBoard(blackRook2);
        gameBoard.addPieceToBoard(blackBishop1);
        gameBoard.addPieceToBoard(blackBishop2);
        gameBoard.addPieceToBoard(blackKnight1);
        gameBoard.addPieceToBoard(blackKnight2);
        gameBoard.addPieceToBoard(blackPawn1);
        gameBoard.addPieceToBoard(blackPawn2);
        gameBoard.addPieceToBoard(blackPawn3);
        gameBoard.addPieceToBoard(blackPawn4);
        gameBoard.addPieceToBoard(blackPawn5);
        gameBoard.addPieceToBoard(blackPawn6);
        gameBoard.addPieceToBoard(blackPawn7);
        gameBoard.addPieceToBoard(blackPawn8);

        checkRep();
        startGame();
    }

    /**
     * 启动游戏，每个玩家一个回合，直到一名玩家终止游戏
     * 如果发生非法操作，则当前玩家重新进行回合
     */
    private void startGame(){
        Scanner scanner = new Scanner(System.in);
        //用以标记行为
        int operation = 9;
        //用以标记回合
        boolean round = true;
        //用以标记操作是否合法
        boolean isLegalOperation = true;

        //游戏开始
        while (operation != 8){
            printMenu();
            operation = scanner.nextInt();
            isLegalOperation = performAction(round, operation);
            checkRep();
            //如果操作合法，则跳转到对手回合
            if(isLegalOperation){
                round = !round;
                System.out.println("操作成功！接下来是对手回合\n");
            }
            //如果操作不合法，则重新提示输入
            else {
                System.out.println("操作非法，仍是你的回合，请重新输入操作指令\n");
                continue;
            }
        }

        //游戏结束
        System.out.println("是否要查询走棋历史");
        System.out.println("如果是，请输入9，输入其他数字将退出游戏");
        if(scanner.nextInt() == 9){
            System.out.println(gameAction.getGameLog());
        }
        System.out.println("游戏结束");
        System.exit(0);

    }

    /**
     * 打印游戏操作菜单
     */
    private void printMenu() {
        System.out.println("请输入编号对应的指令，来完成相应操作");
        System.out.println("1.（围棋）从棋盘外将棋子放置到棋盘指定位置");
        System.out.println("2.（围棋）将指定位置的对手棋子移除");
        System.out.println("3.（象棋）将己方的一枚棋子从一个位置移动到另一个位置");
        System.out.println("4.（象棋）吃子，移动己方棋子到对方棋子位置，同时移除对方棋子");
        System.out.println("5.查询指定位置的占用情况");
        System.out.println("6.查询棋盘上两种颜色的棋子数");
        System.out.println("7.跳过回合");
        System.out.println("8.结束游戏");
    }

    /**
     * 进行该局棋类游戏的操作
     * @param round 标记当前操作是谁的回合
     * @param input 代表操作的编码
     * @return 是否操作成功
     */
    private boolean performAction(boolean round, int input){
        switch (input){
            case 1:
                return putPiece(round);
            case 2:
                return removePiece(round);
            case 3:
                return movePiece(round);
            case 4:
                return eatPiece(round);
            case 5:
                return checkPiece(round);
            case 6:
                return computePiece(round);
            case 7:
                return skipRound(round);
            case 8:
                return true;
            default:
                return false;
        }
    }

    /**
     * 围棋的放子操作
     * @param round 标记当前是谁的回合
     * @return 放置棋子是否成功
     */
    private boolean putPiece(boolean round){
        //象棋不存在放子操作
        if(gameKind.equals("chess")){
            return false;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入放置坐标");
        int x = scanner.nextInt();
        int y = scanner.nextInt();

        if(round){
            Piece pieceToAdd = new Piece("white", round, new Position(x, y));
            checkRep();
            return gameAction.put(playerA, pieceToAdd);
        }
        else {
            Piece pieceToAdd = new Piece("black", round, new Position(x, y));
            checkRep();
            return gameAction.put(playerB, pieceToAdd);
        }
    }

    /**
     * 围棋的提子操作
     * @param round 标记当前是谁的回合
     * @return 提子是否成功
     */
    private boolean removePiece(boolean round){
        //象棋不存在提子操作
        if(gameKind.equals("chess")){
            return false;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入提子坐标");
        int x = scanner.nextInt();
        int y = scanner.nextInt();

        if(round){
            checkRep();
            return gameAction.remove(playerA, new Position(x, y));
        }
        else {
            checkRep();
            return gameAction.remove(playerB, new Position(x, y));
        }
    }

    /**
     * 象棋的移动操作
     * @param round 标记当前是谁的回合
     * @return 移动是否成功
     */
    private boolean movePiece(boolean round){
        //围棋不存在移子操作
        if(gameKind.equals("go")){
            return false;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入起始坐标、目标坐标");
        int x1 = scanner.nextInt();
        int y1 = scanner.nextInt();
        int x2 = scanner.nextInt();
        int y2 = scanner.nextInt();

        if(round){
            checkRep();
            return gameAction.move(playerA, new Position(x1, y1), new Position(x2, y2));
        }
        else {
            checkRep();
            return gameAction.move(playerB, new Position(x1, y1), new Position(x2, y2));
        }
    }

    /**
     * 象棋的吃子操作
     * @param round 标记当前是谁的回合
     * @return 吃子是否成功
     */
    private boolean eatPiece(boolean round){
        //围棋不存在吃子操作
        if(gameKind.equals("go")){
            return false;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入起始坐标、目标坐标");
        int x1 = scanner.nextInt();
        int y1 = scanner.nextInt();
        int x2 = scanner.nextInt();
        int y2 = scanner.nextInt();

        if(round){
            checkRep();
            return gameAction.eat(playerA, new Position(x1, y1), new Position(x2, y2));
        }
        else {
            checkRep();
            return gameAction.eat(playerB, new Position(x1, y1), new Position(x2, y2));
        }
    }

    /**
     * 查询输入位置的占用情况
     * @param round 标记当前是谁的回合
     * @return 查询一定成功，和其它操作保持一致
     */
    private boolean checkPiece(boolean round){
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入查询坐标");
        int x = scanner.nextInt();
        int y = scanner.nextInt();

        if(round){
            checkRep();
            return gameAction.checkPosition(playerA, new Position(x, y));
        }
        else {
            checkRep();
            return gameAction.checkPosition(playerB, new Position(x, y));
        }
    }

    /**
     * 分别计算棋盘上黑白棋子的数目
     * @param round 标记当前是谁的回合
     * @return 计算一定成功，和其它操作保持一致
     */
    private boolean computePiece(boolean round){
        if(round){
            checkRep();
            return gameAction.computePieceNumber(playerA);
        }
        else {
            checkRep();
            return gameAction.computePieceNumber(playerB);
        }
    }

    /**
     * 跳过当前回合
     * @param round 标记当前是谁的回合
     * @return 跳过一定成功，和其它操作保持一致
     */
    private boolean skipRound(boolean round){
        if(round){
            checkRep();
            return gameAction.skip(playerA);
        }
        else {
            checkRep();
            return gameAction.skip(playerB);
        }
    }
}
