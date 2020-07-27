package P1;

import EntryState.State;
import Exceptions.*;
import HelperClasses.Location;
import HelperClasses.PlanningEntryAPIsStrategy;
import HelperClasses.TimePair;
import PlanningEntry.PlanningEntry;

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
 * 问题1的客户端程序
 * 可变类
 */
public class FlightScheduleApp {
    private final List<FlightEntry> entries = new ArrayList<>();
    private final List<Plane> planes = new ArrayList<>();
    private final List<Location> locations = new ArrayList<>();
    final static Logger logger = Logger.getLogger(FlightScheduleApp.class.toString());
    private BufferedReader bfr = null;

    //AF：计划项为entries、飞机资源为planes、位置集为locations的航班管理程序
    //RI：true
    //Safety from rep exposure：成员域使用private修饰

    public FlightScheduleApp() {
        //对logger进行设置
        Locale.setDefault(new Locale("en", "EN"));
        logger.setLevel(Level.INFO);
        try {
            FileHandler fileHandler = new FileHandler("src/P1/FlightScheduleLog.txt", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 运行主程序
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
     * 1.为程序添加一架新的飞机
     *
     * @return 是否添加成功
     */
    private boolean addPlane() {
        System.out.println("请输入飞机的编号，型号，座位数和机龄");
        Scanner scanner = new Scanner(System.in);
        String code = scanner.next();
        String model = scanner.next();
        int seat = scanner.nextInt();
        double year = scanner.nextDouble();
        Plane inputPlane = new Plane(code, model, seat, year);

        try {
            if (planes.contains(inputPlane)) {
                throw new RepeatedAdditionException("输入编号为" + code + "的飞机已存在");
            } else {
                planes.add(inputPlane);
                logger.info("添加编号为" + code + "的飞机成功");
                return true;
            }
        } catch (RepeatedAdditionException e) {
            e.printStackTrace();
            logger.warning(e.getMessage());
            return false;
        }

    }

    /**
     * 2.为程序添加一个新的地点
     *
     * @return 是否添加成功
     */
    private boolean addLocation() {
        System.out.println("请输入位置");
        Location inputLocation = new Location(new Scanner(System.in).next());

        try {
            if (locations.contains(inputLocation)) {
                throw new RepeatedAdditionException("添加名为" + inputLocation.getLocationName() + "的地点重复");
            } else {
                locations.add(inputLocation);
                logger.info("添加名为" + inputLocation.getLocationName() + "的地点成功");
                return true;
            }
        } catch (RepeatedAdditionException e) {
            e.printStackTrace();
            logger.warning(e.getMessage());
            return false;
        }
    }

    /**
     * 3.为程序添加一条新的计划项
     *
     * @return 是否添加成功
     */
    private boolean addEntry() {
        //设置计划项名
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入新建计划项名");
        String entryName = scanner.next();
        FlightEntry entry = new FlightEntry(entryName);

        //设置时间
        boolean isSetTimeSucceed = setEntryTime(entry);
        if (!isSetTimeSucceed) {
            return false;
        }

        //设置位置
        boolean isSetLocationSucceed = setEntryLocation(entry);
        if (!isSetLocationSucceed) {
            return false;
        }

        //如果计划项名和时间和已存在的某项不匹配，则添加失败
        for (FlightEntry temp : entries) {
            try {
                temp.checkNameAndTimeCompatible(entry);
            } catch (SameTagException e) {
                logger.warning(e.getMessage());
                e.printStackTrace();
                return false;
            } catch (ElementRelationException e) {
                logger.warning(e.getMessage());
                e.printStackTrace();
                return false;
            }
        }
        logger.info("添加计划项" + entry.getPlanningEntryName() + "成功");
        entries.add(entry);
        return true;
    }

    /**
     * 3.为新计划项设置时间
     *
     * @param inputEntry 需要设置的计划项
     * @return 是否设置成功
     */
    private boolean setEntryTime(FlightEntry inputEntry) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入起始时间，格式yyyy-MM-dd HH:mm");
        String startTime = scanner.nextLine();
        System.out.println("请输入结束时间，格式yyyy-MM-dd HH:mm");
        String endTime = scanner.nextLine();

        //检查输入是否符合规则
        try {
            if ((!checkTimeFormat(startTime)) || (!checkTimeFormat(endTime))) {
                logger.warning("创建计划项" + inputEntry.getPlanningEntryName() + "时输入时间格式错误");
                return false;
            }
            if (TimePair.compareTime(startTime, endTime) != 1) {
                throw new WrongTimeFormatException("创建计划项" + inputEntry.getPlanningEntryName() + "时输入起始时间晚于终止时间");
            }
        } catch (WrongTimeFormatException e) {
            logger.warning(e.getMessage());
            e.printStackTrace();
            return false;
        }

        //进行时间设置
        TimePair timePair = new TimePair(startTime, endTime);
        List<TimePair> time = new ArrayList<>();
        time.add(timePair);
        inputEntry.presetTime(time);
        return true;
    }

    /**
     * 3.为新计划项设置地点
     *
     * @param inputEntry 需要设置的计划项
     * @return 是否设置成功
     */
    private boolean setEntryLocation(FlightEntry inputEntry) {
        Scanner scanner = new Scanner(System.in);
        printLocations();
        System.out.println("请输入起始和到达地点编号");
        int from = scanner.nextInt();
        int to = scanner.nextInt();

        //检查输入编号是否符合范围
        if (!isLegalLocationNumber(from) || !isLegalLocationNumber(to)) {
            logger.warning("为计划项" + inputEntry.getPlanningEntryName() + "设置位置时输入序号超出范围");
            return false;
        }

        //检测输入地点是否重复
        try {
            if (from == to) {
                throw new SetLocationException("创建计划项" + inputEntry.getPlanningEntryName() + "时输入地点重复");
            }
        } catch (SetLocationException e) {
            logger.warning(e.getMessage());
            e.printStackTrace();
            return false;
        }

        //设置位置
        Location fromLocation = locations.get(from);
        Location toLocation = locations.get(to);
        List<Location> locationList = new ArrayList<>();
        locationList.add(fromLocation);
        locationList.add(toLocation);
        inputEntry.presetLocation(locationList);
        return true;

    }

    /**
     * 4.取消某计划项
     *
     * @return 是否取消成功
     */
    private boolean cancelEntry() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入需要取消的计划项编号");
        printEntries();
        int opNumber = scanner.nextInt();

        //检查输入计划项编号是否合法
        if (!isLegalEntryNumber(opNumber)) {
            return false;
        }

        try {
            if (entries.get(opNumber).cancel()) {
                logger.info("取消计划项" + entries.get(opNumber).getPlanningEntryName() + "成功");
                return true;
            } else {
                throw new IllegalOperationException("取消计划项" + entries.get(opNumber).getPlanningEntryName() + "失败");
            }
        } catch (IllegalOperationException e) {
            logger.warning(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 5.为计划项分配资源
     *
     * @return 是否分配成功
     */
    private boolean allocateSource() {
        System.out.println("请输入需要分配的计划项编号");
        printEntries();
        Scanner scanner = new Scanner(System.in);
        int opNumber = scanner.nextInt();

        //检查输入是否在范围内
        if (!isLegalEntryNumber(opNumber)) {
            logger.warning("分配资源时选择计划项失败");
            return false;
        }

        //检查是否满足可分配的要求
        FlightEntry opEntry = entries.get(opNumber);
        try {
            if (opEntry.getState() != State.WAITING) {
                throw new IllegalOperationException("航班" + opEntry.getPlanningEntryName() + "不处于待分配状态，进行分配失败");
            }
        } catch (IllegalOperationException e) {
            logger.warning(e.getMessage());
            e.printStackTrace();
            return false;
        }

        printPlanes();
        System.out.println("请输入需要分配的资源编号");
        int sourceNumber = scanner.nextInt();
        //检查输入是否在范围内
        if (!isLegalPlaneNumber(sourceNumber)) {
            logger.warning("为航班" + opEntry.getPlanningEntryName() + "分配资源时，输入资源编号超出范围");
            return false;
        }
        Plane tempPlane = planes.get(sourceNumber);

        //检测是否存在资源独占冲突
        List<Plane> list = new ArrayList<>();
        list.add(tempPlane);
        opEntry.setSource(list);
        PlanningEntryAPIsStrategy<Plane> api = new PlanningEntryAPIsStrategy<>(1);
        try {
            if (api.checkResource(entries)) {
                //如果存在冲突，抛出异常
                list = new ArrayList<>();
                opEntry.setSource(list);
                throw new AllocateSourceException("为航班" + opEntry.getPlanningEntryName() + "分配资源时出现资源独占冲突");
            }
        } catch (AllocateSourceException e) {
            logger.warning(e.getMessage());
            e.printStackTrace();
            return false;
        }
        logger.info("为计划项" + opEntry.getPlanningEntryName() + "分配资源" + tempPlane.getCode() + "成功");
        return true;
    }

    /**
     * 6.启动某个计划项
     *
     * @return 是否启动成功
     */
    private boolean runEntry() {
        System.out.println("请输入需要启动的计划项编码");
        printEntries();
        Scanner scanner = new Scanner(System.in);
        int opEntryNumber = scanner.nextInt();
        //检查输入是否在范围内
        if (!isLegalEntryNumber(opEntryNumber)) {
            logger.warning("选择计划项时超出范围");
            return false;
        }
        FlightEntry opEntry = entries.get(opEntryNumber);
        try {
            if (opEntry.run()) {
                logger.info("启动计划项" + opEntry.getPlanningEntryName() + "成功");
                return true;
            } else {
                throw new IllegalOperationException("条件不满足，无法启动计划项" + opEntry.getPlanningEntryName());
            }
        } catch (IllegalOperationException e) {
            logger.warning(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 7.结束一个计划项
     *
     * @return 是否结束成功
     */
    private boolean endEntry() {
        System.out.println("请输入需要结束的计划项编码");
        printEntries();
        Scanner scanner = new Scanner(System.in);
        int opEntryNumber = scanner.nextInt();

        //检查输入是否在范围内
        if (!isLegalEntryNumber(opEntryNumber)) {
            logger.warning("选择计划项时超出范围");
            return false;
        }

        FlightEntry opEntry = entries.get(opEntryNumber);

        try {
            if (opEntry.end()) {
                logger.info("结束计划项" + opEntry.getPlanningEntryName() + "成功");
                return true;
            } else {
                throw new IllegalOperationException("结束计划项" + opEntry.getPlanningEntryName() + "时条件不满足，无法结束");
            }
        } catch (IllegalOperationException e) {
            logger.warning(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 8.获取计划项状态
     *
     * @return 是否查询成功
     */
    private boolean getEntryState() {
        printEntries();
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入需要获取项目的编号");
        int input = scanner.nextInt();

        //检查编号是否合法
        if (!isLegalEntryNumber(input)) {
            logger.warning("选择计划项时超出范围");
            return false;
        } else {
            FlightEntry tempEntry = entries.get(input);
            System.out.println("该计划项的状态是：" + tempEntry.getStatusToCharacters());
            logger.info("查询计划项" + tempEntry.getPlanningEntryName() + "的状态成功");
            return true;
        }
    }

    /**
     * 9.检查是否存在资源冲突
     *
     * @return 操作是否成功
     */
    private boolean checkResourceConflict() {
        PlanningEntryAPIsStrategy<Plane> api = new PlanningEntryAPIsStrategy(1);
        if (api.checkResource(entries)) {
            System.out.println("存在资源分配冲突");
        } else {
            System.out.println("不存在资源分配冲突");
        }
        logger.info("检查已有计划项是否存在资源冲突");
        return true;
    }

    /**
     * 10.获取使用指定资源的所有计划项，以及指定项目的前序计划项
     *
     * @return 是否操作成功
     */
    private boolean getEntriesUseInputResourceAndPreEntry() {
        printPlanes();
        System.out.println("请输入需要查询的飞机序号");
        Scanner scanner = new Scanner(System.in);
        int inputPlaneNumber = scanner.nextInt();
        if (!isLegalPlaneNumber(inputPlaneNumber)) {
            logger.warning("选择资源时超出范围");
            return false;
        }
        Plane targetPlane = planes.get(inputPlaneNumber);
        List<FlightEntry> targetEntries = new ArrayList<>();
        //遍历寻找使用指定资源的计划项，并打印
        for (FlightEntry tempEntry : entries) {
            if (tempEntry.getSource().get(0).equals(targetPlane)) {
                targetEntries.add(tempEntry);
            }
        }
        System.out.println("使用指定资源的计划项如下");
        for (int i = 0; i < targetEntries.size(); i++) {
            System.out.println(i + "." + targetEntries.get(i).toString());
        }

        //寻找指定计划项的前序计划项
        System.out.println("输入指定计划项的编号");
        int inputEntryNumber = scanner.nextInt();
        if (inputEntryNumber < 0 || inputEntryNumber >= targetEntries.size()) {
            logger.warning("选择计划项时超出范围");
            System.out.println("输入序号超出范围");
            return false;
        }
        FlightEntry inputEntry = targetEntries.get(inputEntryNumber);
        PlanningEntry<Plane> preEntry = null;
        PlanningEntryAPIsStrategy<Plane> api = new PlanningEntryAPIsStrategy<>(1);
        preEntry = api.findPreSameResourceEntry(targetPlane, inputEntry, targetEntries);
        if (preEntry == null) {
            System.out.println("无前序计划项");
        } else {
            System.out.println("前序计划项如下：");
            FlightEntry tempEntry = (FlightEntry) preEntry;
            System.out.println(tempEntry.toString());
        }
        logger.info("获取使用" + targetPlane.getCode() + "的所有计划项，以及" + inputEntry.getPlanningEntryName() + "的前序计划项成功");
        return true;
    }

    /**
     * 11.显示指定位置的信息板
     *
     * @return 是否显示成功
     */
    private boolean showInfoBoard() {
        printLocations();
        System.out.println("请输入需要查询的位置");
        Scanner scanner = new Scanner(System.in);
        int inputLocationNumber = scanner.nextInt();
        if (!isLegalLocationNumber(inputLocationNumber)) {
            logger.warning("选择位置时超出范围");
            return false;
        }
        String locationName = locations.get(inputLocationNumber).getLocationName();
        logger.info("查询" + locationName + "的航班信息板成功");
        new FlightBoard(locationName, entries);
        return true;
    }

    /**
     * 12.删除指定位置
     *
     * @return 是否删除成功
     */
    private boolean deleteLocation() {
        printLocations();
        System.out.println("请输入需要删除的位置编号");
        Scanner scanner = new Scanner(System.in);
        int opNumber = scanner.nextInt();
        if (isLegalLocationNumber(opNumber)) {
            //检测是否有占用该位置的未结束航班
            Location opLocation = locations.get(opNumber);
            for (FlightEntry tempEntry : entries) {
                if (tempEntry.getLocation().get(0).equals(opLocation) || tempEntry.getLocation().get(1).equals(opLocation)) {
                    try {
                        if (tempEntry.getState() != State.ENDED && tempEntry.getState() != State.CANCELLED) {
                            throw new DeleteLocationException("待删除地点" + opLocation.getLocationName() +
                                    "被未结束航班" + tempEntry.getPlanningEntryName() + "占用");
                        }
                    } catch (DeleteLocationException e) {
                        logger.warning(e.getMessage());
                        e.printStackTrace();
                        return false;
                    }

                }
            }
            //不被占用则直接删除
            logger.info("删除位置" + opLocation.getLocationName() + "成功");
            locations.remove(opNumber);
            return true;
        } else {
            logger.warning("选择位置时超出范围");
            System.out.println("输入序号超出范围");
            return false;
        }
    }

    /**
     * 13.删除指定资源
     *
     * @return 是否删除成功
     */
    private boolean deletePlane() {
        printPlanes();
        System.out.println("请输入需要删除的飞机编号");
        Scanner scanner = new Scanner(System.in);
        int opNumber = scanner.nextInt();
        if (isLegalPlaneNumber(opNumber)) {
            //检测该飞机是否被未结束的航班占用
            Plane opPlane = planes.get(opNumber);
            for (FlightEntry tempEntry : entries) {
                //如果当前计划项没有分配资源则进入下轮循环
                if (tempEntry.getSource().size() == 0) {
                    continue;
                }
                //如果牵涉到待删除资源，检查状态
                if (tempEntry.getSource().get(0).equals(opPlane)) {
                    try {
                        if (tempEntry.getState() != State.CANCELLED && tempEntry.getState() != State.ENDED) {
                            throw new DeleteSourceException("存在占用飞机" + opPlane.getCode() + "的未结束航班" + tempEntry.getPlanningEntryName());
                        }
                    } catch (DeleteSourceException e) {
                        logger.warning(e.getMessage());
                        e.printStackTrace();
                        return false;
                    }
                }
            }
            //无资源占用，删除对应飞机即可
            logger.info("删除飞机" + opPlane.getCode() + "成功");
            planes.remove(opNumber);
            return true;
        } else {
            logger.warning("选择资源时超出范围");
            System.out.println("输入序号超出范围");
            return false;
        }
    }

    /**
     * 14.从文件中获取计划项
     *
     * @return 操作是否成功
     * @throws FileNotFoundException 未找到目标文件抛出异常
     */
    private boolean getEntriesFromFile() {
        System.out.println("请输入1-5之间的编号");
        Scanner scanner = new Scanner(System.in);
        String opNum = scanner.next();
        String filePath = "src/Files/FlightSchedule_" + opNum + ".txt";
        EntriesFromFile entriesFromFile = new EntriesFromFile(filePath);

        //打印已加入的计划项
        System.out.println("已经读入的计划项如下：");
        entriesFromFile.printEntries();

        //将读取文件中的计划项的元素（航班、地点、飞机）加入到APP的List中
        for (FlightEntry temp : entriesFromFile.getEntries()) {
            entries.add(temp);
        }
        for (Plane tempPlane : entriesFromFile.getPlanes()) {
            planes.add(tempPlane);
        }
        for (Location tempLocation : entriesFromFile.getLocations()) {
            locations.add(tempLocation);
        }
        return true;
    }

    /**
     * 15.显示程序运行的日志记录
     *
     * @return 是否显示成功
     */
    private boolean showLog() {
        try {
            bfr = new BufferedReader(new FileReader("src/P1/FlightScheduleLog.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //支持按照信息类型、操作类型、时间查找
        System.out.println("请输入按照何种条件查找");
        System.out.println("1.按照信息类型");
        System.out.println("2.按照操作类型");
        System.out.println("3.按照时间");
        Scanner scanner = new Scanner(System.in);
        switch (scanner.nextInt()) {
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
     *
     * @return 是否查找成功
     */
    private boolean searchByInfoType() {
        System.out.println("请输入需要查找的信息类型: 1.INFO 2.WARNING 3.SEVERE");
        Scanner scanner = new Scanner(System.in);
        int opNumber = scanner.nextInt();
        if (opNumber != 1 && opNumber != 2 && opNumber != 3) {
            System.out.println("输入信息错误，请重新操作");
            return false;
        }

        String opLogString = readSingleLog();
        Pattern pattern = Pattern.compile("(.*?)P1\\.(.*?)\\n([A-Z]+): (.*?)");
        while (opLogString != null) {
            Matcher matcher = pattern.matcher(opLogString);
            matcher.matches();
            if (opNumber == 1 && matcher.group(3).equals("INFO")) {
                System.out.println("时间：" + matcher.group(1) + "\t位置：" + matcher.group(2) + "\t内容：" + matcher.group(4));
            } else if (opNumber == 2 && matcher.group(3).equals("WARNING")) {
                System.out.println("时间：" + matcher.group(1) + "\t位置：" + matcher.group(2) + "\t内容：" + matcher.group(4));
            } else if (opNumber == 3 && matcher.group(3).equals("SEVERE")) {
                System.out.println("时间：" + matcher.group(1) + "\t位置：" + matcher.group(2) + "\t内容：" + matcher.group(4));
            }
            opLogString = readSingleLog();
        }
        return true;
    }

    /**
     * 15.根据日志操作类型查找
     *
     * @return 是否查找成功
     */
    private boolean searchByOperationType() {
        System.out.println("请输入需要查找的信息类型:");
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
        Scanner scanner = new Scanner(System.in);
        int opNumber = scanner.nextInt();
        if (opNumber < 1 || opNumber > 13) {
            System.out.println("输入信息错误，请重新操作");
            return false;
        }

        String opLogString = readSingleLog();
        Pattern pattern = Pattern.compile("(.*?)P1\\.([A-Z||a-z]+)\\s([A-Z||a-z]+)\\n([A-Z]+): (.*?)");
        while (opLogString != null) {
            Matcher matcher = pattern.matcher(opLogString);
            matcher.matches();
            if (opNumber == 1 && matcher.group(3).equals("addPlane")) {
                System.out.println("时间：" + matcher.group(1) + "\t操作：" + matcher.group(3) + "\t内容：" + matcher.group(5));
            } else if (opNumber == 2 && matcher.group(3).equals("addLocation")) {
                System.out.println("时间：" + matcher.group(1) + "\t操作：" + matcher.group(3) + "\t内容：" + matcher.group(5));
            } else if (opNumber == 3 && matcher.group(3).equals("addEntry")) {
                System.out.println("时间：" + matcher.group(1) + "\t操作：" + matcher.group(3) + "\t内容：" + matcher.group(5));
            } else if (opNumber == 4 && matcher.group(3).equals("cancelEntry")) {
                System.out.println("时间：" + matcher.group(1) + "\t操作：" + matcher.group(3) + "\t内容：" + matcher.group(5));
            } else if (opNumber == 5 && matcher.group(3).equals("allocateSource")) {
                System.out.println("时间：" + matcher.group(1) + "\t操作：" + matcher.group(3) + "\t内容：" + matcher.group(5));
            } else if (opNumber == 6 && matcher.group(3).equals("runEntry")) {
                System.out.println("时间：" + matcher.group(1) + "\t操作：" + matcher.group(3) + "\t内容：" + matcher.group(5));
            } else if (opNumber == 7 && matcher.group(3).equals("endEntry")) {
                System.out.println("时间：" + matcher.group(1) + "\t操作：" + matcher.group(3) + "\t内容：" + matcher.group(5));
            } else if (opNumber == 8 && matcher.group(3).equals("getEntryState")) {
                System.out.println("时间：" + matcher.group(1) + "\t操作：" + matcher.group(3) + "\t内容：" + matcher.group(5));
            } else if (opNumber == 9 && matcher.group(3).equals("checkResourceConflict")) {
                System.out.println("时间：" + matcher.group(1) + "\t操作：" + matcher.group(3) + "\t内容：" + matcher.group(5));
            } else if (opNumber == 10 && matcher.group(3).equals("getEntriesUseInputResourceAndPreEntry")) {
                System.out.println("时间：" + matcher.group(1) + "\t操作：" + matcher.group(3) + "\t内容：" + matcher.group(5));
            } else if (opNumber == 11 && matcher.group(3).equals("showInfoBoard")) {
                System.out.println("时间：" + matcher.group(1) + "\t操作：" + matcher.group(3) + "\t内容：" + matcher.group(5));
            } else if (opNumber == 12 && matcher.group(3).equals("deleteLocation")) {
                System.out.println("时间：" + matcher.group(1) + "\t操作：" + matcher.group(3) + "\t内容：" + matcher.group(5));
            } else if (opNumber == 13 && matcher.group(3).equals("deletePlane")) {
                System.out.println("时间：" + matcher.group(1) + "\t操作：" + matcher.group(3) + "\t内容：" + matcher.group(5));
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
        if ((!checkTimeFormat(startTime)) || (!checkTimeFormat(endTime))) {
            return false;
        }
        if (TimePair.compareTime(startTime, endTime) != 1) {
            System.out.println("输入起始时间等于或晚于终止时间");
            return false;
        }

        Pattern pattern = Pattern.compile("(20\\d{2}-\\d{2}-\\d{2} \\d{2}:\\d{2}):\\d{2}\\sP1\\.(.*?)\\n(.*?)");
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
     *
     * @return 读取的内容
     */
    private String readSingleLog() {
        try {
            String stringToReturn = bfr.readLine();
            if (stringToReturn == null) {
                return null;
            } else {
                stringToReturn += "\n" + bfr.readLine();
                return stringToReturn;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 检查单个的时间是否符合"yyyy-MM-dd HH:mm"的格式
     *
     * @param inputTime 需要检查的时间
     * @return 该时间是否符合格式
     */
    private boolean checkTimeFormat(String inputTime) {
        Pattern pattern = Pattern.compile("20\\d{2}-\\d{2}-\\d{2} \\d{2}:\\d{2}");
        Matcher matcher = pattern.matcher(inputTime);
        try {
            if (matcher.matches()) {
                return true;
            } else {
                throw new WrongTimeFormatException("输入时间不符合规则");
            }
        } catch (WrongTimeFormatException e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 检查输入数字是否在entries范围中
     *
     * @param num 需要检查的数字
     * @return 是否在范围中
     */
    private boolean isLegalEntryNumber(int num) {
        try {
            if (num >= 0 && num < entries.size()) {
                return true;
            } else {
                throw new InputOutOfBoundException("输入计划项编号超出范围");
            }
        } catch (InputOutOfBoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 检查输入数字是否在planes范围中
     *
     * @param num 需要检查的数字
     * @return 是否在范围中
     */
    private boolean isLegalPlaneNumber(int num) {
        try {
            if (num >= 0 && num < planes.size()) {
                return true;
            } else {
                throw new InputOutOfBoundException("输入飞机编号超出范围");
            }
        } catch (InputOutOfBoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 检查输入数字是否在locations范围中
     *
     * @param num 需要检查的数字
     * @return 是否在locations范围中
     */
    private boolean isLegalLocationNumber(int num) {
        try {
            if (num >= 0 && num < locations.size()) {
                return true;
            } else {
                throw new InputOutOfBoundException("输入位置编号超出范围");
            }
        } catch (InputOutOfBoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 显示菜单选项
     */
    private void printMenu() {
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
        System.out.println("15.查找日志");
        System.out.println("16.退出程序");
    }

    /**
     * 显示已存在的计划项信息，方便操作
     */
    private void printEntries() {
        for (int i = 0; i < entries.size(); i++) {
            FlightEntry tempEntry = entries.get(i);
            System.out.println(i + "." + tempEntry.toString());
        }
    }

    /**
     * 显示已存在的飞机信息
     */
    private void printPlanes() {
        for (int i = 0; i < planes.size(); i++) {
            Plane tempPlane = planes.get(i);
            System.out.println(i + "." + tempPlane.toString());
        }
    }

    /**
     * 显示已输入的位置信息
     */
    private void printLocations() {
        for (int i = 0; i < locations.size(); i++) {
            Location tempLocation = locations.get(i);
            System.out.println(i + "." + tempLocation.getLocationName());
        }
    }

    public static void main(String argv[]) {
        new FlightScheduleApp().runApp();
    }
}
