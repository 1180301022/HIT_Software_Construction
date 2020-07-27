package PlanningEntry;

import HelperClasses.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * 单个位置的情况，包括创建位置的操作
 * 可变类
 */
public class SingleLocation implements LocationNumber {
    private List<Location> location = new ArrayList<>();

    //AF：位置为location的单个位置
    //RI：true
    //Safety from rep exposure：成员域用private修饰，返回时采用防御性复制
    @Override
    public void presetLocation(List<Location> inputLocation){
        if(inputLocation.size() != 1){
            return;
        }
        location = new ArrayList<Location>();
        location.add(new Location(inputLocation.get(0).getLocationName()));
    }

    @Override
    public List<Location> getLocation(){
        List<Location> locationToReturn = new ArrayList<Location>();
        locationToReturn.add(location.get(0));
        return locationToReturn;
    }

}
