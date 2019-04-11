package algoritms.approxtsp;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
public class Util {

    public static Edge[] kruskalMinSpanTree(Edge[] edgesToProccess, int vertices) {
        DisjointSubset subset = new DisjointSubset(vertices);
        Arrays.sort(edgesToProccess);

        int minAmountOfEdges = vertices - 1;
        int edgesAdded = 0;

        Edge[] minSpanTree = new Edge[minAmountOfEdges];

        for (Edge edge: edgesToProccess) {
            if (edgesAdded == minAmountOfEdges) {
                break;
            }

            if (!subset.isCycle(edge.getSource(), edge.getDestination())) {
                minSpanTree[edgesAdded++] = edge;
            } // else pass edge
        }
        return minSpanTree;
    }

    public static int[] christofides(Map<Integer, List<Integer>> adjacent, Map<String, Integer> edgesWeights, Edge[] minSpan, int N) {
        Set<Edge> minSpanTree = getSetFromMinSpanTree(minSpan);
        Set<Integer> oddVertices = getOddDegreeMinSpanningVertices(adjacent);   // Get odd degree vertices from MST
        Set<Edge> perfectMatching = getMinWeightPerfectMatching(oddVertices, edgesWeights);        // Get min-weight perfect matching
        Set<Edge> multiGraph = new HashSet<>();                         // Combine sets
        multiGraph.addAll(minSpanTree);
        multiGraph.addAll(perfectMatching);

        List<Integer> eulerian = findEulerianCircuit(multiGraph);

        return findHamiltonianCircuit(eulerian, edgesWeights);
    }

    public static int[] findHamiltonianCircuit(List<Integer> eulerian, Map<String, Integer> edgesWeights) {

        Stack<Integer> path = new Stack<Integer>();
        Set<Integer> usedVertices = new HashSet<>();
        Iterator<Integer> it = eulerian.iterator();

        int curCity = it.next();
        usedVertices.add(curCity);
        path.push(curCity);
        while(it.hasNext()){
            curCity = it.next();
            if(!usedVertices.contains(curCity))
            {
                path.push(curCity);
                usedVertices.add(curCity);
            }
        }
        int N = path.size();
        int[] cities = new int[N];
        for (int i = 0; i < N; i++) {
            cities[i] = path.get(i);
        }

        // Optimize path
        cities = optimizeHamiltonianPath(cities, edgesWeights, N);

        // Return to stack
        path.clear();
        for (int i = 0; i < cities.length; i++) {
            path.push(cities[i]);
        }
        int[] result = new int[path.size() + 1];
        for (int i = 0; i < path.size(); i++) {
            result[i] = path.get(i);
        }
        result[result.length - 1] = result[0];
        return result;
    }

    public static int[] optimizeHamiltonianPath(int[] cities, Map<String, Integer> edgesWeights, int N) {

        int lastDistance;

        do {

            lastDistance = getPathCost(cities, edgesWeights) ;

            // Remove edge to form path
            for (int i = 0; i < N; i++) {

                boolean changed = false;

                // Set maximumGain for current iteration
                int maximumGain = 0;
                int reverseFrom = 0;

                int x = i + 1;
                int y = i + 2;
                if (x >= N) x %= N;
                if (y >= N) y %= N;

                // Check each subsequent edge
                while (y != i) {

                    // Get difference from swapping edges
                    int diff = getDistance(cities[x], cities[y], edgesWeights) -
                            getDistance(cities[i], cities[x], edgesWeights);

                    // If improvement, save edge
                    if (diff > maximumGain) {
                        maximumGain = diff;
                        reverseFrom = y;
                    }

                    if (x + 1 >= N) x = 0;
                    else x++;
                    if (y + 1 >= N) y = 0;
                    else y++;
                }

                // If improvement found
                if (maximumGain > 0) {

                    // Create copy of array
                    int[] citiesCopy = Arrays.copyOf(cities, cities.length);
                    int reverseTo = i < reverseFrom ? i + N : i;

                    // Reverse section of path
                    while (reverseTo > reverseFrom) {
                        int temp = citiesCopy[reverseTo % N];
                        citiesCopy[reverseTo % N] = citiesCopy[reverseFrom % N];
                        citiesCopy[reverseFrom % N] = temp;
                        reverseTo--;
                        reverseFrom++;
                    }

                    // Check new distance against old
                    if (getPathCost(citiesCopy, edgesWeights) < getPathCost(cities, edgesWeights)) {
                        changed = true;
                        cities = citiesCopy;
                    }
                }

                if (changed) {
                    i--; // Repeat same loop
                }
            }
        } while (lastDistance > getPathCost(cities, edgesWeights));    // Repeat until no improvement made

        return cities;
    }

    public static List<Integer> findEulerianCircuit(Set<Edge> multigraph) {

        List<Integer> eulerian = new ArrayList<>();
        Stack<Integer> ids = new Stack<Integer>();
        Stack<Edge> edges = new Stack<Edge>();
        Iterator<Edge> it = multigraph.iterator();

        Edge e = it.next();
        ids.push(e.getSource());

        while(!multigraph.isEmpty()){

            if(e.getSource() == ids.peek()) {
                multigraph.remove(e);

                ids.push(e.getDestination());
                //reset iterator becuase removes can make it have a null reference
                it = multigraph.iterator();

            }
            else if(e.getDestination() == ids.peek()) {
                multigraph.remove(e);
                ids.push(e.getSource());
                //reset iterator becuase removes can make it have a null reference
                it = multigraph.iterator();
            }

            if(!it.hasNext()) {
                eulerian.add(0, ids.pop());
                it = multigraph.iterator();
            }

            if(it.hasNext()) {
                e = it.next();
            }
        }

        while(!ids.isEmpty())
        {
            eulerian.add(0, ids.pop());
        }

        return eulerian;
    }

    public static Set<Edge> getSetFromMinSpanTree(Edge[] minSpanTree) {
        Set<Edge> minSpan = new HashSet<>();
        for (Edge edge: minSpanTree) {
            minSpan.add(edge);
        }
        return minSpan;
    }

    public static int getDistance(int i, int j, Map<String, Integer> edgeWeights) {
        String key = i + "," + j;
        return edgeWeights.get(key);
    }

    public static Set<Edge> getMinWeightPerfectMatching(Set<Integer> vertices, Map<String, Integer> edgeWeights) {
        Set<Edge> perfectMatchingEdges = new HashSet<>();
        PriorityQueue<Edge> pq = new PriorityQueue<>();



        for (Integer i : vertices) {
            for (Integer j : vertices) {
                if (i != j) {
                    pq.add(new Edge(i, j, getDistance(i, j, edgeWeights)));
                }
            }
        }

        // Iterate until all vertices removed and matched
        while (!vertices.isEmpty()) {

            Edge edge;
            do {
                edge = pq.poll();
            } while ((!vertices.contains(edge.getDestination())) || (!vertices.contains(edge.getSource())));

            // Check if we can optimize pair better
            int biggestSwapDifference = 0;
            Edge edgeToSwap = null;
            for (Edge oldEdge : perfectMatchingEdges) {

                int pairDistance = oldEdge.getWeight() + edge.getWeight();
                int swapPairDistance1 = getDistance(oldEdge.getSource(), edge.getSource(), edgeWeights) + getDistance(oldEdge.getDestination(), edge.getDestination(), edgeWeights);
                int swapPairDistance2 = getDistance(oldEdge.getSource(), edge.getDestination(), edgeWeights) + getDistance(oldEdge.getDestination(), edge.getSource(), edgeWeights);

                if (swapPairDistance1 < swapPairDistance2 && swapPairDistance1 - pairDistance < biggestSwapDifference) {
                    biggestSwapDifference = swapPairDistance1 - pairDistance;
                    edgeToSwap = oldEdge;
                } else if (swapPairDistance2 - pairDistance < biggestSwapDifference) {
                    biggestSwapDifference = swapPairDistance2 - pairDistance;
                    edgeToSwap = oldEdge;
                }
            }

            // Swap edge for optimized edges
            if (biggestSwapDifference < 0 && edgeToSwap != null) {
                int id1 = edgeToSwap.getSource();
                int id2 = edgeToSwap.getDestination();
                int id3 = edge.getSource();
                int id4 = edge.getDestination();
                int swapPairDistance1 = getDistance(id1, id3, edgeWeights) + getDistance(id2, id4, edgeWeights);
                int swapPairDistance2 = getDistance(id1, id4, edgeWeights) + getDistance(id2, id3, edgeWeights);
                if (swapPairDistance1 < swapPairDistance2) {
                    perfectMatchingEdges.remove(edgeToSwap);
                    perfectMatchingEdges.add(new Edge(id1, id3, getDistance(id1, id3, edgeWeights)));
                    perfectMatchingEdges.add(new Edge(id2, id4, getDistance(id2, id4, edgeWeights)));
                }
                else {
                    perfectMatchingEdges.remove(edgeToSwap);
                    perfectMatchingEdges.add(new Edge(id1, id4, getDistance(id1, id4, edgeWeights)));
                    perfectMatchingEdges.add(new Edge(id2, id3, getDistance(id2, id3, edgeWeights)));
                }
            }

            // Add edge to set, remove both endpoints from vertices set
            if (biggestSwapDifference == 0)
                perfectMatchingEdges.add(edge);
            vertices.remove(edge.getSource());
            vertices.remove(edge.getDestination());
        }

        return perfectMatchingEdges;

    }


    public static Set<Integer> getOddDegreeMinSpanningVertices(Map<Integer, List<Integer>> adjacent) {

        Set<Integer> oddDegreeVertices = new HashSet<>();
        for (Map.Entry<Integer, List<Integer>> entry: adjacent.entrySet()) {
            if (entry.getValue().size() % 2 == 1) {
                oddDegreeVertices.add(entry.getKey());
            }
        }

        return oddDegreeVertices;
    }

    public static int[] depthFirstTraverse(int startNode, Map<Integer, List<Integer>> adjacent) {
        List<Integer> tour = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();

        dfs(startNode, adjacent, visited, tour);

        tour.add(startNode);

        int[] path = new int[tour.size()];
        for (int i = 0; i < tour.size(); i++) path[i] = tour.get(i);
        return path;
    }

    public static Map<String, Integer> getEdgesWeightMap(Edge[] pathWeights) {
        Map<String, Integer> edgesWeights = new HashMap<>();
        for (Edge weight: pathWeights) {
            edgesWeights.put(Integer.toString(weight.getSource()) + "," + Integer.toString(weight.getDestination()), weight.getWeight());
            edgesWeights.put(Integer.toString(weight.getDestination()) + "," + Integer.toString(weight.getSource()), weight.getWeight());
        }
        return edgesWeights;
    }

    public static int getPathCost(int[] tour, Map<String, Integer> edgesWeights) {
        int pathCost = 0;
        for (int i = 0; i < tour.length - 1; i++) {
            String key = tour[i] + "," + tour[i + 1];
            Integer weight = edgesWeights.get(key);
            if (weight != null) {
                pathCost += weight;
            } else {
                System.out.println("Could not find weight for " + key);
            }
        }
        return pathCost;
    }

    public static Map<Integer, List<Integer>> reverseAdjacent(Map<Integer, List<Integer>> adjacent) {
        Map<Integer, List<Integer>> reversed = new HashMap<>();
        adjacent.forEach((integer, integers) -> reversed.put(integer, reverseList(integers)));
        return reversed;
    }

    public static<T> List<T> reverseList(List<T> list) {
        return IntStream.range(0, list.size())
                .map(i -> (list.size() - 1 - i))    // IntStream
                .mapToObj(list::get)                // Stream<T>
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static void dfs(int node, Map<Integer, List<Integer>> adjacentList, Set<Integer> visited, List<Integer> preOrder) {
        preOrder.add(node);
        visited.add(node);
        for (Integer adjacent : adjacentList.get(node)) {
            if (!visited.contains(adjacent)) {
                dfs(adjacent, adjacentList, visited, preOrder);
            }
        }
    }

    public static Map<Integer, List<Integer>> getAdjacentList(Edge[] minSpanTree, int vertices) {
        Map<Integer, List<Integer>> adjacent = new HashMap<>();
        for (int i = 0; i < vertices; i++) {
            adjacent.put(i, new ArrayList<>());
        }

        for (Edge edge: minSpanTree) {
            adjacent.get(edge.getSource()).add(edge.getDestination());
            adjacent.get(edge.getDestination()).add(edge.getSource());
        }
        return adjacent;
    }

    public static Edge[] convertCoordinatesToEdges(int[][] coordinates) {
        int n = coordinates.length;
        List<Edge> edges = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                Edge edge = new Edge();
                edge.setSource(i);
                edge.setDestination(j);
                int x1 = coordinates[i][0];
                int y1 = coordinates[i][1];
                int x2 = coordinates[j][0];
                int y2 = coordinates[j][1];
                int distance = measureDistance(x1, y1, x2, y2);
                edge.setWeight(distance);
                edges.add(edge);
            }
        }
        Edge[] result = new Edge[edges.size()];
        for (int i = 0; i < edges.size(); i++) {
            result[i] = edges.get(i);
        }
        return result;
    }


    public static int measureDistance(int x1, int y1, int x2, int y2) {
        return (int) Math.round(Math.sqrt(Math.pow((x2-x1),2)+Math.pow((y2-y1),2)));
    }

    public static void addEdge(List<Edge> list, String line) {
        String[] split = line.split(" ");
        Edge edge = new Edge();
        edge.setSource(Integer.parseInt(split[0]));
        edge.setDestination(Integer.parseInt(split[1]));
        edge.setWeight(Integer.parseInt(split[2]));
        list.add(edge);
    }

    public static Edge[] readEdgesFromFile(String file) {
        List<Edge> edges = new ArrayList<>();
        Path path;
        try {
            path = Paths.get(Util.class.getClassLoader()
                    .getResource(file).toURI());
            Files.lines(path).forEach((line) -> addEdge(edges, line));

        } catch (URISyntaxException | NullPointerException | IOException e) {
            e.printStackTrace();
        }
        Edge[] result = new Edge[edges.size()];
        for (int i = 0; i < edges.size(); i++) {
            result[i] = edges.get(i);
        }
        return result;
    }



    public static void main(String[] args) {
        System.out.println("1 nodes");

        int[][] coord = new int[1][2];
        coord[0][0] = 1;
        coord[0][1] = 3;
        ApproximateTSP.approximateSolution(coord);
        System.out.println("2 nodes");

        coord = new int[2][2];
        coord[0][0] = 1;
        coord[0][1] = 3;
        coord[1][0] = 3;
        coord[1][1] = 1;
        ApproximateTSP.approximateSolution(coord);
        System.out.println("4 nodes");

        coord = new int[4][2];
        coord[0][0] = 1;
        coord[0][1] = 3;
        coord[1][0] = 3;
        coord[1][1] = 1;
        coord[2][0] = 3;
        coord[2][1] = 3;
        coord[3][0] = 6;
        coord[3][1] = 3;
        ApproximateTSP.approximateSolution(coord);
        coord = new int[1000][2];
        Random rand = new Random();
        int max = 30;
        int offset = 10;
        for (int i = 0; i < coord.length; i++) {
            int myValue1 = rand.nextInt(max-offset)+offset;
            int myValue2 = rand.nextInt(max-offset)+offset;
            coord[i][0] = myValue1;
            coord[i][1] = myValue2;
        }
        coord[0][0] = 1;
        coord[0][1] = 2;
        coord[1][0] = 3;
        coord[1][1] = 1;
        coord[2][0] = 4;
        coord[2][1] = 3;
        coord[3][0] = 2;
        coord[3][1] = 4;
        coord[4][0] = 3;
        coord[4][1] = 6;
        coord[5][0] = 900;
        coord[5][1] = 3;
        coord[6][0] = 3;
        coord[6][1] = 300;
        coord[7][0] = 1245;
        coord[7][1] = 100;
        coord[8][0] = 500;
        coord[8][1] = 6;
        coord[9][0] = 123;
        coord[9][1] = 12312;
        coord[10][0] = 123;
        coord[10][1] = 12312;
        System.out.println("1000 nodes");
        long current = System.currentTimeMillis();
        ApproximateTSP.approximateSolution(coord);
        System.out.println(System.currentTimeMillis() - current);
    }
}