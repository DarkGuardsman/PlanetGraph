package com.buildbroken.graph.planet.gui;

import com.buildbroken.graph.planet.data.Galaxy;
import com.buildbroken.graph.planet.data.StarSystem;
import com.buildbroken.graph.planet.gui.components.RenderPanel;

import javax.swing.*;
import java.awt.*;

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
        plotPanel.drawLines(10, 10);
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
        return panel;
    }

    protected void generateData()
    {
        System.out.println("MainFrame#generateData() - generating data");
        galaxy.clear();
        while (galaxy.starSystems.size() < 1000)
        {
            double x = Math.random() * 500;
            double y = Math.random() * 500;
            StarSystem system = new StarSystem(x, y, randomColor());

            double distance = system.distance(250, 250);

            if (distance > 250)
            {
                System.out.println("\t- to far from center, " + system);
            }
            else if (distance > 50)
            {
                if (!galaxy.areAnyStarsNear(x, y, 5))
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
    }

    protected Color randomColor()
    {
        return new Color((int) Math.floor(Math.random() * 255), (int) Math.floor(Math.random() * 255), (int) Math.floor(Math.random() * 255));
    }
}
