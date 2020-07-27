/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

/**
 * Tests for instance methods of Graph.
 * 
 * <p>PS2 instructions: you MUST NOT add constructors, fields, or non-@Test
 * methods to this class, or change the spec of {@link #emptyInstance()}.
 * Your tests MUST only obtain Graph instances by calling emptyInstance().
 * Your tests MUST NOT refer to specific concrete implementations.
 */
public abstract class GraphInstanceTest {

    /**
    * 测试策略：
    * 设计尽量简单的测试实例，覆盖尽量多的源程序代码。
    * 测试图的接口方法，从而在图的不同实现类的测试中复用。
    * 具体的测试策略在每个测试方法头处详细说明。
     */
    
    /**
     * Overridden by implementation-specific test classes.
     * 
     * @return a new empty graph of the particular implementation being tested
     */
    public abstract Graph<String> emptyInstance();
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    /**
    * 测试图的add方法
    * add的参数划分：图中不存在的点，图中已存在的点
     *同时向图中添加一个点，并检查该点是否在图中
     */
    @Test
    public void testAdd () {
        Graph<String> graphInstance = emptyInstance();
        String vertexToAdd = "a";
        assertTrue(graphInstance.add(vertexToAdd));
        assertTrue(graphInstance.vertices().contains(vertexToAdd));
        assertFalse(graphInstance.add(vertexToAdd));
    }

    /**
    * 测试图的set方法
     * set的参数划分：输入的边不在图中，输入的边在图中
     *               输入的权值为0，输入的权值非0
     *               输入的点不在图中，输入的点在图中
    * 1.向图中添加两点，并设置两点之间的边值。第一次设置时返回0
    * 2.第二次设置时，返回第一次设置的值
    * 3.在set方法中输入未加入点，set结束后，查看是否在图中
     */
    @Test
    public void testSet() {
        Graph<String> graphInstance = emptyInstance();
        String vertex1 = "a";
        String vertex2 = "b";
        String vertex3 = "c";
        graphInstance.add(vertex1);
        graphInstance.add(vertex2);
        assertEquals(0,graphInstance.set(vertex1,vertex2,2));
        assertEquals(2,graphInstance.set(vertex1,vertex2,0));
        assertEquals(0,graphInstance.set(vertex1,vertex3,3));
        assertTrue(graphInstance.vertices().contains(vertex3));
    }

    /**
    * 测试图的remove方法
     * remove的参数划分：输入点在图中，输入点不在图中
    * 1.首先建立图 c-1-> a -2->b
    * 2.删除a点，检查是否返回true，检查和a有关的边是否都被删除
    * 3.重复删除a点，检查是否返回false
     */
    @Test
    public void testRemove(){
        Graph<String> graphInstance = emptyInstance();
        String vertexA = "a";
        String vertexB = "b";
        String vertexC = "c";
        graphInstance.add(vertexA);
        graphInstance.add(vertexB);
        graphInstance.add(vertexC);
        graphInstance.set(vertexC,vertexA,1);
        graphInstance.set(vertexA,vertexB,2);

        assertTrue(graphInstance.remove(vertexA));
        assertFalse(graphInstance.vertices().contains(vertexA));
        assertFalse(graphInstance.targets(vertexA).containsKey(vertexB));
        assertFalse(graphInstance.sources(vertexA).containsKey(vertexC));
        assertFalse(graphInstance.remove(vertexA));
    }

    /**
    * 测试图的vertices方法
     * vertices点个数的划分：等于0，大于0
     * 1.建立一个图，检测vertices中元素个数是否为0
     * 2.向图中加入点，测试图中的点是否和加入的点相同，数目是否相等
     */
    @Test
    public void testVertices(){
        Graph<String> graphInstance = emptyInstance();
        Set<String> resultSet = new HashSet<String>();
        String vertexA = "a";
        String vertexB = "b";
        assertEquals(0,graphInstance.vertices().size());
        resultSet.add(vertexA);
        resultSet.add(vertexB);
        graphInstance.add(vertexA);
        graphInstance.add(vertexB);
        assertEquals(2,graphInstance.vertices().size());
        assertTrue(graphInstance.vertices().containsAll(resultSet));
    }

    /**
    * 测试图的sources方法
    * 构建图（具体在实验报告中）并检查各点的sources是否符合预期
     */
    @Test
    public void testSources(){
        Graph<String> graphInstance = emptyInstance();
        String vertexA = "a";
        String vertexB = "b";
        String vertexC = "c";
        String vertexD = "d";
        String vertexE = "e";
        graphInstance.add(vertexA);
        graphInstance.add(vertexB);
        graphInstance.add(vertexC);
        graphInstance.add(vertexD);
        graphInstance.add(vertexE);
        graphInstance.set(vertexC,vertexA,1);
        graphInstance.set(vertexB,vertexA,2);
        graphInstance.set(vertexA,vertexD,3);

        Map<String,Integer> sourcesA = new HashMap<String,Integer>();
        Map<String,Integer> sourcesB = new HashMap<String,Integer>();
        Map<String,Integer> sourcesC = new HashMap<String,Integer>();
        Map<String,Integer> sourcesD = new HashMap<String,Integer>();
        Map<String,Integer> sourcesE = new HashMap<String,Integer>();
        sourcesA.put(vertexC, 1);
        sourcesA.put(vertexB, 2);
        sourcesD.put(vertexA, 3);

        assertEquals(sourcesA,graphInstance.sources(vertexA));
        assertEquals(Collections.emptyMap(),graphInstance.sources(vertexB));
        assertEquals(Collections.emptyMap(),graphInstance.sources(vertexC));
        assertEquals(sourcesD,graphInstance.sources(vertexD));
        assertEquals(Collections.emptyMap(),graphInstance.sources(vertexE));
    }

    /**
     * 测试图的targets方法
     * 构建图（具体在实验报告中）并检查各点的targets是否符合预期
     */
    @Test
    public void testTargets(){
        Graph<String> graphInstance = emptyInstance();
        String vertexA = "a";
        String vertexB = "b";
        String vertexC = "c";
        String vertexD = "d";
        String vertexE = "e";
        graphInstance.add(vertexA);
        graphInstance.add(vertexB);
        graphInstance.add(vertexC);
        graphInstance.add(vertexD);
        graphInstance.add(vertexE);
        graphInstance.set(vertexC,vertexA,1);
        graphInstance.set(vertexB,vertexA,2);
        graphInstance.set(vertexA,vertexD,3);

        Map<String,Integer> targetsA = new HashMap<String,Integer>();
        Map<String,Integer> targetsB = new HashMap<String,Integer>();
        Map<String,Integer> targetsC = new HashMap<String,Integer>();
        Map<String,Integer> targetsD = new HashMap<String,Integer>();
        Map<String,Integer> targetsE = new HashMap<String,Integer>();
        targetsA.put(vertexD, 3);
        targetsC.put(vertexA, 1);
        targetsB.put(vertexA, 2);

        assertEquals(targetsA,graphInstance.targets(vertexA));
        assertEquals(targetsB,graphInstance.targets(vertexB));
        assertEquals(targetsC,graphInstance.targets(vertexC));
        assertEquals(Collections.emptyMap(),graphInstance.targets(vertexD));
        assertEquals(Collections.emptyMap(),graphInstance.targets(vertexE));
    }
}
