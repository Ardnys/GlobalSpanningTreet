import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Stream;


public class CommandParser {
    private static final Map<String, CommandType> commandMap = new HashMap<>();

    static {
        commandMap.put("Q1", CommandType.TRANSFER);
        commandMap.put("Q2", CommandType.NCITIES);
        commandMap.put("Q3", CommandType.TYPE);
        commandMap.put("TRANSFER", CommandType.TRANSFER);
        commandMap.put("NCITIES", CommandType.NCITIES);
        commandMap.put("TYPE", CommandType.TYPE);
    }

    private final String filePath;

    public CommandParser(String filePath) {
        this.filePath = filePath;
    }

    public Optional<Queue<Command>> parse() {
        Queue<Command> commandQueue = new ArrayDeque<>();
        try (var reader = Files.newBufferedReader(Paths.get(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) continue;

                var tokens = Stream.of(line.split(" ")).
                        filter(s -> !s.isEmpty()).
                        map(String::trim).
                        toList();

                var userCommand = tokens.get(0);
                if (tokens.size() < 3) {
                    System.err.println("Not enough arguments in the command " + userCommand);
                    continue;
                } else if (tokens.size() > 6) {
                    System.err.println("Too many arguments in the command " + userCommand);
                    continue;
                }
                resolveTokens(tokens, userCommand, commandQueue);
            }
        } catch (IOException e) {
            System.err.println("Error while reading the command file.");
            return Optional.empty();
        }
        return Optional.of(commandQueue);
    }

    private void resolveTokens(List<String> tokens, String userCommand, Queue<Command> commandQueue) {
        CommandType commandType = commandMap.getOrDefault(userCommand.toUpperCase(), CommandType.NONE);
        String fromCity = tokens.get(1);
        String toCity = tokens.get(2);

        switch (commandType) {
            case TRANSFER -> {
                List<String> args = tokens.subList(3, tokens.size());
                Command command = new Command(commandType, fromCity, toCity, args);
                commandQueue.add(command);
//                System.out.println("Added command: " + command);
            }
            case TYPE -> {
                if (tokens.size() > 4) {
                    System.err.printf("Too many arguments in %s %s. Only %s is taken.%n", userCommand, tokens.subList(3, tokens.size()), tokens.get(3));
                    // it's not a breaking error so I think I can deal with it like this
                }
                var type = tokens.get(3).toUpperCase();
                var trans = switch (type) {
                    case "A", "AIRWAY", "AIR" -> Optional.of(Transportation.AIRWAY);
                    case "R", "RAILWAY", "RAIL" -> Optional.of(Transportation.RAILWAY);
                    case "H", "HIGHWAY", "HIGH" -> Optional.of(Transportation.HIGHWAY);
                    default -> {
                        System.err.println("Invalid transportation type in " + userCommand + " command");
                        yield Optional.empty();
                    }
                };
                trans.ifPresentOrElse(t -> {
                    Command command = new Command(commandType, fromCity, toCity, List.of(trans.toString()));
                    commandQueue.add(command);
                }, () -> {
                });
                // the reason why I spend all the effort to convert the type to proper transportation is to
                // reduce the complexity of the execution. It's going to be complicated somewhere. Let it be
                // before it's converted to Command type.
//                System.out.println("Added command: " + command);
            }
            case NCITIES -> {
                if (tokens.size() > 4) {
                    System.err.printf("Too many arguments in %s %s. Only %s is taken.%n", userCommand, tokens.subList(3, tokens.size()), tokens.get(3));
                }
                try {
                    var n = Integer.parseInt(tokens.get(3));
                    Command command = new Command(commandType, fromCity, toCity, List.of(String.valueOf(n)));
                    commandQueue.add(command);
//                    System.out.println("Added command: " + command);
                    // again, parsed it just to make it graceful. maybe it's stupid but it's late at night
                    // and I am FEELING it
                } catch (NumberFormatException e) {
                    System.err.println("Expected an integer in " + userCommand + " but found " + tokens.get(3));
                }
            }
            case NONE -> {
                System.err.println("Invalid command " + userCommand);
                var projectLabel = """
                         _____ _       _           _   _____                         _               _____             _  \s
                        |  __ \\ |     | |         | | /  ___|                       (_)             |_   _|           | | \s
                        | |  \\/ | ___ | |__   __ _| | \\ `--. _ __   __ _ _ __  _ __  _ _ __   __ _    | |_ __ ___  ___| |_\s
                        | | __| |/ _ \\| '_ \\ / _` | |  `--. \\ '_ \\ / _` | '_ \\| '_ \\| | '_ \\ / _` |   | | '__/ _ \\/ _ \\ __|
                        | |_\\ \\ | (_) | |_) | (_| | | /\\__/ / |_) | (_| | | | | | | | | | | | (_| |   | | | |  __/  __/ |_\s
                         \\____/_|\\___/|_.__/ \\__,_|_| \\____/| .__/ \\__,_|_| |_|_| |_|_|_| |_|\\__, |   \\_/_|  \\___|\\___|\\__|
                                                            | |                               __/ |                       \s
                                                            |_|                              |___/                        \s
                        """;
                var helpPage = """
                        Q1/TRANSFER: City1 City2 Ai Rj Hk | where i,j,k are unsigned integers.
                            London Sydney A2 R1 H3. Start in London, use 2 Airways, 1 Railway, 3 Highways, and finish in Sydney. Return one path in the given order.
                        Q2/NCITIES: City1 City2 N | where N is an unsigned integer.
                            Sydney Melbourne 3. Start from Sydney, pass 3 different cities, finish in Melbourne. Return all possible paths.
                        Q3/TYPE: City1 City2 Type | where type is the transportation type.
                            Paris London Railway. Start in Paris and finish in London using only the given transportation type. Return all possible paths.
                        """;
                System.out.printf("%s%n%s%n", projectLabel, helpPage);
                // TODO: provide a helpful message that informs the user about available commands
            }
        }
    }
}
