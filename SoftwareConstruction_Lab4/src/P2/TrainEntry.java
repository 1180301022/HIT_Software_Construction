package P2;

import EntryState.StateEnvironment;
import HelperClasses.Location;
import EntryState.State;
import HelperClasses.TimePair;
import P1.FlightEntry;
import PlanningEntry.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

interface TrainInterface extends LocationNumber, SourceType<Train>, TimeNumber {}

/**
 * 针对问题2的列车计划项
 * 可变类
 */
public class TrainEntry extends CommonPlanningEntry<Train> implements TrainInterface {

    //AF:名字为name，位置为location，状态为status，资源为source，时间为time的列车计划项
    //RI:车次的位置不能重复
    //Safety from rep exposure:成员域继承自父类且用protected修饰，返回时采用防御性复制

    private void checkRep(){
        Set<Location> locationSet = new HashSet<>();
        for(Location tempLocation : location.getLocation()){
            locationSet.add(tempLocation);
        }
        assert locationSet.size()==location.getLocation().size();
    }

    /**
     * 创建一个新的列车计划项
     * @param entryName 计划项名
     */
    public TrainEntry(String entryName){
        makePlanningEntry(entryName);
        location = new MultipleLocation();
        source = new MultipleSource<Train>();
        time = new MultipleTimePair();
    }

    /**
     * 将状态改为阻塞
     * @return 是否操作成功
     */
    public boolean block(){
        if(!state.setBlocked()){
            checkRep();
            return false;
        }
        checkRep();
        return true;
    }

    /**
     * 检查输入计划项和当前计划项的名字、时间是否兼容
     * 兼容：计划项名不同；计划项名相同，日期不同，具体时刻相同
     * @param inputEntry 输入计划项
     * @return 是否兼容
     */
    public boolean isNameAndTimeCompatible(TrainEntry inputEntry){
        //如果计划项名字不同则一定可以兼容
        if(!inputEntry.getPlanningEntryName().equals(name)){
            checkRep();
            return true;
        }
        //如果名字相同，且出发日期或者到达日期相同，则不可兼容
        int thisStartYear = TimePair.getYear(time.getTime().get(0).getStartTime().toCharArray());
        int thisEndYear = TimePair.getYear(time.getTime().get(0).getEndTime().toCharArray());
        int thisStartMonth = TimePair.getMonth(time.getTime().get(0).getStartTime().toCharArray());
        int thisEndMonth = TimePair.getMonth(time.getTime().get(0).getEndTime().toCharArray());
        int thisStartDay = TimePair.getDay(time.getTime().get(0).getStartTime().toCharArray());
        int thisEndDay = TimePair.getDay(time.getTime().get(0).getEndTime().toCharArray());
        int inputStartYear = TimePair.getYear(inputEntry.getTime().get(0).getStartTime().toCharArray());
        int inputEndYear = TimePair.getYear(inputEntry.getTime().get(0).getEndTime().toCharArray());
        int inputStartMonth = TimePair.getMonth(inputEntry.getTime().get(0).getStartTime().toCharArray());
        int inputEndMonth = TimePair.getMonth(inputEntry.getTime().get(0).getEndTime().toCharArray());
        int inputStartDay = TimePair.getDay(inputEntry.getTime().get(0).getStartTime().toCharArray());
        int inputEndDay = TimePair.getDay(inputEntry.getTime().get(0).getEndTime().toCharArray());
        if(((thisStartYear==inputStartYear)&&(thisStartDay==inputStartDay)&&(thisStartMonth==inputStartMonth)) ||
                ((thisEndMonth==inputEndMonth)&&(thisEndYear==inputEndYear)&&(thisEndDay==inputEndDay))){
            checkRep();
            return false;
        }
        //名字相同且出发、到达日期均不在同一天，起始时刻相同，可兼容；否则不可兼容
        int thisStartHour = TimePair.getHour(time.getTime().get(0).getStartTime().toCharArray());
        int thisEndHour = TimePair.getHour(time.getTime().get(0).getEndTime().toCharArray());
        int thisStartMinute = TimePair.getMinute(time.getTime().get(0).getStartTime().toCharArray());
        int thisEndMinute = TimePair.getMinute(time.getTime().get(0).getEndTime().toCharArray());
        int inputStartHour = TimePair.getHour(inputEntry.getTime().get(0).getStartTime().toCharArray());
        int inputEndHour = TimePair.getHour(inputEntry.getTime().get(0).getEndTime().toCharArray());
        int inputStartMinute = TimePair.getMinute(inputEntry.getTime().get(0).getStartTime().toCharArray());
        int inputEndMinute = TimePair.getMinute(inputEntry.getTime().get(0).getEndTime().toCharArray());
        if((thisStartHour==inputStartHour) && (thisEndHour==inputEndHour) && (thisStartMinute==inputStartMinute) &&
                (thisEndMinute==inputEndMinute)){
            checkRep();
            return true;
        }
        else {
            checkRep();
            return false;
        }
    }

    @Override
    public String getEntryType(){
        checkRep();
        return "train";
    }

    @Override
    public void presetLocation(List<Location> locations) {
        checkRep();
        location.presetLocation(locations);
    }

    @Override
    public void setSource(List<Train> inputSource) {
        source.setSource(inputSource);
        if(inputSource.size()==0){
            state = new StateEnvironment();
            checkRep();
            return;
        }
        checkRep();
        state.setAllocated();
    }

    @Override
    public void presetTime(List<TimePair> inputTime) {
        checkRep();
        time.presetTime(inputTime);
    }

    @Override
    public String getLocationToString() {
        int size = location.getLocation().size();
        String from = location.getLocation().get(0).getLocationName();
        String to = location.getLocation().get(size-1).getLocationName();
        checkRep();
        return from + "-" + to;
    }

    @Override
    public String toString(){
        checkRep();
        return ("车次：" + name + "\t状态："+ getStatusToCharacters() +"\t地点：" + getLocationToString());
    }
}
