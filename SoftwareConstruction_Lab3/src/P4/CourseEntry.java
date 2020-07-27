package P4;

import HelperClasses.Location;
import EntryState.State;
import HelperClasses.TimePair;
import PlanningEntry.*;

import java.util.List;

interface CourseInterface extends LocationNumber, SourceType<Teacher>, TimeNumber{}

/**
 * 针对问题4的课程计划项
 * 可变类
 */
public class CourseEntry extends CommonPlanningEntry<Teacher> implements CourseInterface{

    //AF:名字为name，位置为location，状态为status，资源为source，时间为time的课程计划项
    //RI:true
    //Safety from rep exposure:成员域用private修饰，返回时采用防御性复制

    /**
     * 创建新的课程计划项
     * @param courseName 课程名
     */
    public CourseEntry(String courseName){
        makePlanningEntry(courseName);
        location = new SingleLocation();
        source = new MultipleSource<Teacher>();
        time = new SingleTimePair();
    }

    /**
     * 在项目启动前重新设置位置
     * @param resetLocation 需要重新设置的位置
     */
    public void resetLocation(List<Location> resetLocation) {
        if(state.getState()==State.ALLOCATED || state.getState()==State.WAITING){
            location.presetLocation(resetLocation);
        }
    }

    @Override
    public String getEntryType(){
        return "course";
    }

    @Override
    public void presetLocation(List<Location> locations) {
        location.presetLocation(locations);
    }

    @Override
    public void setSource(List<Teacher> inputSource) {
        source.setSource(inputSource);
        state.setAllocated();
    }

    @Override
    public void presetTime(List<TimePair> inputTime) {
        time.presetTime(inputTime);
    }

    @Override
    public String getLocationToString() {
        return location.getLocation().get(0).getLocationName();
    }

    @Override
    public String toString(){
        return ("课程名：" + name + "\t时间：" + getTime().get(0).toString() + "\t状态：" + getStatusToCharacters());
    }
}
