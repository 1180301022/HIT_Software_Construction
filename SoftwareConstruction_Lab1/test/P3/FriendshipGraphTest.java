package P3;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.*;

public class FriendshipGraphTest
{
    FriendshipGraph graph = null;
    //HashSet<Person> persons = null;
    Person p1 = null;
    Person p2 = null;
    Person p3 = null;
    Person p4 = null;
    Person p5 = null;

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

    @Test
    public void addVertexTest()
    {
        graph.addVertex(p1);
        graph.addVertex(p2);
        graph.addVertex(p3);
        graph.addVertex(p4);
        assertEquals(true,graph.persons.contains(p1));
        assertEquals(true,graph.persons.contains(p2));
        assertEquals(true,graph.persons.contains(p3));
        assertEquals(true,graph.persons.contains(p4));
    }

    @Test
    public void addEdgeTest()
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


        assertEquals(true,p1.friends.contains(p3));
        assertEquals(true,p3.friends.contains(p1));
        assertEquals(true,p1.friends.contains(p4));
        assertEquals(true,p4.friends.contains(p1));
        assertEquals(true,p3.friends.contains(p4));
        assertEquals(true,p4.friends.contains(p3));
        assertEquals(true,p2.friends.contains(p4));
        assertEquals(true,p4.friends.contains(p2));
    }
    @Test
    public void getDistanceTest()
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