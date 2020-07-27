package P2;

import graph.ConcreteEdgesGraph;

import java.util.*;

/**
 * 按照Lab1的要求，利用P1实现的Graph重新完成FriendshipGraph
 * isCounted标明某点是否在计算最短距离时被考察过
 * 提供向图中加入点、加入边、初始化isCounted、计算最短距离、查看已加入图中的点等操作
 * 可变类
 */

public class FriendshipGraph extends ConcreteEdgesGraph<Person> {
    private final Map<Person, Boolean> isCounted = new HashMap<Person,Boolean>();

    // Abstraction function:
    //   AF(vertices, edges, isCounted) = 含有顶点vertices、边edges的图，且每个点都有考察标记
    // Representation invariant:
    //   true
    // Safety from rep exposure:
    //   所有成员域都用private修饰
    //   使用防御拷贝

    /**
     * 向图中加入顶点，如果输入重复点，则不重复加入，并输出错误信息
     * 如果非重复点，则加入图中，并在标志是否考察的Map（getDistance方法中用到）中添加项，并赋值false，输出提示信息
     * @param personToAdd 输入的要加入的点
     */
    void addVertex(Person personToAdd){
        if(!add(personToAdd)){
            System.out.println("输入姓名重复");
            return;
        }
        isCounted.put(personToAdd,false);
        System.out.println("点"+personToAdd.getName()+"添加成功");
    }

    /**
     * 向图中添加边，如果输入点存在不在图中点，则输出错误信息并退出方法
     * 如果输入点均在图中，则按输入设置边，提示成功信息
     * @param sourceVertex 输入源点
     * @param targetVertex 输入终点
     */
    void addEdge(Person sourceVertex, Person targetVertex){
        if(!(vertices().contains(sourceVertex)) || !(vertices().contains(targetVertex))){
            System.out.println("输入点不在图中");
            return;
        }
        set(sourceVertex, targetVertex, 1);
        System.out.println("从"+sourceVertex.getName()+"到"+targetVertex.getName()+"设置成功");
    }

    /**
     * 返回从起始点到终止点的最短距离
     * @param sourceVertex 输入起始点
     * @param targetVertex 输入终止点
     * @return 如果输入点存在不在图中的点，或者两点之间不可达，则输出-1；如果两点在图中可达，输出最短距离
     */
    int getDistance(Person sourceVertex, Person targetVertex){
        if(!(vertices().contains(sourceVertex)) || !(vertices().contains(targetVertex))){
            System.out.println("输入点不在图中");
            return -1;
        }
        //1.特殊情况：输入相同的点，直接返回0
        if(sourceVertex.equals(targetVertex)){
            return 0;
        }
        //2.一般情况，利用队列进行广度优先搜索
        setIsCountedFalse();
        int distance = 0;
        Queue<Person> queue = new LinkedList<Person>();
        queue.offer(sourceVertex);
        while (queue.size()!=0){
            //分层进行广搜，每进行一层则距离+1
            int queueLength = queue.size();
            for(int i=0 ; i<queueLength ; i++){
                Person queueHead = queue.poll();
                for(Person nextToQueueHead : targets(queueHead).keySet()){
                    //找到目标点
                    if(nextToQueueHead.equals(targetVertex)){
                        return ++distance;
                    }
                    //非目标点，如果未被考察则加入队列
                    if(!(isCounted.get(nextToQueueHead))){
                        isCounted.put(nextToQueueHead,true);
                        queue.offer(nextToQueueHead);
                    }
                }
            }
            distance++;
        }
        //队列为空
        return -1;
    }

    /**
     * 将点是否考察过修改成否（false）
     * 每次调用getDistance方法前都需要进行的初始化操作
     */
    void setIsCountedFalse(){
        for(Person tempPerson : isCounted.keySet()){
            isCounted.put(tempPerson,false);
        }
    }

    /**
     * 返回已加入图中的点
     */
    Set<Person> getPersons(){
        return vertices();
    }

    /**
     * 返回输入点的目标点集合
     * @param inputPerson 输入的Person
     * @return 从输入Person出发的一条边，到达的点的集合
     */
    Set<Person> getTargets(Person inputPerson){
        Set<Person> setToReturn = new HashSet<>();
        for(Person tempPerson : targets(inputPerson).keySet()){
            setToReturn.add(tempPerson);
        }
        return setToReturn;
    }

    public static void main(String argv[]){
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
