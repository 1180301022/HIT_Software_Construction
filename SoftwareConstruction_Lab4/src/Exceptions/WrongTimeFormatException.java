package Exceptions;

/**
 * 时间格式错误时抛出的异常
 */
public class WrongTimeFormatException extends Exception {
    public WrongTimeFormatException(String info){
        super(info);
    }
}
