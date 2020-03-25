import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/***************************************************************
 * file: ProducerConsumerAtomics.java
 * author: Roberto Ramirez
 * class: CS 3700 – Parallel Programming
 *
 * assignment: Homework 5
 * date last modified: 3/18/2020
 *
 * purpose: implement an atomics solution for the producer consumer problem.
 *
 * Times:
 * 5 producers and 2 consumers: Time of execution(seconds): 250
 * 2 producers and 5 consumers: Time of execution(seconds): 42
 *
 ****************************************************************/
class ProducerConsumerQueue{

    private BlockingQueue queue = new ArrayBlockingQueue<Integer>(10);
    private AtomicInteger fillCount = new AtomicInteger(0);

    // method: take
    // purpose: dequeue an element and return it to the consumer thread wait if empty, decrement the Atomic integer
    public synchronized int take() throws InterruptedException {
        while(fillCount.get() == 0){
            wait();
        }
        fillCount.decrementAndGet();
        notifyAll();
        return (Integer)queue.remove();
    }

    // method: put
    // purpose: enqueue an element into the queue wait if full, increment Atomic integer
    public synchronized void put(int num) throws InterruptedException {
        while(fillCount.get() == 10){
            wait();
        }
        queue.add(num);
        fillCount.incrementAndGet();
        notifyAll();
    }
}

class ProducerThread extends Thread{
    private ProducerConsumerQueue queue;
    ArrayList<Thread> threadList;

    public ProducerThread(ProducerConsumerQueue q, ArrayList<Thread> list){
        this.queue = q;
        this.threadList = list;
    }

    private boolean producerCheck(){
        int aliveCount = 0;
        int size = threadList.size();
        for(int i = 0; i < size; i++) {
            if(threadList.get(i).isAlive()){
                aliveCount++;
            }
        }
        if (aliveCount >= 3)
            return false;
        else
            return true;
    }

    // method: run
    // purpose: add 100 integers to the shared queue and signal consumer thread to exit
    @Override
    public void run() {
        for (int i = 0; i < 100; i++){
            try { queue.put(i); } catch (InterruptedException e) { e.printStackTrace(); }
        }
        try {
            if(threadList.size() == 5) {
                if (producerCheck())
                    queue.put(-1);
            } else{
                for (int i = 0; i < 3; i++){
                    queue.put(-1);
                }
            }
        } catch (InterruptedException e) { e.printStackTrace(); }
    }
}

class ConsumerThread extends Thread{
    private ProducerConsumerQueue queue;

    public ConsumerThread(ProducerConsumerQueue q){
        this.queue = q;
    }

    // method: run
    // purpose: dequeue elements from the shared buffer until negative value signal is received
    @Override
    public void run() {
        int dequeueValue;
        while(true){
            try {
                dequeueValue = queue.take();
                if(dequeueValue < 0){
                    break;
                }
                Thread.sleep(1000);
                // if the sleep is in the take method the execution time is doubled with 2 consumers
                //System.out.println("Value pulled: " + dequeueValue);
            }
            catch (InterruptedException e) { e.printStackTrace(); }
        }
    }
}

public class ProducerConsumerAtomics {
    private int numProducers;
    private int numConsumers;

    ProducerConsumerAtomics(int producers, int consumers){
        numProducers = producers;
        numConsumers = consumers;
    }

    // method: runProducerConsumer
    // purpose: encapsulate the setup for running the producer consumer implementation.
    //          It allows for running both 5 producer 2 consumer and 2 producer 5 consumer implementations in main
    public void runProducerConsumer(){
        long start_time = System.currentTimeMillis();

        ProducerConsumerQueue queue = new ProducerConsumerQueue();
        ArrayList<Thread> producerList = new ArrayList<>();
        ArrayList<Thread> consumerList = new ArrayList<>();

        for(int i = 0; i < numProducers; i++){
            Thread producerThread = new Thread(new ProducerThread(queue, producerList));
            producerThread.start();
            producerList.add(producerThread);
        }

        for(int j = 0; j < numConsumers; j++){
            Thread consumerThread = new Thread(new ConsumerThread(queue));
            consumerThread.start();
            consumerList.add(consumerThread);
        }

        long end_time;
        while(true){
            if (!consumerCheck(consumerList)){
                end_time = System.currentTimeMillis();
                break;
            }
        }
        System.out.println("Time of execution(s): " + ((end_time - start_time)/1000) );
    }

    // method: consumerCheck
    // purpose: when all consumer threads are terminated this returns false
    // and then ending execution time is able to be recorded.
    public boolean consumerCheck(ArrayList<Thread> consumerList){
        int aliveCount = 0;
        int size = consumerList.size();
        for(int i = 0; i < size; i++) {
            if(consumerList.get(i).isAlive()){
                aliveCount++;
            }
        }
        if (aliveCount > 0)
            return true;
        else
            return false;
    }

    public static void main(String args[]){
        ProducerConsumerAtomics testOne = new ProducerConsumerAtomics(5, 2);
        testOne.runProducerConsumer();

        ProducerConsumerAtomics testTwo = new ProducerConsumerAtomics(2, 5);
        testTwo.runProducerConsumer();
    }
}
