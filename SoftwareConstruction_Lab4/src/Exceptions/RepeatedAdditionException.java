package Exceptions;

/**
 * 重复添加计划项/资源/地点时抛出的异常
 */
public class RepeatedAdditionException extends Exception {
    public RepeatedAdditionException(String info){
        super(info);
    }
}
