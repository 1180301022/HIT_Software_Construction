package EntryState;

/**
 * 改变状态的环境类
 * 可变类
 */
public class StateEnvironment {
    private CommonState state;
    //AF：状态为state的环境
    //RI：true
    //Safety from rep exposure：成员域使用private修饰

    /**
     * 创建一个新环境类，并将初始状态设置为WAITING
     */
    public StateEnvironment(){
        state = new StateWaiting();
    }

    /**
     * 将状态设置为ALLOCATED
     * @return 操作是否成功
     */
    public boolean setAllocated(){
        State nowConcreteState = state.getState();
        if(nowConcreteState == State.WAITING){
            state = new StateAllocated();
            return true;
        }
        return false;
    }

    /**
     * 将状态设置为RUNNING
     * @return 操作是否成功
     */
    public boolean setRunning(){
        State nowConcreteState = state.getState();
        if(nowConcreteState == State.ALLOCATED || nowConcreteState == State.BLOCKED){
            state = new StateRunning();
            return true;
        }
        return false;
    }

    /**
     * 将状态设置为CANCELLED
     * @return 操作是否成功
     */
    public boolean setCancelled(){
        State nowConcreteState = state.getState();
        if(nowConcreteState == State.WAITING || nowConcreteState == State.BLOCKED || nowConcreteState == State.ALLOCATED){
            state = new StateCancelled();
            return true;
        }
        return false;
    }

    /**
     * 将状态设置为ENDED
     * @return 操作是否成功
     */
    public boolean setEnded(){
        State nowConcreteState = state.getState();
        if(nowConcreteState == State.RUNNING){
            state = new StateEnded();
            return true;
        }
        return false;
    }

    /**
     * 将状态设置为BLOCKED
     * @return 操作是否成功
     */
    public boolean setBlocked(){
        State nowConcreteState = state.getState();
        if(nowConcreteState == State.RUNNING){
            state = new StateBlocked();
            return true;
        }
        return false;
    }

    /**
     * 获取当前的状态
     * @return 当前状态
     */
    public State getState(){
        return state.getState();
    }
}
