package Exceptions;

/**
 * 自定义异常类，针对输入文件格式的异常
 */

public class FileFormatException extends Exception{
    public FileFormatException(String info){
        super(info);
    }
}
