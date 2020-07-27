package Exceptions;

/**
 * 删除位置时，有未结束计划项占用该位置时发生的异常
 */
public class DeleteLocationException extends Exception{
    public DeleteLocationException(String info){
        super(info);
    }
}
