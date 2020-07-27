package PlanningEntry;

import EntryState.State;
import EntryState.StateEnvironment;
import HelperClasses.*;

import java.util.List;

/**
 * 实现一般PlanningEntry操作的类
 * 可变类
 * @param <R> 具体资源类型
 */
public abstract class CommonPlanningEntry<R> implements PlanningEntry<R> {
    protected String name;
    protected LocationNumber location;
    protected StateEnvironment state;
    protected SourceType<R> source;
    protected TimeNumber time;

    //AF:名字为name，位置为location，状态为status，资源为source，时间为time的计划项
    //RI:true
    //Safety from rep exposure:成员域用protected修饰，返回时采用防御性复制

    @Override
    public void makePlanningEntry(String inputName) {
        name = inputName;
        state = new StateEnvironment();
    }

    @Override
    public String getPlanningEntryName() {
        return name;
    }

    @Override
    public State getState() {
        return state.getState();
    }

    @Override
    public String getStatusToCharacters(){
        switch (state.getState()){
            case ALLOCATED: return "已分配";
            case ENDED: return "已抵达";
            case RUNNING: return "运行中";
            case CANCELLED: return "已取消";
            case BLOCKED: return "中途停止";
            default: return "未分配";
        }
    }

    @Override
    public List<Location> getLocation(){
        return location.getLocation();
    }

    @Override
    public List<R> getSource(){
        return source.getSource();
    }

    @Override
    public List<TimePair> getTime(){
        return time.getTime();
    }

    @Override
    public boolean cancel() {
        return state.setCancelled();
    }

    @Override
    public boolean run() {
        return state.setRunning();
    }

    @Override
    public boolean end() {
        return state.setEnded();
    }

    @Override
    public int hashCode(){
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj){
        PlanningEntry temp = (PlanningEntry) obj;
        if(temp.getPlanningEntryName().equals(name)){
            return true;
        }
        return false;
    }

    @Override
    public int compareTo(PlanningEntry inputEntry){
        String thisStartTime = time.getTime().get(0).getStartTime();
        String inputStartTime = ((TimePair)inputEntry.getTime().get(0)).getStartTime();
        if(TimePair.compareTime(thisStartTime, inputStartTime) == 1){
            return -1;
        }
        else if(TimePair.compareTime(thisStartTime, inputStartTime) == 2){
            return 1;
        }
        return 0;
    }
}
