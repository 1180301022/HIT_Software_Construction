package P3;

/**
 * 棋类游戏的玩家类
 * 属性：姓名、拥有的棋子颜色
 * 方法：构造方法、获得玩家名、获得棋子颜色
 * 不可变类型
 */
public class Player {

    private final String playerName;
    private final boolean playerColor;

    // Abstraction function:
    //   AF(playerName, playerColor) = 名为playerName，棋子颜色为playerColor的玩家
    // Representation invariant:
    //   true
    // Safety from rep exposure:
    //   所有成员域都用private修饰
    //   所有成员域都是不可变类型

    /**
     * 创建名为playerName，棋子颜色为playerColor的玩家
     * @param name 玩家名
     * @param color 玩家棋子颜色
     */
    public Player(String name, boolean color){
        playerName = name;
        playerColor = color;
    }

    /**
     * 获取玩家名
     * @return 玩家名
     */
    public String getPlayerName(){
        return playerName;
    }

    /**
     * 获取玩家棋子颜色
     * @return 玩家棋子颜色
     */
    public boolean getPlayerColor(){
        return playerColor;
    }
}
