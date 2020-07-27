package HelperClasses;

import EntryState.State;
import PlanningEntry.PlanningEntry;

import java.util.Iterator;
import java.util.List;

/**
 * PlanningEntry的可复用API
 * 提供方法：（均为针对单个资源）查询位置占用冲突、查询资源分配冲突、提取面向特定资源的前序计划项
 *
 * @param <R> 具体资源种类
 *            不可变类
 */

public class PlanningEntryAPIsImpl1<R> implements PlanningEntryAPIs<R> {
    @Override
    public boolean checkLocationConflict(List<? extends PlanningEntry> entries) {
        for (int i = 0; i < entries.size(); i++) {
            for (int j = i + 1; j < entries.size(); j++) {
                if (entries.get(i).getLocation().get(0).equals(entries.get(j).getLocation().get(0))) {
                    //如果计划项已经取消/完成，则进入下一轮循环
                    if ((entries.get(i).getState() == State.CANCELLED) || (entries.get(j).getState() == State.CANCELLED) ||
                            (entries.get(i).getState() == State.ENDED) || (entries.get(j).getState() == State.ENDED)) {
                        continue;
                    }
                    //牵涉到相同位置，检查时间是否有冲突
                    TimePair timePairI = (TimePair) entries.get(i).getTime().get(0);
                    TimePair timePairJ = (TimePair) entries.get(j).getTime().get(0);
                    if (timePairI.compareTimePair(timePairJ) == 3) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean checkResourceExclusiveConflict(List<? extends PlanningEntry> entries) {
        //针对单资源
        for (int i = 0; i < entries.size(); i++) {
            for (int j = i + 1; j < entries.size(); j++) {
                //如果存在没分配资源的计划项，或者计划项已经取消/完成，则进入下一轮循环
                if ((entries.get(i).getSource().size() == 0) || (entries.get(j).getSource().size() == 0) ||
                        (entries.get(i).getState() == State.CANCELLED) || (entries.get(j).getState() == State.CANCELLED) ||
                        (entries.get(i).getState() == State.ENDED) || (entries.get(j).getState() == State.ENDED)) {
                    continue;
                }
                R source1 = (R) entries.get(i).getSource().get(0);
                R source2 = (R) entries.get(j).getSource().get(0);
                if (source1.equals(source2)) {
                    //牵涉到相同资源，检查是否时间冲突
                    TimePair timePairI = (TimePair) entries.get(i).getTime().get(0);
                    TimePair timePairJ = (TimePair) entries.get(j).getTime().get(0);
                    if (timePairI.compareTimePair(timePairJ) == 3) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public PlanningEntry findPreEntryPerResource(R r, PlanningEntry e, List<? extends PlanningEntry> entries) {
        //单个时间对，单个资源
        PlanningEntry entryToReturn = null;
        String lastEndTime = "1000-01-01 10:10";
        Iterator<? extends PlanningEntry> iterator = entries.iterator();
        if (entries.get(0).getTime().size() == 1) {
            while (iterator.hasNext()) {
                PlanningEntry temp = iterator.next();
                //如果考察到自身则直接进行下一轮循环
                if (temp.equals(e)) {
                    continue;
                }
                //如果不是自身，查看资源是否符合
                if (temp.getSource().get(0).equals(r)) {
                    //资源符合，考察时间
                    String tempEndTime = ((TimePair) temp.getTime().get(0)).getEndTime();
                    String thisStartTime = ((TimePair) e.getTime().get(0)).getStartTime();
                    if ((TimePair.compareTime(lastEndTime, tempEndTime) == 1) &&
                            (TimePair.compareTime(tempEndTime, thisStartTime) == 1)) {
                        lastEndTime = tempEndTime;
                        entryToReturn = temp;
                    }
                }
            }
        }
        return entryToReturn;
    }
}
