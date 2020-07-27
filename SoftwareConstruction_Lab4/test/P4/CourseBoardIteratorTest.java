package P4;

import HelperClasses.Location;
import HelperClasses.TimePair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CourseBoardIteratorTest {
    /**
     * 由于TrainBoard的可视化，所以在此只测试迭代器
     * 在排列窗口中的项目时，使用了迭代器，且加入列表的顺序按照从后到前
     * 查看迭代器是否能实现根据 Board上项目的 起始时间 从早到晚完成遍历
     */
    @Test
    public void testCourseBoard(){
        Location location1 = new Location("c1");
        Location location2 = new Location("c2");
        List<Location> locationList1 = new ArrayList<>();
        List<Location> locationList2 = new ArrayList<>();
        locationList1.add(location1);
        locationList2.add(location2);

        TimePair timePair1 = new TimePair("2020-05-04 08:00", "2020-05-04 09:30");
        TimePair timePair2 = new TimePair("2020-05-04 09:45", "2020-05-04 10:00");
        List<TimePair> timePairList1 = new ArrayList<>();
        List<TimePair> timePairList2 = new ArrayList<>();
        timePairList1.add(timePair1);
        timePairList2.add(timePair2);

        Teacher teacher1 = new Teacher("zhang", "412723200004030036", "male", "professor");
        Teacher teacher2 = new Teacher("gao", "412723200004030035", "male", "professor");
        List<Teacher> teacherList1 = new ArrayList<>();
        List<Teacher> teacherList2 = new ArrayList<>();
        teacherList1.add(teacher1);
        teacherList2.add(teacher2);

        CourseEntry entry1 = new CourseEntry("course1");
        CourseEntry entry2 = new CourseEntry("course2");
        CourseEntry entry3 = new CourseEntry("course3");
        entry1.presetLocation(locationList1);
        entry2.presetLocation(locationList1);
        entry3.presetLocation(locationList2);
        entry1.presetTime(timePairList1);
        entry2.presetTime(timePairList2);
        entry3.presetTime(timePairList1);
        entry1.setSource(teacherList1);
        entry2.setSource(teacherList2);
        entry3.setSource(teacherList2);

        List<CourseEntry> entries = new ArrayList<>();
        entries.add(entry2);
        entries.add(entry1);
        entries.add(entry3);

        new CourseBoard("c1", entries);
    }
}
