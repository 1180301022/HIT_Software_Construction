package P3;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class FriendshipGraph
{
    HashSet<Person> persons = null;
    public FriendshipGraph()
    {
        persons = new HashSet<Person>();//���ͼ�еĵ�
    }

    void addVertex(Person p)//���ͼ�����ӵ㣨��Person���������������ظ�
    {
        for(Person pInSet : persons)
        {
            if(p.name.equals(pInSet.name))
            {
                System.out.println("��������"+p.name+"�ظ���");
                System.exit(1);
            }
        }
        persons.add(p);
        return;
    }

    void addEdge(Person p1,Person p2)//����p1 p2
    {
        p1.friends.add(p2);
        return;
    }

    int getDistance(Person p1,Person p2)//��ȡp1 p2�����·��
    {
        //���������p1==p2��ֱ�ӷ���0
        if(p1==p2)
        {
            return 0;
        }

        //˼·������Person���е�friends��Ϊ�ڽӱ��ʾͼ��ʹ�ù����������
        Queue<Person> tempQueue = new LinkedList<Person>();//ʹ�ö��н�������
        int tempDistance = 1;//��¼����֮��ľ���

        tempQueue.offer(p1);
        p1.isCounted=true;//��ʼ�����

        while (tempQueue.peek()!=null)//���в�Ϊ��ʱѭ��
        {
            for(int i=0;i<tempQueue.size();i++)
            {
                Person head = tempQueue.poll();//����Ԫ�س���
                for(Person p : head.friends)//��������Ԫ������ֱ�����ӵĵ�
                {
                    if(p.equals(p2))//�ҵ�Ŀ��㣬��ʼ��ÿ�����isCounted
                    {
                        for(Person eachP : persons)
                        { eachP.isCounted=false;}
                        return tempDistance;
                    }
                    else
                    {
                       if(p.isCounted)//�Ѿ��������
                        {
                            continue;
                        }
                        else //û�б������.���
                        {
                           tempQueue.offer(p);
                            p.isCounted=true;
                        }
                    }
                }
            }
            tempDistance++;//����++
        }
        //����Ϊ�գ�������֮��û�����ӣ���Person�е�isCounted��ʼ��Ϊfalse
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

        System.out.println("main�в�������д�ڱ�����");
        System.out.println("rachel��ross�ľ��룺"+graph.getDistance(rachel, ross));
        System.out.println("rachel��ben�ľ��룺"+graph.getDistance(rachel, ben));
        System.out.println("rachel������ľ��룺"+graph.getDistance(rachel, rachel));
        System.out.println("rachel��kramer�ľ���:"+graph.getDistance(rachel, kramer));

    }
}