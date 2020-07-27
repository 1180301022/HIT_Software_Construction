/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Tests for ConcreteVerticesGraph.
 * 
 * This class runs the GraphInstanceTest tests against ConcreteVerticesGraph, as
 * well as tests for that particular implementation.
 * 
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteVerticesGraphTest extends GraphInstanceTest {
    
    /*
     * Provide a ConcreteVerticesGraph for tests in GraphInstanceTest.
     */
    @Override public Graph<String> emptyInstance() {
        return new ConcreteVerticesGraph();
    }

    /**
     * 新建图（c-1-> a -2->b），调用toString()方法查看输出是否正确
     */
    @Test
    public void testGraphToString(){
        Graph graphInstance = emptyInstance();
        graphInstance.add("a");
        graphInstance.add("b");
        graphInstance.add("c");
        graphInstance.set("c","a",1);
        graphInstance.set("a","b",2);
        String stringToOutput = "a\n" + "from c weight 1\n" +
                "to b weight 2\n" + "b\n" + "from a weight 2\n" + "c\n" + "to a weight 1\n";
        assertEquals(stringToOutput,graphInstance.toString());
    }

    /**
     * 测试从GraphInstanceTest类继承的方法
     * 测试策略在GraphInstanceTest中标明
     */
    @Test
    public void testGraphMethods(){
        Graph graphInstance = emptyInstance();
        testRemove();
        testTargets();
        testSources();
        testVertices();
        testAdd();
        testSet();
    }


    /**
     * Vertex类的方法的测试策略：
     * 利用与GraphInstanceTest中相同的图（经测试代码覆盖度较高），对Vertex类的各种方法进行测试。
     * 每个方法具体的测试策略在方法头标明。
     */

    /**
     * 1.新建一个图，测试其属性值是否等于输入值
     * 2.测试每个点的getTargets方法是否返回正确的结果
     */
    @Test
    public void testGetTargets(){
        Vertex vertexA = new Vertex("a");
        Vertex vertexB = new Vertex("b");
        Vertex vertexC = new Vertex("c");
        Vertex vertexD = new Vertex("d");
        Vertex vertexE = new Vertex("e");
        Map<Vertex,Integer> targetsA = new HashMap<Vertex,Integer>();
        Map<Vertex,Integer> targetsB = new HashMap<Vertex,Integer>();
        Map<Vertex,Integer> targetsC = new HashMap<Vertex,Integer>();
        Map<Vertex,Integer> targetsD = new HashMap<Vertex,Integer>();
        Map<Vertex,Integer> targetsE = new HashMap<Vertex,Integer>();
        targetsA.put(vertexD,3);
        targetsB.put(vertexA,2);
        targetsC.put(vertexA,1);
        vertexA.setTargets(vertexD,3);
        vertexD.setSources(vertexA,3);
        vertexB.setTargets(vertexA,2);
        vertexA.setSources(vertexB,2);
        vertexC.setTargets(vertexA,1);
        vertexA.setSources(vertexC,1);
        assertEquals(targetsA,vertexA.getTargets());
        assertEquals(targetsB,vertexB.getTargets());
        assertEquals(targetsC,vertexC.getTargets());
        assertEquals(Collections.emptyMap(),vertexD.getTargets());
        assertEquals(Collections.emptyMap(),vertexE.getTargets());
    }

    /**
     * 1.新建一个图，测试其属性值是否等于输入值
     * 2.测试每个点的getSources方法是否返回正确的结果
     */
    @Test
    public void testGetSources(){
        Vertex vertexA = new Vertex("a");
        Vertex vertexB = new Vertex("b");
        Vertex vertexC = new Vertex("c");
        Vertex vertexD = new Vertex("d");
        Vertex vertexE = new Vertex("e");
        Map<Vertex,Integer> sourcesA = new HashMap<Vertex,Integer>();
        Map<Vertex,Integer> sourcesB = new HashMap<Vertex,Integer>();
        Map<Vertex,Integer> sourcesC = new HashMap<Vertex,Integer>();
        Map<Vertex,Integer> sourcesD = new HashMap<Vertex,Integer>();
        Map<Vertex,Integer> sourcesE = new HashMap<Vertex,Integer>();
        sourcesA.put(vertexB,2);
        sourcesA.put(vertexC,1);
        sourcesD.put(vertexA,3);
        vertexA.setTargets(vertexD,3);
        vertexD.setSources(vertexA,3);
        vertexB.setTargets(vertexA,2);
        vertexA.setSources(vertexB,2);
        vertexC.setTargets(vertexA,1);
        vertexA.setSources(vertexC,1);
        assertEquals(sourcesA,vertexA.getSources());
        assertEquals(Collections.emptyMap(),vertexB.getSources());
        assertEquals(Collections.emptyMap(),vertexC.getSources());
        assertEquals(sourcesD,vertexD.getSources());
        assertEquals(Collections.emptyMap(),vertexE.getSources());
    }

    /**
     * 1.新建一个图，测试其属性值是否等于输入值
     * 2.测试toString方法是否输出正确
     */
    @Test
    public void testVertexToString(){
        Vertex vertexA = new Vertex("a");
        Vertex vertexB = new Vertex("b");
        Vertex vertexC = new Vertex("c");
        Vertex vertexD = new Vertex("d");
        Vertex vertexE = new Vertex("e");
        vertexA.setSources(vertexC,1);
        vertexA.setSources(vertexB,2);
        vertexD.setSources(vertexA,3);
        vertexC.setTargets(vertexA,1);
        vertexB.setTargets(vertexA,2);
        vertexA.setTargets(vertexD,3);
        String expectedString = "a\n"+"from b weight 2\n"+"from c weight 1\n"+"to d weight 3\n";
        assertEquals(expectedString,vertexA.toString());
    }
}
