/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package turtle;

import jdk.jfr.Unsigned;

import javax.swing.text.AbstractDocument;
import java.util.*;

public class TurtleSoup {

    /**
     * Draw a square.
     * 
     * @param turtle the turtle context
     * @param sideLength length of each side
     */
    public static void drawSquare(Turtle turtle, int sideLength)
    {
        turtle.forward(sideLength);
        turtle.turn(90);
        turtle.forward(sideLength);
        turtle.turn(90);
        turtle.forward(sideLength);
        turtle.turn(90);
        turtle.forward(sideLength);
    }

    /**
     * Determine inside angles of a regular polygon.
     * 
     * There is a simple formula for calculating the inside angles of a polygon;
     * you should derive it and use it here.
     * 
     * @param sides number of sides, where sides must be > 2
     * @return angle in degrees, where 0 <= angle < 360
     */
    public static double calculateRegularPolygonAngle(int sides)
    {
        if(sides<=2)
        {
            System.out.println("Input Error!");
            return -1;
        }
        else
        {
            double sum=(sides-2)*180.0;
            return sum/sides;
        }
    }

    /**
     * Determine number of sides given the size of interior angles of a regular polygon.
     * 
     * There is a simple formula for this; you should derive it and use it here.
     * Make sure you *properly round* the answer before you return it (see java.lang.Math).
     * HINT: it is easier if you think about the exterior angles.
     * 
     * @param angle size of interior angles in degrees, where 0 < angle < 180
     * @return the integer number of sides
     */
    public static int calculatePolygonSidesFromAngle(double angle)
    {
        double exAngle = 180-angle;
        return (int)Math.round(360/exAngle);
    }

    /**
     * Given the number of sides, draw a regular polygon.
     * 
     * (0,0) is the lower-left corner of the polygon; use only right-hand turns to draw.
     * 
     * @param turtle the turtle context
     * @param sides number of sides of the polygon to draw
     * @param sideLength length of each side
     */
    public static void drawRegularPolygon(Turtle turtle, int sides, int sideLength)
    {
        double angle = calculateRegularPolygonAngle(sides);
        for(int i=0;i<sides;i++)
        {
            turtle.turn(180-angle);
            turtle.forward(sideLength);
        }
    }

    /**
     * Given the current direction, current location, and a target location, calculate the Bearing
     * towards the target point.
     * 
     * The return value is the angle input to turn() that would point the turtle in the direction of
     * the target point (targetX,targetY), given that the turtle is already at the point
     * (currentX,currentY) and is facing at angle currentBearing. The angle must be expressed in
     * degrees, where 0 <= angle < 360. 
     *
     * HINT: look at http://en.wikipedia.org/wiki/Atan2 and Java's math libraries
     * 
     * @param currentBearing current direction as clockwise from north
     * @param currentX current location x-coordinate
     * @param currentY current location y-coordinate
     * @param targetX target point x-coordinate
     * @param targetY target point y-coordinate
     * @return adjustment to Bearing (right turn amount) to get to target point,
     *         must be 0 <= angle < 360
     */
    public static double calculateBearingToPoint(double currentBearing, int currentX, int currentY,
                                                 int targetX, int targetY)
    {
        double angleBetweenPoints = Math.toDegrees(Math.atan2(targetY-currentY,targetX-currentX));//获取从起始点到目标点的度数
        double result = 90.0 - angleBetweenPoints - currentBearing;
        if(result<0)//如果所得为负数，则角度增加360°
        {
            result+=360;
        }
        return result;
    }

    /**
     * Given a sequence of points, calculate the Bearing adjustments needed to get from each point
     * to the next.
     * 
     * Assumes that the turtle starts at the first point given, facing up (i.e. 0 degrees).
     * For each subsequent point, assumes that the turtle is still facing in the direction it was
     * facing when it moved to the previous point.
     * You should use calculateBearingToPoint() to implement this function.
     * 
     * @param xCoords list of x-coordinates (must be same length as yCoords)
     * @param yCoords list of y-coordinates (must be same length as xCoords)
     * @return list of Bearing adjustments between points, of size 0 if (# of points) == 0,
     *         otherwise of size (# of points) - 1
     */
    public static List<Double> calculateBearings(List<Integer> xCoords, List<Integer> yCoords)
    {
        List<Double> angleAdjustments = new ArrayList<Double>();
        double heading = 0;
        double toAdjust;
        for(int i=0;i<xCoords.size()-1;i++)
        {
            toAdjust= calculateBearingToPoint(heading,xCoords.get(i),yCoords.get(i),xCoords.get(i+1),yCoords.get(i+1));
            heading+=toAdjust;
            if(heading-360>0)
            {
                heading -= 360;
            }
            angleAdjustments.add(toAdjust);
        }
        return angleAdjustments;
    }
    
    /**
     * Given a set of points, compute the convex hull, the smallest convex set that contains all the points 
     * in a set of input points. The gift-wrapping algorithm is one simple approach to this problem, and 
     * there are other algorithms too.
     * 
     * @param points a set of points with xCoords and yCoords. It might be empty, contain only 1 point, two points or more.
     * @return minimal subset of the input points that form the vertices of the perimeter of the convex hull
     */
    public static Set<Point> convexHull(Set<Point> points)
    {
        //1.选取横坐标最小的点为起始点
        //2.标记起始点为已加入，从其余点中找到顺时针角度最小的点，每次循环遍历除自身外的所有点
        //3.标记该点为起始点，重复2直到下一个点为已加入
        //特殊情况：输入点数为0，1，2，3时，返回输入集合
                    //多个点同时在一条边上时，创建并加入一个临时集合，在遍历完成后如果该集合的点满足角度最小，则选择边长最大的

        /*输入元素为0 1 2 个时，直接返回输入集合*/
        if(points.size()<=2)
        {
            return points;
        }

        /*输入元素大于2时*/
        HashSet<Point> result = new HashSet<Point>();//输出结果集合
        double startX = 1.0/0;//初始为无穷大
        double targetX=0;//目标点横坐标
        Point startP = null;//当前点/起始点
        Point targetP = null;//目标点
        double minAngle = 1.0/0;//每次比较的最小的角度
        double tempAngle=0;//每次比较的当前角度
        double heading = 90;//heading记录当前的方向，初始为90

        for(Point p : points)//遍历寻找x的最小值
        {
            if(p.x()-startX<0)//当前点的x值小于startX
            {
                startX=p.x();
                startP=p;
            }
        }//此时获得所有点中x最小的点startP

        result.add(startP);//将第一个起始点加入输出集合
        while(true)//每得到一个新起始点，遍历除该点之外的所有点，寻找顺时针最小值
        {
            for(Point p : points)
            {
                if(p.equals(startP))//如果待比较点和起始点相同，则跳过
                {
                    continue;
                }
                tempAngle=calculateBearingToPoint(heading,(int)startP.x(),(int)startP.y(),(int)p.x(),(int)p.y());
                if(tempAngle-minAngle<0)//角度小于minAngle
                {
                    targetP=p;
                    targetX=p.x();
                    minAngle=tempAngle;
                }
                else if((tempAngle-minAngle<1e-6)&&(Math.abs(startP.x()-p.x())-Math.abs(startP.x()-targetP.x())>0))//角度相等且距离更远时，选取更远的点
                {
                    targetP=p;
                    targetX=p.x();
                }
            }

            //首先检测是否在集合中，如果在则形程环路，退出循环
            if(result.contains(targetP))
            {
                break;
            }
            //常规情况：targetP不在输出集合，修改一系列的start
            result.add(targetP);
            startP=targetP;
            startX=targetX;
            heading+=minAngle;
            if(heading-360>0)
            {
                heading-=360;
            }
            minAngle=1.0/0;
        }
        return result;
    }
    
    /**
     * Draw your personal, custom art.
     * 
     * Many interesting images can be drawn using the simple implementation of a turtle.  For this
     * function, draw something interesting; the complexity can be as little or as much as you want.
     * 
     * @param turtle the turtle context
     */
    public static void drawPersonalArt(Turtle turtle)
    {
        for(int i=1;i<=70;i++)
        {
            drawRegularPolygon(turtle,100,i%10);
            turtle.turn(5);

            //PenColor中共有10种颜色
            switch (i%10)
            {
                case 0:
                    turtle.color(PenColor.BLACK);
                    break;
                case 1:
                    turtle.color(PenColor.BLUE);
                    break;
                case 2:
                    turtle.color(PenColor.CYAN);
                    break;
                case 3:
                    turtle.color(PenColor.GRAY);
                    break;
                case 4:
                    turtle.color(PenColor.GREEN);
                    break;
                case 5:
                    turtle.color(PenColor.MAGENTA);
                    break;
                case 6:
                    turtle.color(PenColor.ORANGE);
                    break;
                case 7:
                    turtle.color(PenColor.PINK);
                    break;
                case 8:
                    turtle.color(PenColor.RED);
                    break;
                case 9:
                    turtle.color(PenColor.YELLOW);
                    break;
            }

        }

    }

    /**
     * Main method.
     * 
     * This is the method that runs when you run "java TurtleSoup".
     * 
     * @param args unused
     */
    public static void main(String args[])
    {
        DrawableTurtle turtle = new DrawableTurtle();

        drawPersonalArt(turtle);

        //drawSquare(turtle,80);
        turtle.draw();
    }

}
