/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for ConcreteEdgesGraph.
 * 
 * This class runs the GraphInstanceTest tests against ConcreteEdgesGraph, as
 * well as tests for that particular implementation.
 * 
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteEdgesGraphTest extends GraphInstanceTest {
    
    /*
     * Provide a ConcreteEdgesGraph for tests in GraphInstanceTest.
     */
    @Override public Graph<String> emptyInstance() {
        return new ConcreteEdgesGraph();
    }
    
    /**
     * 测试从GraphInstanceTest类继承的方法
     * 测试策略在GraphInstanceTest中标明
     */
    @Test
    public void graphMethodsTest(){
        testAdd();
        testRemove();
        testSet();
        testVertices();
        testSources();
        testTargets();
    }

    /**
    * 向新建的图中添加若干点和边，调用toString()方法查看输出是否正确
     */
    @Test
    public void edgesToStringTest(){
        Graph<String> graphInstance = emptyInstance();
        String vertexA = "a";
        String vertexB = "b";
        String vertexC = "c";
        String toStringOutput = "From:a To:b Weight:1\n" + "From:b To:c Weight:2\n";
        graphInstance.add(vertexA);
        graphInstance.add(vertexB);
        graphInstance.add(vertexC);
        graphInstance.set(vertexA, vertexB, 1);
        graphInstance.set(vertexB, vertexC, 2);

        assertEquals(toStringOutput, graphInstance.toString());
    }

    /**
     * Edge的测试策略：
     * Edge的各种方法耦合性很强，所以选择在一个测试中对它们进行测试。
     * 测试方法：
     * 1.新建一条边，测试其属性值是否等于输入值
     * 2.测试toString方法是否输出正确
     */
    @Test
    public void EdgeTest(){
        String vertexA = "a";
        String vertexB = "b";
        String toStringOutput = "From:a To:b Weight:1\n";
        Edge edgeInstance = new Edge(vertexA, vertexB ,1);
        assertEquals(vertexA, edgeInstance.getSourceVertex());
        assertEquals(vertexB, edgeInstance.getTargetVertex());
        assertEquals(1 ,edgeInstance.getWeight());
        assertEquals(toStringOutput, edgeInstance.toString());
    }


    
}
