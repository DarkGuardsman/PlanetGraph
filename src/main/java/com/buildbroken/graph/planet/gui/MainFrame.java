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

        galaxy = new Galaxy();

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
        plotPanel.drawLines(1, 1);

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
        galaxy.clear();
        for (int i = 0; i < 1000; i++)
        {
            StarSystem system = new StarSystem(Math.random() * 500, Math.random() * 500, randomColor());
            galaxy.starSystems.add(system);
        }
    }

    protected Color randomColor()
    {
        return new Color((int) Math.floor(Math.random() * 255), (int) Math.floor(Math.random() * 255), (int) Math.floor(Math.random() * 255));
    }
}
