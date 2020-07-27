package P4;

import HelperClasses.TimeOnBoard;
import HelperClasses.TimePair;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 *表示指定位置课程信息的类
 * 可变类
 */
public class CourseBoard extends JFrame {
    private List<CourseEntry> coursesToday = new ArrayList<>();

    //AF：显示课程为coursesToday的信息板
    //RI：true
    //Safety from rep exposure：成员域使用private修饰、创建迭代器时返回副本的迭代器

    /**
     * 创建新的指定位置课程的信息板
     * @param classroom 指定教室
     * @param entries 课程列表
     */
    public CourseBoard(String classroom, List<CourseEntry> entries){
        //获取当前时间
        String nowTime = getLocalTime();
        int nowYear = TimePair.getYear(nowTime.toCharArray());
        int nowMonth = TimePair.getMonth(nowTime.toCharArray());
        int nowDay = TimePair.getDay(nowTime.toCharArray());

        //设置窗口常规项目
        commonWindowSettings(classroom);

        //遍历输入计划项，寻找在本日且在本教室的课程并加入到coursesToday
        for(CourseEntry tempEntry : entries){
            //相同教室
            if(tempEntry.getLocationToString().equals(classroom)){
                int tempYear = TimePair.getYear(tempEntry.getTime().get(0).getStartTime().toCharArray());
                int tempMonth = TimePair.getMonth(tempEntry.getTime().get(0).getStartTime().toCharArray());
                int tempDay = TimePair.getDay(tempEntry.getTime().get(0).getStartTime().toCharArray());
                //相同日期，则加入到board
                if(nowYear==tempYear && nowMonth==tempMonth && nowDay==tempDay){
                    coursesToday.add(tempEntry);
                }
            }
        }

        //设置实时变化的时间
        JLabel label = new JLabel();
        new Thread(new TimeOnBoard(label)).start();

        //设置表格
        String[] title1 = {"上课时间", "课程内容", "教师", "状态"};
        String[][] array1 = new String[coursesToday.size()][4];
        Iterator<CourseEntry> iterator = iterator();
        for(int i=0 ; i<coursesToday.size() ; i++){
            CourseEntry tempEntry = iterator.next();
            array1[i][0] = tempEntry.getTime().get(0).toString();
            array1[i][1] = tempEntry.getPlanningEntryName();
            if(tempEntry.getSource().size()==0){
                array1[i][2] = "暂无";
            }
            else {
                array1[i][2] = tempEntry.getSource().get(0).getName();
            }
            array1[i][3] = tempEntry.getStatusToCharacters();
        }
        JTable table1 = new JTable(array1, title1);
        Container container = getContentPane();
        container.add(label, BorderLayout.NORTH);
        container.add(new JScrollPane(table1), BorderLayout.CENTER);
        setVisible(true);
    }

    /**
     * 获取当前系统时间
     * @return 当前系统时间
     */
    private String getLocalTime(){
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return dateTime.format(formatter);
    }

    /**
     * 窗口格式设置
     * @param name 地点
     */
    private void commonWindowSettings(String name){
        setTitle("当前教室："+name);
        setSize(1000, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    /**
     * 获取按照出发时间从早到晚的顺序遍历所有计划项的迭代器
     * @return CourseBoard上所有计划项的迭代器
     */
    public Iterator<CourseEntry> iterator(){
        List<CourseEntry> toGenerateIterator = new ArrayList<>();
        for(CourseEntry temp : coursesToday){
            toGenerateIterator.add(temp);
        }
        Collections.sort(toGenerateIterator);
        return toGenerateIterator.iterator();
    }
}
