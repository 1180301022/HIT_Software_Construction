/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import java.util.*;

/**
 * An implementation of Graph.
 */
public class ConcreteVerticesGraph<L> implements Graph<L> {
    
    private final List<Vertex<L>> vertices = new ArrayList<>();
    
    // Abstraction function:
    //   AF(vertices) = 由vertices中的点和边信息构成的图
    // Representation invariant:
    //   vertices!=null
    // Safety from rep exposure:
    //   成员域用private修饰
    //   返回时采用防御拷贝

    private void checkRep(){
        assert vertices != null;
    }

    @Override public boolean add(L vertex) {
        for(Vertex<L> tempVertex : vertices){
            if(tempVertex.getVertexName().equals(vertex)){
                checkRep();
                return false;
            }
        }
        vertices.add(new Vertex<L>(vertex));
        checkRep();
        return true;
    }
    
    @Override public int set(L source, L target, int weight) {
        //遍历vertices，寻找输入点是否存在
        Vertex sourceVertex = null;
        Vertex targetVertex = null;
        for(Vertex tempVertex : vertices){
            if(tempVertex.getVertexName().equals(source)){
                sourceVertex = tempVertex;
            }
            if(tempVertex.getVertexName().equals(target)){
                targetVertex = tempVertex;
            }
        }
        //如果vertices存在输入点，则已被赋值；如果仍为null，则需新建点
        if(sourceVertex==null){
            sourceVertex = new Vertex<L>(source);
            vertices.add(sourceVertex);
        }
        if(targetVertex==null){
            targetVertex = new Vertex<L>(target);
            vertices.add(targetVertex);
        }

        targetVertex.setSources(sourceVertex,weight);
        checkRep();
        return sourceVertex.setTargets(targetVertex,weight);
    }
    
    @Override public boolean remove(L vertex) {
        Vertex vertexToRemove = null;
        //首先查看是否是图中的点，如果不是直接返回false
        for(Vertex<L> tempVertex : vertices){
            if(tempVertex.getVertexName().equals(vertex)){
                vertexToRemove = tempVertex;
            }
        }
        if(vertexToRemove == null){
            checkRep();
            return false;
        }
        //此时得到需要删除的点vertexToRemove
        Map<Vertex<L>,Integer> itsSourceVertex = vertexToRemove.getSources();
        Map<Vertex<L>,Integer> itsTargetVertex = vertexToRemove.getTargets();
        for(Vertex<L> sourceVertex : itsSourceVertex.keySet()){
            sourceVertex.setTargets(vertexToRemove,0);
        }
        for(Vertex<L> targetVertex : itsTargetVertex.keySet()){
            targetVertex.setSources(vertexToRemove,0);
        }
        vertices.remove(vertexToRemove);
        checkRep();
        return true;

    }
    
    @Override public Set<L> vertices() {
        Set<L> setToReturn = new HashSet<L>();
        for(Vertex<L> tempVertex : vertices){
            setToReturn.add(tempVertex.getVertexName());
        }
        checkRep();
        return setToReturn;
    }
    
    @Override public Map<L, Integer> sources(L target) {
        Map<L, Integer> mapToReturn = new HashMap<L, Integer>();
        Vertex vertexToOperate = null;
        //首先查看是否是图中的点，如果不是直接返回空map
        for(Vertex<L> tempVertex : vertices){
            if(tempVertex.getVertexName().equals(target)){
                vertexToOperate = tempVertex;
            }
        }
        if(vertexToOperate == null){
            checkRep();
            return Collections.emptyMap();
        }
        //获取目标点的起始点
        Iterator iter = vertexToOperate.getSources().entrySet().iterator();
        while (iter.hasNext()){
            Map.Entry<Vertex<L>,Integer> tempEntry = (Map.Entry<Vertex<L>, Integer>) iter.next();
            mapToReturn.put(tempEntry.getKey().getVertexName(),tempEntry.getValue());
        }
        checkRep();
        return mapToReturn;
    }
    
    @Override public Map<L, Integer> targets(L source) {
        Map<L, Integer> mapToReturn = new HashMap<L, Integer>();
        Vertex vertexToOperate = null;
        //首先查看是否是图中的点，如果不是直接返回空map
        for(Vertex tempVertex : vertices){
            if(tempVertex.getVertexName().equals(source)){
                vertexToOperate = tempVertex;
            }
        }
        if(vertexToOperate == null){
            checkRep();
            return Collections.emptyMap();
        }

        Iterator iter = vertexToOperate.getTargets().entrySet().iterator();
        while (iter.hasNext()){
            Map.Entry<Vertex<L>,Integer> tempEntry = (Map.Entry<Vertex<L>, Integer>) iter.next();
            mapToReturn.put(tempEntry.getKey().getVertexName(),tempEntry.getValue());
        }
        checkRep();
        return mapToReturn;
    }

    @Override
    public String toString(){
        StringBuilder stringToReturn = new StringBuilder("");
        for(Vertex tempVertex : vertices){
            stringToReturn.append(tempVertex.toString());
        }
        checkRep();
        return stringToReturn.toString();
    }
    
}

/**
 * 组成图中的点的类型
 * 以vertexName为标识符，sources中记录了起点和权值，targets记录了终点和权值
 * 提供获取点标识符、获取起点和权值、获取终点和权值、设置起点、设置终点、转成字符串的操作
 * 可变类型
 */
class Vertex<L> {

    private final L vertexName;
    private final Map<Vertex<L>,Integer> sources = new HashMap<Vertex<L>,Integer>();
    private final Map<Vertex<L>,Integer> targets = new HashMap<Vertex<L>,Integer>();

    // Abstraction function:
    //   AF(vertexName, sources, targets) = 以vertexName为标识符，以sources中的点为起点，
    //                                      同时以targets中的点为终点的图中的点
    // Representation invariant:
    //   sources集合和targets集合中的权值都是非负整数
    // Safety from rep exposure:
    //   所有成员域都用private修饰
    //   返回时采用防御拷贝

    /**
     * 创建一个点
     * @param inputName 新建点的名称
     * @return 返回具有输入名称的新建的点
     */
    public Vertex(L inputName){
        vertexName = inputName;
    }

    private void checkRep(){
        for(Vertex tempVertex : sources.keySet()){
            if(sources.get(tempVertex) < 0){
                assert false;
            }
        }
        for(Vertex tempVertex : targets.keySet()){
            if(targets.get(tempVertex) < 0){
                assert false;
            }
        }
        assert true;
    }

    /**
     * 获取该点的名称
     * @return 返回该点的名称
     */
    public L getVertexName() {
        checkRep();
        return vertexName;
    }

    /**
     * 获取以该点作为起始点的终止点和对应边的权值
     * @return 以映射的关系，返回以该点作为起始点的终止点和边的权值的集合，如果不存在则返回空的集合
     */
    public Map<Vertex<L>,Integer> getTargets(){
        Map<Vertex<L>,Integer> mapToReturn = new HashMap<Vertex<L>,Integer>();
        for(Vertex<L> tempVertex : targets.keySet()){
            mapToReturn.put(tempVertex, targets.get(tempVertex));
        }
        checkRep();
        return mapToReturn;
    }

    /**
     * 获取以该点作为终止点的起始点和对应边的权值
     * @return 以映射的关系，返回以该点作为终止点的起始点和边的权值的集合，如果不存在则返回空的集合
     */
    public Map<Vertex<L>,Integer> getSources(){
        Map<Vertex<L>,Integer> mapToReturn = new HashMap<Vertex<L>,Integer>();
        for(Vertex<L> tempVertex : sources.keySet()){
            mapToReturn.put(tempVertex, sources.get(tempVertex));
        }
        checkRep();
        return mapToReturn;
    }

    /**
     * 设置以该点作为终止点的起始点和对应边的权值
     * @param inputVertex 输入的起始点
     * @param inputWeight 需要设置的该边的权值，要求为非负整数
     * @return 如果输入点和该点存在边，则返回修改之前边的权值；否则返回0
     */
    public int setSources(Vertex<L> inputVertex, int inputWeight){
        boolean vertexInSet = false;
        for(Vertex<L> tempVertex : sources.keySet()){
            if(tempVertex.equals(inputVertex)){
                vertexInSet = true;
            }
        }
        //1.该点不在sources中
        if(!vertexInSet){
            if (inputWeight != 0) {
                sources.put(inputVertex, inputWeight);
                checkRep();
                return 0;
            }
            else {
                checkRep();
                return -1;
            }
        }
        //2.该点在sources中
        else {
            int toReturn = sources.get(inputVertex);
            if(inputWeight!=0)
            {
                sources.put(inputVertex,inputWeight);
            }
            else {
                sources.remove(inputVertex);
            }
            checkRep();
            return toReturn;
        }
    }

    /**
     * 设置以该点作为起始点的终止点和对应边的权值
     * @param inputVertex 输入的终止点
     * @param inputWeight 需要设置的该边的权值，要求为非负整数
     * @return 如果输入点和该点存在边，则返回修改之前边的权值；否则返回0
     */
    public int setTargets(Vertex<L> inputVertex, int inputWeight){

        boolean vertexInSet = false;
        for(Vertex<L> tempVertex : targets.keySet()){
            if(tempVertex.equals(inputVertex)){
                vertexInSet = true;
            }
        }
        //1.该点不在targets中
        if(!vertexInSet){
            if (inputWeight != 0) {
                targets.put(inputVertex, inputWeight);
                checkRep();
                return 0;
            }
            else {
                checkRep();
                return -1;
            }
        }
        //2.该点在targets中
        else {
            int toReturn = targets.get(inputVertex);
            if(inputWeight!=0)
            {
                targets.put(inputVertex,inputWeight);
            }
            else {
                targets.remove(inputVertex);
            }
            checkRep();
            return toReturn;
        }
    }


    @Override
    public String toString() {
        String stringToReturn = vertexName+"\n";
        for(Vertex<L> tempVertex : sources.keySet()){
            stringToReturn += "from " + tempVertex.getVertexName() + " weight " + sources.get(tempVertex) + "\n";
        }
        for(Vertex<L> tempVertex : targets.keySet()){
            stringToReturn += "to " + tempVertex.getVertexName() + " weight " + targets.get(tempVertex) + "\n";
        }
        checkRep();
        return stringToReturn;
    }
}
