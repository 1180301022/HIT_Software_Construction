package PlanningEntry;

import HelperClasses.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * 含有两个位置的位置类
 * 提供方法：设置位置，获取位置
 * 可变类
 */
public class DoubleLocation implements LocationNumber{
    private List<Location> locations = new ArrayList<>();

    //AF：位置为locations的双位置序列
    //RI：true
    //Safety from rep exposure：成员域用private修饰，返回时采用防御性复制

    @Override
    public void presetLocation(List<Location> locations){
        if(locations.size() != 2){
            System.exit(1);
        }
        this.locations = new ArrayList<Location>();
        this.locations.add(locations.get(0));
        this.locations.add(locations.get(1));
    }

    @Override
    public List<Location> getLocation(){
        List<Location> locationToReturn = new ArrayList<Location>();
        locationToReturn.add(locations.get(0));
        locationToReturn.add(locations.get(1));
        return locationToReturn;
    }

}
