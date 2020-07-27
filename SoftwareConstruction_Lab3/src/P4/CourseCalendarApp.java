package P4;

import EntryState.State;
import HelperClasses.EntryFactory;
import HelperClasses.Location;
import HelperClasses.PlanningEntryAPIsStrategy;
import HelperClasses.TimePair;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 问题4的主程序
 * 可变类
 */
public class CourseCalendarApp {
    private List<CourseEntry> entries = new ArrayList<>();
    private List<Teacher> teachers = new ArrayList<>();
    private List<Location> locations = new ArrayList<>();

    //AF：课程计划项为entries、教师资源为teachers、教室为locations的课表管理程序
    //RI：true
    //Safety from rep exposure：成员域使用private修饰

    /**
     * 客户端主程序框架
     */
    public void runApp() {
        //识别用户输入操作
        printMenu();
        int operation = 0;
        Scanner scanner = new Scanner(System.in);
        //运行具体程序
        while (true) {
            boolean isOperationSucceed = false;
            operation = scanner.nextInt();
            switch (operation) {
                case 1:
                    isOperationSucceed = addTeacher();
                    break;
                case 2:
                    isOperationSucceed = addLocation();
                    break;
                case 3:
                    isOperationSucceed = addEntry();
                    break;
                case 4:
                    isOperationSucceed = cancelEntry();
                    break;
                case 5:
                    isOperationSucceed = allocateSource();
                    break;
                case 6:
                    isOperationSucceed = runEntry();
                    break;
                case 7:
                    isOperationSucceed = resetLocation();
                    break;
                case 8:
                    isOperationSucceed = endEntry();
                    break;
                case 9:
                    isOperationSucceed = getEntryState();
                    break;
                case 10:
                    isOperationSucceed = checkResourceAndLocationConflict();
                    break;
                case 11:
                    isOperationSucceed = getEntriesUseInputResourceAndPreEntry();
                    break;
                case 12:
                    isOperationSucceed = showInfoBoard();
                    break;
                case 13:
                    isOperationSucceed = deleteLocation();
                    break;
                case 14:
                    isOperationSucceed = deleteTeacher();
                    break;
                default:
                    System.exit(0);
            }
            if (isOperationSucceed) {
                System.out.println("操作成功\n");
            } else {
                System.out.println("操作失败\n");
            }
            printMenu();
        }
    }

    /**
     * 1.为程序添加一个新的教师
     *
     * @return 是否添加成功
     */
    private boolean addTeacher() {
        System.out.println("请输入教师的姓名，身份证号，性别和职称");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.next();
        String id = scanner.next();
        String gender = scanner.next();
        String title = scanner.next();
        Teacher inputTeacher = new Teacher(name, id, gender, title);

        if (teachers.contains(inputTeacher)) {
            System.out.println("该教师已被添加");
            return false;
        }
        teachers.add(inputTeacher);
        return true;
    }

    /**
     * 2.为程序添加一个新的地点
     *
     * @return 是否添加成功
     */
    private boolean addLocation() {
        System.out.println("请输入位置");
        Location inputLocation = new Location(new Scanner(System.in).next());
        if (locations.contains(inputLocation)) {
            System.out.println("输入位置已存在");
            return false;
        }
        locations.add(inputLocation);
        return true;
    }

    /**
     * 3.添加一个新计划项
     *
     * @return 是否添加成功
     */
    private boolean addEntry() {
        System.out.println("请输入课程名");
        Scanner scanner = new Scanner(System.in);
        String entryName = scanner.next();
        CourseEntry entry = (CourseEntry) EntryFactory.manufacture("course", entryName);
        if (setEntryTime(entry) && setEntryLocation(entry)) {
            entries.add(entry);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 3.为新计划项设置时间
     *
     * @param entry 需要设置的计划项
     * @return 是否设置成功
     */
    private boolean setEntryTime(CourseEntry entry) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入时间，格式yyyy-MM-dd HH:mm（每输入一个完整时间敲一下回车）");
        String startTime = scanner.nextLine();
        String endTime = scanner.nextLine();
        //检查输入格式是否满足要求
        if (!isLegalTimeFormat(startTime) || !isLegalTimeFormat(endTime)) {
            System.out.println("输入时间不符合规则");
            return false;
        }
        TimePair inputTime = new TimePair(startTime, endTime);
        List<TimePair> timePairList = new ArrayList<>();
        timePairList.add(inputTime);
        entry.presetTime(timePairList);
        return true;
    }

    /**
     * 3.为新计划项设置位置
     *
     * @param entry 新建计划项
     * @return 是否设置成功
     */
    private boolean setEntryLocation(CourseEntry entry) {
        printLocations();
        System.out.println("请输入需要添加教室的序号");
        Scanner scanner = new Scanner(System.in);
        int input = scanner.nextInt();
        if(!isLegalLocationNumber(input)){
            System.out.println("输入数字超出范围");
            return false;
        }

        List<Location> locationList = new ArrayList<>();
        Location tempLocation = locations.get(input);
        locationList.add(tempLocation);
        entry.presetLocation(locationList);
        return true;
    }

    /**
     * 4.取消某计划项
     * @return 是否取消成功
     */
    private boolean cancelEntry(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入需要取消的计划项编号");
        printEntries();
        int opNumber = scanner.nextInt();
        if(!isLegalEntryNumber(opNumber)){
            System.out.println("输入编号超出区间");
            return false;
        }
        if(entries.get(opNumber).cancel()){
            return true;
        }
        else {
            System.out.println("条件不满足，无法取消");
            return false;
        }
    }

    /**
     * 5.为计划项分配教师
     * @return 是否分配成功
     */
    private boolean allocateSource(){
        System.out.println("请输入需要分配的计划项编号");
        printEntries();
        Scanner scanner = new Scanner(System.in);
        int opNumber = scanner.nextInt();
        if(!isLegalEntryNumber(opNumber)){
            System.out.println("输入编号超出区间");
            return false;
        }
        CourseEntry opEntry = entries.get(opNumber);
        if(opEntry.getState()!= State.WAITING){
            System.out.println("操作不符合要求");
            return false;
        }

        //首先获取分配教师数目
        System.out.println("请输入需要设置的教师数目");
        int teacherNumber = scanner.nextInt();

        List<Teacher> list = new ArrayList<>();
        System.out.println("请按顺序输入需要分配的教师编号");
        printTeachers();
        for(int i=0 ; i<teacherNumber ; i++){
            int sourceNumber = scanner.nextInt();
            if(!isLegalTeacherNumber(sourceNumber)){
                System.out.println("输入编号超出区间");
                return false;
            }
            Teacher tempTeacher = teachers.get(sourceNumber);
            list.add(tempTeacher);
        }
        opEntry.setSource(list);
        return true;
    }

    /**
     * 6.启动某个计划项
     * @return 是否启动成功
     */
    private boolean runEntry(){
        System.out.println("请输入需要启动的计划项编码");
        printEntries();
        Scanner scanner = new Scanner(System.in);
        int opEntryNumber = scanner.nextInt();
        if(!isLegalEntryNumber(opEntryNumber)){
            System.out.println("输入编号超出区间");
            return false;
        }
        CourseEntry opEntry = entries.get(opEntryNumber);
        if(opEntry.run()){
            return true;
        }
        else {
            System.out.println("条件不满足，无法启动");
            return false;
        }
    }

    /**
     * 7.重新设置未启动的计划项的地点
     * @return 是否操作成功
     */
    private boolean resetLocation(){
        printEntries();
        System.out.println("请输入需要重新设置教室的计划项编号");
        Scanner scanner = new Scanner(System.in);
        int opEntryNumber = scanner.nextInt();
        if(!isLegalEntryNumber(opEntryNumber)){
            System.out.println("输入序号超出范围");
            return false;
        }
        CourseEntry opEntry = entries.get(opEntryNumber);
        if(opEntry.getState()!=State.WAITING && opEntry.getState()!=State.ALLOCATED){
            System.out.println("操作不符合要求");
            return false;
        }

        printLocations();
        System.out.println("请输入需要重新设置的教室编号");
        int opLocationNumber = scanner.nextInt();
        if(!isLegalLocationNumber(opLocationNumber)){
            System.out.println("输入序号超出范围");
            return false;
        }
        List<Location> locationList = new ArrayList<>();
        locationList.add(locations.get(opLocationNumber));
        opEntry.resetLocation(locationList);
        return true;
    }

    /**
     * 8.结束一个计划项
     * @return 是否结束成功
     */
    private boolean endEntry(){
        System.out.println("请输入需要结束的计划项编码");
        printEntries();
        Scanner scanner = new Scanner(System.in);
        int opEntryNumber = scanner.nextInt();
        if(!isLegalEntryNumber(opEntryNumber)){
            System.out.println("输入编号超出区间");
            return false;
        }
        CourseEntry opEntry = entries.get(opEntryNumber);
        if(opEntry.end()){
            return true;
        }
        else {
            System.out.println("条件不满足，无法结束");
            return false;
        }
    }

    /**
     * 9.获取计划项状态
     * @return 是否查询成功
     */
    private boolean getEntryState(){
        printEntries();
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入需要获取项目的编号");
        int input = scanner.nextInt();
        //检查编号是否合法
        if(!isLegalEntryNumber(input)){
            System.out.println("输入编号超出范围");
            return false;
        }
        else {
            CourseEntry tempEntry = entries.get(input);
            System.out.println("该计划项的状态是：" + tempEntry.getStatusToCharacters());
            return true;
        }
    }

    /**
     * 10.检查是否存在资源或位置冲突
     * @return 操作是否成功
     */
    private boolean checkResourceAndLocationConflict(){
        PlanningEntryAPIsStrategy<Teacher> api = new PlanningEntryAPIsStrategy<>(1);
        if(api.checkResource(entries)){
            System.out.println("存在资源分配冲突");
        }
        else {
            System.out.println("不存在资源分配冲突");
        }
        if(api.checkLocation(entries)){
            System.out.println("存在位置分配冲突");
        }
        else {
            System.out.println("不存在位置分配冲突");
        }
        return true;
    }

    /**
     * 11.获取使用指定资源的所有计划项，以及指定项目的前序计划项
     * @return 是否操作成功
     */
    private boolean getEntriesUseInputResourceAndPreEntry(){
        //选择需要查询的资源
        System.out.println("请输入需要查询的教师编号");
        printTeachers();
        Scanner scanner = new Scanner(System.in);
        int opTeacherNumber = scanner.nextInt();
        if(!isLegalTeacherNumber(opTeacherNumber)){
            System.out.println("输入序号超出范围");
            return false;
        }
        //获得使用目标资源的计划项
        Teacher opTeacher = teachers.get(opTeacherNumber);
        List<CourseEntry> opEntries = new ArrayList<>();
        for(CourseEntry tempEntry : entries){
            for(Teacher tempTeacher : tempEntry.getSource()){
                if(tempTeacher.equals(opTeacher)){
                    opEntries.add(tempEntry);
                }
            }
        }
        //打印使用目标资源的计划项
        System.out.println("该教师的课程如下");
        for(int i=0 ; i<opEntries.size() ; i++){
            System.out.println(i+"."+opEntries.get(i).toString());
        }
        //获取需要查询的计划项
        System.out.println("请输入需要查询前序计划项的编号");
        int opEntryNumber = scanner.nextInt();
        if(opEntryNumber<0 || opEntryNumber>=opEntries.size()){
            System.out.println("输入序号超出范围");
            return false;
        }
        //寻找前序计划项
        CourseEntry targetEntry = opEntries.get(opEntryNumber);
        PlanningEntryAPIsStrategy<Teacher> api = new PlanningEntryAPIsStrategy<>(2);
        CourseEntry finalEntry = (CourseEntry) api.findPreSameResourceEntry(opTeacher, targetEntry, opEntries);
        if(finalEntry==null){
            System.out.println("无前序计划项");
        }
        else {
            System.out.println("前序计划项："+finalEntry.toString());
        }
        return true;
    }

    /**
     * 12.显示指定位置的信息板
     * @return 是否显示成功
     */
    private boolean showInfoBoard(){
        System.out.println("请输入需要查询的位置的编号");
        printLocations();
        Scanner scanner = new Scanner(System.in);
        int opNumber = scanner.nextInt();
        if(!isLegalLocationNumber(opNumber)){
            System.out.println("输入编号超出范围");
            return false;
        }
        else {
            new CourseBoard(locations.get(opNumber).getLocationName(), entries);
            return true;
        }
    }

    /**
     * 13.删除指定位置
     * @return 是否删除成功
     */
    private boolean deleteLocation(){
        printLocations();
        System.out.println("请输入需要删除的位置编号");
        Scanner scanner = new Scanner(System.in);
        int opNumber = scanner.nextInt();
        if(isLegalLocationNumber(opNumber)){
            locations.remove(opNumber);
            return true;
        }
        else {
            System.out.println("输入序号超出范围");
            return false;
        }
    }

    /**
     * 14.删除指定资源
     * @return 是否删除成功
     */
    private boolean deleteTeacher(){
        printTeachers();
        System.out.println("请输入需要删除的教师编号");
        Scanner scanner = new Scanner(System.in);
        int opNumber = scanner.nextInt();
        if(isLegalTeacherNumber(opNumber)){
            teachers.remove(opNumber);
            return true;
        }
        else {
            System.out.println("输入序号超出范围");
            return false;
        }
    }

    /**
     * 显示已添加的地点
     */
    private void printLocations() {
        for (int i = 0; i < locations.size(); i++) {
            Location temp = locations.get(i);
            System.out.println(i + "." + temp.getLocationName());
        }
    }

    /**
     * 显示已添加计划项
     */
    private void printEntries() {
        for (int i = 0; i < entries.size(); i++) {
            CourseEntry temp = entries.get(i);
            System.out.println(i + "." + temp.toString());
        }
    }

    /**
     * 显示已添加教师
     */
    private void printTeachers() {
        for (int i = 0; i < teachers.size(); i++) {
            Teacher temp = teachers.get(i);
            System.out.println(i + "." + temp.toString());
        }
    }

    /**
     * 检查输入数字是否在entries范围中
     *
     * @param num 需要检查的数字
     * @return 是否在范围中
     */
    private boolean isLegalEntryNumber(int num) {
        if (num >= 0 && num < entries.size()) {
            return true;
        }
        return false;
    }

    /**
     * 检查输入数字是否在teachers范围中
     *
     * @param num 需要检查的数字
     * @return 是否在范围中
     */
    private boolean isLegalTeacherNumber(int num) {
        if (num >= 0 && num < teachers.size()) {
            return true;
        }
        return false;
    }

    /**
     * 检查输入数字是否在locations的范围中
     *
     * @param num 需要检查的数字
     * @return 是否在locations范围中
     */
    private boolean isLegalLocationNumber(int num) {
        if (num >= 0 && num < locations.size()) {
            return true;
        }
        return false;
    }

    /**
     * 检查输入时间是否满足yyyy-MM-dd HH:mm的格式
     *
     * @param inputTime 输入时间
     * @return 是否满足格式
     */
    private boolean isLegalTimeFormat(String inputTime) {
        Pattern pattern = Pattern.compile("20\\d{2}-\\d{2}-\\d{2} \\d{2}:\\d{2}");
        Matcher matcher = pattern.matcher(inputTime);
        if(matcher.matches()){
            return true;
        }
        else {
            System.out.println("输入时间不符合规则");
            return false;
        }
    }

    /**
     * 显示菜单选项
     */
    private void printMenu() {
        System.out.println("欢迎使用课程管理程序，请输入对应序号完成操作");
        System.out.println("1.增加一个教师");
        System.out.println("2.增加一个教室");
        System.out.println("3.增加一条计划项");
        System.out.println("4.取消某个计划项");
        System.out.println("5.为某个计划项分配资源");
        System.out.println("6.启动某个计划项");
        System.out.println("7.重新设置指定计划项的教室");
        System.out.println("8.结束某个计划项");
        System.out.println("9.查询某计划项的状态");
        System.out.println("10.检测已存在计划项是否存在资源或位置冲突");
        System.out.println("11.获取使用指定资源的所有计划项，以及指定项目的前序计划项");
        System.out.println("12.显示指定位置教室的信息板");
        System.out.println("13.删除指定位置");
        System.out.println("14.删除指定教师");
        System.out.println("15.退出程序");
    }

    public static void main(String argv[]){
        new CourseCalendarApp().runApp();
    }
}
