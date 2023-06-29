import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Custom implementation of blocking queue
 */
public class BlockingThreadsQueue<T> {
    Queue<T> queue;
    int maxSize;
    ReentrantLock lock;

    public BlockingThreadsQueue(int maxSize) {
        queue = new LinkedList<>();
        this.maxSize = maxSize;
        lock = new ReentrantLock();
    }

    public boolean enqueue(T val) {
        lock.lock();
        try {
            if (queue.size() == maxSize) {
                return false;
            } else {
                queue.offer(val);
                return true;
            }
        } finally {
            lock.unlock();
        }
    }

    public T peek() {
        // no need to add thread safety as first element will always remain same
        if (queue.peek() != null) {
            return queue.peek();
        }
        return null;
    }

    public T dequeue() {
        lock.lock();
        try {
            // Guaranteed that queue always has at least one element if this method is being called
            return queue.poll();
        } finally {
            lock.unlock();
        }
    }
}
