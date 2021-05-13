import java.util.concurrent.ConcurrentHashMap;

public class WordsContainer {
    public boolean ready;

    private ConcurrentHashMap<String, Integer> words = new ConcurrentHashMap<>();

    public void registerWord(String word) {
        var value = words.get(word);
        words.put(word, (value == null ? 0 : value) + 1);
    }

    public void printResult(int wordsNum, int minWordLen) {
        words.entrySet().stream()
                .sorted(ConcurrentHashMap.Entry.<String, Integer>comparingByValue().reversed())
                .filter(s-> s.getKey().length() > minWordLen)
                .limit(wordsNum)
                .forEach(System.out::println);
    }
}
