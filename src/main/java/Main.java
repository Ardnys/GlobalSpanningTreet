public class Main {
    public static void main(String[] args) {
        var inputPath = "src/transportation_network.inp";
        InputParser parser = new InputParser(inputPath);
        Graph graph = parser.parse().orElseGet(() -> {
            System.err.println("ERROR: Could not receive the graph.");
            System.exit(1);
            return null;
        });
        graph.printGraph();
        System.out.println("------------------------");
//        graph.q1("City1", "City2", "H2 R1 A1");
        graph.q2("City1", "City2", 3);


    }
}
