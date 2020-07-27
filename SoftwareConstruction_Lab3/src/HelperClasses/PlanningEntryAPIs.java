package HelperClasses;

import PlanningEntry.PlanningEntry;

import java.util.List;

/**
 * 表示可复用API通用操作的接口
 * @param <R> 具体的资源类型
 */
public interface PlanningEntryAPIs<R> {
    /**
     * 比较是否存在位置占用
     * 由于仅在教室分配问题中存在该问题，所以比较是否有重复教室被使用即可
     * Location的equals方法已经重写，可用于比较
     * @param entries 需要检查的entry集
     * @return 是否存在位置占用冲突
     */
    public boolean checkLocationConflict(List<? extends PlanningEntry> entries);

    /**
     * 检查是否存在资源分配冲突，在飞机、火车、课程问题中均可能存在该冲突
     * 飞机和课程问题中资源仅有一项，火车问题资源有多项
     * 所有资源种类的hashCode,equals方法已重写，可用于比较
     * @param entries 需要检查的entry集
     * @return 是否存在资源分配冲突
     */
    public boolean checkResourceExclusiveConflict(List<? extends PlanningEntry> entries);

    /**
     * 提取面向特定资源的前序计划项
     * @param r 寻找的目标资源
     * @param e 寻找该entry的前序计划项
     * @param entries 输入的entry集合
     * @return 返回前序计划项。若不存在这样的计划项，则返回 null。如果存在多个，返回其中任意一个即可。
     */
    public PlanningEntry findPreEntryPerResource(R r, PlanningEntry e, List<? extends PlanningEntry> entries);
}
