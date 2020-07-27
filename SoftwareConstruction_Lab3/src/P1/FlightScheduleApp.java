package P1;

import EntryState.State;
import HelperClasses.Location;
import HelperClasses.PlanningEntryAPIsStrategy;
import HelperClasses.TimePair;
import PlanningEntry.PlanningEntry;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 问题1的客户端程序
 * 可变类
 */
public class FlightScheduleApp {
    private List<FlightEntry> entries = new ArrayList<>();
    private List<Plane> planes = new ArrayList<>();
    private List<Location> locations = new ArrayList<>();

    //AF：计划项为entries、飞机资源为planes、位置集为locations的航班管理程序
    //RI：true
    //Safety from rep exposure：成员域使用private修饰

    /**
     * 运行主程序
     */
    public void runApp() throws FileNotFoundException {
        //识别用户输入操作
        printMenu();
        int operation = 0;
        Scanner scanner = new Scanner(System.in);
        //运行具体程序
        while (true){
            boolean isOperationSucceed = false;
            operation = scanner.nextInt();
            switch (operation){
                case 1:
                    isOperationSucceed = addPlane();
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
                    isOperationSucceed = endEntry();
                    break;
                case 8:
                    isOperationSucceed = getEntryState();
                    break;
                case 9:
                    isOperationSucceed = checkResourceConflict();
                    break;
                case 10:
                    isOperationSucceed = getEntriesUseInputResourceAndPreEntry();
                    break;
                case 11:
                    isOperationSucceed = showInfoBoard();
                    break;
                case 12:
                    isOperationSucceed = deleteLocation();
                    break;
                case 13:
                    isOperationSucceed = deletePlane();
                    break;
                case 14:
                    isOperationSucceed = getEntriesFromFile();
                    break;
                default:
                    System.exit(0);
            }
            if(isOperationSucceed){
                System.out.println("操作成功\n");
            }
            else {
                System.out.println("操作失败\n");
            }
            printMenu();
        }
    }

    /**
     * 1.为程序添加一架新的飞机
     * @return 是否添加成功
     */
    private boolean addPlane(){
        System.out.println("请输入飞机的编号，型号，座位数和机龄");
        Scanner scanner = new Scanner(System.in);
        String code = scanner.next();
        String model = scanner.next();
        int seat = scanner.nextInt();
        double year = scanner.nextDouble();
        Plane inputPlane = new Plane(code, model, seat, year);

        if(planes.contains(inputPlane)){
            System.out.println("该编号的飞机已存在");
            return false;
        }
        planes.add(inputPlane);
        return true;
    }

    /**
     * 2.为程序添加一个新的地点
     * @return 是否添加成功
     */
    private boolean addLocation(){
        System.out.println("请输入位置");
        Location inputLocation = new Location(new Scanner(System.in).next());
        if(locations.contains(inputLocation)){
            System.out.println("输入位置已存在");
            return false;
        }
        locations.add(inputLocation);
        return true;
    }

    /**
     * 3.为程序添加一条新的计划项
     * @return 是否添加成功
     */
    private boolean addEntry(){
        //设置计划项名
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入新建计划项名");
        String entryName = scanner.next();
        FlightEntry entry = new FlightEntry(entryName);

        System.out.println("该航班是否有中转？如果有请输入1，否则输入2");
        int hasMiddleLocationNum = scanner.nextInt();
        boolean hasMiddleLocation;
        if(hasMiddleLocationNum==1){
            hasMiddleLocation = true;
        }
        else {
            hasMiddleLocation = false;
        }

        //设置时间
        boolean isSetTimeSucceed = setEntryTime(entry, hasMiddleLocation);

        //如果计划项名和时间和已存在的某项不匹配，则添加失败
        for(FlightEntry temp : entries){
            if(!temp.isNameAndTimeCompatible(entry)){
                System.out.println("输入信息有误，添加失败");
                return false;
            }
        }

        //设置位置
        boolean isSetLocationSucceed = setEntryLocation(entry, hasMiddleLocation);
        //如果位置和时间成功设置，则新增条目成功
        if(isSetTimeSucceed && isSetLocationSucceed){
            entries.add(entry);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * 3.为新计划项设置时间
     * @param inputEntry 需要设置的计划项
     * @return 是否设置成功
     */
    private boolean setEntryTime(FlightEntry inputEntry, boolean hasMiddleLocation){
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入起始时间，格式yyyy-MM-dd HH:mm");
        String startTime1 = scanner.nextLine();
        System.out.println("请输入结束时间，格式yyyy-MM-dd HH:mm");
        String endTime1 = scanner.nextLine();
        String startTime2 = "";
        String endTime2 = "";

        //如果有中转则需要输入两个时间对
        if(hasMiddleLocation){
            System.out.println("请输入起始时间，格式yyyy-MM-dd HH:mm");
            startTime2 = scanner.nextLine();
            System.out.println("请输入结束时间，格式yyyy-MM-dd HH:mm");
            endTime2 = scanner.nextLine();
            //对第二个时间对格式进行检查
            if((!checkTimeFormat(startTime2)) || (!checkTimeFormat(endTime2))){
                return false;
            }
        }
        //对第一个时间对格式进行检查
        if((!checkTimeFormat(startTime1)) || (!checkTimeFormat(endTime1))){
            return false;
        }

        //进行时间设置
        List<TimePair> time = new ArrayList<>();
        TimePair timePair1 = new TimePair(startTime1, endTime1);
        time.add(timePair1);
        //如果有中转需要加入第二个时间对
        if(hasMiddleLocation){
            TimePair timePair2 = new TimePair(startTime2, endTime2);
            time.add(timePair2);
        }
        inputEntry.presetTime(time);
        return true;
    }

    /**
     * 3.为新计划项设置地点
     * @param inputEntry 需要设置的计划项
     * @return 是否设置成功
     */
    private boolean setEntryLocation(FlightEntry inputEntry, boolean hasMiddleLocation){
        Scanner scanner = new Scanner(System.in);
        printLocations();
        System.out.println("请按顺序输入地点编号");
        int from = scanner.nextInt();
        int to = scanner.nextInt();
        int too = -1;
        //如果存在，读取第三个位置
        if(hasMiddleLocation){
            too = scanner.nextInt();
        }

        if(isLegalLocationNumber(from) && isLegalLocationNumber(to)){
            Location fromLocation = locations.get(from);
            Location toLocation = locations.get(to);
            List<Location> locationList = new ArrayList<>();
            locationList.add(fromLocation);
            locationList.add(toLocation);
            //如果存在中转，对第三个位置进行设置
            if(hasMiddleLocation){
                if(isLegalLocationNumber(too)){
                    Location tooLocation = locations.get(too);
                    locationList.add(tooLocation);
                }
                else {
                    System.out.println("输入地点未添加");
                    return false;
                }
            }

            inputEntry.presetLocation(locationList);
            return true;
        }
        else{
            System.out.println("输入地点未添加");
            return false;
        }
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
     * 5.为计划项分配资源
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
        FlightEntry opEntry = entries.get(opNumber);
        if(opEntry.getState()!= State.WAITING){
            System.out.println("操作不符合要求");
            return false;
        }

        printPlanes();
        System.out.println("请输入需要分配的资源编号");
        int sourceNumber = scanner.nextInt();
        if(!isLegalPlaneNumber(sourceNumber)){
            System.out.println("输入编号超出区间");
            return false;
        }
        Plane tempPlane = planes.get(sourceNumber);

        List<Plane> list = new ArrayList<>();
        list.add(tempPlane);
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
        FlightEntry opEntry = entries.get(opEntryNumber);
        if(opEntry.run()){
            return true;
        }
        else {
            System.out.println("条件不满足，无法启动");
            return false;
        }
    }

    /**
     * 7.结束一个计划项
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
        FlightEntry opEntry = entries.get(opEntryNumber);
        if(opEntry.end()){
            return true;
        }
        else {
            System.out.println("条件不满足，无法结束");
            return false;
        }
    }

    /**
     * 8.获取计划项状态
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
            FlightEntry tempEntry = entries.get(input);
            System.out.println("该计划项的状态是：" + tempEntry.getStatusToCharacters());
            return true;
        }
    }

    /**
     * 9.检查是否存在资源冲突
     * @return 操作是否成功
     */
    private boolean checkResourceConflict(){
        PlanningEntryAPIsStrategy<Plane> api = new PlanningEntryAPIsStrategy(1);
        if(api.checkResource(entries)){
            System.out.println("存在资源分配冲突");
        }
        else {
            System.out.println("不存在资源分配冲突");
        }
        return true;
    }

    /**
     * 10.获取使用指定资源的所有计划项，以及指定项目的前序计划项
     * @return 是否操作成功
     */
    private boolean getEntriesUseInputResourceAndPreEntry(){
        printPlanes();
        System.out.println("请输入需要查询的飞机序号");
        Scanner scanner = new Scanner(System.in);
        int inputPlaneNumber = scanner.nextInt();
        if(!isLegalPlaneNumber(inputPlaneNumber)){
            System.out.println("输入序号超出范围");
            return false;
        }
        Plane targetPlane = planes.get(inputPlaneNumber);
        List<FlightEntry> targetEntries = new ArrayList<>();
        //遍历寻找使用指定资源的计划项，并打印
        for(FlightEntry tempEntry : entries){
            if(tempEntry.getSource().get(0).equals(targetPlane)){
                targetEntries.add(tempEntry);
            }
        }
        System.out.println("使用指定资源的计划项如下");
        for(int i=0 ; i<targetEntries.size() ; i++){
            System.out.println(i + "." + targetEntries.get(i).toString());
        }

        //寻找指定计划项的前序计划项
        System.out.println("输入指定计划项的编号");
        int inputEntryNumber = scanner.nextInt();
        if(inputEntryNumber<0 || inputEntryNumber>=targetEntries.size()){
            System.out.println("输入序号超出范围");
            return false;
        }
        FlightEntry inputEntry = targetEntries.get(inputEntryNumber);
        PlanningEntry<Plane> preEntry = null;
        PlanningEntryAPIsStrategy<Plane> api = new PlanningEntryAPIsStrategy<>(1);
        preEntry = api.findPreSameResourceEntry(targetPlane, inputEntry, targetEntries);
        if(preEntry == null){
            System.out.println("无前序计划项");
        }
        else {
            System.out.println("前序计划项如下：");
            FlightEntry tempEntry = (FlightEntry)preEntry;
            System.out.println(tempEntry.toString());
        }
        return true;
    }

    /**
     * 11.显示指定位置的信息板
     * @return 是否显示成功
     */
    private boolean showInfoBoard(){
        printLocations();
        System.out.println("请输入需要查询的位置");
        Scanner scanner = new Scanner(System.in);
        int inputLocationNumber = scanner.nextInt();
        if(!isLegalLocationNumber(inputLocationNumber)){
            System.out.println("输入位置不存在");
            return false;
        }
        String locationName = locations.get(inputLocationNumber).getLocationName();
        new FlightBoard(locationName, entries);
        return true;
    }

    /**
     * 12.删除指定位置
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
     * 13.删除指定资源
     * @return 是否删除成功
     */
    private boolean deletePlane(){
        printPlanes();
        System.out.println("请输入需要删除的飞机编号");
        Scanner scanner = new Scanner(System.in);
        int opNumber = scanner.nextInt();
        if(isLegalPlaneNumber(opNumber)){
            planes.remove(opNumber);
            return true;
        }
        else {
            System.out.println("输入序号超出范围");
            return false;
        }
    }

    /**
     * 14.从文件中获取计划项
     * @return 操作是否成功
     * @throws FileNotFoundException 未找到目标文件抛出异常
     */
    private boolean getEntriesFromFile() throws FileNotFoundException {
        System.out.println("请输入1-5之间的编号");
        Scanner scanner = new Scanner(System.in);
        String opNum = scanner.next();
        String filePath = "src/P1/FlightSchedule_"+opNum+".txt";
        EntriesFromFile entriesFromFile = new EntriesFromFile(filePath);
        entriesFromFile.printEntries();
        for(FlightEntry temp : entriesFromFile.getEntries()){
            planes.add(temp.getSource().get(0));
            locations.add(temp.getLocation().get(0));
            entries.add(temp);
        }
        return true;
    }

    /**
     * 检查单个的时间是否符合"yyyy-MM-dd HH:mm"的格式
     * @param inputTime 需要检查的时间
     * @return 该时间是否符合格式
     */
    private boolean checkTimeFormat(String inputTime){
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
     * 检查输入数字是否在entries范围中
     * @param num 需要检查的数字
     * @return 是否在范围中
     */
    private boolean isLegalEntryNumber(int num){
        if(num>=0 && num<entries.size()){
            return true;
        }
        return false;
    }

    /**
     * 检查输入数字是否在planes范围中
     * @param num 需要检查的数字
     * @return 是否在范围中
     */
    private boolean isLegalPlaneNumber(int num){
        if(num>=0 && num<planes.size()){
            return true;
        }
        return false;
    }

    /**
     * 检查输入数字是否在locations范围中
     * @param num 需要检查的数字
     * @return 是否在locations范围中
     */
    private boolean isLegalLocationNumber(int num){
        if(num>=0 && num<locations.size()){
            return true;
        }
        return false;
    }

    /**
     * 显示菜单选项
     */
    private void printMenu(){
        System.out.println("欢迎使用航班管理程序，请输入对应序号完成操作");
        System.out.println("1.增加一架飞机");
        System.out.println("2.增加一个地点");
        System.out.println("3.增加一条计划项");
        System.out.println("4.取消某个计划项");
        System.out.println("5.为某个计划项分配资源");
        System.out.println("6.启动某个计划项");
        System.out.println("7.结束某个计划项");
        System.out.println("8.查询某计划项的状态");
        System.out.println("9.检测已存在计划项是否存在资源冲突");
        System.out.println("10.获取使用指定资源的所有计划项，以及指定项目的前序计划项");
        System.out.println("11.显示指定位置机场的信息板");
        System.out.println("12.删除指定位置");
        System.out.println("13.删除指定资源");
        System.out.println("14.从文件中读取计划项");
        System.out.println("15.退出程序");
    }

    /**
     * 显示已存在的计划项信息，方便操作
     */
    private void printEntries(){
        for(int i=0 ; i<entries.size() ; i++){
            FlightEntry tempEntry = entries.get(i);
            System.out.println(i + "." + tempEntry.toString());
        }
    }

    /**
     * 显示已存在的飞机信息
     */
    private void printPlanes(){
        for(int i=0 ; i<planes.size() ; i++){
            Plane tempPlane = planes.get(i);
            System.out.println(i + "." + tempPlane.toString());
        }
    }

    /**
     * 显示已输入的位置信息
     */
    private void printLocations(){
        for(int i=0 ; i<locations.size() ; i++){
            Location tempLocation = locations.get(i);
            System.out.println(i + "." + tempLocation.getLocationName());
        }
    }

    public static void main(String argv[]) throws FileNotFoundException {
        new FlightScheduleApp().runApp();
    }
}
