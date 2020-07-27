package P1;

/**
 * 问题1中R的设计，代表资源“飞机”
 * 不可变类
 */
public class Plane {
    private String code;
    private String model;
    private int seatNumber;
    private double year;

    //AF(code, model, seatNumber, year) = 编号code，型号为model，
    //                                      座位数为seatNumber，机龄为double的飞机
    //RI：(year-0>=1e-5)&&(seatNumber>0)
    //Safety from rep exposure：所有成员域都用private修饰，因为成员域是不可变类所以可以
    //                          在方法中直接返回

    private void checkRep(){
        assert (year-0>=1e-5)&&(seatNumber>0);
    }

    public Plane(String code, String model, int seatNumber, double year){
        this.code = code;
        this.model = model;
        this.seatNumber = seatNumber;
        this.year = year;
    }

    public String getCode(){
        checkRep();
        return code;
    }

    public String getModel() {
        checkRep();
        return model;
    }

    public int getSeatNumber() {
        checkRep();
        return seatNumber;
    }

    public double getYear() {
        checkRep();
        return year;
    }

    @Override
    public boolean equals(Object obj){
        if(obj instanceof Plane){
            Plane temp = (Plane) obj;
            if(code.equals(temp.getCode())){
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode(){
        return code.hashCode();
    }

    @Override
    public String toString(){
        return "飞机编号:" + code + "\t飞机型号:" + model + "\t座位数:" + seatNumber + "\t机龄:" + year;
    }
}
