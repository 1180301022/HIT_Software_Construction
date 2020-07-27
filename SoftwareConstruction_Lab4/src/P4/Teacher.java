package P4;

/**
 * 问题4中R的设计，代表资源“教师”
 * 不可变类
 */
public class Teacher {
    private String name;
    private String id;
    private String gender;
    private String title;

    //AF(name, id, gender, title) = 姓名name，身份证号为id，
    //                                      性别为gender，职称为title的教师
    //RI：gender=="male" || gender=="female"
    //Safety from rep exposure：所有成员域都用private修饰，因为成员域是不可变类所以可以
    //                          在方法中直接返回

    private void checkRep(){
        assert (gender=="male" || gender=="female");
    }

    public Teacher(String name, String id, String gender, String title){
        this.name = name;
        this.id = id;
        this.gender = gender;
        this.title = title;
        checkRep();
    }

    public String getName() {
        checkRep();
        return name;
    }

    public String getId() {
        checkRep();
        return id;
    }

    public String getGender() {
        checkRep();
        return gender;
    }

    public String getTitle() {
        checkRep();
        return title;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Teacher){
            Teacher temp = (Teacher) obj;
            if(id.equals(temp.getId())){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString(){
        return "姓名："+name+"\t身份证号："+id+"\n性别："+gender+"\n职称："+title;
    }
}
