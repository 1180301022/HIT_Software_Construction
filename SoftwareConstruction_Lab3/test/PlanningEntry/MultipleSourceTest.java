package PlanningEntry;

import P2.Train;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MultipleSourceTest {
    /**
     * 测试MultipleSource类的方法
     * 考察输出是否等于设置值
     * 输入划分：和设置值相等，和设置值不等
     */
    @Test
    public void testMultipleSourceMethods(){
        Train train1 = new Train(1, "Type1", 100, 1.5);
        Train train2 = new Train(11, "Type2", 110, 3.1);
        Train train3 = new Train(10, "Type1", 100, 1.5);
        List<Train> list1 = new ArrayList<>();
        List<Train> list2 = new ArrayList<>();
        list1.add(train1);
        list1.add(train2);
        list2.add(train1);
        list2.add(train3);
        SourceType<Train> source = new MultipleSource<Train>();
        source.setSource(list1);

        for(int i=0 ; i<source.getSource().size() ; i++){
            if(!list1.get(i).equals(source.getSource().get(i))){
                assert false;
            }
        }

        boolean flag = false;
        for(int i=0 ; i<source.getSource().size() ; i++){
            if(!list2.get(i).equals(source.getSource().get(i))){
                flag = true;
            }
        }
        assert flag;
    }
}
