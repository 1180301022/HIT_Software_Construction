package debug;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AllocatePlaneTest {
    /**
     * 测试是否能计算出能否安排无冲突分配
     * 输入划分：可以无冲突分配，不可以无冲突分配
     */


    //1.可以无冲突分配
    @Test
    public void testAllocationSuccess(){
        FlightClient flightClient = new FlightClient();
        Plane plane = new Plane();
        plane.setPlaneNo("123");
        List<Plane> planes = new ArrayList<>();
        planes.add(plane);

        Flight flight1 = new Flight();
        flight1.setFlightNo("123");
        Calendar calendar10 = Calendar.getInstance();
        calendar10.set(2020, 5, 27);
        Calendar calendar11 = Calendar.getInstance();
        calendar11.set(2020, 5, 27, 18, 00, 00);
        Calendar calendar12 = Calendar.getInstance();
        calendar12.set(2020, 5, 27, 19, 00, 00);
        flight1.setFlightDate(calendar10);
        flight1.setDepartTime(calendar11);
        flight1.setArrivalTime(calendar12);

        Flight flight2 = new Flight();
        flight2.setFlightNo("456");
        Calendar calendar20 = Calendar.getInstance();
        calendar10.set(2020, 5, 27);
        Calendar calendar21 = Calendar.getInstance();
        calendar11.set(2020, 5, 27, 20, 00, 00);
        Calendar calendar22 = Calendar.getInstance();
        calendar12.set(2020, 5, 27, 21, 00, 00);
        flight2.setFlightDate(calendar20);
        flight2.setDepartTime(calendar21);
        flight2.setArrivalTime(calendar22);

        List<Flight> flights = new ArrayList<>();
        flights.add(flight1);
        flights.add(flight2);
        assert flightClient.planeAllocation(planes, flights)==true;


    }

    //2.不可以无冲突分配
    @Test
    public void testAllocationFail(){
        FlightClient flightClient = new FlightClient();
        Plane plane = new Plane();
        plane.setPlaneNo("1430");
        List<Plane> planes = new ArrayList<>();
        planes.add(plane);

        Flight flight1 = new Flight();
        flight1.setFlightNo("123");
        Calendar calendar10 = Calendar.getInstance();
        calendar10.set(2020, 5, 27);
        Calendar calendar11 = Calendar.getInstance();
        calendar11.set(2020, 5, 27, 18, 00, 00);
        Calendar calendar12 = Calendar.getInstance();
        calendar12.set(2020, 5, 27, 19, 00, 00);
        flight1.setFlightDate(calendar10);
        flight1.setDepartTime(calendar11);
        flight1.setArrivalTime(calendar12);

        Flight flight2 = new Flight();
        flight2.setFlightNo("456");
        Calendar calendar20 = Calendar.getInstance();
        calendar20.set(2020, 5, 27);
        Calendar calendar21 = Calendar.getInstance();
        calendar21.set(2020, 5, 27, 18, 10, 00);
        Calendar calendar22 = Calendar.getInstance();
        calendar22.set(2020, 5, 27, 21, 00, 00);
        flight2.setFlightDate(calendar20);
        flight2.setDepartTime(calendar21);
        flight2.setArrivalTime(calendar22);

        List<Flight> flights = new ArrayList<>();
        flights.add(flight1);
        flights.add(flight2);
        assert flightClient.planeAllocation(planes, flights)==false;
    }
}
