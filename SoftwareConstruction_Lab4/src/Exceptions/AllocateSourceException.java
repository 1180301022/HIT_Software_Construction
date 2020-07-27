package Exceptions;

/**
 * 分配资源导致出现资源独占冲突时发生的异常
 */
public class AllocateSourceException extends Exception{
    public AllocateSourceException(String info){
        super(info);
    }
}
