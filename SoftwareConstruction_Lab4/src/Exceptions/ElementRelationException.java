package Exceptions;

/**
 * 自定义异常类，针对输入文件中计划项各元素依赖关系错误的异常
 */

public class ElementRelationException extends Exception{
    public ElementRelationException(String info){
        super(info);
    }
}
