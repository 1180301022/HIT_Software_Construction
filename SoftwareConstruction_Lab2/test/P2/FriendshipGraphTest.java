package P2;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.*;

/**
 * 对FriendshipGraph类的测试
 * 设计一个相对简单，但能覆盖较多代码的测试用例对各个方法进行测试
 */
public class FriendshipGraphTest
{
    FriendshipGraph graph = null;
    Person p1 = null;
    Person p2 = null;
    Person p3 = null;
    Person p4 = null;
    Person p5 = null;

    /**
     * 每个方法开始前的初始操作，创建一个新图，和5个新的Person实例
     */
    @Before
    public void beforeAllTests()
    {
        graph = new FriendshipGraph();
        p1 = new Person("Dino");
        p2 = new Person("Xiaozhu");
        p3 = new Person("Damao");
        p4 = new Person("Wubai");
        p5 = new Person("Zy");
    }

    /**
     * 测试addVertex和getPersons方法
     * 由于两方法耦合度高，所以选择一并测试
     * 加入点分类：已在图中的点、未在图中的点
     */
    @Test
    public void testAddVertexAndGetPersons()
    {
        graph.addVertex(p1);
        graph.addVertex(p2);
        graph.addVertex(p3);
        graph.addVertex(p4);
        Person p11 = new Person("Dino");
        assertTrue(graph.getPersons().contains(p1));
        assertTrue(graph.getPersons().contains(p2));
        assertTrue(graph.getPersons().contains(p3));
        assertTrue(graph.getPersons().contains(p4));
        assertTrue(graph.getPersons().contains(p11));
        graph.addVertex(p11);
    }

    /**
     * 测试addEdge方法和getTargets方法
     * 测试用例在实验报告中
     */
    @Test
    public void testAddEdgeAndGetTargets()
    {
        graph.addVertex(p1);
        graph.addVertex(p2);
        graph.addVertex(p3);
        graph.addVertex(p4);

        graph.addEdge(p1,p3);
        graph.addEdge(p3,p1);
        graph.addEdge(p1,p4);
        graph.addEdge(p4,p1);
        graph.addEdge(p3,p4);
        graph.addEdge(p4,p3);
        graph.addEdge(p2,p4);
        graph.addEdge(p4,p2);

        assertEquals(true,graph.getTargets(p1).contains(p3));
        assertEquals(true,graph.getTargets(p3).contains(p1));
        assertEquals(true,graph.getTargets(p1).contains(p4));
        assertEquals(true,graph.getTargets(p4).contains(p1));
        assertEquals(true,graph.getTargets(p3).contains(p4));
        assertEquals(true,graph.getTargets(p4).contains(p3));
        assertEquals(true,graph.getTargets(p2).contains(p4));
        assertEquals(true,graph.getTargets(p4).contains(p2));
    }

    /**
     * 测试getDistance方法
     * 测试用例在实验报告中
     */
    @Test
    public void testGetDistance()
    {
        graph.addVertex(p1);
        graph.addVertex(p2);
        graph.addVertex(p3);
        graph.addVertex(p4);

        graph.addEdge(p1,p3);
        graph.addEdge(p3,p1);
        graph.addEdge(p1,p4);
        graph.addEdge(p4,p1);
        graph.addEdge(p3,p4);
        graph.addEdge(p4,p3);
        graph.addEdge(p2,p4);
        graph.addEdge(p4,p2);

        assertEquals(0,graph.getDistance(p1,p1));
        assertEquals(2,graph.getDistance(p1,p2));
        assertEquals(1,graph.getDistance(p1,p3));
        assertEquals(1,graph.getDistance(p1,p4));
        assertEquals(-1,graph.getDistance(p1,p5));
        assertEquals(2,graph.getDistance(p2,p3));
        assertEquals(1,graph.getDistance(p2,p4));
        assertEquals(-1,graph.getDistance(p2,p5));
        assertEquals(-1,graph.getDistance(p3,p5));
        assertEquals(-1,graph.getDistance(p4,p5));
        assertEquals(1,graph.getDistance(p3,p4));

    }
}