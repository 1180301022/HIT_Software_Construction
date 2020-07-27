package P4;

import EntryState.State;
import Exceptions.*;
import HelperClasses.EntryFactory;
import HelperClasses.Location;
import HelperClasses.PlanningEntryAPIsStrategy;
import HelperClasses.TimePair;
import P2.TrainScheduleApp;

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
 * 问题4的主程序
 * 可变类
 */
public class CourseCalendarApp {
    private final List<CourseEntry> entries = new ArrayList<>();
    private final List<Teacher> teachers = new ArrayList<>();
    private final List<Location> locations = new ArrayList<>();
    final static Logger logger = Logger.getLogger(CourseCalendarApp.class.toString());
    private BufferedReader bfr = null;

    //AF：课程计划项为entries、教师资源为teachers、教室为locations的课表管理程序
    //RI：true
    //Safety from rep exposure：成员域使用private修饰

    public CourseCalendarApp(){
        //对logger进行设置
        Locale.setDefault(new Locale("en", "EN"));
        logger.setLevel(Level.INFO);
        try{
            FileHandler fileHandler = new FileHandler("src/P4/CourseCalendarLog.txt", true);
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
                case 15:
                    isOperationSucceed = showLog();
                    break;
                default:
                    return;
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

        //检测该教师是否已经存在
        try{
            if (teachers.contains(inputTeacher)) {
                throw new RepeatedAdditionException("身份证号为"+id+"的教师已被添加");
            }
        }
        catch (RepeatedAdditionException e){
            logger.warning(e.getMessage());
            e.printStackTrace();
            return false;
        }

        logger.info("身份证号为"+id+"的教师添加成功");
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

        //检测该地点是否已经存在
        try{
            if (locations.contains(inputLocation)) {
                throw new RepeatedAdditionException("输入位置"+inputLocation.getLocationName()+"已存在");
            }
        }
        catch (RepeatedAdditionException e){
            logger.warning(e.getMessage());
            e.printStackTrace();
            return false;
        }

        logger.info(inputLocation.getLocationName()+"添加成功");
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
            logger.info("课程"+entryName+"添加成功");
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
        try{
            if (!isLegalTimeFormat(startTime) || !isLegalTimeFormat(endTime)) {
                logger.warning("设置"+entry.getPlanningEntryName()+"的时间时，输入时间格式错误");
                return false;
            }
            if(TimePair.compareTime(startTime, endTime)!=1){
                throw new WrongTimeFormatException("设置"+entry.getPlanningEntryName()+"的时间时，输入结束时间早于起始时间");
            }
        }
        catch (WrongTimeFormatException e){
            logger.warning(e.getMessage());
            e.printStackTrace();
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
            return false;
        }

        //检测分配该位置是否会发生位置独占冲突
        //先将设置好位置的计划项加入到List中，检查后再清除
        Location opLocation = locations.get(input);
        List<Location> locationList = new ArrayList<>();
        locationList.add(opLocation);
        entry.presetLocation(locationList);
        entries.add(entry);
        PlanningEntryAPIsStrategy<Teacher> api = new PlanningEntryAPIsStrategy<>(1);
        try{
            if(api.checkLocation(entries)){
                throw new SetLocationException("为"+entry.getPlanningEntryName()+"分配位置"+opLocation+"发生位置独占冲突");
            }
        }
        catch (SetLocationException e){
            logger.warning(e.getMessage());
            e.printStackTrace();
            entries.remove(entry);
            return false;
        }
        entries.remove(entry);
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
                logger.info("取消计划项"+entries.get(opNumber).getPlanningEntryName()+"成功");
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
     * 5.为计划项分配教师
     * @return 是否分配成功
     */
    private boolean allocateSource(){
        System.out.println("请输入需要分配的计划项编号");
        printEntries();
        Scanner scanner = new Scanner(System.in);
        int opNumber = scanner.nextInt();
        if(!isLegalEntryNumber(opNumber)){
            return false;
        }
        CourseEntry opEntry = entries.get(opNumber);

        try{
            if(opEntry.getState()!= State.WAITING){
                throw new IllegalOperationException("为计划项"+opEntry.getPlanningEntryName()+"操作不符合要求");
            }
        }
        catch (IllegalOperationException e){
            logger.warning(e.getMessage());
            e.printStackTrace();
            return false;
        }

        printTeachers();
        System.out.println("请输入需要分配的教师编号");
        int sourceNumber = scanner.nextInt();
        if(!isLegalTeacherNumber(sourceNumber)){
            return false;
        }
        //检查分配后是否出现冲突
        Teacher tempTeacher = teachers.get(sourceNumber);
        List<Teacher> list = new ArrayList<>();
        list.add(tempTeacher);
        opEntry.setSource(list);
        PlanningEntryAPIsStrategy<Teacher> api = new PlanningEntryAPIsStrategy<>(1);
        try{
            if(api.checkResource(entries)){
                throw new AllocateSourceException("教师"+tempTeacher.getName()+"已被同时间段的计划项占用");
            }
        }
        catch (AllocateSourceException e){
            //如果发生异常，将计划项占用的资源清空
            logger.warning(e.getMessage());
            e.printStackTrace();
            list = new ArrayList<>();
            opEntry.setSource(list);
            return false;
        }
        logger.info("为计划项"+entries.get(opNumber).getPlanningEntryName()+"分配教师成功");
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
        CourseEntry opEntry = entries.get(opEntryNumber);
        try{
            if(opEntry.run()){
                logger.info("启动课程"+opEntry.getPlanningEntryName()+"成功");
                return true;
            }
            else {
                throw new IllegalOperationException("条件不满足，无法取消"+opEntry.getPlanningEntryName());
            }
        }
        catch (IllegalOperationException e){
            logger.warning(e.getMessage());
            e.printStackTrace();
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
            return false;
        }
        CourseEntry opEntry = entries.get(opEntryNumber);

        try{
            if(opEntry.getState()!=State.WAITING && opEntry.getState()!=State.ALLOCATED){
                throw new IllegalOperationException(opEntry.getPlanningEntryName()+"不符合更改位置的要求");
            }
        }
        catch (IllegalOperationException e){
            logger.warning(e.getMessage());
            e.printStackTrace();
            return false;
        }

        printLocations();
        System.out.println("请输入需要重新设置的教室编号");
        int opLocationNumber = scanner.nextInt();
        if(!isLegalLocationNumber(opLocationNumber)){
            return false;
        }
        List<Location> locationList = new ArrayList<>();
        locationList.add(locations.get(opLocationNumber));
        opEntry.resetLocation(locationList);
        logger.info("更改"+opEntry.getPlanningEntryName()+"的位置成功");
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
            return false;
        }
        CourseEntry opEntry = entries.get(opEntryNumber);
        try{
            if(opEntry.end()){
                logger.info("结束"+opEntry.getPlanningEntryName()+"成功");
                return true;
            }
            else {
                throw new IllegalOperationException("条件不满足，无法取消"+opEntry.getPlanningEntryName());
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
            CourseEntry tempEntry = entries.get(input);
            System.out.println("该计划项的状态是：" + tempEntry.getStatusToCharacters());
            logger.info("查询计划项"+tempEntry.getPlanningEntryName()+"的状态");
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
        logger.info("查询是否存在资源或位置冲突");
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
            return false;
        }
        //获得使用目标资源的计划项
        Teacher opTeacher = teachers.get(opTeacherNumber);
        List<CourseEntry> opEntries = new ArrayList<>();
        for(CourseEntry tempEntry : entries){
            if(tempEntry.getSource().get(0).equals(opTeacher)){
                opEntries.add(tempEntry);
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
        try{
            if(opEntryNumber<0 || opEntryNumber>=opEntries.size()){
                throw new InputOutOfBoundException("查询前序计划项时输入序号超出范围");
            }
        }
        catch (InputOutOfBoundException e){
            logger.warning(e.getMessage());
            e.printStackTrace();
            return false;
        }
        //寻找前序计划项
        CourseEntry targetEntry = opEntries.get(opEntryNumber);
        PlanningEntryAPIsStrategy<Teacher> api = new PlanningEntryAPIsStrategy<>(1);
        CourseEntry finalEntry = (CourseEntry) api.findPreSameResourceEntry(opTeacher, targetEntry, opEntries);
        if(finalEntry==null){
            System.out.println("无前序计划项");
        }
        else {
            System.out.println("前序计划项："+finalEntry.toString());
        }
        logger.info("查询教师"+opTeacher.getName()+"的所有课程，以及"+targetEntry.getPlanningEntryName()+"的前序计划项");
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
            logger.warning("查询课程表时输入错误的教室");
            return false;
        }
        else {
            new CourseBoard(locations.get(opNumber).getLocationName(), entries);
            logger.info("查询教室"+locations.get(opNumber).getLocationName()+"的课程表");
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
            //检测是否存在占用该位置并且未结束的计划项
            Location opLocation = locations.get(opNumber);
            for(CourseEntry tempEntry : entries){
                if(tempEntry.getLocation().get(0).equals(opLocation)){
                    try {
                        if(tempEntry.getState()!=State.ENDED && tempEntry.getState()!=State.CANCELLED){
                            throw new DeleteLocationException("存在占用"+opLocation.getLocationName()+"并且未结束的计划项");
                        }
                    }
                    catch (DeleteLocationException e){
                        logger.warning(e.getMessage());
                        e.printStackTrace();
                        return false;
                    }
                }
            }
            //不存在占用该位置并且未结束的计划项，删除位置即可
            logger.info("成功删除教室"+locations.get(opNumber).getLocationName());
            locations.remove(opNumber);
            return true;
        }
        else {
            logger.warning("删除教室时输入错误的教室");
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
            //检测是否存在占用该教师且未结束的计划项
            Teacher opTeacher = teachers.get(opNumber);
            for(CourseEntry tempEntry : entries){
                if(tempEntry.getSource().get(0).equals(opTeacher)){
                    try{
                        if(tempEntry.getState()!=State.CANCELLED && tempEntry.getState()!=State.ENDED){
                            throw new DeleteSourceException("存在占用"+opTeacher.getName()+"且未结束的计划项");
                        }
                    }
                    catch (DeleteSourceException e){
                        logger.warning(e.getMessage());
                        e.printStackTrace();
                        return false;
                    }
                }
            }
            //不存在占用该教师且未结束的计划项，直接删除即可
            logger.info("删除教师"+opTeacher.getName()+"成功");
            teachers.remove(opNumber);
            return true;
        }
        else {
            logger.warning("删除教师时输入错误的编号");
            System.out.println("输入序号超出范围");
            return false;
        }
    }

    /**
     * 15.显示程序运行的日志记录
     * @return 是否显示成功
     */
    private boolean showLog() {
        try{
            bfr = new BufferedReader(new FileReader("src/P4/CourseCalendarLog.txt"));
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
        Pattern pattern = Pattern.compile("(.*?)P4\\.(.*?)\\n([A-Z]+): (.*?)");
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
        Scanner scanner = new Scanner(System.in);
        int opNumber = scanner.nextInt();
        if(opNumber<1 || opNumber>14){
            System.out.println("输入信息错误，请重新操作");
            return false;
        }

        String opLogString = readSingleLog();
        Pattern pattern = Pattern.compile("(.*?)P4\\.([A-Z||a-z]+)\\s([A-Z||a-z]+)\\n([A-Z]+): (.*?)");
        while (opLogString!=null){
            Matcher matcher = pattern.matcher(opLogString);
            matcher.matches();
            if(opNumber==1 && matcher.group(3).equals("addTeacher")){
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
            else if(opNumber==7 && matcher.group(3).equals("resetLocation")){
                System.out.println("时间："+matcher.group(1)+"\t操作："+matcher.group(3)+"\t内容："+matcher.group(5));
            }
            else if(opNumber==8 && matcher.group(3).equals("endEntry")){
                System.out.println("时间："+matcher.group(1)+"\t操作："+matcher.group(3)+"\t内容："+matcher.group(5));
            }
            else if(opNumber==9 && matcher.group(3).equals("getEntryState")){
                System.out.println("时间："+matcher.group(1)+"\t操作："+matcher.group(3)+"\t内容："+matcher.group(5));
            }
            else if(opNumber==10 && matcher.group(3).equals("checkResourceAndLocationConflict")){
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
            else if(opNumber==14 && matcher.group(3).equals("deleteTeacher")){
                System.out.println("时间："+matcher.group(1)+"\t操作："+matcher.group(3)+"\t内容："+matcher.group(5));
            }
            opLogString = readSingleLog();
        }
        return true;
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

        Pattern pattern = Pattern.compile("(20\\d{2}-\\d{2}-\\d{2} \\d{2}:\\d{2}):\\d{2}\\sP4\\.(.*?)\\n(.*?)");
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
        try{
            if (num >= 0 && num < entries.size()) {
                return true;
            }
            else {
                throw new InputOutOfBoundException("输入编号超出课程编号范围");
            }
        }
        catch (InputOutOfBoundException e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 检查输入数字是否在teachers范围中
     *
     * @param num 需要检查的数字
     * @return 是否在范围中
     */
    private boolean isLegalTeacherNumber(int num) {
        try{
            if (num >= 0 && num < teachers.size()) {
                return true;
            }
            else {
                throw new InputOutOfBoundException("输入编号超出教师编号范围");
            }
        }
        catch (InputOutOfBoundException e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 检查输入数字是否在locations的范围中
     *
     * @param num 需要检查的数字
     * @return 是否在locations范围中
     */
    private boolean isLegalLocationNumber(int num) {
        try{
            if (num >= 0 && num < locations.size()) {
                return true;
            }
            else {
                throw new InputOutOfBoundException("输入编号超出教室编号范围");
            }
        }
        catch (InputOutOfBoundException e){
            e.printStackTrace();
            return false;
        }
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

        try{
            if(matcher.matches()){
                return true;
            }
            else {
                throw new WrongTimeFormatException("输入时间格式不符合规则");
            }
        }
        catch (WrongTimeFormatException e){
            e.printStackTrace();
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
        System.out.println("15.查询日志");
        System.out.println("16.退出程序");
    }

    public static void main(String argv[]){
        new CourseCalendarApp().runApp();
    }
}
