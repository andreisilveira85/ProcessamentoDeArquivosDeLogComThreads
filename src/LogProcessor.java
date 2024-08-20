import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.Callable;

public class LogProcessor implements Callable<Integer> {
    private String logFilePath;
    private String targetWord;

    public LogProcessor(String logFilePath, String targetWord) {
        this.logFilePath = logFilePath;
        this.targetWord = targetWord;
    }

    @Override
    public Integer call() throws Exception {
        int wordCount = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(logFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                wordCount += countOccurrences(line, targetWord);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wordCount;
    }

    private int countOccurrences(String line, String word) {
        int count = 0;
        int index = 0;
        while ((index = line.indexOf(word, index)) != -1) {
            count++;
            index += word.length();
        }
        return count;
    }
}

