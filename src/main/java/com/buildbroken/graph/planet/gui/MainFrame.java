package com.buildbroken.graph.planet.gui;

import com.buildbroken.graph.planet.data.Galaxy;
import com.buildbroken.graph.planet.data.StarSystem;
import com.buildbroken.graph.planet.data.StarSystemLink;
import com.buildbroken.graph.planet.gui.components.RenderPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/18/2018.
 */
public class MainFrame extends JFrame
{
    private RenderPanel plotPanel;

    private Galaxy galaxy;

    public MainFrame()
    {
        //Set frame properties
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setMinimumSize(new Dimension(800, 800));
        setLocation(200, 200);
        setTitle("Planet Graph - Prototype");

        galaxy = new Galaxy(250, 250);

        add(buildCenter());

        pack();
    }

    protected JPanel buildCenter()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(plotPanel = new RenderPanel(galaxy), BorderLayout.CENTER);
        plotPanel.setMinimumSize(new Dimension(600, 600));
        plotPanel.setPreferredSize(new Dimension(600, 600));
        //plotPanel.drawLines(10, 10);
        plotPanel.setPlotSize(600, 600);
        plotPanel.setPlotOffset(50, 50);

        panel.add(buildControls(), BorderLayout.WEST);
        return panel;
    }

    protected JPanel buildControls()
    {
        JPanel panel = new JPanel();

        Button run = new Button("Generate Data");
        run.addActionListener(e -> generateData());
        panel.add(run);

        run = new Button("Test Data");
        run.addActionListener(e -> testData());
        panel.add(run);

        return panel;
    }

    protected void testData()
    {
        galaxy.clear();
        galaxy.starSystems.add(new StarSystem(10, 10, Color.RED));
        galaxy.starSystems.add(new StarSystem(100, 100, Color.GREEN));
        galaxy.links.add(new StarSystemLink(galaxy.starSystems.get(0), galaxy.starSystems.get(1)));
    }

    protected void generateData()
    {
        final int minStarDistance = 5;
        final int starLinkDistance = 25;

        System.out.println("MainFrame#generateData() - generating data");

        galaxy.clear();

        //==========================
        //Generate stars
        //==========================
        while (galaxy.starSystems.size() < 1000)
        {
            //Randomize position
            double angle = Math.random() * 2 * Math.PI;
            double range = (Math.random() * 250);
            double x = range * Math.sin(angle) + 250;
            double y = range * Math.cos(angle) + 250;

            //Create system
            StarSystem system = new StarSystem(x, y, randomColor());
            system.renderSize = 10 + (int) Math.random() * 5;

            //Limit distance
            double distance = system.distance(250, 250);
            if (distance > 250)
            {
                System.out.println("\t- to far from center, " + system);
            }
            else if (distance > 50)
            {
                //Prevent stars from stacking on each other
                if (!galaxy.areAnyStarsNear(x, y, minStarDistance))
                {
                    System.out.println("\t- added " + system);
                    galaxy.starSystems.add(system);
                }
                else
                {
                    System.out.println("\t- to close to other stars, " + system);
                }
            }
            else
            {
                System.out.println("\t- to close to center, " + system);
            }
        }

        //==========================
        //Build connections
        //==========================
        LinkedList<StarSystem> systemsToPath = new LinkedList();
        systemsToPath.addAll(galaxy.starSystems);

        while(systemsToPath.peek() != null)
        {
            StarSystem star = systemsToPath.poll();

            //Find nearby stars
            ArrayList<StarSystem> possibleConnections = new ArrayList();
            for(StarSystem s : galaxy.starSystems)
            {
                if(s != star && s.distance(star.x, star.y) <= starLinkDistance)
                {
                    possibleConnections.add(s);
                }
            }

            Collections.sort(possibleConnections, Comparator.comparingDouble(o -> o.distanceSQ(star.x, star.y)));
            int linksToMake = 1 + (int) (Math.random() * 4);
            for(int i = 0; i < possibleConnections.size() && i < linksToMake; i++)
            {
                galaxy.links.add(new StarSystemLink(possibleConnections.get(i), star));
            }
        }

        this.repaint();
    }

    protected Color randomColor()
    {
        return new Color((int) Math.floor(Math.random() * 255), (int) Math.floor(Math.random() * 255), (int) Math.floor(Math.random() * 255));
    }
}
