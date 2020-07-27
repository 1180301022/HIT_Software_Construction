package EntryState;
/**
 * 表示WAITING状态的状态类
 */
public class StateWaiting implements CommonState{
    @Override
    public State getState() {
        return State.WAITING;
    }

}
