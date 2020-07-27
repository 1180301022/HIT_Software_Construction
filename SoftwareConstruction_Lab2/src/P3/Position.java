package P3;
/**
 *记录、获取棋子位置
 * 不可变类型
 */
public class Position {
    private int x;
    private int y;

    // Abstraction function:
    //   AF(x, y) = 以x为横坐标，y为纵坐标的位置
    // Representation invariant:
    //   x>=0 && y>=0
    // Safety from rep exposure:
    //   所有成员域都用private修饰

    /**
     * 构造方法，建立以x为横坐标，y为纵坐标的位置
     * @param x 横坐标位置
     * @param y 纵坐标位置
     */
    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    private void checkRep(){
        assert (x>=0)&&(y>=0);
    }

    /**
     * 获取当前位置横坐标
     * @return 当前位置横坐标
     */
    public int getX(){
        checkRep();
        return x;
    }

    /**
     * 获取当前位置纵坐标
     * @return 当前位置纵坐标
     */
    public int getY(){
        checkRep();
        return y;
    }

    @Override
    public int hashCode(){
        return x<<y;
    }

    @Override
    public boolean equals(Object input){
        Position inputPosition = (Position)input;
        if(x==inputPosition.getX() && y==inputPosition.getY()){
            return true;
        }
        return false;
    }

    @Override
    public String toString(){
        return "("+x+","+y+")";
    }
}
