package PlanningEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 表示单个资源的类
 * 可变类
 * @param <R> 具体的资源信息
 */
public class SingleSource<R> implements SourceType<R> {
    private List<R> source = new ArrayList<>();

    //AF：资源为source的单个资源
    //RI：true
    //Safety from rep exposure：成员域用private修饰，返回时采用防御式复制

    @Override
    public void setSource(List<R> inputSource){
        if(inputSource.size() != 1){
            System.exit(1);
        }
        source = new ArrayList<R>();
        source.add(inputSource.get(0));
    }

    @Override
    public List<R> getSource(){
        List<R> sourceToReturn = new ArrayList<R>();
        if(source.size() == 1){
            sourceToReturn.add(source.get(0));
        }
        return sourceToReturn;
    }

}
