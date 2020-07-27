package P2;

import EntryState.State;
import HelperClasses.Location;
import HelperClasses.PlanningEntryAPIsStrategy;
import HelperClasses.TimePair;
import P1.Plane;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 问题2的客户端程序
 * 可变类
 */
public class TrainScheduleApp {
    private List<TrainEntry> entries = new ArrayList<>();
    private List<Train> trains = new ArrayList<>();
    private List<Location> locations = new ArrayList<>();

    //AF：计划项为entries、列车资源为trains、位置集为locations的列车管理程序
    //RI：true
    //Safety from rep exposure：成员域使用private修饰

    /**
     * 客户端主程序框架
     */
    public void runApp(){
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
                    isOperationSucceed = addTrain();
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
                    isOperationSucceed = blockEntry();
                    break;
                case 8:
                    isOperationSucceed = endEntry();
                    break;
                case 9:
                    isOperationSucceed = getEntryState();
                    break;
                case 10:
                    isOperationSucceed = checkResourceConflict();
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
                    isOperationSucceed = deleteTrain();
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
     * 1.为程序添加一个新的车厢
     * @return 是否添加成功
     */
    private boolean addTrain(){
        System.out.println("请输入车厢的编号，类型，座位数和机龄");
        Scanner scanner = new Scanner(System.in);
        int code = scanner.nextInt();
        String model = scanner.next();
        int seat = scanner.nextInt();
        double year = scanner.nextDouble();
        Train inputTrain = new Train(code, model, seat, year);

        if(trains.contains(inputTrain)){
            System.out.println("该编号的车厢已存在");
            return false;
        }
        trains.add(inputTrain);
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
     * 3.添加一个新计划项
     * @return 是否添加成功
     */
    private boolean addEntry(){
        System.out.println("请输入车次");
        Scanner scanner = new Scanner(System.in);
        String entryName = scanner.next();
        TrainEntry entry = new TrainEntry(entryName);
        if(setEntryTime(entry) && setEntryLocation(entry)){
            //如果计划项名和时间和已存在的某项不匹配，则添加失败
            for(TrainEntry temp : entries){
                if(!temp.isNameAndTimeCompatible(entry)){
                    System.out.println("输入信息有误，添加失败");
                    return false;
                }
            }
            entries.add(entry);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * 3.为新计划项设置时间
     * @param entry 需要设置的计划项
     * @return 是否设置成功
     */
    private boolean setEntryTime(TrainEntry entry){
        Scanner scanner = new Scanner(System.in);
        List<TimePair> timePairList = new ArrayList<>();
        System.out.println("请输入时间对的个数");
        int timePairNumber = scanner.nextInt();
        String getReturn = scanner.nextLine();//读入回车
        if(timePairNumber+1 > locations.size()){
            System.out.println("已添加地点数不足");
            return false;
        }
        System.out.println("请输入时间，格式yyyy-MM-dd HH:mm（每输入一个完整时间敲一下回车）");
        for(int i=0 ; i<timePairNumber ; i++){
            String startTime = scanner.nextLine();
            String endTime = scanner.nextLine();
            //检查输入格式是否满足要求
            if(!isLegalTimeFormat(startTime) || !isLegalTimeFormat(endTime)){
                System.out.println("输入时间不符合规则");
                return false;
            }
            TimePair inputTime = new TimePair(startTime, endTime);
            timePairList.add(inputTime);
        }
        entry.presetTime(timePairList);
        return true;
    }

    /**
     * 3.为新计划项设置位置
     * @param entry 新建计划项
     * @return 是否设置成功
     */
    private boolean setEntryLocation(TrainEntry entry){
        int locationNumber = entry.getTime().size()+1;
        printLocations();
        System.out.println("总共需要添加"+locationNumber+"个地点，请依次输入序号，不能重复");
        Scanner scanner = new Scanner(System.in);
        List<Location> locationList = new ArrayList<>();
        List<Integer> inputNumbers = new ArrayList<>();
        for(int i=0 ; i<locationNumber ; i++){
            int input = scanner.nextInt();
            if(inputNumbers.contains(input)){
                System.out.println("输入重复序号");
                return false;
            }
            if(!isLegalLocationNumber(input)){
                System.out.println("输入数字超出范围");
                return false;
            }
            inputNumbers.add(input);
            Location tempLocation = locations.get(input);
            locationList.add(tempLocation);
        }
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
     * 5.为计划项分配资源
     * @return 是否分配成功
     */
    private boolean allocateSource(){
        System.out.println("请输入需要分配的计划项编号");
        printEntries();
        Scanner scanner = new Scanner(System.in);
        int inputEntryNumber = scanner.nextInt();
        if(!isLegalEntryNumber(inputEntryNumber)){
            System.out.println("输入序号超出范围");
            return false;
        }
        //输入编号合法，获得需要设置的计划项
        TrainEntry tempEntry = entries.get(inputEntryNumber);
        if(tempEntry.getState()!= State.WAITING){
            System.out.println("该计划项已分配资源");
            return false;
        }
        //该计划项未分配资源
        System.out.println("请输入需要分配车厢的数目");
        int trainNumber = scanner.nextInt();
        if(trainNumber>trains.size()){
            System.out.println("已有车厢数量不足");
            return false;
        }
        List<Train> trainList = new ArrayList<>();
        List<Integer> opList = new ArrayList<>();
        System.out.println("请输入需要分配的资源的序号，不可重复输入");
        printTrains();
        for(int i=0 ; i<trainNumber ; i++){
            int opNumber = scanner.nextInt();
            if(opList.contains(opNumber)){
                System.out.println("输入数字重复");
                return false;
            }
            Train tempTrain = trains.get(opNumber);
            trainList.add(tempTrain);
        }
        tempEntry.setSource(trainList);
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
        TrainEntry opEntry = entries.get(opEntryNumber);
        if(opEntry.run()){
            return true;
        }
        else {
            System.out.println("条件不满足，无法启动");
            return false;
        }
    }

    /**
     * 7.挂起指定计划项
     * @return 是否挂起成功
     */
    private boolean blockEntry(){
        System.out.println("请输入需要启动的计划项编码");
        printEntries();
        Scanner scanner = new Scanner(System.in);
        int opEntryNumber = scanner.nextInt();
        if(!isLegalEntryNumber(opEntryNumber)){
            System.out.println("输入编号超出区间");
            return false;
        }
        TrainEntry opEntry = entries.get(opEntryNumber);
        if(opEntry.block()){
            return true;
        }
        else {
            System.out.println("条件不满足，无法启动");
            return false;
        }
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
        TrainEntry opEntry = entries.get(opEntryNumber);
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
            TrainEntry tempEntry = entries.get(input);
            System.out.println("该计划项的状态是：" + tempEntry.getStatusToCharacters());
            return true;
        }
    }

    /**
     * 10.检查是否存在资源冲突
     * @return 操作是否成功
     */
    private boolean checkResourceConflict(){
        PlanningEntryAPIsStrategy<Plane> api = new PlanningEntryAPIsStrategy(2);
        if(api.checkResource(entries)){
            System.out.println("存在资源分配冲突");
        }
        else {
            System.out.println("不存在资源分配冲突");
        }
        return true;
    }

    /**
     * 11.获取使用指定资源的所有计划项，以及指定项目的前序计划项
     * @return 是否操作成功
     */
    private boolean getEntriesUseInputResourceAndPreEntry(){
        printTrains();
        System.out.println("请输入需要查询资源的序号");
        Scanner scanner = new Scanner(System.in);
        int trainNumber = scanner.nextInt();
        if(!isLegalTrainNumber(trainNumber)){
            System.out.println("输入序号超出范围");
            return false;
        }
        Train targetTrain = trains.get(trainNumber);
        //遍历计划项，寻找使用该资源的计划项
        List<TrainEntry> entriesWithTargetTrain = new ArrayList<>();
        for(TrainEntry tempEntry : entries){
            for(Train tempTrain : tempEntry.getSource()){
                if(tempTrain.equals(targetTrain)){
                    entriesWithTargetTrain.add(tempEntry);
                    break;
                }
            }
        }
        System.out.println("使用该资源的计划项如下");
        for(int i=0 ; i<entriesWithTargetTrain.size() ; i++){
            System.out.println(i+"."+entriesWithTargetTrain.get(i).toString());
        }
        System.out.println("请输入需要查询前序计划项的序号");
        int entryNumber = scanner.nextInt();
        if(entryNumber<0 || entryNumber>=entriesWithTargetTrain.size()){
            System.out.println("输入序号超出范围");
            return false;
        }
        TrainEntry targetEntry = entriesWithTargetTrain.get(entryNumber);
        PlanningEntryAPIsStrategy<Train> api = new PlanningEntryAPIsStrategy<>(2);
        TrainEntry finalEntry = (TrainEntry) api.findPreSameResourceEntry(targetTrain, targetEntry, entries);
        if(finalEntry==null){
            System.out.println("无前序计划项");
            return true;
        }
        else {
            System.out.println("前序计划项是\n"+finalEntry.toString());
            return true;
        }
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
            new TrainBoard(locations.get(opNumber).getLocationName(), entries);
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
    private boolean deleteTrain(){
        printTrains();
        System.out.println("请输入需要删除的车厢编号");
        Scanner scanner = new Scanner(System.in);
        int opNumber = scanner.nextInt();
        if(isLegalTrainNumber(opNumber)){
            trains.remove(opNumber);
            return true;
        }
        else {
            System.out.println("输入序号超出范围");
            return false;
        }
    }

    /**
     * 检查输入时间是否满足yyyy-MM-dd HH:mm的格式
     * @param inputTime 输入时间
     * @return 是否满足格式
     */
    private boolean isLegalTimeFormat(String inputTime){
        Pattern pattern = Pattern.compile("20\\d{2}-\\d{2}-\\d{2} \\d{2}:\\d{2}");
        Matcher matcher = pattern.matcher(inputTime);
        if(matcher.matches()){
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * 显示已添加的地点
     */
    private void printLocations(){
        for(int i=0 ; i<locations.size() ; i++){
            Location temp = locations.get(i);
            System.out.println(i+"."+temp.getLocationName());
        }
    }

    /**
     * 显示已添加计划项
     */
    private void printEntries(){
        for(int i=0 ; i<entries.size() ; i++){
            TrainEntry temp = entries.get(i);
            System.out.println(i+"."+temp.toString());
        }
    }

    /**
     * 显示已添加车厢
     */
    private void printTrains(){
        for(int i=0 ; i<trains.size() ; i++){
            Train temp = trains.get(i);
            System.out.println(i+"."+temp.toString());
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
     * 检查输入数字是否在trains范围中
     * @param num 需要检查的数字
     * @return 是否在范围中
     */
    private boolean isLegalTrainNumber(int num){
        if(num>=0 && num<trains.size()){
            return true;
        }
        return false;
    }

    /**
     * 检查输入数字是否在locations的范围中
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
        System.out.println("欢迎使用车站管理程序，请输入对应序号完成操作");
        System.out.println("1.增加一个车厢");
        System.out.println("2.增加一个地点");
        System.out.println("3.增加一条计划项");
        System.out.println("4.取消某个计划项");
        System.out.println("5.为某个计划项分配资源");
        System.out.println("6.启动某个计划项");
        System.out.println("7.挂起某个计划项");
        System.out.println("8.结束某个计划项");
        System.out.println("9.查询某计划项的状态");
        System.out.println("10.检测已存在计划项是否存在资源冲突");
        System.out.println("11.获取使用指定资源的所有计划项，以及指定项目的前序计划项");
        System.out.println("12.显示指定位置车站的信息板");
        System.out.println("13.删除指定位置");
        System.out.println("14.删除指定车厢");
        System.out.println("15.退出程序");
    }

    public static void main(String argv[]){
        new TrainScheduleApp().runApp();
    }
}
