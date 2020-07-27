package PlanningEntry;

import HelperClasses.Location;
import EntryState.State;
import HelperClasses.TimePair;

import java.util.List;

public interface PlanningEntry<R> extends Comparable<PlanningEntry>{
    /**
     * 对新创建的计划项进行共享的初始化操作
     */
    public void makePlanningEntry(String name);

    /**
     * 获取计划项种类
     * @return 计划项的具体种类
     */
    public String getEntryType();

    /**
     * 获取计划项名字
     * @return 计划项名字
     */
    public String getPlanningEntryName();

    /**
     * 获取计划项状态
     * @return 计划项状态
     */
    public State getState();

    /**
     * 获取当前状态的文字信息
     * @return 当前状态
     */
    public String getStatusToCharacters();

    /**
     * 将状态改为取消
     */
    public boolean cancel();

    /**
     * 将状态改为运行
     */
    public boolean run();

    /**
     * 将状态改为结束
     */
    public boolean end();

    /**
     * 获取计划项的位置信息
     * @return 计划项中的一系列位置
     */
    public List<Location> getLocation();

    /**
     * 获取计划项位置的文字信息
     * @return 已设置的位置
     */
    public String getLocationToString();

    /**
     * 获取计划项分配的资源信息
     * @return 计划项分配的一系列资源
     */
    public List<R> getSource();

    /**
     * 获取计划项的时间信息
     * @return 计划项进行时的一系列时间
     */
    public List<TimePair> getTime();
}
