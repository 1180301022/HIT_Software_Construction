package HelperClasses;

import EntryState.State;
import PlanningEntry.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
/**
 * PlanningEntry的可复用API
 * 提供方法：（均为针对多个资源）查询位置占用冲突、查询资源分配冲突、提取面向特定资源的前序计划项
 *
 * @param <R> 具体资源种类
 *            不可变类
 */
public class PlanningEntryAPIsImpl2<R> implements PlanningEntryAPIs<R> {
    @Override
    public boolean checkLocationConflict(List<? extends PlanningEntry> entries) {
        //火车站不存在位置冲突
        return false;
    }

    @Override
    public boolean checkResourceExclusiveConflict(List<? extends PlanningEntry> entries) {
        //针对多个资源，如果两个List中有任何一个资源相等，则存在冲突
        for(int i=0 ; i<entries.size() ; i++){
            for(int j=i+1 ; j<entries.size() ; j++){
                //如果任何一个计划项未分配资源，或者计划项已经取消/结束，则一定不存在冲突，进入下轮循环
                if(entries.get(i).getSource().size()==0 || entries.get(j).getSource().size()==0||
                        (entries.get(i).getState() == State.CANCELLED) || (entries.get(j).getState() == State.CANCELLED) ||
                        (entries.get(i).getState() == State.ENDED) || (entries.get(j).getState() == State.ENDED)){
                    continue;
                }
                //均分配过资源，比较第i和第j两个List：创建一个集合，将两个List的元素都加入集合中，并考察集合元素个数
                Set<R> temp = new HashSet<R>();
                for(int k=0 ; k<entries.get(i).getSource().size() ; k++){
                    R source1 = (R) entries.get(i).getSource().get(k);
                    temp.add(source1);
                }
                for(int k=0 ; k<entries.get(j).getSource().size() ; k++){
                    R source2 = (R) entries.get(j).getSource().get(k);
                    temp.add(source2);
                }
                //如果集合元素个数小于两List元素个数和，则存在相同资源被分配两次的情况
                if(temp.size() != entries.get(i).getSource().size()+entries.get(j).getSource().size()){
                    //检查是否存在时间上的冲突
                    List<TimePair> times1 = entries.get(i).getTime();
                    List<TimePair> times2 = entries.get(j).getTime();
                    int size1 = times1.size();
                    int size2 = times2.size();
                    String time1Start = times1.get(0).getStartTime();
                    String time1End = times1.get(size1-1).getEndTime();
                    String time2Start = times2.get(0).getStartTime();
                    String time2End = times2.get(size2-1).getEndTime();
                    TimePair timePair1 = new TimePair(time1Start, time1End);
                    TimePair timePair2 = new TimePair(time2Start, time2End);
                    if(timePair1.compareTimePair(timePair2) == 3){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public PlanningEntry findPreEntryPerResource(R r, PlanningEntry e, List<? extends PlanningEntry> entries) {
        //多个时间对，多个资源
        PlanningEntry entryToReturn = null;
        String lastEndTime = "1000-01-01 10:10";
        Iterator<? extends PlanningEntry> iterator = entries.iterator();
        while (iterator.hasNext()) {
            PlanningEntry<R> temp = iterator.next();
            //如果考察到自身则直接进行下一轮循环
            if (temp.equals(e)) {
                continue;
            }
            //查看资源是否相同
            boolean flag = false;
            for (R tempR : temp.getSource()) {
                if (tempR.equals(r)) {
                    flag = true;
                }
            }
            //资源相同，查看时间是否符合
            if (flag) {
                int tempTimeSize = temp.getTime().size();
                String tempEndTime = ((TimePair) temp.getTime().get(tempTimeSize - 1)).getEndTime();
                String thisStartTime = ((TimePair) e.getTime().get(0)).getStartTime();
                if ((TimePair.compareTime(lastEndTime, tempEndTime) == 1) &&
                        (TimePair.compareTime(tempEndTime, thisStartTime) == 1)) {
                    lastEndTime = tempEndTime;
                    entryToReturn = temp;
                }
            }
        }
        return entryToReturn;
    }
}
