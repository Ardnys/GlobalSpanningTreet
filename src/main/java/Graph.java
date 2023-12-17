import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
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

    public void execute(Queue<Command> commandQueue) {
        // TODO: convert commands into things that can be executed in the functions here
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

    private String q1(String city1, String city2, String order) {
        var city1Idx = cityToIndex.get(city1);
        var destIdx = cityToIndex.get(city2);

        List<Transportation> transOrder = new ArrayList<>();

        var transports = Stream.of(order.split(" "))
                .filter(s -> !s.isEmpty())
                .map(String::trim)
                .toList();
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

        Stack<String> path = new Stack<>();

        path.add(indexToCity.get(city1Idx));
        boolean yes = cruisin(city1Idx, 0, transOrder, destIdx, path);
        System.out.printf("found: %b%nset: %nstr: %s%n", yes, path);

        // TODO: fix return statement
        return "";
    }

    private boolean cruisin(int cityIdx, int orderIdx, List<Transportation> order, int destinationIdx,
                            Stack<String> path) {
        if (cityIdx == destinationIdx && orderIdx == order.size()) return true;
        if (orderIdx >= order.size()) return false;

        var trans = order.get(orderIdx);
        var neighs = adjMatrix.get(cityIdx);
        for (int i = 0; i < neighs.size(); i++) {
            boolean validMove = neighs.get(i).contains(trans) && !path.contains(indexToCity.get(i));
            if (orderIdx == order.size() - 1 && validMove) {
//                System.out.printf("cruisin in %s to %s by %s%n", indexToCity.get(cityIdx), indexToCity.get(i), trans);
                path.add(indexToCity.get(i));

                if (cruisin(i, orderIdx + 1, order, destinationIdx, path)) {
                    return true;
                } else {
                    path.pop();
//                    System.out.println("trying again");
                }
            } else {
                if (i != destinationIdx && validMove) {
//                    System.out.printf("cruisin in %s to %s by %s%n", indexToCity.get(cityIdx), indexToCity.get(i), trans);
                    path.add(indexToCity.get(i));

                    if (cruisin(i, orderIdx + 1, order, destinationIdx, path)) {
                        return true;
                    } else {
                        path.pop();
//                        System.out.println("trying again");
                    }
                }
            }
        }
        path.pop();
        return false;
    }

    private List<String> q2(String city1, String city2, int nCities) {
        Stack<String> path = new Stack<>();
        List<String> paths = new ArrayList<>();

        path.add(city1);

        coastin(cityToIndex.get(city1), cityToIndex.get(city2), nCities, path, paths);
        System.out.println(String.join("\n", paths));

        return new ArrayList<>();
    }

    private void coastin(int cityIdx, int destinationIdx, int citiesLeft,
                         Stack<String> path, List<String> paths) {
        if (cityIdx == destinationIdx && citiesLeft == -1) {
            var pathStr = path.toString();
            paths.add(pathStr);
            path.pop();
            path.pop();
            return;
        }

        var neighs = adjMatrix.get(cityIdx);

        for (int i = 0; i < neighs.size(); i++) {
            if (path.contains(indexToCity.get(i))) continue;
            if (citiesLeft != 0 && i == destinationIdx) continue;
            var trans = neighs.get(i);
            for (var t : trans) {
                path.add(t.toString());
                path.add(indexToCity.get(i));
                int prevSize = paths.size();
                coastin(i, destinationIdx, citiesLeft - 1, path, paths);
                if (prevSize == paths.size()) {
                    path.pop();
                }
            }
        }

        path.pop();
        if (!path.isEmpty()) {
            path.pop();
        }
    }

    private List<String> q3(String city1, String city2, Transportation type) {
        Stack<String> path = new Stack<>();
        List<String> paths = new ArrayList<>();

        path.add(city1);

        driftin(cityToIndex.get(city1), cityToIndex.get(city2), type, path, paths);
        System.out.println(String.join("\n", paths));

        return new ArrayList<>();
    }

    private void driftin(int cityIdx, int destinationIdx, Transportation type,
                         Stack<String> path, List<String> paths) {
        if (cityIdx == destinationIdx) {
            var pathStr = path.toString();
            paths.add(pathStr);
            path.pop();
            return;
        }
        var neighs = adjMatrix.get(cityIdx);
        for (int i = 0; i < neighs.size(); i++) {
            if (path.contains(indexToCity.get(i))) continue;
            var trans = neighs.get(i).stream().filter(t -> t == type).toList();
            if (trans.isEmpty()) continue;
            path.add(indexToCity.get(i));
            int prevSize = paths.size();
            driftin(i, destinationIdx, type, path, paths);
            if (prevSize == paths.size()) path.pop();
        }
        path.pop();
    }
}
