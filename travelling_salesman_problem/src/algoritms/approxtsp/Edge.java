package algoritms.approxtsp;

public class Edge implements Comparable<Edge> {
    private int source, destination, weight;
    public Edge() {
    }

    public Edge(int source, int destination, int weight) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    public int compareTo(Edge compareEdge) {
        if (this.weight != compareEdge.weight) {
            return this.weight - compareEdge.weight;
        } else if (this.destination != compareEdge.destination) {
            return this.source - compareEdge.source;
        } else {
            return this.destination - compareEdge.destination;
        }
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getDestination() {
        return destination;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "weight=" + weight +
                ", source=" + source +
                ", destination=" + destination +
                '}';
    }
}