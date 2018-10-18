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
public class StarSystem extends Point
{
    public final List<StarSystemObject> objects = new ArrayList();
    public final List<StarSystemLink> links = new ArrayList();

    public Color renderColor;
    public int renderSize = 10;


    public StarSystem(double x, double y, Color color)
    {
        super(x, y);
        this.renderColor = color;
    }

    public StarSystem(double x, double y, Color color, int size)
    {
        this(x, y, color);
        this.renderSize = size;
    }

    public String toString()
    {
        return "Star[" + x + ", " + y + "]";
    }

    public void addLink(StarSystemLink starSystemLink)
    {
        links.add(starSystemLink);
    }

    public void removeLink(StarSystemLink starSystemLink)
    {
        links.remove(starSystemLink);
    }

    public boolean hasLink(StarSystem star)
    {
        for(StarSystemLink link : links)
        {
            if(link.a == star || link.b == star)
            {
                return true;
            }
        }
        return false;
    }
}
