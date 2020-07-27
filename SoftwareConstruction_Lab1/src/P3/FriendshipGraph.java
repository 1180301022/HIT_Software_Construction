package P3;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class FriendshipGraph
{
    HashSet<Person> persons = null;
    public FriendshipGraph()
    {
        persons = new HashSet<Person>();//存放图中的点
    }

    void addVertex(Person p)//向该图中增加点（即Person），不允许人名重复
    {
        for(Person pInSet : persons)
        {
            if(p.name.equals(pInSet.name))
            {
                System.out.println("输入姓名"+p.name+"重复！");
                System.exit(1);
            }
        }
        persons.add(p);
        return;
    }

    void addEdge(Person p1,Person p2)//连接p1 p2
    {
        p1.friends.add(p2);
        return;
    }

    int getDistance(Person p1,Person p2)//获取p1 p2的最短路径
    {
        //特殊情况：p1==p2，直接返回0
        if(p1==p2)
        {
            return 0;
        }

        //思路：利用Person类中的friends作为邻接表表示图，使用广度优先搜索
        Queue<Person> tempQueue = new LinkedList<Person>();//使用队列进行搜索
        int tempDistance = 1;//记录两点之间的距离

        tempQueue.offer(p1);
        p1.isCounted=true;//起始点入队

        while (tempQueue.peek()!=null)//队列不为空时循环
        {
            for(int i=0;i<tempQueue.size();i++)
            {
                Person head = tempQueue.poll();//队首元素出队
                for(Person p : head.friends)//遍历出队元素所有直接连接的点
                {
                    if(p.equals(p2))//找到目标点，初始化每个点的isCounted
                    {
                        for(Person eachP : persons)
                        { eachP.isCounted=false;}
                        return tempDistance;
                    }
                    else
                    {
                       if(p.isCounted)//已经被考察过
                        {
                            continue;
                        }
                        else //没有被考察过.入队
                        {
                           tempQueue.offer(p);
                            p.isCounted=true;
                        }
                    }
                }
            }
            tempDistance++;//距离++
        }
        //队列为空，则两点之间没有连接，对Person中的isCounted初始化为false
        for(Person eachP : persons)
        {
            eachP.isCounted=false;
        }
        return -1;
    }

    public static void main(String argv[])
    {
        FriendshipGraph graph = new FriendshipGraph();
        Person rachel = new Person("Rachel");
        Person ross = new Person("Ross");
        Person ben = new Person("Ben");
        Person kramer = new Person("Kramer");
        Person newAdd = new Person("newAdd");

        graph.addVertex(rachel);
        graph.addVertex(ross);
        graph.addVertex(ben);
        graph.addVertex(kramer);
        graph.addVertex(newAdd);

        graph.addEdge(rachel, ross);
        graph.addEdge(ross, rachel);
        graph.addEdge(ross, ben);
        graph.addEdge(ben, ross);
        graph.addEdge(ross,newAdd);
        graph.addEdge(newAdd,ross);
        graph.addEdge(kramer,newAdd);
        graph.addEdge(newAdd,kramer);

        System.out.println("main中测试用例写在报告中");
        System.out.println("rachel和ross的距离："+graph.getDistance(rachel, ross));
        System.out.println("rachel和ben的距离："+graph.getDistance(rachel, ben));
        System.out.println("rachel到自身的距离："+graph.getDistance(rachel, rachel));
        System.out.println("rachel到kramer的距离:"+graph.getDistance(rachel, kramer));

    }
}