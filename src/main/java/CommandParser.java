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
                    // TODO: properly handle not enough argument case
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
                System.out.println("Added command: " + command);
            }
            case TYPE -> {
                if (tokens.size() > 4) {
                    // TODO: show user the arguments and which one the system took
                    System.err.println("Too many arguments in " + userCommand + ". Only first is taken.");
                    // it's not a breaking error so I think I can deal with it like this
                }
                var type = tokens.get(3).toUpperCase();
                var trans = switch (type) {
                    case "A", "AIRWAY", "AIR" -> Transportation.AIRWAY;
                    case "R", "RAILWAY", "RAIL" -> Transportation.RAILWAY;
                    case "H", "HIGHWAY", "HIGH" -> Transportation.HIGHWAY;
                    default -> {
                        System.err.println("Invalid transportation type in " + userCommand + " command");
                        yield null;
                    }
                };
                if (trans == null) {
                    return;
                }
                // TODO: maybe an ERROR type for the transportation to eliminate null but whatever
                // also enums are amazing. look at it

                Command command = new Command(commandType, fromCity, toCity, List.of(trans.toString()));
                // the reason why I spend all the effort to convert the type to proper transportation is to
                // reduce the complexity of the execution. It's going to be complicated somewhere. Let it be
                // before it's converted to Command type.
                commandQueue.add(command);
                System.out.println("Added command: " + command);
            }
            case NCITIES -> {
                if (tokens.size() > 4) {
                    System.err.println("Too many arguments in " + userCommand);
                    // TODO: show user the arguments and which one the system took
                }
                try {
                    var n = Integer.parseInt(tokens.get(3));
                    Command command = new Command(commandType, fromCity, toCity, List.of(String.valueOf(n)));
                    commandQueue.add(command);
                    System.out.println("Added command: " + command);
                    // again, parsed it just to make it graceful. maybe it's stupid but it's late at night
                    // and I am FEELING it
                } catch (NumberFormatException e) {
                    System.err.println("Expected an integer in " + userCommand + " but found " + tokens.get(3));
                }
            }
            case NONE -> {
                System.err.println("Invalid command " + userCommand);
                // TODO: provide a helpful message that informs the user about available commands
            }
        }
    }
}
