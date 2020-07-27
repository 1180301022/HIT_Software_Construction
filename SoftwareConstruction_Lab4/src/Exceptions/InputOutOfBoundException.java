package Exceptions;

/**
 * 输入操作码超出范围时抛出的异常
 */
public class InputOutOfBoundException extends Exception {
    public InputOutOfBoundException(String info){
        super(info);
    }
}
