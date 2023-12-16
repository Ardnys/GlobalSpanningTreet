import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Graph {
    private final Map<Integer, String> indexToCity = new HashMap<>();
    private final Map<String, Integer> cityToIndex = new HashMap<>();
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

    public void addIndex(int idx, String city) {
        // TODO: maybe this can be better but i messed it up yesterday
        indexToCity.put(idx, city);
        cityToIndex.put(city, idx);
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
    public String q1(String city1, String city2, String order) {
        var city1Idx = cityToIndex.get(city1);
        var destIdx = cityToIndex.get(city2);

        List<Transportation> transOrder = new ArrayList<>();


        var transports = Stream.of(order.split(" ")).
                filter(s -> !s.isEmpty()).
                map(String::trim).
                collect(Collectors.toList());
        for (var t : transports) {
            int times = Integer.parseInt(t.substring(1));
            String transType = t.substring(0, 1);
            Transportation type = null;
            switch (transType) {
                case "A" -> type = Transportation.AIRWAY;
                case "R" -> type = Transportation.RAILWAY;
                case "H" -> type = Transportation.HIGHWAY;
            }
            System.out.printf("(%s,%s) -> %d%n", transType, type, times);
            for (int i = 0; i < times; i++) {
                transOrder.add(type);
            }
        }
        for (var t : transOrder)  {
            System.out.printf("%s ", t);
        }
        System.out.println();
        Set<Integer> visited = new HashSet<>();


       boolean yes = cruisin(city1Idx, 0, transOrder, destIdx, visited);
        System.out.println(yes);


        return "";
    }
    private boolean cruisin(int cityIdx, int orderIdx, List<Transportation> order,
                            int destinationIdx, Set<Integer> visited) {
        if (cityIdx == destinationIdx && orderIdx == order.size()) return true;
        if (orderIdx >= order.size()) return false;
        System.out.println("wee");

        var trans = order.get(orderIdx);
        var neighs = adjMatrix.get(cityIdx);
        for (int i = 0; i < neighs.size(); i++) {
            if (orderIdx == order.size()-1) {
                if (neighs.get(i).contains(trans) && !visited.contains(i)) {
                    System.out.printf("cruisin in %s to %s by %s%n", indexToCity.get(cityIdx), indexToCity.get(i), trans);
                    visited.add(i);
                    if (cruisin(i, orderIdx+1, order, destinationIdx, visited)) {
                        return true;
                    } else {
                        visited.remove(i);
                        System.out.println("trying again");
                    }
                }
            } else {
                if (i != destinationIdx && neighs.get(i).contains(trans) && !visited.contains(i)) {
                    System.out.printf("cruisin in %s to %s by %s%n", indexToCity.get(cityIdx), indexToCity.get(i), trans);
                    visited.add(i);
                    if (cruisin(i, orderIdx+1, order, destinationIdx, visited)) {
                        return true;
                    } else {
                        visited.remove(i);
                        System.out.println("trying again");
                    }
                }
            }

        }

        return false;

    }
}
