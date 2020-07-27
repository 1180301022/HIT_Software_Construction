package P4;

import HelperClasses.Location;
import HelperClasses.TimePair;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class CourseEntryTest {
    /**
     * 测试CourseEntry的getter、setter方法
     * 查看输出是否等于期望
     */
    @Test
    public void testCourseEntryMethods(){
        Location location1 = new Location("c1");
        Location location2 = new Location("c2");
        List<Location> locationList1 = new ArrayList<>();
        List<Location> locationList2 = new ArrayList<>();
        locationList1.add(location1);
        locationList2.add(location2);

        TimePair timePair1 = new TimePair("2020-04-30 08:00", "2020-04-30 09:30");
        List<TimePair> timePairList1 = new ArrayList<>();
        timePairList1.add(timePair1);

        Teacher teacher1 = new Teacher("zhang", "412723200004030036", "male", "professor");
        List<Teacher> teacherList1 = new ArrayList<>();
        teacherList1.add(teacher1);

        CourseEntry entry1 = new CourseEntry("course1");

        entry1.presetLocation(locationList1);
        entry1.presetTime(timePairList1);
        entry1.setSource(teacherList1);
        assertEquals(location1, entry1.getLocation().get(0));
        assertEquals(timePair1, entry1.getTime().get(0));
        assertEquals(teacher1, entry1.getSource().get(0));

        entry1.resetLocation(locationList2);
        assertEquals(location2, entry1.getLocation().get(0));
    }
}
