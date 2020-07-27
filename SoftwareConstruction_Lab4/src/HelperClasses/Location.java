package HelperClasses;

/**
 * 表示单个位置的类
 * 提供创建位置、获取位置名的方法
 * 不可变类
 */
public class Location {
    private String locationName;
    //AF：名为locationName的地址
    //RI：true
    //Safety from rep exposure：所有成员域都用private修饰

    public Location(String locationName){
        this.locationName = locationName;
    }

    /**
     * 获取位置名
     * @return 位置名
     */
    public String getLocationName() {
        return locationName;
    }

    @Override
    public boolean equals(Object input){
        if(input instanceof Location){
            Location temp = (Location) input;
            if(temp.getLocationName().equals(locationName)){
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode(){
        return locationName.hashCode();
    }
}