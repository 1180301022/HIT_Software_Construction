package EntryState;
/**
 * 表示CANCELLED状态的状态类
 */
public class StateCancelled implements CommonState {
    @Override
    public State getState() {
        return State.CANCELLED;
    }

}
