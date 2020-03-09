import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class FreqMapThread implements Callable {

    private String inputString;

    FreqMapThread(String string){
        inputString = string;
    }

    @Override
    public Map<String, Long> call() throws IOException {
        Map<String, Long> frequentChars = Arrays.stream(inputString
                .split("")).collect(Collectors.groupingBy(c -> c, Collectors.counting()));

        return frequentChars;
    }
}
