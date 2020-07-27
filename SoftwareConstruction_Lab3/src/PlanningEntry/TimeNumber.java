package PlanningEntry;

import HelperClasses.TimePair;

import java.util.List;

public interface TimeNumber {
    /**
     * 计划项开始之前提前设定时间
     * @param inputTime 设定的一系列时间
     */
    public void presetTime(List<TimePair> inputTime);

    /**
     * 获取计划项已设置的时间
     * @return 已经设置的时间
     */
    public List<TimePair> getTime();
}
