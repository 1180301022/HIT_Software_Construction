package HelperClasses;

import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 表示Board上可变的时间的类
 * 可变类
 */
public class TimeOnBoard implements Runnable {
    private JLabel label;

    //AF：显示为label内容的时间
    //RI：true
    //Safety from rep exposure：成员域使用private修饰

    /**
     * 构造显示值可变的时间
     * @param inputLabel 输入标签，用于显示时间
     */
    public TimeOnBoard(JLabel inputLabel){
        label = inputLabel;
    }

    @Override
    public void run() {
        while (true){
            LocalDateTime dateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String nowTime = dateTime.format(formatter);
            label.setText("当前时间：" + nowTime);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
