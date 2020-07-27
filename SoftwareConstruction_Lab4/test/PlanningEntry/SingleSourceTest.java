package PlanningEntry;

import P4.Teacher;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SingleSourceTest {
    /**
     * 测试SingleSource类的方法
     * 考察输出是否等于设置值
     * 输入划分：和设置值相等，和设置值不等
     */
    @Test
    public void testSingleSourceMethods(){
        SourceType<Teacher> source = new SingleSource();
        Teacher zhang = new Teacher("zhang", "412723200004030036", "male", "professor");
        Teacher gao = new Teacher("gao", "412723200004030035", "female", "professor");
        List<Teacher> list = new ArrayList<>();
        list.add(zhang);
        source.setSource(list);
        assertEquals(zhang, source.getSource().get(0));
        assertFalse(gao.equals(source.getSource().get(0)));
    }
}
