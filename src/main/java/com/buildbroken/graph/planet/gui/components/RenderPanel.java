package com.buildbroken.graph.planet.gui.components;


import com.buildbroken.graph.planet.data.Galaxy;
import com.buildbroken.graph.planet.data.StarSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Simple panel used to draw 2D plot points
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/18/2018.
 */
public class RenderPanel extends JPanel
{
    /** Data to display in the panel */
    protected final List<Consumer<Graphics2D>> rendersToRun = new ArrayList();
    protected final Galaxy galaxy;

    /** Spacing from each side */
    int PAD = 20;

    int plotSizeX = -1;
    int plotSizeY = -1;

    double plotLineSpacingX = -1;
    double plotLineSpacingY = -1;

    public RenderPanel(Galaxy galaxy)
    {
        this.galaxy = galaxy;
        addComponentListener(new ResizeListenerBoxSize());
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawBorder(g2);
        drawGrid(g2);
        drawRuler(g2);
        drawData(g2);
        drawExtras(g2);
    }

    /**
     * Draws a border around the component to define the edge
     *
     * @param g2
     */
    protected void drawBorder(Graphics2D g2)
    {
        g2.drawRect(1, 1, getWidth() - 2, getHeight() - 2); //TODO why -2?
    }

    protected void drawGrid(Graphics2D g2)
    {
        if (plotLineSpacingX > 0)
        {
            drawGridX(g2);
        }
        if (plotLineSpacingY > 0)
        {
            drawGridY(g2);
        }
    }

    protected void drawGridX(Graphics2D g2)
    {
        double start = 0;
        double end = getDrawMaxX();

        double current = start;
        double xScale = getScaleX();

        while (current < end)
        {
            //Increase
            current += plotLineSpacingX;

            //Get pixel point of x
            int x = PAD + (int) Math.ceil(current * xScale);

            //Draw line
            g2.draw(new Line2D.Double(x, PAD, x, getHeight() - PAD));
        }
    }

    protected void drawGridY(Graphics2D g2)
    {
        double start = 0;
        double end = getDrawMaxY();

        double current = start;
        double xScale = getScaleY();

        while (current < end)
        {
            //Increase
            current += plotLineSpacingY;

            //Get pixel point of x
            int y = PAD + (int) Math.ceil(current * xScale);

            //Draw line
            g2.draw(new Line2D.Double(PAD, y, getWidth() - PAD, y));
        }
    }

    /**
     * Draws the ruler
     *
     * @param g2
     */
    protected void drawRuler(Graphics2D g2)
    {
        //Left line
        g2.draw(new Line2D.Double(PAD, PAD, PAD, getHeight() - PAD));

        //Bottom line
        g2.draw(new Line2D.Double(PAD, getHeight() - PAD, getWidth() - PAD, getHeight() - PAD));
    }

    /**
     * Draws the data points
     *
     * @param g2
     */
    protected void drawData(Graphics2D g2)
    {
        if (galaxy != null)
        {
            //Render data points
            for (StarSystem pos : galaxy.starSystems)
            {
                drawCircle(g2, pos.renderColor, pos.x, pos.y, pos.renderSize, true);
            }
        }
    }

    public void drawCircle(Graphics2D g2, Color color, double point_x, double point_y, double size, boolean fill)
    {
        drawEllipse(g2, color, point_x, point_y, size, size, fill);
    }

    public void drawEllipse(Graphics2D g2, Color color, double point_x, double point_y, double size_x, double size_y, boolean fill)
    {
        //Calculate scale to fit display
        double scaleX = getScaleX();
        double scaleY = getScaleY();

        //Get pixel position
        double x = PAD + scaleX * point_x;
        double y = getHeight() - PAD - scaleY * point_y;

        if (x >= 0 && x <= getWidth() && y <= getHeight())
        {
            //Generate circle
            Ellipse2D circle = new Ellipse2D.Double(x - (size_x / 2), y - (size_y / 2), size_x, size_y);

            //Set color
            g2.setPaint(color != null ? color : Color.red);

            //Draw
            if (fill)
            {
                g2.fill(circle);
            }
            else
            {
                g2.draw(circle);
            }
        }
    }

    protected void drawExtras(Graphics2D g2)
    {
        rendersToRun.forEach(render -> render.accept(g2));
    }


    /**
     * Scale to draw the data on the screen.
     * <p>
     * Modifies the position to correspond to the pixel location
     *
     * @return scale of view ((width - padding) / size)
     */
    public double getScaleX()
    {
        return (double) (getWidth() - 2 * PAD) / getDrawMaxX();
    }

    /**
     * Scale to draw the data on the screen.
     * <p>
     * Modifies the position to correspond to the pixel location
     *
     * @return scale of view ((width - padding) / size)
     */
    public double getScaleY()
    {
        return (double) (getHeight() - 2 * PAD) / getDrawMaxY();
    }

    public double getDrawMaxX()
    {
        return plotSizeX > 0 ? plotSizeX : getPointMaxX();
    }

    public double getDrawMaxY()
    {
        return plotSizeY > 0 ? plotSizeY : getPointMaxY();
    }

    /**
     * Max y value in the data set
     *
     * @return
     */
    public double getPointMaxY()
    {
        double max = -Integer.MAX_VALUE;
        for (StarSystem pos : galaxy.starSystems)
        {
            if (pos.y() > max)
            {
                max = pos.y();
            }
        }
        return max;
    }

    /**
     * Max x value in the data set
     *
     * @return
     */
    public double getPointMaxX()
    {
        double max = -Integer.MAX_VALUE;
        for (StarSystem pos : galaxy.starSystems)
        {
            if (pos.x() > max)
            {
                max = pos.x();
            }
        }
        return max;
    }

    /**
     * Sets the plot size of the display.
     * <p>
     * By default the display will auto scale to match the data.
     * This can be used to ensure the data scales to a defined value.
     *
     * @param x
     * @param y
     */
    public void setPlotSize(int x, int y)
    {
        this.plotSizeY = y;
        this.plotSizeX = x;
    }

    public int getPlotSizeX()
    {
        return plotSizeX;
    }

    public int getPlotSizeY()
    {
        return plotSizeY;
    }

    public void drawLines(double i)
    {
        plotLineSpacingX = i;
        plotLineSpacingY = i;
    }

    public void drawLines(double x, double y)
    {
        plotLineSpacingX = x;
        plotLineSpacingY = y;
    }

    public void addRendersToRun(Consumer<Graphics2D> renderFunction)
    {
        rendersToRun.add(renderFunction);
    }
}