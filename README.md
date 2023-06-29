# Problem Statement

A barber shop consists of a waiting room with n chairs, and a barber chair for giving haircuts.

If there are no customers to be served, the barber goes to sleep.

If a customer enters the barbershop and all chairs are occupied, then the customer leaves the shop.

If the barber is busy, but chairs are available, then the customer sits in one of the free chairs. If the barber is
asleep, the customer wakes up the barber.

Write a program to coordinate the interaction between the barber and the customers.

## Solution approach
1. To keep barber waiting for customers, use a counting semaphore `barber` initialized to 0.
2. Barber thread runa an infinite loop in which it first attempts to acquire the semaphore, and once that is successful 
it proceeds to attend to the waiting customer(s).
3. A blocking queue implementation with a max size has been used to allow incoming customer threads to take a seat if 
queue is not full.
4. When attempting to take a seat, every thread attempts to enqueue a `QueueElement` having the thread name and a
related semaphore.
5. If enqueue was successful, the barber semaphore is released, and the semaphore of `QueueElement` is acquired.
6. When a barber completes attending to a customer, it dequeues the customer, and releases the semaphore associated
with it.
7. A minor side task of "closing the store" has been added which interrupts the barber thread thus terminating the
program.

## Sample run
```
Barber shop opened with 5 chairs
Customer-1 found a seat
Attending to Customer-1.
Customer-2 found a seat
Customer-3 found a seat
Customer-4 found a seat
Customer-5 found a seat
Customer-1 has been addressed.
Attending to Customer-2.
Customer-1 left their seat.
Customer-6 found a seat
Customer-7 could not find an empty seat so they left.
Customer-2 has been addressed.
Attending to Customer-3.
Customer-2 left their seat.
Customer-3 has been addressed.
Attending to Customer-4.
Customer-3 left their seat.
Customer-4 has been addressed.
Attending to Customer-5.
Customer-4 left their seat.
Customer-5 has been addressed.
Attending to Customer-6.
Customer-5 left their seat.
Customer-6 has been addressed.
Customer-6 left their seat.
Closing shop!```