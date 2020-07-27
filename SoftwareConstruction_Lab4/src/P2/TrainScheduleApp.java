package P2;

import EntryState.State;
import Exceptions.*;
import HelperClasses.Location;
import HelperClasses.PlanningEntryAPIsStrategy;
import HelperClasses.TimePair;
import P1.Plane;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 问题2的客户端程序
 * 可变类
 */
public class TrainScheduleApp {
    private final List<TrainEntry> entries = new ArrayList<>();
    private final List<Train> trains = new ArrayList<>();
    private final List<Location> locations = new ArrayList<>();
    final static Logger logger = Logger.getLogger(TrainScheduleApp.class.toString());
    private BufferedReader bfr = null;

    //AF：计划项为entries、列车资源为trains、位置集为locations的列车管理程序
    //RI：true
    //Safety from rep exposure：成员域使用private修饰

    public TrainScheduleApp(){
        //对logger进行设置
        Locale.setDefault(new Locale("en", "EN"));
        logger.setLevel(Level.INFO);
        try{
            FileHandler fileHandler = new FileHandler("src/P2/TrainScheduleLog.txt", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
                case 15:
                    isOperationSucceed = showLog();
                    break;
                default:
                    return;
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

        //检测输入是否已经存在
        try{
            if(trains.contains(inputTrain)){
                throw new RepeatedAdditionException("编号为"+code+"的车厢已存在");
            }
        }
        catch (RepeatedAdditionException e){
            logger.warning(e.getMessage());
            e.printStackTrace();
            return false;
        }

        trains.add(inputTrain);
        logger.info("编号为"+code+"的车厢已添加");
        return true;
    }

    /**
     * 2.为程序添加一个新的地点
     * @return 是否添加成功
     */
    private boolean addLocation(){
        System.out.println("请输入位置");
        Location inputLocation = new Location(new Scanner(System.in).next());

        //检测输入是否已经存在
        try{
            if(locations.contains(inputLocation)){
                throw new RepeatedAdditionException("输入位置"+inputLocation.getLocationName()+"已存在");
            }
        }
        catch (RepeatedAdditionException e){
            logger.warning(e.getMessage());
            e.printStackTrace();
            return false;
        }

        locations.add(inputLocation);
        logger.info("输入位置"+inputLocation.getLocationName()+"已添加");
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
                try{
                    if(!temp.isNameAndTimeCompatible(entry)){
                        throw new ElementRelationException(entryName+"和某已存在计划项冲突");
                    }
                }
                catch (ElementRelationException e){
                    logger.warning(e.getMessage());
                    e.printStackTrace();
                    return false;
                }
            }
            logger.info("计划项"+entryName+"添加成功");
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
        scanner.nextLine();//读入回车

        try{
            if(timePairNumber+1 > locations.size()){
                throw new InputOutOfBoundException("已添加地点数不足，设置计划项"+entry.getPlanningEntryName()+"的时间失败");
            }
        }
        catch (InputOutOfBoundException e){
            logger.warning(e.getMessage());
            e.printStackTrace();
            return false;
        }

        System.out.println("请输入时间，格式yyyy-MM-dd HH:mm（每输入一个完整时间敲一下回车）");
        for(int i=0 ; i<timePairNumber ; i++){
            String startTime = scanner.nextLine();
            String endTime = scanner.nextLine();
            //检查输入格式是否满足要求
            try{
                if(!isLegalTimeFormat(startTime) || !isLegalTimeFormat(endTime)){
                    logger.warning("为计划项"+entry.getPlanningEntryName()+"设置时间时输入格式错误");
                    return false;
                }
                if(TimePair.compareTime(startTime, endTime)!=1){
                    throw new WrongTimeFormatException("创建计划项"+entry.getPlanningEntryName()+"时输入起始时间晚于到达时间");
                }
            }
            catch (WrongTimeFormatException e){
                logger.warning(e.getMessage());
                e.printStackTrace();
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

            try{
                if(inputNumbers.contains(input)){
                    throw new RepeatedAdditionException("输入地点"+locations.get(input).getLocationName()+"重复");
                }
                if(!isLegalLocationNumber(input)){
                    logger.warning("输入地点序号超出范围");
                }
            }
            catch (RepeatedAdditionException e){
                logger.warning(e.getMessage());
                e.printStackTrace();
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
            return false;
        }
        try{
            if(entries.get(opNumber).cancel()){
                logger.info("计划项"+entries.get(opNumber).getPlanningEntryName()+"取消成功");
                return true;
            }
            else {
                throw new IllegalOperationException("条件不满足，无法取消"+entries.get(opNumber).getPlanningEntryName());
            }
        }
        catch (IllegalOperationException e){
            logger.warning(e.getMessage());
            e.printStackTrace();
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
            logger.warning("为计划项分配资源时未选择范围内计划项");
            return false;
        }

        //输入编号合法，获得需要设置的计划项
        TrainEntry tempEntry = entries.get(inputEntryNumber);
        try{
            if(tempEntry.getState()!= State.WAITING){
                throw new IllegalOperationException("计划项"+tempEntry.getPlanningEntryName()+"已分配资源，无法再次分配");
            }
        }
        catch (IllegalOperationException e){
            logger.warning(e.getMessage());
            e.printStackTrace();
            return false;
        }

        //该计划项未分配资源
        System.out.println("请输入需要分配车厢的数目");
        int trainNumber = scanner.nextInt();

        try{
            if(trainNumber>trains.size()){
                throw new InputOutOfBoundException("已有车厢数量不足，为计划项"+tempEntry.getPlanningEntryName()+"分配车厢失败");
            }
        }
        catch (InputOutOfBoundException e){
            logger.warning(e.getMessage());
            e.printStackTrace();
            return false;
        }

        List<Train> trainList = new ArrayList<>();
        List<Integer> opList = new ArrayList<>();
        System.out.println("请输入需要分配的资源的序号，不可重复输入");
        printTrains();
        for(int i=0 ; i<trainNumber ; i++){
            int opNumber = scanner.nextInt();
            try{
                if(opList.contains(opNumber)){
                    throw new RepeatedAdditionException("输入重复车厢，为计划项"+tempEntry.getPlanningEntryName()+"分配车厢失败");
                }
            }
            catch (RepeatedAdditionException e){
                logger.warning(e.getMessage());
                e.printStackTrace();
                return false;
            }
            Train tempTrain = trains.get(opNumber);
            trainList.add(tempTrain);
        }

        //获得分配列表，检查是否存在资源冲突
        tempEntry.setSource(trainList);
        try{
            PlanningEntryAPIsStrategy<Train> api = new PlanningEntryAPIsStrategy<>(2);
            if(api.checkResource(entries)){
                throw new AllocateSourceException("分配的车厢和已存在计划项冲突，为计划项"+tempEntry.getPlanningEntryName()+"分配车厢失败");
            }
        }
        catch (AllocateSourceException e){
            //将该计划项还原到未分配状态
            logger.warning(e.getMessage());
            e.printStackTrace();
            trainList = new ArrayList<>();
            tempEntry.setSource(trainList);
            return false;
        }
        logger.info("为计划项"+tempEntry.getPlanningEntryName()+"分配车厢成功");
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
            return false;
        }

        TrainEntry opEntry = entries.get(opEntryNumber);
        try{
            if(opEntry.run()){
                logger.info("启动计划项"+opEntry.getPlanningEntryName()+"成功");
                return true;
            }
            else {
                throw new IllegalOperationException("条件不满足，无法启动计划项"+opEntry.getPlanningEntryName());
            }
        }
        catch (IllegalOperationException e){
            logger.warning(e.getMessage());
            e.printStackTrace();
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
            return false;
        }
        TrainEntry opEntry = entries.get(opEntryNumber);
        try{
            if(opEntry.block()){
                logger.info("挂起计划项"+opEntry.getPlanningEntryName()+"成功");
                return true;
            }
            else {
                throw new IllegalOperationException("条件不满足，无法挂起计划项"+opEntry.getPlanningEntryName());
            }
        }
        catch (IllegalOperationException e){
            logger.warning(e.getMessage());
            e.printStackTrace();
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
            return false;
        }
        TrainEntry opEntry = entries.get(opEntryNumber);
        try{
            if(opEntry.end()){
                logger.info("结束计划项"+opEntry.getPlanningEntryName()+"成功");
                return true;
            }
            else {
                throw new IllegalOperationException("条件不满足，无法结束计划项"+opEntry.getPlanningEntryName());
            }
        }
        catch (IllegalOperationException e){
            logger.warning(e.getMessage());
            e.printStackTrace();
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
            return false;
        }
        else {
            TrainEntry tempEntry = entries.get(input);
            System.out.println("该计划项的状态是：" + tempEntry.getStatusToCharacters());
            logger.info("查询计划项"+tempEntry.getPlanningEntryName()+"的状态");
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
        logger.info("检查目前的计划项是否存在资源冲突");
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

        try{
            if(entryNumber<0 || entryNumber>=entriesWithTargetTrain.size()){
                throw new InputOutOfBoundException("输入计划项序号超出范围");
            }
        }
        catch (InputOutOfBoundException e){
            logger.warning(e.getMessage());
            e.printStackTrace();
            return false;
        }

        TrainEntry targetEntry = entriesWithTargetTrain.get(entryNumber);
        PlanningEntryAPIsStrategy<Train> api = new PlanningEntryAPIsStrategy<>(2);
        TrainEntry finalEntry = (TrainEntry) api.findPreSameResourceEntry(targetTrain, targetEntry, entries);
        logger.info("查询"+targetEntry.getPlanningEntryName()+"前序计划项");
        if(finalEntry==null){
            System.out.println("计划项"+targetEntry.getPlanningEntryName()+"无前序计划项");
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
            return false;
        }
        else {
            logger.info("查询了"+locations.get(opNumber).getLocationName()+"位置的信息板");
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
            //检测是否存在占用该位置且未结束的计划项
            Location opLocation = locations.get(opNumber);
            for(TrainEntry tempEntry : entries){
                for(Location tempLocation : tempEntry.getLocation()){
                    if(tempLocation.equals(opLocation)){
                        try{
                            if(tempEntry.getState()!=State.ENDED && tempEntry.getState()!=State.CANCELLED){
                                throw new DeleteLocationException("存在占用该位置且未结束的计划项"+tempEntry.getPlanningEntryName());
                            }
                        }
                        catch (DeleteLocationException e){
                            logger.warning(e.getMessage());
                            e.printStackTrace();
                            return false;
                        }
                    }
                }
            }
            //不存在占用该位置的未结束的计划项，可删除
            logger.info("删除位置"+locations.get(opNumber).getLocationName());
            locations.remove(opNumber);
            return true;
        }
        else {
            logger.warning("指定删除位置超出范围");
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
            //检测是否存在占用该车厢且未结束的计划项
            Train opTrain = trains.get(opNumber);
            for(TrainEntry tempEntry : entries){
                if(tempEntry.getSource().size()==0){
                    continue;
                }
                for(Train tempTrain : tempEntry.getSource()){
                    if(tempTrain.equals(opTrain)){
                        try{
                            if(tempEntry.getState()!=State.CANCELLED && tempEntry.getState()!=State.ENDED){
                                throw new DeleteSourceException("存在占用该车厢且未结束的计划项"+tempEntry.getPlanningEntryName());
                            }
                        }
                        catch (DeleteSourceException e){
                            logger.warning(e.getMessage());
                            e.printStackTrace();
                            return false;
                        }
                    }
                }
            }
            //不存在占用该车厢且未结束的计划项，可直接删除
            logger.info("删除车厢"+opTrain.getNumber()+"成功");
            trains.remove(opNumber);
            return true;
        }
        else {
            logger.warning("指定删除车厢超出范围");
            return false;
        }
    }

    /**
     * 15.按照筛选条件显示日志
     * @return 是否操作成功
     */
    private boolean showLog(){
        try{
            bfr = new BufferedReader(new FileReader("src/P2/TrainScheduleLog.txt"));
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        //支持按照信息类型、操作类型、时间查找
        System.out.println("请输入按照何种条件查找");
        System.out.println("1.按照信息类型");
        System.out.println("2.按照操作类型");
        System.out.println("3.按照时间");
        Scanner scanner = new Scanner(System.in);
        switch (scanner.nextInt()){
            case 1:
                return searchByInfoType();
            case 2:
                return searchByOperationType();
            case 3:
                return searchByTime();
            default:
                System.out.println("输入编号有误，请重新操作");
                return false;
        }
    }

    /**
     * 15.根据日志信息类型查找
     * @return 是否查找成功
     */
    private boolean searchByInfoType(){
        System.out.println("请输入需要查找的信息类型: 1.INFO 2.WARNING 3.SEVERE");
        Scanner scanner = new Scanner(System.in);
        int opNumber = scanner.nextInt();
        if(opNumber!=1 && opNumber!=2 && opNumber!=3){
            System.out.println("输入信息错误，请重新操作");
            return false;
        }

        String opLogString = readSingleLog();
        Pattern pattern = Pattern.compile("(.*?)P2\\.(.*?)\\n([A-Z]+): (.*?)");
        while (opLogString!=null){
            Matcher matcher = pattern.matcher(opLogString);
            matcher.matches();
            if(opNumber==1 && matcher.group(3).equals("INFO")){
                System.out.println("时间："+matcher.group(1)+"\t位置："+matcher.group(2)+"\t内容："+matcher.group(4));
            }
            else if(opNumber==2 && matcher.group(3).equals("WARNING")){
                System.out.println("时间："+matcher.group(1)+"\t位置："+matcher.group(2)+"\t内容："+matcher.group(4));
            }
            else if(opNumber==3 && matcher.group(3).equals("SEVERE")){
                System.out.println("时间："+matcher.group(1)+"\t位置："+matcher.group(2)+"\t内容："+matcher.group(4));
            }
            opLogString = readSingleLog();
        }
        return true;
    }

    /**
     * 15.根据日志操作类型查找
     * @return 是否查找成功
     */
    private boolean searchByOperationType(){
        System.out.println("请输入需要查找的信息类型:");
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
        Scanner scanner = new Scanner(System.in);
        int opNumber = scanner.nextInt();
        if(opNumber<1 || opNumber>14){
            System.out.println("输入信息错误，请重新操作");
            return false;
        }

        String opLogString = readSingleLog();
        Pattern pattern = Pattern.compile("(.*?)P2\\.([A-Z||a-z]+)\\s([A-Z||a-z]+)\\n([A-Z]+): (.*?)");
        while (opLogString!=null) {
            Matcher matcher = pattern.matcher(opLogString);
            matcher.matches();
            if(opNumber==1 && matcher.group(3).equals("addTrain")){
                System.out.println("时间："+matcher.group(1)+"\t操作："+matcher.group(3)+"\t内容："+matcher.group(5));
            }
            else if(opNumber==2 && matcher.group(3).equals("addLocation")){
                System.out.println("时间："+matcher.group(1)+"\t操作："+matcher.group(3)+"\t内容："+matcher.group(5));
            }
            else if(opNumber==3 && matcher.group(3).equals("addEntry")){
                System.out.println("时间："+matcher.group(1)+"\t操作："+matcher.group(3)+"\t内容："+matcher.group(5));
            }
            else if(opNumber==4 && matcher.group(3).equals("cancelEntry")){
                System.out.println("时间："+matcher.group(1)+"\t操作："+matcher.group(3)+"\t内容："+matcher.group(5));
            }
            else if(opNumber==5 && matcher.group(3).equals("allocateSource")){
                System.out.println("时间："+matcher.group(1)+"\t操作："+matcher.group(3)+"\t内容："+matcher.group(5));
            }
            else if(opNumber==6 && matcher.group(3).equals("runEntry")){
                System.out.println("时间："+matcher.group(1)+"\t操作："+matcher.group(3)+"\t内容："+matcher.group(5));
            }
            else if(opNumber==7 && matcher.group(3).equals("blockEntry")){
                System.out.println("时间："+matcher.group(1)+"\t操作："+matcher.group(3)+"\t内容："+matcher.group(5));
            }
            else if(opNumber==8 && matcher.group(3).equals("endEntry")){
                System.out.println("时间："+matcher.group(1)+"\t操作："+matcher.group(3)+"\t内容："+matcher.group(5));
            }
            else if(opNumber==9 && matcher.group(3).equals("getEntryState")){
                System.out.println("时间："+matcher.group(1)+"\t操作："+matcher.group(3)+"\t内容："+matcher.group(5));
            }
            else if(opNumber==10 && matcher.group(3).equals("checkResourceConflict")){
                System.out.println("时间："+matcher.group(1)+"\t操作："+matcher.group(3)+"\t内容："+matcher.group(5));
            }
            else if(opNumber==11 && matcher.group(3).equals("getEntriesUseInputResourceAndPreEntry")){
                System.out.println("时间："+matcher.group(1)+"\t操作："+matcher.group(3)+"\t内容："+matcher.group(5));
            }
            else if(opNumber==12 && matcher.group(3).equals("showInfoBoard")){
                System.out.println("时间："+matcher.group(1)+"\t操作："+matcher.group(3)+"\t内容："+matcher.group(5));
            }
            else if(opNumber==13 && matcher.group(3).equals("deleteLocation")){
                System.out.println("时间："+matcher.group(1)+"\t操作："+matcher.group(3)+"\t内容："+matcher.group(5));
            }
            else if(opNumber==14 && matcher.group(3).equals("deleteTrain")){
                System.out.println("时间："+matcher.group(1)+"\t操作："+matcher.group(3)+"\t内容："+matcher.group(5));
            }
            opLogString = readSingleLog();
        }
        return true;
    }

    /**
     * 15.每次读取日志的一项（两行）
     * @return 读取的内容
     */
    private String readSingleLog(){
        try{
            String stringToReturn = bfr.readLine();
            if(stringToReturn == null){
                return null;
            }
            else {
                stringToReturn += "\n"+bfr.readLine();
                return stringToReturn;
            }
        }
        catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 15.根据日志时间查找
     *
     * @return 是否查找成功
     */
    private boolean searchByTime() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入起始时间，格式yyyy-MM-dd HH:mm");
        String startTime = scanner.nextLine();
        System.out.println("请输入结束时间，格式yyyy-MM-dd HH:mm");
        String endTime = scanner.nextLine();

        //检查输入是否符合规则
        if ((!isLegalTimeFormat(startTime)) || (!isLegalTimeFormat(endTime))) {
            return false;
        }
        if (TimePair.compareTime(startTime, endTime) != 1) {
            System.out.println("输入起始时间等于或晚于终止时间");
            return false;
        }

        Pattern pattern = Pattern.compile("(20\\d{2}-\\d{2}-\\d{2} \\d{2}:\\d{2}):\\d{2}\\sP2\\.(.*?)\\n(.*?)");
        String opString = readSingleLog();

        while (opString!=null){
            Matcher matcher = pattern.matcher(opString);
            matcher.matches();
            String tempTime = matcher.group(1);
            if(TimePair.compareTime(startTime, tempTime)==1 && TimePair.compareTime(endTime, tempTime)==2){
                System.out.println("时间："+matcher.group(1)+"\t位置："+matcher.group(2)+"\t信息："+matcher.group(3));
            }
            opString = readSingleLog();
        }
        return true;
    }

    /**
     * 检查输入时间是否满足yyyy-MM-dd HH:mm的格式
     * @param inputTime 输入时间
     * @return 是否满足格式
     */
    private boolean isLegalTimeFormat(String inputTime){
        Pattern pattern = Pattern.compile("20\\d{2}-\\d{2}-\\d{2} \\d{2}:\\d{2}");
        Matcher matcher = pattern.matcher(inputTime);
        try{
            if(matcher.matches()){
                return true;
            }
            else {
                throw new WrongTimeFormatException("输入时间不满足yyyy-MM-dd HH:mm格式");
            }
        }
        catch (WrongTimeFormatException e){
            e.printStackTrace();
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
        try{
            if(num>=0 && num<entries.size()){
                return true;
            }
            else{
                throw new InputOutOfBoundException("输入计划项编号超出范围");
            }
        }
        catch (InputOutOfBoundException e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 检查输入数字是否在trains范围中
     * @param num 需要检查的数字
     * @return 是否在范围中
     */
    private boolean isLegalTrainNumber(int num){
        try{
            if(num>=0 && num<trains.size()){
                return true;
            }
            else{
                throw new InputOutOfBoundException("输入车厢编号超出范围");
            }
        }
        catch (InputOutOfBoundException e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 检查输入数字是否在locations的范围中
     * @param num 需要检查的数字
     * @return 是否在locations范围中
     */
    private boolean isLegalLocationNumber(int num){
        try{
            if(num>=0 && num<locations.size()){
                return true;
            }
            else{
                throw new InputOutOfBoundException("输入位置编号超出范围");
            }
        }
        catch (InputOutOfBoundException e){
            e.printStackTrace();
            return false;
        }
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
        System.out.println("15.查找日志");
        System.out.println("16.退出程序");
    }

    public static void main(String argv[]){
        new TrainScheduleApp().runApp();
    }
}
