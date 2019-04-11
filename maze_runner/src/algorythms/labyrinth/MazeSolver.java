package algorythms.labyrinth;

import java.util.*;
import java.io.IOException;
import java.net.URISyntaxException;

public class MazeSolver {
    private MazeRunner mazeRunner;

    /**
     * Array to hold all paths points.
     */
    Point[][] points;
    /**
     * String to hold optimal path directions.
     */
    String movesString = "";
    /**
     * Queue that holds unvisited paths sorted in descending order.
     */
    PriorityQueue<Point> pq = new PriorityQueue<>(1,new Comparator<Point>(){
        public int compare(Point a, Point b){
            return a.distance-b.distance;
        }
    });

    public MazeSolver(String fileName) throws IOException, URISyntaxException {
        mazeRunner = new MazeRunner(fileName);
        initialize();
    }

    public MazeRunner getMazeRunner() {
        return mazeRunner;
    }

    /**
     * Returns the list of steps to take to get from beginning ("B") to
     * the treasure ("T").
     * @return  return the steps taken as a list of strings (e.totalCostToNode., ["E", "E", "E"])
     *          return null if there is no path (there is no way to get to the treasure).
     */
    public List<String> solve() {
        bfsSearch();

        if (!movesString.isEmpty()) {

            return new ArrayList<>(Arrays.asList(movesString.split(",")));
        }
        return null;
    }

    /**
     * Initializes point array with the MAX VALUE.
     * Sets current position weight.
     * Settles queue for the first checking point.
     */
    public void initialize() {
        AbstractMap.SimpleEntry<Integer, Integer> currentPosition = mazeRunner.getPosition();
        points = new Point[mazeRunner.getSize().getKey()][mazeRunner.getSize().getValue()];
        for(int i=0;i<points.length;i++){
            for(int j=0;j<points[0].length;j++){
                points[i][j]=new Point(i,j,Integer.MAX_VALUE);
            }
        }
        points[currentPosition.getKey()][currentPosition.getValue()].distance = getCurrentWeight();


        pq.offer(points[currentPosition.getKey()][currentPosition.getValue()]);

    }

    /**
     * Algorithm defines 4 directions: North, South, West, East.
     * Polls possible optimal paths from queue and moves till the obstacle or treasure found in the one direction.
     * Tries 4 directions to find optimal solution.
     * Saves current most optimal path in the movesString variable.
     */
    public void bfsSearch() {
        String[] dirs = new String[]{"N", "S", "W", "E"};
        while (!pq.isEmpty()) {

            Point focusPoint = pq.poll(); //current point from which we start checking different directions
            for (String direction: dirs) {
                String moves = focusPoint.moves;
                int totalCost = 0; // total cost to the target
                for (String dir: new ArrayList<>(Arrays.asList(moves.split(","))) ) { // move to the checking point
                    mazeRunner.move(dir);
                }
                while (isPossibleToMove(direction) && mazeRunner.move(direction)) { // moves in the direction if possible
                    int currentPositionWeight = getCurrentWeight();
                    if (currentPositionWeight != -2) {       // if treasure not found add current weight
                        totalCost += currentPositionWeight;
                    }

                    moves += direction + ",";   //adds current direction to the temporary path
                    int currentX = mazeRunner.getPosition().getKey();
                    int currentY = mazeRunner.getPosition().getValue();

                    if (currentX == focusPoint.x &&     //if it is start there is no use of checking its weight
                            currentY == focusPoint.y) {
                        continue;
                    }
                    Point minPoint = points[currentX][currentY];
                    if (focusPoint.distance + totalCost < // if possible better distance weight, update points array
                            minPoint.distance) {
                        points[currentX][currentY].distance = focusPoint.distance + totalCost;
                        points[currentX][currentY].moves = moves;
                        if (currentPositionWeight == -2) {
                            //points[currentX][currentY].end = true;
                            movesString = points[currentX][currentY].moves;
                        }
                        minPoint.distance = focusPoint.distance + totalCost;
                        minPoint.moves = moves;
                        pq.offer(minPoint); // add next checking point to the queue
                    }
                }
                List<String> list = new ArrayList<>(Arrays.asList(moves.split(",")));
                Collections.reverse(list);
                for (String dir: list) { // return back to the start position to be able to move to the checking point in the next iteration
                    moveBack(dir);
                }

            }
        }
    }

    /**
     * Moves in the inverse direction of the given direction.
     * @param lastDirection from which to go back
     */
    public void moveBack(String lastDirection) {
        if (lastDirection.equals("N")) mazeRunner.move("S");
        else if (lastDirection.equals("S")) mazeRunner.move("N");
        else if (lastDirection.equals("W")) mazeRunner.move("E");
        else if (lastDirection.equals("E")) mazeRunner.move("W");
    }

    /**
     * Checks for the wall, start, end position. Allows to move or not.
     * @param direction that is checked for move
     * @return true or false for possibility to move in the direction
     */
    public boolean isPossibleToMove(String direction) {
        int weight = 0;
        if (direction.equals("N")) weight = getNorthWeight();
        else if (direction.equals("S")) weight = getSouthWeight();
        else if (direction.equals("W")) weight = getWestWeight();
        else if (direction.equals("E")) weight = getEastWeight();
        if (weight == -2) return true;
        else if (weight < 0) return false;
        return true;
    }

    /**
     * Gets north point of current position in the transparent maze.
     * @return weight of the north point from current position
     */
    public int getNorthWeight() {
        return mazeRunner.scan().get(1).get(0);
    }
    /**
     * Gets south point of current position in the transparent maze.
     * @return weight of the south point from current position
     */
    public int getSouthWeight() {
        return mazeRunner.scan().get(1).get(2);
    }
    /**
     * Gets west point of current position in the transparent maze.
     * @return weight of the west point from current position
     */
    public int getWestWeight() {
        return mazeRunner.scan().get(0).get(1);
    }
    /**
     * Gets east point of current position in the transparent maze.
     * @return weight of the east point from current position
     */
    public int getEastWeight() {
        return mazeRunner.scan().get(2).get(1);
    }
    /**
     * Gets current point of current position in the transparent maze.
     * @return weight of the current point from current position
     */
    public int getCurrentWeight() {
        return mazeRunner.scan().get(1).get(1);
    }


    public static void main(String[] args) {
        try {

            MazeSolver mazeSolver = new MazeSolver("maze2.txt");
            long startTime = System.currentTimeMillis();
            System.out.println(mazeSolver.solve());
            long endTime = System.currentTimeMillis();
            long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
            System.out.println(duration);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
