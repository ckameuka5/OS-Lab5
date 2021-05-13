import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int threadNum = Integer.parseInt(args[0]);

        RefsQueue queue = new RefsQueue(100);

        WordsContainer wordsContainer = new WordsContainer();

        DiggerThread digger = new DiggerThread(queue, "https://tut.by/", 20);
        digger.start();

        ArrayList<CounterThread> counters = new ArrayList<>();

        for(int i = 0; i < threadNum; i++) {
            counters.add(new CounterThread(queue, wordsContainer, 5));
            counters.get(i).start();
        }

        digger.join();
        for(int i = 0; i < threadNum; i++) {
            counters.get(i).join();
        }

        wordsContainer.printResult(10, 5);
    }
}
