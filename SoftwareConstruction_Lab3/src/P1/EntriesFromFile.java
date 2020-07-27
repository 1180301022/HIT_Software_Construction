package P1;

import HelperClasses.Location;
import HelperClasses.TimePair;

import java.io.*;
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
    private BufferedReader bfr = null;

    //AF：计划项为entries、阅读器为bfr的根据文件的计划项创建器
    //RI：true
    //Safety from rep exposure：成员域使用private修饰

    /**
     * 创建计划项创建器
     * @param filePath 输入的文件地址
     * @throws FileNotFoundException 未找到文件抛出的异常
     */
    public EntriesFromFile(String filePath) throws FileNotFoundException {
        try {
            bfr = new BufferedReader(new FileReader(filePath));
            createEntries();
        }
        catch (FileNotFoundException e){
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
        while (true){
            String firstLine = readSingleLine();
            //如果读到末尾则结束
            if(firstLine==null){
                return;
            }
            //未到末尾，构造计划项
            for(int i=0 ; i<12 ;i++){
                firstLine += readSingleLine();
            }
            createSingleEntry(firstLine);
        }
    }

    /**
     * 创建单个的计划项，即对一个13行的输入进行读取
     */
    private boolean createSingleEntry(String info){
        //用以匹配单个计划项的输入
        Pattern pattern = Pattern.compile("Flight:(.*?),(.*?)\\{DepartureAirport:(.*?)" +
                "ArrivalAirport:(.*?)DepatureTime:(.*?)ArrivalTime:(.*?)Plane:(N\\d{4}|B\\d{4})" +
                "\\{Type:(.*?)Seats:(.*?)Age:(.*?)\\}\\}");
        Matcher matcher = pattern.matcher(info);
        //如果匹配，创建新计划项
        if(matcher.matches()) {
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

            //查看出发日期是否和实际出发日期相等
            if(!isEqualDay(departureDay, departureTime)){
                System.out.println("两次输入出发日期不相等");
                return false;
            }

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

            //查找是否出现冲突
            for (FlightEntry temp : entries) {
                if (!temp.isNameAndTimeCompatible(entry)) {
                    System.out.println("输入计划项"+entry.toString()+"\t和已存在计划项"+temp.toString()+"冲突");
                    return false;
                }
            }

            //没有冲突，则分配资源并加入计划项
            int seat = Integer.parseInt(planeSeatNumber);
            double age = Float.parseFloat(planeAge);
            Plane plane = new Plane(planeName, planeModel, seat, age);
            List<Plane> planeList = new ArrayList<>();
            planeList.add(plane);
            entry.setSource(planeList);

            entries.add(entry);
            return true;
        }
        //如果不匹配则直接返回
        return false;
    }

    /**
     * 获取根据输入得到的计划项
     * @return 创建的计划项
     */
    public List<FlightEntry> getEntries(){
        List<FlightEntry> entriesToReturn = new ArrayList<>();
        for(FlightEntry temp : entries){
            entriesToReturn.add(temp);
        }
        return entriesToReturn;
    }

    /**
     * 显示添加的计划项
     */
    public void printEntries(){
        for(FlightEntry temp : entries){
            System.out.println(temp.toString());
        }
    }

    /**
     * 从文件中读入一行
     * @return 读入的一行
     * @throws IOException 文件未找到时抛出异常
     */
    private String readSingleLine() throws IOException {
        try{
            String line = bfr.readLine();
            return line;
        }
        catch (FileNotFoundException e){
            System.out.println("File Not Found Error!");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断两个时间的年月日是否相等
     * @param day1 输入时间1
     * @param day2 输入时间2
     * @return 年月日是否相等
     */
    private boolean isEqualDay(String day1, String day2){
        int day1Year = TimePair.getYear(day1.toCharArray());
        int day2Year = TimePair.getYear(day2.toCharArray());
        int day1Month = TimePair.getMonth(day1.toCharArray());
        int day2Month = TimePair.getMonth(day2.toCharArray());
        int day1Day = TimePair.getDay(day1.toCharArray());
        int day2Day = TimePair.getDay(day2.toCharArray());
        if(day1Year==day2Year && day1Month==day2Month && day1Day==day2Day){
            return true;
        }
        else {
            return false;
        }
    }
}
