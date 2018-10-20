package com.buildbroken.graph.planet.gui;

import com.buildbroken.graph.planet.data.Galaxy;
import com.buildbroken.graph.planet.gui.components.RenderPanel;
import com.buildbroken.graph.planet.logic.GalaxyBuilder;

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
        run.addActionListener(e -> {
            galaxy.clear();
            new GalaxyBuilder().generateData(galaxy);
            plotPanel.repaint();
        });
        panel.add(run);

        return panel;
    }


}
