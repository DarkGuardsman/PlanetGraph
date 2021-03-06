package com.buildbroken.graph.planet.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/18/2018.
 */
public class Galaxy extends Point
{
    public final List<StarSystemLink> links = new ArrayList();
    public final List<StarSystem> starSystems = new ArrayList();

    public Galaxy(double x, double y)
    {
        super(x, y);
    }

    public boolean areAnyStarsNear(double x, double y, double distance)
    {
        for (StarSystem object : starSystems)
        {
            if (object.distance(x, y) <= distance)
            {
                return true;
            }
        }
        return false;
    }


    public void clear()
    {
        links.clear();
        starSystems.clear();
    }
}
