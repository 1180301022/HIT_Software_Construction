package EntryState;

/**
 * 表示ALLOCATED状态的状态类
 */
public class StateAllocated implements CommonState {
    @Override
    public State getState() {
        return State.ALLOCATED;
    }
}
