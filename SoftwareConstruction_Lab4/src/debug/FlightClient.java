package debug;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Note that this class may use the other two class: Flight and Plane.
 * 
 * Debug and fix errors. DON'T change the initial logic of the code.
 *
 */
public class FlightClient {
	
	/**
	 * Given a list of flights and a list of planes, suppose each flight has not yet been
	 * allocated a plane to, this method tries to allocate a plane to each flight and ensures that
	 * there're no any time conflicts between all the allocations. 
	 * For example:
	 *  Flight 1 (2020-01-01 8:00-10:00) and Flight 2 (2020-01-01 9:50-10:40) are all allocated 
	 *  the same plane B0001, then there's conflict because from 9:50 to 10:00 the plane B0001
	 *  cannot serve for the two flights simultaneously.
	 *  
	 * @param planes a list of planes
	 * @param flights a list of flights each of which has no plane allocated
	 * @return false if there's no allocation solution that could avoid any conflicts
	 */
	
	public boolean planeAllocation(List<Plane> planes, List<Flight> flights) {
		boolean bFeasible = true;
		Random r = new Random();

		
		for (Flight f : flights) {
			boolean bAllocated = false;
			int count = 0;
			//如果分配过则考察下个计划项
			if(f.getPlane()!=null){
				bAllocated = true;
			}

			Calendar fStart = f.getDepartTime();
			Calendar fEnd = f.getArrivalTime();

			//如果未分配，尝试为其分配
			while (!bAllocated) {
				Plane p = planes.get(r.nextInt(planes.size()));
				boolean bConflict = false;
				for (Flight t : flights) {
					Plane q = t.getPlane();
					//如果两航班的飞机不相同
					if (t.equals(f) || q==null || !q.equals(p)){
						continue;
					}
					Calendar tStart = t.getDepartTime();
					Calendar tEnd = t.getArrivalTime();

					//如果时间冲突，则需要尝试其它飞机
					if (!(fEnd.before(tStart)) && !(tEnd.before(fStart))) {
						bConflict = true;
						count++;
						break;
					}
				}

				//如果没有冲突则为该航班分配飞机
				if (!bConflict) {
					f.setPlane(p);
					break;
				}

				//如果循环次数超过阈值则认为无法成功分配
				if(count > 100000){
					bFeasible = false;
					break;
				}
			}
		}
		return bFeasible;
	}
}
