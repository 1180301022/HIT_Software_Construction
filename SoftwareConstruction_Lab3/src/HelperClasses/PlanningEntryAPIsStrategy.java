package HelperClasses;

import PlanningEntry.PlanningEntry;

import java.util.List;

/**
 * 采用策略模式创建API实例
 * @param <R> 具体的资源种类
 *  不可变类
 */
public class PlanningEntryAPIsStrategy<R> {
    private PlanningEntryAPIs api;

    //AF：方法集为api的可复用API
    //RI：true
    //Safety from rep exposure：成员域使用private修饰

    /**
     * 根据输入值，创建具体的API实例
     * @param flag 输入1创建检测单资源、单位置的计划项；输入2创建检测多位置、多资源的计划项
     */
    public PlanningEntryAPIsStrategy(int flag){
        if(flag == 1){
            api = new PlanningEntryAPIsImpl1();
        }
        else if(flag == 2){
            api = new PlanningEntryAPIsImpl2();
        }
        else {
            System.out.println("输入错误");
            System.exit(-1);
        }
    }

    /**
     * 检测输入计划项是否存在位置冲突
     * @param entries 需要检测的计划项
     * @return 是否存在位置冲突
     */
    public boolean checkLocation(List<? extends PlanningEntry> entries){
        return api.checkLocationConflict(entries);
    }

    /**
     * 检测输入计划项是否存在资源分配冲突
     * @param entries 需要检测的计划项
     * @return 是否存在位置冲突
     */
    public boolean checkResource(List<? extends PlanningEntry> entries){
        return api.checkResourceExclusiveConflict(entries);
    }

    /**
     *
     * @param r 指定资源
     * @param e 指定计划项
     * @param entries 需要检测的计划项
     * @return 使用指定资源的，输入计划项的前序计划项
     */
    public PlanningEntry findPreSameResourceEntry(R r, PlanningEntry e, List<? extends PlanningEntry> entries){
        return api.findPreEntryPerResource(r, e, entries);
    }
}
