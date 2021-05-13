import java.util.concurrent.ConcurrentLinkedQueue;

public class RefsQueue {
    private int capacity;
    public ConcurrentLinkedQueue<String> queue;

    public RefsQueue(int capacity) {
        this.queue = new ConcurrentLinkedQueue<>();
        this.capacity = capacity;
    }

    public synchronized String get() {
        while (queue.isEmpty()) {
            if(DiggerThread.exploringEnd) {
                return null;
            }

            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        notify();
        return queue.poll();
    }

    public synchronized void put(String value) {
        if(RefsContainer.registerRef(value)) {
            if (queue.size() >= capacity) {
                //return;
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            queue.add(value);
            notify();
        }
    }
}
