package HelperClasses;

import PlanningEntry.PlanningEntry;
import org.junit.Test;

public class EntryFactoryTest {
    /**
     * 测试策略：测试工厂方法产生的计划项是否是对应种类
     */
    @Test
    public void testCreateEntries(){
        PlanningEntry entry1 = EntryFactory.manufacture("flight", "e1");
        assert entry1.getEntryType().equals("flight");
        PlanningEntry entry2 = EntryFactory.manufacture("train", "e2");
        assert entry2.getEntryType().equals("train");
        PlanningEntry entry3 = EntryFactory.manufacture("course", "e3");
        assert entry3.getEntryType().equals("course");
    }
}
