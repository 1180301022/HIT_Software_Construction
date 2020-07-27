package EntryState;
/**
 * 表示RUNNING状态的状态类
 */
public class StateRunning implements CommonState{
    @Override
    public State getState() {
        return State.RUNNING;
    }

}
