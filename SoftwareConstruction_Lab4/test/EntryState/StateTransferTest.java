package EntryState;

import org.junit.Test;

import static org.junit.Assert.*;

public class StateTransferTest {
    /**
     * 测试策略：从每种状态出发，尝试转换成其余所有状态（遍历），查看是否满足要求
     */
    @Test
    public void testStateMethods(){
        //state1、state2从初始的WAITING状态可以转换成ALLOCATED、CANCELLED状态
        StateEnvironment state1 = new StateEnvironment();
        StateEnvironment state2 = new StateEnvironment();
        StateEnvironment state3 = new StateEnvironment();
        StateEnvironment state4 = new StateEnvironment();
        StateEnvironment state5 = new StateEnvironment();
        assertEquals(State.WAITING, state1.getState());
        state1.setBlocked();
        assertEquals(State.WAITING, state1.getState());
        state1.setRunning();
        assertEquals(State.WAITING, state1.getState());
        state1.setEnded();
        assertEquals(State.WAITING, state1.getState());
        state1.setAllocated();
        state3.setAllocated();
        state4.setAllocated();
        state5.setAllocated();
        assertEquals(State.ALLOCATED, state1.getState());
        state2.setCancelled();
        assertEquals(State.CANCELLED, state2.getState());
        //state1、state3处于ALLOCATED状态，可以转换成RUNNING、CANCELLED状态
        state1.setBlocked();
        assertEquals(State.ALLOCATED, state1.getState());
        state1.setAllocated();
        assertEquals(State.ALLOCATED, state1.getState());
        state1.setEnded();
        assertEquals(State.ALLOCATED, state1.getState());
        state3.setCancelled();
        assertEquals(State.CANCELLED, state3.getState());
        state1.setRunning();
        state4.setRunning();
        state5.setRunning();
        assertEquals(State.RUNNING, state1.getState());
        //state1、state4处于RUNNING状态，可以转换成ENDED、BLOCKED状态
        state1.setAllocated();
        assertEquals(State.RUNNING, state1.getState());
        state1.setRunning();
        assertEquals(State.RUNNING, state1.getState());
        state1.setCancelled();
        assertEquals(State.RUNNING, state1.getState());
        state1.setEnded();
        assertEquals(State.ENDED, state1.getState());
        state4.setBlocked();
        state5.setBlocked();
        assertEquals(State.BLOCKED, state4.getState());
        //state4、state5处于BLOCKED状态，可以转换成RUNNING、CANCELLED状态
        state4.setAllocated();
        assertEquals(State.BLOCKED, state4.getState());
        state4.setEnded();
        assertEquals(State.BLOCKED, state4.getState());
        state4.setCancelled();
        assertEquals(State.CANCELLED, state4.getState());
        state5.setRunning();
        assertEquals(State.RUNNING, state5.getState());
        //state1处于ENDED状态，不能再改变
        state1.setAllocated();
        assertEquals(State.ENDED, state1.getState());
        state1.setRunning();
        assertEquals(State.ENDED, state1.getState());
        state1.setCancelled();
        assertEquals(State.ENDED, state1.getState());
        state1.setBlocked();
        assertEquals(State.ENDED, state1.getState());
        state1.setEnded();
        assertEquals(State.ENDED, state1.getState());
        //state4处于CANCELLED状态，不能再改变
        state4.setAllocated();
        assertEquals(State.CANCELLED, state4.getState());
        state4.setRunning();
        assertEquals(State.CANCELLED, state4.getState());
        state4.setBlocked();
        assertEquals(State.CANCELLED, state4.getState());
        state4.setEnded();
        assertEquals(State.CANCELLED, state4.getState());
        state4.setCancelled();
        assertEquals(State.CANCELLED, state4.getState());
    }

}
