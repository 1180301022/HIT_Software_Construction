package PlanningEntry;

import HelperClasses.Location;

import java.util.List;

/**
 * 表示计划项中位置数目的接口
 * 实现接口的类分为单位置、双位置、多位置
 */
public interface LocationNumber {
    /**
     * 计划项开始前提前设置位置
     * @param locations 需要设置的位置
     */
    public void presetLocation(List<Location> locations);

    /**
     * 获取计划项已设置的位置
     * @return 已经设置的位置
     */
    public List<Location> getLocation();

}
