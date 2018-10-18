package com.buildbroken.graph.planet.data;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 2D data point with color
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/27/2018.
 */
public class StarSystem
{
    public final double x;
    public final double y;

    public final List<StarSystemObject> objects = new ArrayList();

    public Color renderColor;
    public int renderSize = 4;


    public StarSystem(double x, double y, Color color)
    {
        this.x = x;
        this.y = y;
        this.renderColor = color;
    }

    public StarSystem(double x, double y, Color color, int size)
    {
        this(x, y, color);
        this.renderSize = size;
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
