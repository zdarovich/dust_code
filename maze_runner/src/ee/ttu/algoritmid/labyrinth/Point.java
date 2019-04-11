package ee.ttu.algoritmid.labyrinth;

/**
 * Endpoint of some possible path.
 * Holds instruction how to get to this adn its distance.
 */
public class Point {
    /**
     * X coordinate of the checking point
     */
    int x;
    /**
     * Y coordinate of the checking point
     */
    int y;
    /**
     * Distance from start to the checking point
     */
    int distance;
    /**
     * Moves from start to the checking point
     */
    String moves = "";

    public Point(int x, int y, int dis){
        this.x = x;
        this.y = y;
        this.distance = dis;
    }
}
