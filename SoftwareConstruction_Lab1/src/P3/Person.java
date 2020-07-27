package P3;


import java.util.Vector;

public class Person
{
    String name;//人名
    Vector<Person> friends = null;//存放和其直接连接的Person
    boolean isCounted;//是否在一次getDistance中被考察
    public Person (String inputName)
    {
        friends = new Vector<Person>();
        name = inputName;
        isCounted=false;
    }
}