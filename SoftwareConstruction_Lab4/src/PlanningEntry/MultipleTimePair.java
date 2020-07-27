package PlanningEntry;

import HelperClasses.TimePair;
import java.util.ArrayList;
import java.util.List;

/**
 * 表示具有多个时间对的时间序列
 * 提供方法：设置时间，获得已设置时间
 * 可变类
 */
public class MultipleTimePair implements TimeNumber {
    private List<TimePair> time = new ArrayList<>();

    //AF：时间为time的时间对序列
    //RI：各个时间序列无交叉，且满足从早到晚的顺序
    //Safety from rep exposure：成员域用private修饰，返回时采用防御性复制

    private void checkRep(){
        for(int i=0 ; i<time.size()-2 ; i++){
            if(time.get(i).compareTimePair(time.get(i+1)) != 2){
                assert false;
            }
        }
        assert true;
    }

    @Override
    public void presetTime(List<TimePair> inputTime){
        time = new ArrayList<TimePair>();
        for(TimePair temp : inputTime){
            time.add(temp);
        }
        checkRep();
    }

    @Override
    public List<TimePair> getTime(){
        List<TimePair> listToReturn = new ArrayList<TimePair>();
        for(TimePair temp : time){
            listToReturn.add(temp);
        }
        checkRep();
        return listToReturn;
    }
}
