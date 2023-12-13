import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InputParser {
    private final String[] transportation = {"Airway", "Railway", "Highway"};
    Map<String, List<List<String>>> mapOfMatrices = new HashMap<>();
    List<String> indexes = new ArrayList<>();
    private String path = "";

    public InputParser(String path) {
        this.path = path;
    }

    public void printMatrices() {
        for (Map.Entry<String, List<List<String>>> entry : mapOfMatrices.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(String.join(" ", indexes));
            for (List<String> row : entry.getValue()) {
                System.out.println(String.join(" ", row));
            }
            System.out.println("------------");
        }
    }


    public void parse() {
        try (var reader = Files.newBufferedReader(Paths.get(path))) {
            String line;
            List<String> parsedLine = new ArrayList<>();
            List<List<String>> currentMatrix = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) continue;

                var tokens = Stream.of(line.split(" ")).
                        filter(s -> !s.isEmpty()).
                        map(String::trim).
                        collect(Collectors.toList());

                for (var token : tokens) {
                    if (Arrays.asList(transportation).contains(token)) {
                        currentMatrix = new ArrayList<>();
                        mapOfMatrices.put(token, currentMatrix);
                    } else {
                        if (token.equals("0") || token.equals("1")) {
                            parsedLine.add(token);
                        } else if (!token.equals("Cities") && !indexes.contains(token)){
                            indexes.add(token);
                        }
                    }
                }
                if (!parsedLine.isEmpty()) {
                    currentMatrix.add(new ArrayList<>(parsedLine));
                }
                parsedLine.clear();
            }
            printMatrices();


        } catch (IOException e) {
            System.err.println("ERROR: something went wrong while reading the file: " + e.getMessage());
        }
    }


}
