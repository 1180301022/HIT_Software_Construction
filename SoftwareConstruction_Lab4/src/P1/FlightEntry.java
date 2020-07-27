package P1;

import EntryState.StateEnvironment;
import Exceptions.ElementRelationException;
import Exceptions.FileFormatException;
import Exceptions.SameTagException;
import HelperClasses.Location;
import HelperClasses.TimePair;
import PlanningEntry.*;

import java.io.Reader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

interface FlightInterface extends LocationNumber, SourceType<Plane>, TimeNumber {}

/**
 * 针对问题1的航班计划项
 * 可变类
 */
public class FlightEntry extends CommonPlanningEntry<Plane> implements FlightInterface{

    //AF:名字为name，位置为location，状态为status，资源为source，时间为time的航班计划项
    //RI:出发地不能和目的地相等
    //Safety from rep exposure:成员域用private修饰，返回时采用防御性复制

    private void checkRep(){
        assert !(location.getLocation().get(0).equals(location.getLocation().get(1)));
    }

    /**
     * 创建一个新的航班计划项
     * @param inputName 计划项名
     */
    public FlightEntry(String inputName){
        makePlanningEntry(inputName);
        location = new DoubleLocation();
        time = new SingleTimePair();
        source = new SingleSource<Plane>();
    }

    /**
     * 检查输入计划项和当前计划项的名字、时间是否兼容
     * 兼容：计划项名不同；计划项名相同，日期不同，具体时刻相同
     * @param inputEntry 输入计划项
     * @return 是否兼容
     */
    public void checkNameAndTimeCompatible(FlightEntry inputEntry) throws ElementRelationException, SameTagException {
        //首先比较计划项的名字
        //名字不同，一定兼容
        if(!isNameEqual(inputEntry)){
            return;
        }

        //如果名字相同，出发/到达地点有任一个不同，则不可兼容（属于3.3）
        for(int i=0; i<2 ; i++){
            if(!location.getLocation().get(i).equals(inputEntry.getLocation().get(i))){
                throw new ElementRelationException("计划项"+inputEntry.getPlanningEntryName()+"与已读取的同名航班地点不同");
            }
        }

        //如果名字相同，且出发日期或者到达日期相同，则不可兼容（属于2）
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
            throw new SameTagException("计划项"+inputEntry.getPlanningEntryName()+"与已读取的同名航班日期相同");
        }

        //名字相同且出发、到达日期均不在同一天，起止时刻相同，可兼容；否则不可兼容（属于3.3）
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
            return;
        }
        else {
            throw new ElementRelationException("计划项"+inputEntry.getPlanningEntryName()+"与已读取的同名航班日期不同，但起止时刻不同");
        }
    }

    /**
     * 在文本格式要求的前提下，比较两计划项名是否相等
     * @param inputEntry 待比较的计划项
     * @return 计划项名是否相等
     */
    public boolean isNameEqual(FlightEntry inputEntry){
        char x0 = name.toCharArray()[0];
        char x1 = name.toCharArray()[1];
        char y0 = inputEntry.getPlanningEntryName().toCharArray()[0];
        char y1 = inputEntry.getPlanningEntryName().toCharArray()[1];
        //前两位不同则计划项名一定不同
        if((x0!=y0) || (x1!=y1)){
            return false;
        }

        //此时前两位相同，比较长度是否相同
        if(inputEntry.getPlanningEntryName().length() == name.length()){
            //长度相同，直接比较是否相等
            if(inputEntry.getPlanningEntryName().equals(name)){
                return true;
            }
        }

        //此时长度不同，检测是否满足格式
        else {
            Pattern pattern = Pattern.compile("([A-Z]{2})(0{1,3})(\\d)");
            Matcher matcher1 = pattern.matcher(inputEntry.getPlanningEntryName());
            Matcher matcher2 = pattern.matcher(name);
            //如果都满足格式，则看最后一位数字是否相等
            if(matcher1.matches() && matcher2.matches()){
                //最后一位数字相等，计划项名相同
                if(matcher1.group(3).equals(matcher2.group(3))) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String getEntryType(){
        return "flight";
    }

    @Override
    public void presetLocation(List<Location> locations){
        assert locations.size()==2;
        location.presetLocation(locations);
        checkRep();
    }

    @Override
    public void setSource(List<Plane> inputSource){
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
    public void presetTime(List<TimePair> inputTime){
        assert inputTime.size()==1;
        time.presetTime(inputTime);
    }

    @Override
    public int hashCode(){
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj){
        if(obj instanceof FlightEntry){
            FlightEntry temp = (FlightEntry) obj;
            //如果航班名相同，考察时间
            if(temp.getPlanningEntryName().equals(name)){
                //如果时间相同，则是同一航班
                if(temp.getTime().get(0).compareTimePair(time.getTime().get(0)) == 3){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String getLocationToString() {
        String from = location.getLocation().get(0).getLocationName();
        String to = location.getLocation().get(1).getLocationName();
        return from + "-" + to;
    }

    @Override
    public String toString(){
        return ("航班名：" + name + "\t状态："+ getStatusToCharacters() + "\t时间：" +
                    time.getTime().get(0).toString() + "\t地点：" + getLocationToString());
    }

}
