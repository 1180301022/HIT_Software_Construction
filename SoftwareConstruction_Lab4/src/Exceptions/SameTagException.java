package Exceptions;

/**
 * 自定义异常类，针对输入文件中计划项标签完全相同的异常
 */

public class SameTagException extends Exception {
    public SameTagException(String info){
        super(info);
    }
}
