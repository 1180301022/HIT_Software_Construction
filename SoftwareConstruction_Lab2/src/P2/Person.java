package P2;

import java.util.Objects;

/**
 * 在FriendshipGraph作为顶点的Person类
 * name作为姓名指明顶点标识符
 * 提供获得姓名的操作
 * 不可变类
 */
public class Person {
    private final String name;

    // Abstraction function:
    //   AF(name) = 姓名为name的人
    // Representation invariant:
    //   name!=""
    // Safety from rep exposure:
    //   所有成员域都用private修饰
    //   String类是不可变类，可以直接返回而不产生暴露

    private void checkRep(){
        assert name!="";
    }

    /**
     * 创建Person实例
     * @param inputString 指定的姓名
     */
    public Person(String inputString){
        name = inputString;
        checkRep();
    }

    /**
     * 获取当前Person的姓名
     * @return 当前Person姓名
     */
    public String getName(){
        checkRep();
        return name;
    }

    @Override
    public int hashCode(){
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object inputObject){
        Person input = (Person)inputObject;
        if(name.equals(input.getName())){
            return true;
        }
        return false;
    }

}
