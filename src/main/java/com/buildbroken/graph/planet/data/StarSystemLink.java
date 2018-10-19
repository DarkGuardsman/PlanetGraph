package com.buildbroken.graph.planet.data;

import java.awt.*;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/18/2018.
 */
public class StarSystemLink
{
    public StarSystem a;
    public StarSystem b;

    public double weight;

    public Color renderColor = Color.YELLOW;
    public int renderSize = 2;

    public StarSystemLink(StarSystem a, StarSystem b)
    {
        this.a = a;
        this.b = b;
        weight = a.distance(b);
    }

    public StarSystemLink buildLink()
    {
        a.addLink(this);
        b.addLink(this);
        return this;
    }

    public StarSystemLink breakLink()
    {
        a.removeLink(this);
        b.removeLink(this);
        return this;
    }
}
