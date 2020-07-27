package EntryState;

/**
 * 表示PlanningEntry的状态
 */
public enum State {
    WAITING,
    ALLOCATED,
    RUNNING,
    CANCELLED,
    ENDED,
    BLOCKED;
}
