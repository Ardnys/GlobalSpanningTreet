public class Main {
    public static void main(String[] args) {
        var inputPath = "src/transportation_network.inp";
        var commandFilePath = "src/query.inp";
        InputParser inputParser = new InputParser(inputPath);
        CommandParser commandParser = new CommandParser(commandFilePath);
        // absolutely perfectly readable code
        inputParser.parse().ifPresentOrElse(
                graph -> {
                    commandParser.parse().ifPresentOrElse(
                            graph::execute,
                            () -> {
                                System.err.println("Could not receive commands to execute");
                                System.exit(1);
                            }
                    );
                },
                () -> {
                    System.err.println("ERROR: Could not receive the graph.");
                    System.exit(1);
                });
    }
}
