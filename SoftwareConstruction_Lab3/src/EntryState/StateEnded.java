package EntryState;
/**
 * 表示ENDED状态的状态类
 */
public class StateEnded implements CommonState {
    @Override
    public State getState() {
        return State.ENDED;
    }
}
