package PlanningEntry;

import HelperClasses.TimePair;

import java.util.ArrayList;
import java.util.List;

/**
 * 表示单个时间对的类
 * 可变类
 */
public class SingleTimePair implements TimeNumber {
    private List<TimePair> time = new ArrayList<>();

    //AF：时间为time的单个时间对
    //RI：true
    //Safety from rep exposure：所有成员域都用private修饰

    @Override
    public void presetTime(List<TimePair> inputTime){
        if(inputTime.size() != 1){
            System.exit(1);
        }
        time = new ArrayList<TimePair>();
        time.add(inputTime.get(0));
    }

    @Override
    public List<TimePair> getTime(){
        List<TimePair> listToReturn = new ArrayList<TimePair>();
        listToReturn.add(time.get(0));
        return listToReturn;
    }
}
