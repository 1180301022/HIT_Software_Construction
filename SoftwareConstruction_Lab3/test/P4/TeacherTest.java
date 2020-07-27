package P4;

import org.junit.Test;
import static org.junit.Assert.*;

public class TeacherTest {
    /**
     * 测试Teacher类的方法
     * 考察输出是否等于设置值
     */
    @Test
    public void testTeacherMethods(){
        Teacher teacher = new Teacher("zhang", "412723200004030036", "male", "professor");
        assertEquals("zhang", teacher.getName());
        assertEquals("412723200004030036", teacher.getId());
        assertEquals("male", teacher.getGender());
        assertEquals("professor", teacher.getTitle());

        Teacher teacher1 = new Teacher("zhang", "412723200004030036", "male", "professor");
        assertTrue(teacher.equals(teacher1));
    }
}
