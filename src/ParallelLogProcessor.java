import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ParallelLogProcessor {
    private List<String> logFilePaths;
    private String targetWord;
    private int numberOfThreads;

    public ParallelLogProcessor(List<String> logFilePaths, String targetWord, int numberOfThreads) {
        this.logFilePaths = logFilePaths;
        this.targetWord = targetWord;
        this.numberOfThreads = numberOfThreads;
    }

    public int processLogs() {
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        List<Future<Integer>> futures = new ArrayList<>();
        int totalOccurrences = 0;

        try {
            for (String filePath : logFilePaths) {
                LogProcessor logProcessor = new LogProcessor(filePath, targetWord);
                Future<Integer> future = executorService.submit(logProcessor);
                futures.add(future);
            }

            for (Future<Integer> future : futures) {
                try {
                    totalOccurrences += future.get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } finally {
            executorService.shutdown();
        }

        return totalOccurrences;
    }

    public static void main(String[] args) {
        List<String> logFiles = List.of("log1.txt", "log2.txt", "log3.txt");
        String wordToSearch = "ERROR";
        int threads = 3;

        ParallelLogProcessor parallelLogProcessor = new ParallelLogProcessor(logFiles, wordToSearch, threads);
        int totalOccurrences = parallelLogProcessor.processLogs();

        System.out.println("A palavra '" + wordToSearch + "' apareceu " + totalOccurrences + " vezes nos arquivos de log.");
    }
}

