public class Main {
    public static void main(String[] args) throws InterruptedException {
       int threadsNum = Integer.parseInt(args[0]);

       RefsQueue queue = new RefsQueue(50);
       queue.put("https://tut.by/");

       WordsContainer wordsContainer = new WordsContainer();

       for(int i = 0; i < threadsNum; i++) {
           (new DiggerThread(queue, wordsContainer, 2)).start();
       }

       while(!DiggerThread.countingEnd) {
           Thread.sleep(1000);
       }

       wordsContainer.printResult(10, 6);
    }
}
