package PlanningEntry;

import java.util.List;

/**
 * 表示计划项中资源种类的接口
 * @param <R> 资源具体种类
 */
public interface SourceType<R> {
    /**
     * 计划开始前为其设置资源
     * @param inputSource 需要设置的资源
     */
    public void setSource(List<R> inputSource);

    /**
     * 获取已设置的资源
     * @return 已设置的一系列资源
     */
    public List<R> getSource();
}
