import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Operation {
    public static void main(String[] args) throws InterruptedException {
        int chairs = ThreadLocalRandom.current().nextInt(1, 20);
        final BarberShop barberShop = new BarberShop(chairs);
        System.out.println("Barber shop opened with " + chairs + " chairs");

        // each thread is a potential customer
        int customers = ThreadLocalRandom.current().nextInt(chairs, 3 * chairs);
        List<Thread> threads = new ArrayList<>(customers);
        for (int i = 1; i <= customers; ++i) {
            Thread thread = new Thread(() -> {
                barberShop.takeSeat(Thread.currentThread().getName());
            });
            thread.setName("Customer-"+i);
            threads.add(thread);
            thread.start();
            // jitter in the rate of incoming customers
            Thread.sleep(ThreadLocalRandom.current().nextInt(1, 800));
        }

        for (Thread t : threads) {
            t.join();
        }

        barberShop.closeShop();
    }
}
