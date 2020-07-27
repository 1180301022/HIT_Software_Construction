package P1;

import HelperClasses.*;
import javax.swing.*;
import java.awt.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

/**
 * 表示指定位置航班信息板的类
 * 可变类
 */
public class FlightBoard extends JFrame {
    //存放符合要求的项目
    List<FlightEntry> arrivalFlights = new ArrayList<>();
    List<FlightEntry> departureFlights = new ArrayList<>();

    //AF：到达列表为arrivalFlights、出发列表为departureFlights的信息板
    //RI：true
    //Safety from rep exposure：成员域使用private修饰、创建迭代器时返回副本的迭代器

    /**
     * 创建并显示含有一小时内的到达列表和出发列表的Board
     * @param airportName 当前机场名
     * @param entries 计划项列表
     */
    public FlightBoard(String airportName, List<FlightEntry> entries){
        //设置窗口常规项目
        setTitle("当前机场："+airportName);
        setSize(1000, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        //遍历entries，寻找从airportName出发的航班的出发时间/到达airportName的航班的到达时间 距离当前时间一小时内的航班
        for(FlightEntry tempEntry : entries){
            //如果是到达airportName的航班
            if(tempEntry.getLocation().get(1).getLocationName().equals(airportName)){
                String arrivalTimeString = tempEntry.getTime().get(0).getEndTime();
                //时间满足条件，将该项按顺序插入到List中
                if(isQualifiedTime(arrivalTimeString)){
                    int originalSize = arrivalFlights.size();
                    arrivalFlights.add(tempEntry);
                    if (originalSize != 0) {
                        for (int i = 0; i < originalSize; i++) {
                            //找到非最后一位的插入位置
                            if (TimePair.compareTime(arrivalTimeString, arrivalFlights.get(i).getTime().get(0).getEndTime()) == 1) {
                                for (int j = originalSize - 1; j >= i; j--) {
                                    arrivalFlights.set(j + 1, arrivalFlights.get(j));
                                }
                                arrivalFlights.set(i, tempEntry);
                                break;
                            }
                        }
                    }
                }
            }

            //如果是从airportName出发的航班
            else if(tempEntry.getLocation().get(0).getLocationName().equals(airportName)){
                String departureTimeString = tempEntry.getTime().get(0).getStartTime();
                //时间满足条件，将该项插入List中
                if(isQualifiedTime(departureTimeString)){
                    int originalSize = departureFlights.size();
                    departureFlights.add(tempEntry);
                    if (originalSize != 0) {
                        for (int i = 0; i < originalSize; i++) {
                            //找到非最后一位的插入位置
                            if (TimePair.compareTime(departureTimeString, departureFlights.get(i).getTime().get(0).getStartTime()) == 1) {
                                for (int j = originalSize - 1; j >= i; j--) {
                                    departureFlights.set(j + 1, departureFlights.get(j));
                                }
                                departureFlights.set(i, tempEntry);
                                break;
                            }
                        }
                    }
                }
            }
        }

        //设置实时变化的时间
        JLabel label = new JLabel();
        new Thread(new TimeOnBoard(label)).start();

        //设置抵达表格
        String[] title1 = {"抵达时间", "航班", "起止位置", "状态"};
        String[][] array1 = new String[arrivalFlights.size()][4];
        for(int i=0 ; i<arrivalFlights.size() ; i++){
            FlightEntry tempEntry = arrivalFlights.get(i);
            array1[i][0] = tempEntry.getTime().get(0).getEndTime();
            array1[i][1] = tempEntry.getPlanningEntryName();
            array1[i][2] = tempEntry.getLocationToString();
            array1[i][3] = tempEntry.getStatusToCharacters();
        }
        JTable table1 = new JTable(array1, title1);

        //设置出发表格
        String[] title2 = {"出发时间", "航班", "起止位置", "状态"};
        String[][] array2 = new String[departureFlights.size()][4];
        for(int i=0 ; i<departureFlights.size() ; i++){
            FlightEntry tempEntry = departureFlights.get(i);
            array2[i][0] = tempEntry.getTime().get(0).getStartTime();
            array2[i][1] = tempEntry.getPlanningEntryName();
            array2[i][2] = tempEntry.getLocationToString();
            array2[i][3] = tempEntry.getStatusToCharacters();
        }
        JTable table2 = new JTable(array2, title2);

        Container container = getContentPane();
        container.add(label, BorderLayout.NORTH);
        container.add(new JScrollPane(table1), BorderLayout.CENTER);
        container.add(new JScrollPane(table2), BorderLayout.SOUTH);
        setVisible(true);
    }

    /**
     * 获取Board上显示的所有计划项的迭代器，可以实现根据出发时间从早到晚的遍历
     * @return Board上计划项的迭代器
     */
    public Iterator<FlightEntry> iterator(){
        List<FlightEntry> listContainsAllEntries = new ArrayList<>();
        for(FlightEntry temp : arrivalFlights){
            listContainsAllEntries.add(temp);
        }
        for(FlightEntry temp : departureFlights){
            listContainsAllEntries.add(temp);
        }
        Collections.sort(listContainsAllEntries);
        return listContainsAllEntries.iterator();
    }

    /**
     * 检测输入时间是否在当前系统时间的一小时之内
     * @param inputTime 输入时间
     * @return 是否在当前时间的一小时之内
     */
    private boolean isQualifiedTime(String inputTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime input = LocalDateTime.parse(inputTime, formatter);
        Duration duration = Duration.between(LocalDateTime.now(), input);
        long hours = duration.toHours();
        if(hours>-1 && hours<1){
            return true;
        }
        return false;
    }

}
