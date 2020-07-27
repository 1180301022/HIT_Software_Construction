package P1;

import HelperClasses.Location;
import HelperClasses.TimePair;
import Exceptions.*;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 从文件中读入并构建计划项
 * 可变类
 */
public class EntriesFromFile {
    private List<FlightEntry> entries = new ArrayList<>();
    private List<Plane> planes = new ArrayList<>();
    private List<Location> locations = new ArrayList<>();
    private BufferedReader bfr = null;

    //AF：计划项为entries、飞机资源为planes、地点为locations、阅读器为bfr的根据文件的计划项创建器
    //RI：true
    //Safety from rep exposure：成员域使用private修饰

    /**
     * 创建计划项创建器
     *
     * @param filePath 输入的文件地址
     */
    public EntriesFromFile(String filePath) {
        try {
            bfr = new BufferedReader(new FileReader(filePath));
            createEntries();
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found Error!");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据输入创建计划项
     */
    private void createEntries() throws IOException {
        //每13行代表一个计划项，将13行的内容读入到字符串中，传入创建单个计划项的方法
        //进行循环直到读到末尾
        while (true) {
            //一次循环构造一个新的计划项
            String firstLine = readSingleLine();
            //如果读到末尾则结束
            if (firstLine == null) {
                return;
            }
            //未到末尾，构造计划项
            for (int i = 0; i < 12; i++) {
                firstLine += readSingleLine();
            }
            //如果某项创建异常则直接退出
            if (!createSingleEntry(firstLine)) {
                return;
            }
        }
    }

    /**
     * 创建单个的计划项，即对一个13行的输入进行读取
     */
    private boolean createSingleEntry(String info) {
        //用以匹配单个计划项的输入
        Pattern pattern = Pattern.compile("Flight:(\\d{4}-\\d{2}-\\d{2}),([A-Z]{2}\\d{2,4})\\{DepartureAirport:(.*?)" +
                "ArrivalAirport:(.*?)DepatureTime:(\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2})ArrivalTime:(\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2})" +
                "Plane:(N\\d{4}|B\\d{4})" + "\\{Type:([A-Z]+[0-9]+)Seats:([5-9][0-9]|[1-5][0-9][0-9]|600)Age:(\\d(\\.\\d)?|[1-2][0-9](\\.\\d)?|30(\\.0)?)\\}\\}");
        Matcher matcher = pattern.matcher(info);

        //检测输入字符串是否符合格式要求，即对要求1的检查，如果不符合则提示错误信息并停止读入
        if (!checkFileFormat(matcher)) {
            return false;
        }

        //对输入字符串进行要求2和3的检查，如果不符合则提示错误信息并停止读入
        if (!checkExceptionAndCreateEntry(matcher)) {
            System.out.println("文件存在错误，请重新输入文件");
            entries.clear();
            planes.clear();
            locations.clear();
            return false;
        }

        //如果通过异常检测并成功建立计划项，返回true并继续进行读取
        return true;

    }

    /**
     * 检测输入文件是否符合格式要求
     *
     * @param inputMatcher 输入matcher
     * @return 格式是否正确
     */
    private boolean checkFileFormat(Matcher inputMatcher) {
        try {
            if (!inputMatcher.matches()) {
                throw new FileFormatException("输入文件格式错误");
            }
        } catch (FileFormatException e) {
            FlightScheduleApp.logger.warning(e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 检测新计划项的元素关系是否异常
     *
     * @return 是否符合要求（元素关系和标签是否正常）
     */
    private boolean checkExceptionAndCreateEntry(Matcher matcher) {
        //读取信息并构造计划项、飞机
        matcher.matches();
        String departureDay = matcher.group(1);
        String flightName = matcher.group(2);
        String departureLocation = matcher.group(3);
        String arrivalLocation = matcher.group(4);
        String departureTime = matcher.group(5);
        String arrivalTime = matcher.group(6);
        String planeName = matcher.group(7);
        String planeModel = matcher.group(8);
        String planeSeatNumber = matcher.group(9);
        String planeAge = matcher.group(10);
        FlightEntry entry = new FlightEntry(flightName);
        //设置位置
        Location location1 = new Location(departureLocation);
        Location location2 = new Location(arrivalLocation);
        List<Location> locationList = new ArrayList<>();
        locationList.add(location1);
        locationList.add(location2);
        entry.presetLocation(locationList);
        //设置时间
        TimePair timePair = new TimePair(departureTime, arrivalTime);
        List<TimePair> timePairList = new ArrayList<>();
        timePairList.add(timePair);
        entry.presetTime(timePairList);

        //构造飞机
        int seat = Integer.parseInt(planeSeatNumber);
        double age = Float.parseFloat(planeAge);
        Plane plane = new Plane(planeName, planeModel, seat, age);
        List<Plane> planeList = new ArrayList<>();
        planeList.add(plane);
        entry.setSource(planeList);

        try {
            //3.1 第一行出发日期和后面的出发日期不同
            if (!isEqualDay(departureDay, departureTime)) {
                throw new ElementRelationException(flightName+"第一行出发日期和后面的出发日期不同");
            }
            //3.2 到达时间比出发时间超过一天
            if (!isArrivalTimeQualified(departureTime, arrivalTime)) {
                throw new ElementRelationException(flightName+"到达时间比出发时间超过一天");
            }
            //3.4 相同编号的飞机信息不同
            if(!isPlaneInfoLegal(plane)){
                throw new ElementRelationException("输入飞机"+plane.getCode()+"的信息和已有冲突");
            }
            //2、3.3 不同航班的编号和时间是否兼容
            //根据之间代码的设计，异常在被调用的方法中抛出
            for (FlightEntry tempEntry : entries) {
                tempEntry.checkNameAndTimeCompatible(entry);
            }
        } catch (SameTagException e) {
            FlightScheduleApp.logger.warning(e.getMessage());
            e.printStackTrace();
            return false;
        } catch (ElementRelationException e) {
            FlightScheduleApp.logger.warning(e.getMessage());
            e.printStackTrace();
            return false;
        }

        //无异常时，将航班和飞机加入List，返回true
        addToLists(entry, plane, location1, location2);
        FlightScheduleApp.logger.info("添加航班"+flightName+"成功");
        return true;
    }

    /**
     * 判断两个时间的年月日是否相等
     *
     * @param day1 输入时间1
     * @param day2 输入时间2
     * @return 年月日是否相等
     */
    private boolean isEqualDay(String day1, String day2) {
        int day1Year = TimePair.getYear(day1.toCharArray());
        int day2Year = TimePair.getYear(day2.toCharArray());
        int day1Month = TimePair.getMonth(day1.toCharArray());
        int day2Month = TimePair.getMonth(day2.toCharArray());
        int day1Day = TimePair.getDay(day1.toCharArray());
        int day2Day = TimePair.getDay(day2.toCharArray());
        if (day1Year == day2Year && day1Month == day2Month && day1Day == day2Day) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检测两输入时间（出发和到达时间）是否在24小时内
     *
     * @param departureTime 出发时间
     * @param arrivalTime   到达时间
     * @return 输入时间差是否在24小时内
     */
    private boolean isArrivalTimeQualified(String departureTime, String arrivalTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime departure = LocalDateTime.parse(departureTime, formatter);
        LocalDateTime arrival = LocalDateTime.parse(arrivalTime, formatter);
        Duration duration = Duration.between(departure, arrival);
        double durationToHour = duration.toHours();
        if (durationToHour > 24) {
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * 检测输入的飞机是否和已存在飞机冲突
     * @param inputPlane 输入飞机信息
     * @return 是否合法
     */
    private boolean isPlaneInfoLegal(Plane inputPlane) {
        //寻找相同编号的飞机并比较信息
        for (Plane tempPlane : planes) {
            if (tempPlane.getCode().equals(inputPlane.getCode())) {
                //如果有任一信息不相等，则不合法
                if(tempPlane.getSeatNumber()!=inputPlane.getSeatNumber() || tempPlane.getYear()-inputPlane.getYear()>1e-5 ||
                    !tempPlane.getModel().equals(inputPlane.getModel())){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 将单个计划项创建的对象（Plane、Location、Entry）加入到当前类的属性
     */
    private void addToLists(FlightEntry entry, Plane plane, Location location1, Location location2){
        entries.add(entry);
        if(!planes.contains(plane)){
            planes.add(plane);
        }
        if(!locations.contains(location1)){
            locations.add(location1);
        }
        if(!locations.contains(location2)){
            locations.add(location2);
        }
    }

    /**
     * 从文件中读入一行
     *
     * @return 读入的一行
     * @throws IOException 文件未找到时抛出异常
     */
    private String readSingleLine() throws IOException {
        try {
            String line = bfr.readLine();
            return line;
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found Error!");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取根据输入得到的计划项
     *
     * @return 创建的计划项
     */
    public List<FlightEntry> getEntries() {
        List<FlightEntry> entriesToReturn = new ArrayList<>();
        for (FlightEntry temp : entries) {
            entriesToReturn.add(temp);
        }
        return entriesToReturn;
    }

    /**
     * 获取创建的飞机
     * @return 创建的飞机的列表
     */
    public List<Plane> getPlanes(){
        List<Plane> planeList = new ArrayList<>();
        for(Plane tempPlane : planes){
            planeList.add(tempPlane);
        }
        return planeList;
    }

    /**
     * 获取创建的地点
     * @return 创建的地点列表
     */
    public List<Location> getLocations(){
        List<Location> locationList = new ArrayList<>();
        for(Location tempLocation : locations){
            locationList.add(tempLocation);
        }
        return locationList;
    }

    /**
     * 显示添加的计划项
     */
    public void printEntries() {
        for (FlightEntry temp : entries) {
            System.out.println(temp.toString());
        }
    }
}
