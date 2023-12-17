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
        for (int i = 0; i < numberOfVertices; i++) {
            List<List<Transportation>> row = new ArrayList<>();
            for (int j = 0; j < numberOfVertices; j++) {
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

        var transports = Stream.of(order.split(" ")).filter(s -> !s.isEmpty()).map(String::trim).collect(Collectors.toList());
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
        for (var t : transOrder) {
            System.out.printf("%s ", t);
        }
        System.out.println();

        Set<Integer> visited = new HashSet<>();
        Stack<String> path = new Stack<>();

        visited.add(city1Idx);
        path.add(indexToCity.get(city1Idx));
        boolean yes = cruisin(city1Idx, 0, transOrder, destIdx, visited, path);
        System.out.printf("found: %b%nset: %s%nstr: %s%n", yes, visited, path);

        // TODO: fix return statement
        return "";
    }

    private boolean cruisin(int cityIdx, int orderIdx, List<Transportation> order, int destinationIdx,
                            Set<Integer> visited, Stack<String> path) {
        // TODO: tidy up the arguments, maybe remove visited and just use path?
        if (cityIdx == destinationIdx && orderIdx == order.size()) return true;
        if (orderIdx >= order.size()) return false;

        var trans = order.get(orderIdx);
        var neighs = adjMatrix.get(cityIdx);
        for (int i = 0; i < neighs.size(); i++) {
            boolean validMove = neighs.get(i).contains(trans) && !visited.contains(i);
            if (orderIdx == order.size() - 1 && validMove) {
                System.out.printf("cruisin in %s to %s by %s%n", indexToCity.get(cityIdx), indexToCity.get(i), trans);
                visited.add(i);
                path.add(indexToCity.get(i));
                System.out.println("visited: " + visited);

                if (cruisin(i, orderIdx + 1, order, destinationIdx, visited, path)) {
                    return true;
                } else {
                    visited.remove(i);
                    System.out.println("trying again");
                }
            } else {
                if (i != destinationIdx && validMove) {
                    System.out.printf("cruisin in %s to %s by %s%n", indexToCity.get(cityIdx), indexToCity.get(i), trans);
                    visited.add(i);
                    path.add(indexToCity.get(i));
                    System.out.println("visited: " + visited);

                    if (cruisin(i, orderIdx + 1, order, destinationIdx, visited, path)) {
                        return true;
                    } else {
                        visited.remove(i);
                        System.out.println("trying again");
                    }
                }
            }
        }
        visited.remove(cityIdx);
        path.pop();
        return false;
    }
    public List<String> q2(String city1, String city2, int nCities) {
        Set<Integer> visited = new HashSet<>();
        Stack<String> path = new Stack<>();
        List<String> paths = new ArrayList<>();

        visited.add(cityToIndex.get(city1));
        path.add(city1);

        coastin(cityToIndex.get(city1), cityToIndex.get(city2), nCities, visited, path, paths);
        System.out.println(String.join("\n", paths));

        return new ArrayList<>();
    }

    private void coastin(int cityIdx, int destinationIdx, int citiesLeft,
                            Set<Integer> visited, Stack<String> path, List<String> paths) {
        if (cityIdx == destinationIdx && citiesLeft == -1) {
            var pathStr = path.toString();
            paths.add(pathStr);
            path.pop();
            path.pop();
            visited.remove(destinationIdx);
            return;
        }

        var neighs = adjMatrix.get(cityIdx);

        for (int i = 0; i < neighs.size(); i++) {
            // maybe check if it's already visited
            if (visited.contains(i)) continue;
            if (citiesLeft != 0 && i == destinationIdx) continue;
            var trans = neighs.get(i);
            for (var t : trans) {
                // stuff
                visited.add(i);
                path.add(t.toString());
                path.add(indexToCity.get(i));
                int prevSize = paths.size();
                coastin(i, destinationIdx, citiesLeft-1, visited, path, paths);
                if (prevSize == paths.size()) {
                    // could not find
                    visited.remove(i);
                }
            }
        }

        visited.remove(cityIdx);
        path.pop();
        if (!path.isEmpty()) {
            path.pop();
        }
    }
}
