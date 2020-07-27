package PlanningEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * 表示多个资源的资源序列
 * 提供方法：设置资源，返回已设置资源
 * 可变类
 * @param <R> 具体资源类型
 */
public class MultipleSource<R> implements SourceType<R> {
    private List<R> source = new ArrayList<>();

    //AF：资源为source的资源序列
    //RI：true
    //Safety from rep exposure：成员域用private修饰，返回时采用防御性复制

    @Override
    public void setSource(List<R> inputSource) {
        source = new ArrayList<R>();
        for (R temp : inputSource) {
            source.add(temp);
        }
    }

    @Override
    public List<R> getSource(){
        List<R> sourceToReturn = new ArrayList<R>();
        for(R temp : source){
            sourceToReturn.add(temp);
        }
        return sourceToReturn;
    }

}
