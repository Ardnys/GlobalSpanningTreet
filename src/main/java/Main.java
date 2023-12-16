public class Main {
    public static void main(String[] args) {
        var inputPath = "src/transportation_network.inp";
        InputParser parser = new InputParser(inputPath);
        Graph graph = parser.parse().orElseGet(() -> {
            System.err.println("ERROR: Could not receive the graph.");
            System.exit(1);
            return null;
        });
        graph.q1("City1", "City2", "   A1    R1 H2  ");


    }
}
