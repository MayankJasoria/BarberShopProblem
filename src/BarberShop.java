import java.util.Objects;
import java.util.concurrent.Semaphore;

public class BarberShop {

    private final Semaphore barber;
    private final BlockingThreadsQueue<QueueElement> queue;
    private final Thread barberThread;

    public BarberShop(int chairs) {
        barber = new Semaphore(0);
        queue = new BlockingThreadsQueue<>(chairs);
        barberThread = new Thread(new Barber());
        barberThread.start();
    }

    public void takeSeat(String s) {
        QueueElement queueElement = new QueueElement(s);
        if (queue.enqueue(queueElement)) {
            System.out.println(s + " found a seat");
            barber.release(1);
            try {
                queueElement.sem.acquire();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(s + " left their seat.");
        } else {
            System.out.println(s + " could not find an empty seat so they left.");
        }
    }

    public void closeShop() {
        barberThread.interrupt();
        barber.release();
    }

    private class Barber implements Runnable {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    barber.acquire();
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println("Closing shop!");
                        break;
                    }
                    System.out.println("Attending to " + queue.peek().s + ".");
                    Thread.sleep(2000); // simulates time taken to handle customer
                    QueueElement element = queue.dequeue();
                    System.out.println(element.s + " has been addressed.");
                    element.sem.release();
                } catch (InterruptedException e) {
                    System.out.println("Closing shop!");
                    break;
                }
            }
        }
    }

    private class QueueElement {
        String s;
        Semaphore sem;
        QueueElement(String s) {
            this.s = s;
            this.sem = new Semaphore(0);
        }
    }
}
