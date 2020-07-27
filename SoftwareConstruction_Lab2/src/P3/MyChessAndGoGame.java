package P3;

import java.util.Scanner;

/**
 * 棋类游戏的启动类
 * 提示用户初始化的输入信息，并启动棋类游戏
 * 方法：main方法
 */
public class MyChessAndGoGame {
    public static void main(String argv[]){
        System.out.println("欢迎使用张乙的棋类游戏，请输入游戏种类(chess/go)和玩家姓名");
        System.out.println("玩家1对应白色棋子，玩家2对应黑色棋子");

        Scanner scanner = new Scanner(System.in);
        String game = scanner.next();
        String player1 = scanner.next();
        String player2 = scanner.next();

        new Game(game, player1, player2);
    }
}
