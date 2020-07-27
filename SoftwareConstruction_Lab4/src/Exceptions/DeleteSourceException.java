package Exceptions;

/**
 * 删除资源时，有未结束计划项占用该资源时发生的异常
 */
public class DeleteSourceException extends Exception{
    public DeleteSourceException(String info){
        super(info);
    }
}
