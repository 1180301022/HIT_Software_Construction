package P2;

/**
 * 问题2中R的设计，代表资源“车厢”
 * 不可变类
 */
public class Train {
    private int number;
    private String type;
    private int capacity;
    private double year;

    //AF(number, type, capacity, year) = 编号number，类型为type，
    //                                      定员数capacity，使用时间为double的车厢
    //RI：(year-0>=1e-5)&&(capacity>0)
    //Safety from rep exposure：所有成员域都用private修饰，因为成员域是不可变类所以可以
    //                          在方法中直接返回

    private void checkRep(){
        assert (year-0>=1e-5)&&(capacity>0);
    }

    public Train(int number, String type, int capacity, double year){
        this.number = number;
        this.type = type;
        this.capacity = capacity;
        this.year= year;
    }

    public int getNumber() {
        checkRep();
        return number;
    }

    public String getType() {
        checkRep();
        return type;
    }

    public int getCapacity() {
        checkRep();
        return capacity;
    }

    public double getYear() {
        checkRep();
        return year;
    }

    @Override
    public int hashCode() {
        return number;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Train){
            Train temp = (Train) obj;
            if(number == temp.getNumber()){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString(){
        return ("编号："+number+"\t类型："+type+"\t定员："+capacity+"\t年份："+year);
    }
}
