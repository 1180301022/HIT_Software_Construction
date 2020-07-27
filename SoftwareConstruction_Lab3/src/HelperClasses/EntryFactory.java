package HelperClasses;

import P1.FlightEntry;
import P2.TrainEntry;
import P4.CourseEntry;
import PlanningEntry.PlanningEntry;

/**
 * 构造PlanningEntry子类的的工厂类
 */
public class EntryFactory {
    public static PlanningEntry manufacture(String type, String name){
        switch (type){
            case "flight" : return new FlightEntry(name);
            case "train": return new TrainEntry(name);
            case "course" : return new CourseEntry(name);
            default: return null;
        }
    }
}
