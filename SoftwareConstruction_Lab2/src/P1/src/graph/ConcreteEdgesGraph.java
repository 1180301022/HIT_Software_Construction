/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;
import java.util.*;

/**
 * An implementation of Graph.
 */
public class ConcreteEdgesGraph<L> implements Graph<L> {
    
    private final Set<L> vertices = new HashSet<>();
    private final List<Edge<L>> edges = new ArrayList<>();
    
    // Abstraction function:
    //   AF(vertices, edges) = 含有顶点vertices、边edges的图
    // Representation invariant:
    //   edges中，所有的起点和终点都包含在vertices中
    // Safety from rep exposure:
    //   所有成员域都用private修饰

    private void checkRep() {
        for(Edge tempEdge : edges){
            if(!(vertices.contains(tempEdge.getSourceVertex())) ||
                    !(vertices.contains(tempEdge.getTargetVertex()))){
                assert false;
            }
        }
        assert true;
    }

    @Override
    public boolean add(L vertex) {
        for(L tempVertex : vertices){
            if(tempVertex.equals(vertex)){
                checkRep();
                return false;
            }
        }
        vertices.add(vertex);
        checkRep();
        return true;
    }
    
    @Override
    public int set(L source, L target, int weight) {
        boolean sourceFlag = false;
        boolean targetFlag = false;
        for(L tempVertex : vertices){
            if(tempVertex.equals(source)){
                sourceFlag = true;
            }
            if(tempVertex.equals(target)){
                targetFlag = true;
            }
        }
        if(!sourceFlag){
            vertices.add(source);
        }
        if(!targetFlag){
            vertices.add(target);
        }

        //遍历所有边，查看是否存在链接source和target的边
        for(Edge<L> tempEdge : edges) {
            //如果有且weight为0，则将去除该边，返回之前的权值；如果有且weight非0，则修改该边，返回之前的权值
            if(tempEdge.getSourceVertex().equals(source) && tempEdge.getTargetVertex().equals(target)) {
                edges.remove(tempEdge);
                if(weight!=0) {
                    edges.add(new Edge<L>(source, target, weight));
                }
                checkRep();
                return tempEdge.getWeight();
            }
        }
        //如果没有该边，将该边加入图，返回0
        edges.add(new Edge(source, target, weight));
        checkRep();
        return 0;
    }
    
    @Override
    public boolean remove(L vertex) {
        boolean vertexInSet = false;
        for(L tempVertex : vertices){
            if(tempVertex.equals(vertex)){
                vertexInSet = true;
            }
        }
        if(!vertexInSet){
            checkRep();
            return false;
        }
        //删除该点，遍历所有边，删除该点作为起点和终点的边
        vertices.remove(vertex);
        Iterator iter = edges.iterator();
        while(iter.hasNext()){
            Edge<L> tempEdge = (Edge<L>)iter.next();
            if(tempEdge.getSourceVertex().equals(vertex) || tempEdge.getTargetVertex().equals(vertex)){
                iter.remove();
            }
        }
        checkRep();
        return true;
    }
    
    @Override
    public Set<L> vertices() {
        checkRep();
        return new HashSet<L>(vertices);
    }
    
    @Override
    public Map<L, Integer> sources(L target) {
        Map<L, Integer> mapToReturn = new HashMap<L, Integer>();

        boolean vertexInSet = false;
        for(L tempVertex : vertices){
            if(tempVertex.equals(target)){
                vertexInSet = true;
            }
        }
        if(!vertexInSet){
            System.out.println("Input vertex do not exist.");
            checkRep();
            return mapToReturn;
        }

        for(Edge<L> tempEdge : edges){
            if(tempEdge.getTargetVertex().equals(target)){
                mapToReturn.put(tempEdge.getSourceVertex(), tempEdge.getWeight());
            }
        }
        checkRep();
        return mapToReturn;
    }
    
    @Override
    public Map<L, Integer> targets(L source) {
        Map<L, Integer> mapToReturn = new HashMap<L, Integer>();

        boolean vertexInSet = false;
        for(L tempVertex : vertices){
            if(tempVertex.equals(source)){
                vertexInSet = true;
            }
        }
        if(!vertexInSet){
            System.out.println("Input vertex do not exist.");
            checkRep();
            return mapToReturn;
        }
        for(Edge<L> tempEdge : edges){
            if(tempEdge.getSourceVertex().equals(source)){
                mapToReturn.put(tempEdge.getTargetVertex(), tempEdge.getWeight());
            }
        }
        checkRep();
        return mapToReturn;
    }

    @Override
    public String toString() {
        StringBuilder stringToReturn = new StringBuilder("");
        for(Edge<L> tempEdge : edges){
            stringToReturn.append(tempEdge.toString());
        }
        checkRep();
        return stringToReturn.toString();
    }
}

/**
 * 代表图中的边的类
 * 蕴含边的起点、终点、权值信息
 * 提供获取起点、获取终点、获取权值、转换为字符串的操作
 * 不可变类型
 */
class Edge<L> {
    private final int weight;
    private final L sourceVertex;
    private final L targetVertex;
    
    // Abstraction function:
    //   AF(source, target, weight) = 以source为起点，target为终点，权值为weight的边
    // Representation invariant:
    //   边的权值weight是非负整数
    // Safety from rep exposure:
    //   所有成员域都用private修饰
    //   成员域都是不可变类型，可以直接返回

    /**
     * 根据输入的起始点、终止点和权值新建一条边
     * @param sourceVertex 起始点
     * @param  targetVertex 终止点
     * @param weight 该边的权值
     * @return 返回起始点的名称
     */
    public Edge(L sourceVertex, L targetVertex, int weight) {
        this.sourceVertex = sourceVertex;
        this.targetVertex = targetVertex;
        this.weight = weight;
    }

    private void checkRep() {
        assert weight>=0;
    }

    /**
    * 获取该边的起始点
    * @return 返回起始点的名称
    */
    public L getSourceVertex() {
        checkRep();
        return sourceVertex;
    }

    /**
     * 获取该边的终止点
     * @return 返回终止点的名称
     */
    public L getTargetVertex() {
        checkRep();
        return targetVertex;
    }

    /**
     * 获取该边的权值
     * @return 返回边的权值
     */
    public int getWeight() {
        checkRep();
        return weight;
    }

    @Override
    public String toString() {
        checkRep();
        return "From:"+sourceVertex.toString()+" To:"+targetVertex.toString()+" Weight:"+weight+"\n";
    }
}
