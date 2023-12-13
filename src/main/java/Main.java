public class Main {
    public static void main(String[] args) {
        var inputPath = "src/transportation_network.inp";
        InputParser parser = new InputParser(inputPath);
        parser.parse();

    }
}
