package com.buildbroken.graph.planet.data;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/18/2018.
 */
public class Point
{
    public final double x;
    public final double y;

    public Point(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public double distanceSQ(Point point)
    {
        return distanceSQ(point.x, point.y);
    }

    public double distanceSQ(double x, double y)
    {
        double deltaX = this.x - x;
        double deltaY = this.y - y;
        return deltaX * deltaX + deltaY * deltaY;
    }

    public double distance(Point point)
    {
        return Math.sqrt(distanceSQ(point));
    }

    public double distance(double x, double y)
    {
        return Math.sqrt(distanceSQ(x, y));
    }

    public double x()
    {
        return x;
    }

    public double y()
    {
        return y;
    }
}
