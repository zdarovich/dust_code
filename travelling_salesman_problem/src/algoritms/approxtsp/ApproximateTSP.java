package algoritms.approxtsp;

import java.util.List;
import java.util.Map;

public class ApproximateTSP {

    /* Approximating search */
    public static int[] approximateSolution(int[][] coordinates) {
        int vertices = coordinates.length;
        int startNode = 0;

        Edge[] allEdges = Util.convertCoordinatesToEdges(coordinates);
        Map<String, Integer> edgesWeights = Util.getEdgesWeightMap(allEdges);

        Edge[] minSpanTree = Util.kruskalMinSpanTree(allEdges, vertices);
        Map<Integer, List<Integer>> adjacent = Util.getAdjacentList(minSpanTree, vertices);
        Map<Integer, List<Integer>> adjacentReversed = Util.reverseAdjacent(adjacent);

        int[] tempPath = Util.depthFirstTraverse(startNode, adjacent);
        int tempCost = Integer.MAX_VALUE;

        for (int i = 0; i < vertices; i++) {
            int temp = 0;

            int[] bestPath = Util.depthFirstTraverse(i, adjacent);

            int bestPathCost = Util.getPathCost(bestPath, edgesWeights);



            int[] bestPathReversed = Util.depthFirstTraverse(i, adjacentReversed);

            int bestPathReversedCost = Util.getPathCost(bestPathReversed, edgesWeights);


            bestPath = bestPathReversedCost < bestPathCost ? bestPathReversed : bestPath;
            temp = bestPathReversedCost < bestPathCost ? bestPathReversedCost : bestPathCost;
            if (temp < tempCost) {
                tempPath = bestPath;
                tempCost = temp;
            }
        }

        //tempPath = Util.bestPath(vertices, allEdges);
        //tempPath = Util.bestShortPath(tempPath, edgesWeights);
        return tempPath;
    }

    public static int[] approximateSolutionFromData(Edge[] allEdges, int vertices) {


        int startNode = 0;

        Edge[] minSpanTree = Util.kruskalMinSpanTree(allEdges, vertices);
        Map<String, Integer> edgesWeights = Util.getEdgesWeightMap(allEdges);



        Map<Integer, List<Integer>> adjacent = Util.getAdjacentList(minSpanTree, vertices);

        int[] bestPath = Util.depthFirstTraverse(startNode, adjacent);

        int bestPathCost = Util.getPathCost(bestPath, edgesWeights);



        Map<Integer, List<Integer>> adjacentReversed = Util.reverseAdjacent(adjacent);

        int[] bestPathReversed = Util.depthFirstTraverse(startNode, adjacentReversed);

        int bestPathReversedCost = Util.getPathCost(bestPathReversed, edgesWeights);



        bestPath = bestPathReversedCost < bestPathCost ? bestPathReversed : bestPath;


        return bestPath;
    }

    public static void main (String[] args) {
        /* Let us create following weighted graph
                 10         5
            0--------1----------5
            |  \     |          |
           6|   5\   |15        |9
            |      \ |          |
            2---4----3-----10---4
            |        | \        |
           4|      17|    \7    |12
            |        |       \  |
            6________7__________8
                4           2         */


        int V = 9;  // Number of vertices in graph
        int E = 14;  // Number of edges in graph

        Edge[] edges = new Edge[E];
        for (int i=0; i<E; ++i)
            edges[i] = new Edge();
        // add edge 0-1
        edges[0].setSource(0);
        edges[0].setDestination(1);
        edges[0].setWeight(10);

        // add edge 0-2
        edges[1].setSource(0);
        edges[1].setDestination(2);
        edges[1].setWeight(6);

        // add edge 0-3
        edges[2].setSource(0);
        edges[2].setDestination(3);
        edges[2].setWeight(5);

        // add edge 1-3
        edges[3].setSource(1);
        edges[3].setDestination(3);
        edges[3].setWeight(15);

        // add edge 2-3
        edges[4].setSource(2);
        edges[4].setDestination(3);
        edges[4].setWeight(4);

        // add edge 1-5
        edges[5].setSource(1);
        edges[5].setDestination(5);
        edges[5].setWeight(5);

        edges[6].setSource(3);
        edges[6].setDestination(4);
        edges[6].setWeight(10);

        edges[7].setSource(5);
        edges[7].setDestination(4);
        edges[7].setWeight(9);

        edges[8].setSource(4);
        edges[8].setDestination(8);
        edges[8].setWeight(12);

        edges[9].setSource(7);
        edges[9].setDestination(8);
        edges[9].setWeight(2);

        edges[10].setSource(3);
        edges[10].setDestination(7);
        edges[10].setWeight(17);

        edges[11].setSource(6);
        edges[11].setDestination(7);
        edges[11].setWeight(4);

        edges[12].setSource(2);
        edges[12].setDestination(6);
        edges[12].setWeight(4);

        edges[13].setSource(3);
        edges[13].setDestination(8);
        edges[13].setWeight(7);

        Edge[] minSpanTree = Util.kruskalMinSpanTree(edges, V);
        for (Edge edge: minSpanTree) {
            System.out.println(edge);
        }

        Map<Integer, List<Integer>> adjacent = Util.getAdjacentList(minSpanTree, V);

        int[] resultantCities = Util.depthFirstTraverse( 0, adjacent);
        /*
        Edge[] allEdges = Util.readEdgesFromFile("ee/ttu/algoritmid/approxtsp/tsp_data_opt_1019_v_237");
        int vertices = 237;
        int[] path = ApproximateTSP.approximateSolutionFromData(allEdges, vertices + 1);
        System.out.println(Util.getPathCost(path, allEdges));
*/
    }
}