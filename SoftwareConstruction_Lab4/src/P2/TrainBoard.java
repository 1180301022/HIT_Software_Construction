package P2;

import HelperClasses.Location;
import HelperClasses.TimeOnBoard;
import HelperClasses.TimePair;
import javax.swing.*;
import java.awt.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

/**
 *表示指定位置列车信息的类
 * 可变类
 */
public class TrainBoard extends JFrame {
    //存放符合要求的项目
    private List<TrainEntry> arrivalTrains = new ArrayList<>();
    private List<TrainEntry> departureTrains = new ArrayList<>();
    //存放符合要求的项目中，位置的索引
    private Map<TrainEntry, Integer> trainsLocationPosition = new HashMap<>();

    //AF：到达列表为arrivalFlights、出发列表为departureFlights的信息板
    //RI：true
    //Safety from rep exposure：成员域使用private修饰、创建迭代器时返回副本的迭代器

    /**
     * 创建并显示含有一小时内的到达列表和出发列表的Board
     * @param name 车站名
     * @param entries 计划项列表
     */
    public TrainBoard(String name, List<TrainEntry> entries){
        //设置窗口常规项目
        commonWindowSettings(name);

        //遍历entries，考察每个entry的所有Location，如果和输入相等则进一步操作
        for(TrainEntry tempEntry : entries){
            //因为需要知道位置的编号，所有采用循环变量
            for(int position=0 ; position<tempEntry.getLocation().size() ; position++){
                Location tempLocation = tempEntry.getLocation().get(position);
                //此时得到和输入相等的Location
                if(tempLocation.getLocationName().equals(name)){
                    //首先将位置索引和该计划项建立映射
                    trainsLocationPosition.put(tempEntry, position);

                    //如果是首个位置，则只考察首个时间对的出发时间
                    if(position==0){
                        String departureTimeString = tempEntry.getTime().get(0).getStartTime();//本计划项的出发时间
                        //如果符合则插入到合适位置
                        if(isQualifiedTime(departureTimeString)){
                            departureInsert(departureTimeString, tempEntry);
                        }
                    }

                    //如果是末尾位置，则只考察末尾时间对的到达时间
                    else if(position==tempEntry.getLocation().size()-1){
                        int lastLocationIndex = tempEntry.getLocation().size()-1;
                        String arrivalTimeString = tempEntry.getTime().get(lastLocationIndex-1).getEndTime();
                        //符合系统时间一小时内的约束
                        if(isQualifiedTime(arrivalTimeString)){
                            arrivalInsert(arrivalTimeString, tempEntry);
                            break;
                        }
                    }

                    //如果是中间位置，需要考虑前个时间对的到达时间和后个时间对的出发时间
                    else{
                        //考察出发时间
                        String departureTimeString = tempEntry.getTime().get(position).getStartTime();
                        if(isQualifiedTime(departureTimeString)){
                            departureInsert(departureTimeString, tempEntry);
                        }
                        //考察到达时间
                        String arrivalTimeString = tempEntry.getTime().get(position-1).getEndTime();
                        if(isQualifiedTime(arrivalTimeString)){
                            arrivalInsert(arrivalTimeString, tempEntry);
                        }
                    }
                    //一个计划项中只有一个位置能和指定位置相等，所有考察该位置后直接考察下个计划项
                    break;
                }
            }
        }

        //设置实时变化的时间
        JLabel label = new JLabel();
        new Thread(new TimeOnBoard(label)).start();

        //设置出发表格
        String[] title2 = {"出发时间", "车次", "起止位置", "状态"};
        String[][] array2 = new String[departureTrains.size()][4];
        for(int i=0 ; i<departureTrains.size() ; i++){
            TrainEntry tempEntry = departureTrains.get(i);
            array2[i][0] = tempEntry.getTime().get(trainsLocationPosition.get(tempEntry)).getStartTime();
            array2[i][1] = tempEntry.getPlanningEntryName();
            array2[i][2] = tempEntry.getLocationToString();
            array2[i][3] = tempEntry.getStatusToCharacters();
        }
        JTable table2 = new JTable(array2, title2);

        //设置抵达表格
        String[] title1 = {"抵达时间", "车次", "起止位置", "状态"};
        String[][] array1 = new String[arrivalTrains.size()][4];
        for(int i=0 ; i<arrivalTrains.size() ; i++){
            TrainEntry tempEntry = arrivalTrains.get(i);
            array1[i][0] = tempEntry.getTime().get(trainsLocationPosition.get(tempEntry)-1).getEndTime();
            array1[i][1] = tempEntry.getPlanningEntryName();
            array1[i][2] = tempEntry.getLocationToString();
            array1[i][3] = tempEntry.getStatusToCharacters();
        }
        JTable table1 = new JTable(array1, title1);

        Container container = getContentPane();
        container.add(label, BorderLayout.NORTH);
        container.add(new JScrollPane(table1), BorderLayout.CENTER);
        container.add(new JScrollPane(table2), BorderLayout.SOUTH);
        setVisible(true);
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

    /**
     * 窗口格式设置
     * @param name 地点
     */
    private void commonWindowSettings(String name){
        setTitle("当前车站："+name);
        setSize(1000, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    /**
     * 向出发列表中插入计划项，并保持从早到晚的顺序
     * @param departureTimeString 待插入的计划项出发时间
     * @param tempEntry 待插入计划项
     */
    private void departureInsert(String departureTimeString, TrainEntry tempEntry){
        int originalSize = departureTrains.size();
        departureTrains.add(tempEntry);
        //寻找合适的位置插入
        for(int i=0 ; i<originalSize ; i++){
            TrainEntry departureEntryInList = departureTrains.get(i);
            int departureEntryLocationPosition = trainsLocationPosition.get(departureEntryInList);
            if(TimePair.compareTime(departureTimeString,
                    departureEntryInList.getTime().get(departureEntryLocationPosition).getStartTime())==1){
                for (int j = originalSize - 1; j >= i; j--) {
                    departureTrains.set(j + 1, departureTrains.get(j));
                }
                departureTrains.set(i, tempEntry);
                break;
            }
        }
    }

    /**
     * 向到达列表中插入计划项，并保持从早到晚的顺序
     * @param arrivalTimeString 待插入的计划项到达时间
     * @param tempEntry 待插入计划项
     */
    private void arrivalInsert(String arrivalTimeString, TrainEntry tempEntry){
        int originalSize = arrivalTrains.size();
        arrivalTrains.add(tempEntry);
        //寻找合适的位置插入
        for(int i=0 ; i<originalSize ; i++){
            TrainEntry arrivalEntryInList = arrivalTrains.get(i);
            int arrivalEntryLocationPosition = trainsLocationPosition.get(arrivalEntryInList);
            if(TimePair.compareTime(arrivalTimeString,
                    arrivalEntryInList.getTime().get(arrivalEntryLocationPosition).getEndTime())==1){
                for (int j = originalSize - 1; j >= i; j--) {
                    arrivalTrains.set(j + 1, arrivalTrains.get(j));
                }
                arrivalTrains.set(i, tempEntry);
                return;
            }
        }
    }

    /**
     * 获取按照出发时间从早到晚的顺序遍历所有计划项的迭代器
     * @return TrainBoard上所有计划项的迭代器
     */
    public Iterator<TrainEntry> iterator(){
        List<TrainEntry> listContainsAllEntries = new ArrayList<>();
        for(TrainEntry temp : arrivalTrains){
            listContainsAllEntries.add(temp);
        }
        for(TrainEntry temp : departureTrains){
            listContainsAllEntries.add(temp);
        }
        Collections.sort(listContainsAllEntries);
        return listContainsAllEntries.iterator();
    }
}
