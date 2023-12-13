import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {
    private final Map<Integer, String> indexToCity = new HashMap<>();
    private final List<List<List<Transportation>>> adjMatrix = new ArrayList<>();
    private final int numberOfVertices;

    public Graph(int n) {
        numberOfVertices = n;
        for (int i = 0; i <  numberOfVertices; i++) {
            List<List<Transportation>> row = new ArrayList<>();
            for (int j = 0; j <  numberOfVertices; j++) {
                row.add(new ArrayList<>());
            }
            adjMatrix.add(row);
        }
    }

    public void addEdge(int i, int j, Transportation type) {
        if (i >= numberOfVertices || j >= numberOfVertices) {
            System.err.println("WARNING: Cannot insert outside the graph");
            return;
        }
        List<Transportation> edges = adjMatrix.get(i).get(j);
        edges.add(type);
    }
    public void printGraph() {
        for (int i = 0; i < adjMatrix.size(); i++) {
            var row = adjMatrix.get(i);
            for (int j = 0; j < row.size(); j++) {
                System.out.printf("(%d,%d) %s ", i, j, row.get(j));
            }
            System.out.println();
        }
    }
}
