package EntryState;
/**
 * 表示BLOCKED状态的状态类
 */
public class StateBlocked implements CommonState {
    @Override
    public State getState() {
        return State.BLOCKED;
    }

}
