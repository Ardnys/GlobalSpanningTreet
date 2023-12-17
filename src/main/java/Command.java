import java.util.List;
public record Command(CommandType commandType, String from, String to, List<String> arguments) {}