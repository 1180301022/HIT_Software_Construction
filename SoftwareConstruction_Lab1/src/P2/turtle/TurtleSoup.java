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
        double angleBetweenPoints = Math.toDegrees(Math.atan2(targetY-currentY,targetX-currentX));//��ȡ����ʼ�㵽Ŀ���Ķ���
        double result = 90.0 - angleBetweenPoints - currentBearing;
        if(result<0)//�������Ϊ��������Ƕ�����360��
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
        //1.ѡȡ��������С�ĵ�Ϊ��ʼ��
        //2.�����ʼ��Ϊ�Ѽ��룬����������ҵ�˳ʱ��Ƕ���С�ĵ㣬ÿ��ѭ������������������е�
        //3.��Ǹõ�Ϊ��ʼ�㣬�ظ�2ֱ����һ����Ϊ�Ѽ���
        //����������������Ϊ0��1��2ʱ���������뼯��
        //�����ͬʱ��һ������ʱ������������һ����ʱ���ϣ��ڱ�����ɺ�����ü��ϵĵ�����Ƕ���С����ѡ��߳�����

        /*����Ԫ��Ϊ0 1 2 ��ʱ��ֱ�ӷ������뼯��*/
        if(points.size()<=2)
        {
            return points;
        }

        /*����Ԫ�ش���2ʱ*/
        HashSet<Point> result = new HashSet<Point>();//����������
        double startX = 1.0/0;//��ʼΪ�����
        double targetX=0;//Ŀ��������
        Point startP = null;//��ǰ��/��ʼ��
        Point targetP = null;//Ŀ���
        double minAngle = 1.0/0;//ÿ�αȽϵ���С�ĽǶ�
        double tempAngle=0;//ÿ�αȽϵĵ�ǰ�Ƕ�
        double heading = 0;//heading��¼��ǰ�ķ��򣬳�ʼΪ0

        for(Point p : points)//����Ѱ��x����Сֵ
        {
            if(p.x()-startX<0)//��ǰ���xֵС��startX
            {
                startX=p.x();
                startP=p;
            }
            else if(p.x()-startX<1e-6)//xֵ���ʱ��ȡyֵ�ϴ����Ϊ��ʼ��
            {
                if(p.y()-startP.y()>0)
                {
                    startX=p.x();
                    startP=p;
                }
            }
        }//��ʱ������е���x��С�ĵ�startP

        result.add(startP);//����һ����ʼ������������
        while(true)//ÿ�õ�һ������ʼ�㣬�������õ�֮������е㣬Ѱ��˳ʱ����Сֵ
        {
            for(Point p : points)
            {
                if(p.equals(startP))//������Ƚϵ����ʼ����ͬ��������
                {
                    continue;
                }
                tempAngle=calculateBearingToPoint(heading,(int)startP.x(),(int)startP.y(),(int)p.x(),(int)p.y());
                if(tempAngle-minAngle<0)//�Ƕ�С��minAngle
                {
                    targetP=p;
                    targetX=p.x();
                    minAngle=tempAngle;
                }
                else if(tempAngle-minAngle<1e-3)//�Ƕ�����Ҿ����Զʱ��ѡȡ��Զ�ĵ�
                {
                    double x1=Math.abs(startP.x()-targetP.x());
                    double y1=Math.abs(startP.y()-targetP.y());
                    double x2=Math.abs(startP.x()-p.x());
                    double y2=Math.abs(startP.y()-p.y());
                    if((y2*y2+x2*x2)>(y1*y1+x1*x1))
                    {
                        targetP=p;
                        targetX=p.x();
                    }
                }
            }

            //���ȼ���Ƿ��ڼ����У���������γ̻�·���˳�ѭ��
            if(result.contains(targetP))
            {
                break;
            }
            //���������targetP����������ϣ��޸�һϵ�е�start
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

            //PenColor�й���10����ɫ
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
        turtle.draw();
    }

}
