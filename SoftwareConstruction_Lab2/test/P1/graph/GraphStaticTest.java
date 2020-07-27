/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

/**
 * Tests for static methods of Graph.
 * 
 * To facilitate testing multiple implementations of Graph, instance methods are
 * tested in GraphInstanceTest.
 */
public class GraphStaticTest {
    
    // Testing strategy
    //    empty()
    //      no inputs, only output is empty graph
    //       observe with vertices()


    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testEmptyVerticesEmpty() {
        assertEquals("expected empty() graph to have no vertices",
                Collections.emptySet(), Graph.empty().vertices());
    }

    /**
     * 测试图的add和vertices方法
     * add的参数划分：图中不存在的点，图中已存在的点
     *同时向图中添加一个点，并检查该点是否在图中
     *
     * vertices点个数的划分：等于0，大于0
     * 1.建立一个图，检测vertices中元素个数是否为0
     * 2.向图中加入点，测试图中的点是否和加入的点相同，数目是否相等
     */
    @Test
    public void testAddAndVertices(){
        Graph<Double> graphInstance = Graph.empty();
        Double vertex1 = 1.0;
        Double vertex2 = 2.0;
        Double vertex3 = 3.0;
        Set<Double> expectedVertices = new HashSet<Double>();
        assertEquals(expectedVertices,graphInstance.vertices());
        expectedVertices.add(vertex1);
        graphInstance.add(vertex1);
        assertEquals(expectedVertices,graphInstance.vertices());
        expectedVertices.add(vertex2);
        graphInstance.add(vertex2);
        assertEquals(expectedVertices,graphInstance.vertices());
        expectedVertices.add(vertex3);
        graphInstance.add(vertex3);
        assertEquals(expectedVertices,graphInstance.vertices());
        assertFalse(graphInstance.add(vertex1));
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
    public void testSet(){
        Graph<Double> graphInstance = Graph.empty();
        Double vertex1 = 1.0;
        Double vertex2 = 2.0;
        Double vertex3 = 3.0;
        graphInstance.add(vertex1);
        graphInstance.add(vertex2);
        assertEquals(0, graphInstance.set(vertex1, vertex2, 1));
        assertEquals(1, graphInstance.set(vertex1, vertex2, 2));
        assertEquals(0, graphInstance.set(vertex2, vertex3, 3));
        assertTrue(graphInstance.vertices().contains(vertex3));
    }

    /**
     * 测试图的remove方法
     * remove的参数划分：输入点在图中，输入点不在图中
     * 1.首先建立图 vertex3-1-> vertex1 -2->vertex2
     * 2.删除vertex1，检查是否返回true，检查和a有关的边是否都被删除
     * 3.重复删除vertex1，检查是否返回false
     */
    @Test
    public void testRemove(){
        Graph<Double> graphInstance = Graph.empty();
        Double vertex1 = 1.0;
        Double vertex2 = 2.0;
        Double vertex3 = 3.0;
        Double vertex4 = 4.0;
        graphInstance.add(vertex1);
        graphInstance.add(vertex2);
        graphInstance.add(vertex3);
        graphInstance.set(vertex3,vertex1,1);
        graphInstance.set(vertex1,vertex2,2);

        assertTrue(graphInstance.remove(vertex1));
        assertFalse(graphInstance.vertices().contains(vertex1));
        assertFalse(graphInstance.targets(vertex1).containsKey(vertex2));
        assertFalse(graphInstance.sources(vertex1).containsKey(vertex3));
        assertFalse(graphInstance.remove(vertex1));
    }

    /**
     * 测试图的sources和targets方法
     * 构建图（具体在实验报告中）并检查各点的sources是否符合预期
     */
    @Test
    public void testSourcesAndTargets(){
        Graph<Double> graphInstance = Graph.empty();
        Double vertex1 = 1.0;
        Double vertex2 = 2.0;
        Double vertex3 = 3.0;
        Double vertex4 = 4.0;
        graphInstance.add(vertex1);
        graphInstance.add(vertex2);
        graphInstance.add(vertex3);
        graphInstance.add(vertex4);
        graphInstance.set(vertex3,vertex1,1);
        graphInstance.set(vertex2,vertex1,2);
        graphInstance.set(vertex1,vertex4,3);

        Map<Double,Integer> targets1 = new HashMap<Double,Integer>();
        Map<Double,Integer> targets2 = new HashMap<Double,Integer>();
        Map<Double,Integer> targets3 = new HashMap<Double,Integer>();
        Map<Double,Integer> targets4 = new HashMap<Double,Integer>();
        targets1.put(vertex4, 3);
        targets3.put(vertex1, 1);
        targets2.put(vertex1, 2);

        Map<Double,Integer> sources1 = new HashMap<Double,Integer>();
        Map<Double,Integer> sources2 = new HashMap<Double,Integer>();
        Map<Double,Integer> sources3 = new HashMap<Double,Integer>();
        Map<Double,Integer> sources4 = new HashMap<Double,Integer>();
        sources1.put(vertex3, 1);
        sources1.put(vertex2, 2);
        sources4.put(vertex1, 3);

        assertEquals(targets1,graphInstance.targets(vertex1));
        assertEquals(targets2,graphInstance.targets(vertex2));
        assertEquals(targets3,graphInstance.targets(vertex3));
        assertEquals(Collections.emptyMap(),graphInstance.targets(vertex4));
    }
}
