package PlanningEntry;

import HelperClasses.Location;

import java.util.*;

/**
 * 表示多个位置
 * 提供方法：计划项开始前设置位置，获取已设置位置序列
 * 可变类
 */
public class MultipleLocation implements LocationNumber{
    private List<Location> locations = new ArrayList<>();

    //AF：位置为locations的位置序列
    //RI：true
    //Safety from rep exposure：成员域用private修饰，返回时采用防御性复制

    @Override
    public void presetLocation(List<Location> inputLocations){
        locations = new ArrayList<Location>();
        for(Location tempLocation : inputLocations){
            locations.add(tempLocation);
        }
    }

    @Override
    public List<Location> getLocation(){
        List<Location> locationToReturn = new ArrayList<Location>();
        for(Location temp : locations){
            locationToReturn.add(temp);
        }
        return locationToReturn;
    }
}
