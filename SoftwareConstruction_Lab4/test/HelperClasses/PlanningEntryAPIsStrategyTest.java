package HelperClasses;

import P2.Train;
import P2.TrainEntry;
import PlanningEntry.*;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

import P4.*;

public class PlanningEntryAPIsStrategyTest {
    /**
     * 测试位置（教室）冲突
     * 输入划分：两条目  1相同位置不冲突时间、2相同位置冲突时间、3不同位置不冲突时间、4不同位置冲突时间
     */
    @Test
    public void testCheckLocationConflict(){
        CourseEntry entry = new CourseEntry("course0");
        CourseEntry entry1 = new CourseEntry("course1");
        CourseEntry entry2 = new CourseEntry("course2");
        CourseEntry entry3 = new CourseEntry("course3");
        CourseEntry entry4 = new CourseEntry("course4");

        List<Location> locationConflict = new ArrayList<Location>();
        locationConflict.add(new Location("location1"));
        List<Location> locationNotConflict = new ArrayList<Location>();
        locationNotConflict.add(new Location("location2"));

        List<TimePair> timeConflict1 = new ArrayList<TimePair>();
        timeConflict1.add(new TimePair("2020-04-22 14:46", "2020-04-22 14:59"));
        List<TimePair> timeConflict2 = new ArrayList<TimePair>();
        timeConflict2.add(new TimePair("2020-04-22 14:50", "2020-04-22 14:58"));
        List<TimePair> timeNotConflict = new ArrayList<TimePair>();
        timeNotConflict.add(new TimePair("2021-04-22 19:46", "2021-04-22 21:59"));

        List<Teacher> sourceConflict = new ArrayList<>();
        sourceConflict.add(new Teacher("zhang", "412723200004030036", "male", "professor"));
        List<Teacher> sourceNotConflict = new ArrayList<>();
        sourceNotConflict.add(new Teacher("gao", "412723200004030035", "female", "professor"));

        entry.presetLocation(locationConflict);
        entry1.presetLocation(locationConflict);
        entry2.presetLocation(locationConflict);
        entry3.presetLocation(locationNotConflict);
        entry4.presetLocation(locationNotConflict);

        entry.presetTime(timeConflict1);
        entry1.presetTime(timeNotConflict);
        entry2.presetTime(timeConflict2);
        entry3.presetTime(timeNotConflict);
        entry4.presetTime(timeConflict2);

        entry.setSource(sourceConflict);
        entry1.setSource(sourceNotConflict);
        entry2.setSource(sourceNotConflict);
        entry3.setSource(sourceNotConflict);
        entry4.setSource(sourceConflict);

        PlanningEntryAPIsStrategy<Train> api = new PlanningEntryAPIsStrategy<>(1);

        List<PlanningEntry> inputEntries = new ArrayList<>();
        inputEntries.add(entry);
        inputEntries.add(entry1);
        assertFalse(api.checkLocation(inputEntries));

        inputEntries.remove(entry1);
        inputEntries.add(entry2);
        assertTrue(api.checkLocation(inputEntries));

        inputEntries.remove(entry2);
        inputEntries.add(entry3);
        assertFalse(api.checkLocation(inputEntries));

        inputEntries.remove(entry3);
        inputEntries.add(entry4);
        assertFalse(api.checkLocation(inputEntries));

    }


    /**
     * 测试单资源情况下的资源冲突
     * 输入划分：两条目  1相同资源不冲突时间、2相同资源冲突时间、3不同资源不冲突时间、4不同资源冲突时间
     */
    @Test
    public void testCheckSingleResourceExclusiveConflict(){
        List<PlanningEntry> entries = new ArrayList<PlanningEntry>();
        CourseEntry entry = new CourseEntry("course0");
        CourseEntry entry1 = new CourseEntry("course1");
        CourseEntry entry2 = new CourseEntry("course2");
        CourseEntry entry3 = new CourseEntry("course3");
        CourseEntry entry4 = new CourseEntry("course4");

        List<Location> locationConflict = new ArrayList<Location>();
        locationConflict.add(new Location("location1"));
        List<Location> locationNotConflict = new ArrayList<Location>();
        locationNotConflict.add(new Location("location2"));

        List<TimePair> timeConflict1 = new ArrayList<TimePair>();
        timeConflict1.add(new TimePair("2020-04-22 14:46", "2020-04-22 14:59"));
        List<TimePair> timeConflict2 = new ArrayList<TimePair>();
        timeConflict2.add(new TimePair("2020-04-22 14:50", "2020-04-22 14:58"));
        List<TimePair> timeNotConflict = new ArrayList<TimePair>();
        timeNotConflict.add(new TimePair("2021-04-22 19:46", "2021-04-22 21:59"));

        List<Teacher> sourceConflict = new ArrayList<>();
        sourceConflict.add(new Teacher("zhang", "412723200004030036", "male", "professor"));
        List<Teacher> sourceNotConflict = new ArrayList<>();
        sourceNotConflict.add(new Teacher("gao", "412723200004030035", "female", "professor"));

        entry.presetLocation(locationConflict);
        entry1.presetLocation(locationNotConflict);
        entry2.presetLocation(locationNotConflict);
        entry3.presetLocation(locationNotConflict);
        entry4.presetLocation(locationNotConflict);

        entry.presetTime(timeConflict1);
        entry1.presetTime(timeNotConflict);
        entry2.presetTime(timeConflict2);
        entry3.presetTime(timeNotConflict);
        entry4.presetTime(timeConflict2);

        entry.setSource(sourceConflict);
        entry1.setSource(sourceConflict);
        entry2.setSource(sourceConflict);
        entry3.setSource(sourceNotConflict);
        entry4.setSource(sourceNotConflict);

        PlanningEntryAPIsStrategy<Teacher> api = new PlanningEntryAPIsStrategy<>(1);
        List<PlanningEntry> inputEntries = new ArrayList<>();
        inputEntries.add(entry);
        inputEntries.add(entry1);
        assertFalse(api.checkResource(inputEntries));

        inputEntries.remove(entry1);
        inputEntries.add(entry2);
        assertTrue(api.checkResource(inputEntries));

        inputEntries.remove(entry2);
        inputEntries.add(entry3);
        assertFalse(api.checkResource(inputEntries));

        inputEntries.remove(entry3);
        inputEntries.add(entry4);
        assertFalse(api.checkResource(inputEntries));
    }

    /**
     * 测试多资源（火车）情况下的资源冲突
     * 输入划分：两条目  1相同资源不冲突时间、2相同资源冲突时间、3不同资源不冲突时间、4不同资源冲突时间
     */
    @Test
    public void testCheckMultipleResourceExclusiveConflict(){
        TrainEntry entry = new TrainEntry("entry0");
        TrainEntry entry1 = new TrainEntry("entry1");
        TrainEntry entry2 = new TrainEntry("entry2");
        TrainEntry entry3 = new TrainEntry("entry3");
        TrainEntry entry4 = new TrainEntry("entry4");

        List<Location> locationConflict = new ArrayList<Location>();
        locationConflict.add(new Location("location1"));
        locationConflict.add(new Location("location11"));
        List<Location> locationNotConflict = new ArrayList<Location>();
        locationNotConflict.add(new Location("location2"));
        locationNotConflict.add(new Location("location22"));

        List<TimePair> timeConflict1 = new ArrayList<TimePair>();
        timeConflict1.add(new TimePair("2020-04-22 14:46", "2020-04-22 14:59"));
        timeConflict1.add(new TimePair("2020-04-22 15:00", "2020-04-22 15:59"));
        List<TimePair> timeConflict2 = new ArrayList<TimePair>();
        timeConflict2.add(new TimePair("2020-04-22 14:50", "2020-04-22 14:58"));
        timeConflict2.add(new TimePair("2020-04-22 15:14", "2020-04-22 19:58"));
        List<TimePair> timeNotConflict = new ArrayList<TimePair>();
        timeNotConflict.add(new TimePair("2021-04-22 19:46", "2021-04-22 21:59"));
        timeNotConflict.add(new TimePair("2021-04-22 22:46", "2021-04-22 23:59"));

        List<Train> sourceConflict1 = new ArrayList<>();
        sourceConflict1.add(new Train(1, "Type1", 100, 1.5));
        sourceConflict1.add(new Train(11, "Type2", 110, 3.1));
        List<Train> sourceConflict2 = new ArrayList<>();
        sourceConflict2.add(new Train(10, "Type1", 100, 1.5));
        sourceConflict2.add(new Train(11, "Type2", 110, 3.1));
        List<Train> sourceNotConflict = new ArrayList<>();
        sourceNotConflict.add(new Train(2, "Type1", 150, 2.0));
        sourceNotConflict.add(new Train(22, "Type1", 150, 2.0));

        entry.presetLocation(locationConflict);
        entry1.presetLocation(locationNotConflict);
        entry2.presetLocation(locationNotConflict);
        entry3.presetLocation(locationNotConflict);
        entry4.presetLocation(locationNotConflict);

        entry.presetTime(timeConflict1);
        entry1.presetTime(timeNotConflict);
        entry2.presetTime(timeConflict2);
        entry3.presetTime(timeNotConflict);
        entry4.presetTime(timeConflict2);

        entry.setSource(sourceConflict1);
        entry1.setSource(sourceConflict2);
        entry2.setSource(sourceConflict2);
        entry3.setSource(sourceNotConflict);
        entry4.setSource(sourceNotConflict);

        PlanningEntryAPIsStrategy<Train> api = new PlanningEntryAPIsStrategy<>(2);
        List<PlanningEntry> inputEntries = new ArrayList<>();
        inputEntries.add(entry);
        inputEntries.add(entry1);
        assertFalse(api.checkResource(inputEntries));

        inputEntries.remove(entry1);
        inputEntries.add(entry2);
        assertTrue(api.checkResource(inputEntries));

        inputEntries.remove(entry2);
        inputEntries.add(entry3);
        assertFalse(api.checkResource(inputEntries));

        inputEntries.remove(entry3);
        inputEntries.add(entry4);
        assertFalse(api.checkResource(inputEntries));
    }

    /**
     * 测试 寻找使用指定资源的条目的 前序条目的方法
     */
    @Test
    public void testFindPreEntryPerResource(){
        TrainEntry entry = new TrainEntry("entry0");
        TrainEntry entry1 = new TrainEntry("entry1");
        TrainEntry entry2 = new TrainEntry("entry2");
        TrainEntry entry3 = new TrainEntry("entry3");
        TrainEntry entry4 = new TrainEntry("entry4");

        List<Location> locationConflict = new ArrayList<Location>();
        locationConflict.add(new Location("location1"));
        locationConflict.add(new Location("location11"));
        List<Location> locationNotConflict = new ArrayList<Location>();
        locationNotConflict.add(new Location("location2"));
        locationNotConflict.add(new Location("location22"));

        List<TimePair> time = new ArrayList<TimePair>();
        time.add(new TimePair("2020-04-22 14:46", "2020-04-22 14:59"));
        time.add(new TimePair("2020-04-22 15:00", "2020-04-22 15:59"));
        List<TimePair> timeWithIntersection = new ArrayList<TimePair>();
        timeWithIntersection.add(new TimePair("2020-04-22 14:10", "2020-04-22 14:11"));
        timeWithIntersection.add(new TimePair("2020-04-22 14:14", "2020-04-22 14:58"));
        List<TimePair> timePre = new ArrayList<TimePair>();
        timePre.add(new TimePair("2019-04-22 19:46", "2019-04-22 21:59"));
        timePre.add(new TimePair("2019-04-22 22:46", "2019-04-22 23:59"));
        List<TimePair> timeAfter = new ArrayList<TimePair>();
        timeAfter.add(new TimePair("2021-04-22 19:46", "2021-04-22 21:59"));
        timeAfter.add(new TimePair("2021-04-22 22:46", "2021-04-22 23:59"));

        List<Train> source1 = new ArrayList<>();
        source1.add(new Train(1, "Type1", 100, 1.5));
        source1.add(new Train(11, "Type2", 110, 3.1));
        List<Train> source2 = new ArrayList<>();
        source2.add(new Train(10, "Type1", 100, 1.5));
        source2.add(new Train(11, "Type2", 110, 3.1));
        List<Train> source3 = new ArrayList<>();
        source3.add(new Train(2, "Type1", 150, 2.0));
        source3.add(new Train(11, "Type2", 110, 3.1));
        List<Train> source4 = new ArrayList<>();
        source4.add(new Train(2, "Type1", 150, 2.0));

        entry.presetLocation(locationConflict);
        entry1.presetLocation(locationNotConflict);
        entry2.presetLocation(locationNotConflict);
        entry3.presetLocation(locationNotConflict);
        entry4.presetLocation(locationNotConflict);

        entry.setSource(source1);
        entry1.setSource(source2);
        entry2.setSource(source3);
        entry3.setSource(source3);
        entry4.setSource(source4);

        entry.presetTime(time);
        entry1.presetTime(timeWithIntersection);
        entry2.presetTime(timeAfter);
        entry3.presetTime(timePre);
        entry4.presetTime(timePre);

        List<PlanningEntry> entries = new ArrayList<>();
        entries.add(entry);
        entries.add(entry1);
        entries.add(entry2);
        entries.add(entry3);
        entries.add(entry4);

        PlanningEntryAPIsStrategy<Train> api = new PlanningEntryAPIsStrategy<>(2);
        Train sourceToFind = new Train(11, "Type2", 110, 3.1);
        assertEquals(entry3, api.findPreSameResourceEntry(sourceToFind, entry, entries));
    }
}
