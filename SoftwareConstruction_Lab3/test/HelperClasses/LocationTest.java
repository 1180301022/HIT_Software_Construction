package HelperClasses;

import org.junit.Test;
import static org.junit.Assert.*;

public class LocationTest {
    /**
     * 测试Location的equals方法和getLocationName方法
     * 输入划分：位置相等，位置不相等
     */
    @Test
    public void testLocationMethods(){
        Location location1 = new Location("1");
        Location location11 = new Location("1");
        Location location2 = new Location("2");
        assertEquals("1", location1.getLocationName());
        assertEquals(location1, location11);
        assertFalse(location1.equals(location2));
    }
}
