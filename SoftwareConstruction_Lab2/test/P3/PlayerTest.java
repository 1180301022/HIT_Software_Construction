package P3;

import static org.junit.Assert.*;
import org.junit.Test;

public class PlayerTest {
    /**
     * 测试策略
     * 创建一个新的玩家实例，测试成员方法输出是否和期望相等
     */
    @Test
    public void testPlayerMethods(){
        Player player = new Player("Dino", true);
        assertEquals("Dino", player.getPlayerName());
        assertEquals(true, player.getPlayerColor());
    }
}
