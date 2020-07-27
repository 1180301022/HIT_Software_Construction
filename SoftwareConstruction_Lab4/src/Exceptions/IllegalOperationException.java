package Exceptions;

/**
 * 改变计划项状态失败时发生的异常
 */
public class IllegalOperationException extends Exception{
    public IllegalOperationException(String info){
        super(info);
    }
}
