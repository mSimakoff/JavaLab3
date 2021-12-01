

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ForkJoinWorkerThread;

/**
 * This class stores the basic state necessary for the A* algorithm to compute a
 * path across a map.  This state includes a collection of "open waypoints" and
 * another collection of "closed waypoints."  In addition, this class provides
 * the basic operations that the A* pathfinding algorithm needs to perform its
 * processing.
 **/
public class AStarState
{
    /** This is a reference to the map that the A* algorithm is navigating. **/
    private Map2D map;

    /*
    Вершины хранится в хэш-карте, где местоположение вершин является ключом, а сами вершины являются значениями.
    openWaypoints - открытые вершины
    closeWaypoints - закрытые вершины
     */
    private HashMap<Location, Waypoint> openWaypoints = new HashMap<>();
    private HashMap<Location, Waypoint> closeWaypoints = new HashMap<>();

    /**
     * Initialize a new state object for the A* pathfinding algorithm to use.
     **/
    public AStarState(Map2D map)
    {
        if (map == null)
            throw new NullPointerException("map cannot be null");

        this.map = map;
    }

    /** Returns the map that the A* pathfinder is navigating. **/
    public Map2D getMap()
    {
        return map;
    }

    /*
     Эта функция должна проверяет все вершины в наборе открытых вершин,
     и после этого она возвращает ссылку на вершину с наименьшей общей
     стоимостью. Если в "открытом" наборе нет вершин, функция возвращает NULL.
     */
    public Waypoint getMinOpenWaypoint() {
        if (numOpenWaypoints() == 0) {
            return null;
        }

        Waypoint minWaypoint = null;
        float min = Float.MAX_VALUE;

        for (Waypoint waypoint : openWaypoints.values()) {
            float cost = waypoint.getTotalCost();
            if (cost < min) {
                min = cost;
                minWaypoint = waypoint;
            }
        }
        return minWaypoint;
    }

    /*
     * Если в наборе «открытых вершин» в настоящее время нет вершины
     для данного местоположения, то необходимо просто добавить новую вершину.
     * Если в наборе «открытых вершин» уже есть вершина для этой локации, добавьте новую вершину
     только в том случае, если стоимость пути до
     новой вершины меньше стоимости пути до текущей. (Убедитесь, что
     используете не общую стоимость.) Другими словами, если путь через новую
     вершину короче, чем путь через текущую вершину, замените текущую вершину
     на новую.
     */
    public boolean addOpenWaypoint(Waypoint newWP)
    {
        Waypoint openWP = openWaypoints.get(newWP.loc);

        if (openWP == null || newWP.getPreviousCost() < openWP.getPreviousCost()) {
            openWaypoints.put(newWP.loc, newWP);
            return true;
        }
        return false;
    }


    // Этот метод возвращает количество точек в наборе открытых вершин.
    public int numOpenWaypoints() {
        return openWaypoints.size();
    }


    /*
     Эта функция перемещает вершину из набора «открытых вершин» в набор «закрытых вершин».
     Так как вершины обозначены местоположением, метод принимает местоположение вершины.
     */
    public void closeWaypoint(Location loc)
    {
        Waypoint waypoint = openWaypoints.remove(loc);
        if (openWaypoints != null) {
            closeWaypoints.put(loc, waypoint);
        }
    }

    /*
     Эта функция должна возвращать значение true, если указанное местоположение встречается
     в наборе закрытых вершин, и false в противном случае.
     Так как закрытые вершины хранятся в хэш-карте с расположениями в качестве
     ключевых значений, данный метод достаточно просто в реализации:
     */
    public boolean isLocationClosed(Location loc)
    {
        return closeWaypoints.containsKey(loc);
    }
}

