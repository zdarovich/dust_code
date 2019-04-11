package algoritms.approxtsp;

public class DisjointSubset {
    private Node[] subsets;

    public DisjointSubset(int vertices) {
        subsets = getNewSubsets(vertices);
    }

    private Node[] getNewSubsets(int vertices) {
        Node[] newSubsets = new Node[vertices];
        for (int i = 0; i < vertices; i++) {
            Node node = new Node();
            node.setCityParent(i);
            node.setRank(0);
            newSubsets[i] = node;
        }
        return newSubsets;
    }

    private int find(int setOfElement) {
        if (this.subsets[setOfElement].cityParent != setOfElement)
            this.subsets[setOfElement].cityParent = find(this.subsets[setOfElement].cityParent);

        return subsets[setOfElement].cityParent;
    }

    private void union(int x, int y) {
        int xroot = find(x);
        int yroot = find(y);

        if (this.subsets[xroot].rank < this.subsets[yroot].rank) {
            this.subsets[xroot].cityParent = yroot;
        } else if (this.subsets[xroot].rank > this.subsets[yroot].rank) {
            this.subsets[yroot].cityParent = xroot;

        } else {
            this.subsets[yroot].cityParent = xroot;
            this.subsets[xroot].rank++;
        }
    }

    public boolean isCycle(int src, int dest) {
        int x = find(src);
        int y = find(dest);

        if (x != y) {
            union(x, y);
            return false;
        }
        return true;
    }

    private class Node {
        private int cityParent;
        private int rank;

        public int getCityParent() {
            return cityParent;
        }

        public void setCityParent(int cityParent) {
            this.cityParent = cityParent;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "cityParent=" + cityParent +
                    ", rank=" + rank +
                    '}';
        }
    }

    public static void main(String[] args) {
        DisjointSubset disjointSubset = new DisjointSubset(7);
        disjointSubset.union(0,4);
        disjointSubset.union(5,1);
        disjointSubset.union(3,1);
        disjointSubset.union(3,6);
        disjointSubset.union(4,2);
        for (Node i: disjointSubset.subsets) {
            System.out.print(i.getCityParent()+1);
            System.out.print(" ");
        }
    }
}
