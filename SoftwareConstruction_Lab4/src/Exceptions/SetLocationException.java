package Exceptions;

/**
 * 设置位置时，发生位置独占冲突时发生的异常
 */
public class SetLocationException extends Exception{
    public SetLocationException(String info){
        super(info);
    }
}
