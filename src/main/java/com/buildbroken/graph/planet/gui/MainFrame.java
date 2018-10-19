package com.buildbroken.graph.planet.gui;

import com.buildbroken.graph.planet.data.Galaxy;
import com.buildbroken.graph.planet.data.StarSystem;
import com.buildbroken.graph.planet.data.StarSystemLink;
import com.buildbroken.graph.planet.gui.components.RenderPanel;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

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
        final int starLinkDistance = 15;
        final int spawnRadius = 250;
        final int minimalSpawnRadius = 50;
        int maxClusterLinkDistance = 50;

        System.out.println("MainFrame#generateData() - generating data");

        galaxy.clear();

        //==========================
        //Generate stars
        //==========================
        while (galaxy.starSystems.size() < 1000)
        {
            //Randomize position
            double angle = Math.random() * 2 * Math.PI;
            double range = (Math.random() * spawnRadius);
            double x = range * Math.sin(angle) + spawnRadius;
            double y = range * Math.cos(angle) + spawnRadius;

            //Create system
            StarSystem system = new StarSystem(x, y, randomColor());
            system.renderSize = 10 + (int) Math.random() * 5;

            //Limit distance
            double distance = system.distance(spawnRadius, spawnRadius);
            if (distance > spawnRadius)
            {
                System.out.println("\t- to far from center, " + system);
            }
            else if (distance > minimalSpawnRadius)
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

        while (systemsToPath.peek() != null)
        {
            StarSystem star = systemsToPath.poll();

            //Find nearby stars
            ArrayList<StarSystem> possibleConnections = new ArrayList();
            for (StarSystem s : galaxy.starSystems)
            {
                if (s != star && s.distance(star.x, star.y) <= starLinkDistance)
                {
                    possibleConnections.add(s);
                }
            }

            Collections.sort(possibleConnections, Comparator.comparingDouble(o -> o.distanceSQ(star.x, star.y)));
            int linksToMake = 1 + (int) (Math.random() * 4);
            for (int i = 0; i < possibleConnections.size() && i < linksToMake; i++)
            {
                galaxy.links.add(new StarSystemLink(possibleConnections.get(i), star).buildLink());
            }
        }

        //TODO remove overlap links

        //Build clusters
        List<HashSet<StarSystem>> clusters = buildClusters(galaxy.starSystems);
        int removeTicks = 0;
        while (clusters.size() > 1)
        {
            if (tryToJoinClusters(clusters.get(0), clusters.get(1), maxClusterLinkDistance))
            {
                clusters.get(0).addAll(clusters.get(1));
                clusters.remove(clusters.get(1));
                removeTicks = 0;
            }
            else
            {
                removeTicks++;
            }

            //Prevent just merging first 2 entries
            Collections.shuffle(clusters);

            if(removeTicks >= 20)
            {
                removeTicks = 0;
                maxClusterLinkDistance += 10;
            }
        }

        //Build links between clusters

        this.repaint();
    }

    protected boolean tryToJoinClusters(HashSet<StarSystem> clusterA, HashSet<StarSystem> clusterB, double maxDistance)
    {
        StarSystem starA = null;
        StarSystem starB = null;
        double distance = 0;
        for (StarSystem starSystemA : clusterA)
        {
            for (StarSystem starSystemB : clusterB)
            {
                final double d = starSystemA.distanceSQ(starSystemB);
                if (d < maxDistance && (starA == null || d < distance))
                {
                    starA = starSystemA;
                    starB = starSystemB;
                    distance = d;
                }
            }
        }

        if (starA != null)
        {
            galaxy.links.add(new StarSystemLink(starA, starB).buildLink());
            return true;
        }
        return false;
    }

    protected List<HashSet<StarSystem>> buildClusters(List<StarSystem> galaxyStars)
    {
        List<HashSet<StarSystem>> clusters = new ArrayList();
        HashSet<StarSystem> pathedStars = new HashSet();
        for (StarSystem system : galaxyStars)
        {
            if (!pathedStars.contains(system))
            {
                //Create hash set to track systems in cluster
                HashSet<StarSystem> systems = new HashSet();
                systems.add(system);

                //Map out all links to create cluster
                collectStarsOnPath(systems, system);

                //Prevent pathing the systems again
                pathedStars.addAll(systems);

                //Add to cluster list
                clusters.add(systems);

                Color color = randomColor();
                for (StarSystem s : systems)
                {
                    for (StarSystemLink link : s.links)
                    {
                        link.renderColor = color;
                    }
                }
            }
        }
        return clusters;
    }

    protected void collectStarsOnPath(HashSet<StarSystem> systems, StarSystem star)
    {
        for (StarSystemLink link : star.links)
        {
            if (link.a != star && !systems.contains(link.a))
            {
                systems.add(link.a);
                collectStarsOnPath(systems, link.a);
            }
            else if (link.b != star && !systems.contains(link.b))
            {
                systems.add(link.b);
                collectStarsOnPath(systems, link.b);
            }
        }
    }

    protected boolean isPartOfCluster(List<StarSystem> systems, StarSystem star)
    {
        for (StarSystem system : systems)
        {
            if (system.hasLink(star))
            {
                return true;
            }
        }
        return false;
    }

    protected Color randomColor()
    {
        return new Color((int) Math.floor(Math.random() * 255), (int) Math.floor(Math.random() * 255), (int) Math.floor(Math.random() * 255));
    }
}
