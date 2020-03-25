import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

/***************************************************************
 * file: ProducerConsumerIsolation.java
 * author: Roberto Ramirez
 * class: CS 3700 – Parallel Programming
 *
 * assignment: Homework 5
 * date last modified: 3/18/2020
 *
 * purpose: implement an isolation sections solution for the producer consumer problem.
 *
 * Times:
 * 5 producers and 2 consumers: Time of execution(s): 253
 * 2 producers and 5 consumers: Time of execution(s): 42
 *
 ****************************************************************/


class ProducerThread extends Thread{

    private ArrayList<Thread> threadList;
    private BlockingQueue queue;
    private Semaphore emptyCount;
    private Semaphore fillCount;

    ProducerThread(ArrayList<Thread> threadlist, BlockingQueue blockingQueue, Semaphore emptySem, Semaphore fillSem){
        this.threadList = threadlist;
        this.queue = blockingQueue;
        this.emptyCount = emptySem;
        this.fillCount = fillSem;
    }
    // method: producerCheck
    // purpose: the producer threads signal the consumer threads to stop.
    //          When producer threads out number consumer threads they all cannot signal consumer threads to stop
    //          I only allow an equal number of producer threads to signal consumer thread.
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
    // purpose: add 100 integers to the shared queue
    public void run() {
        for(int i = 0; i < 100; i++) {
            try {
                emptyCount.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            queue.add(i);
            fillCount.release();
        }
        if(threadList.size() == 5) {
            if (producerCheck()) {
                try {
                    emptyCount.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                queue.add(-1);
                fillCount.release();
            }
        }else{
            for (int i = 0; i < 3; i++){
                try {
                    emptyCount.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                queue.add(-1);
                fillCount.release();
            }
        }
    }
}

class ConsumerThread extends Thread{

    private Semaphore emptyCount;
    private Semaphore fillCount;
    private BlockingQueue queue;

    ConsumerThread(BlockingQueue blockingQueue, Semaphore empty, Semaphore fill){
        this.queue = blockingQueue;
        this.emptyCount = empty;
        this.fillCount = fill;
    }

    // method: run
    // purpose: consume integers from the shared buffer queue until the (-1) integer is consumed
    public void run() {
        Integer dequeueValue;
        while(true){
            try {
                fillCount.acquire();
                dequeueValue = (Integer)queue.remove();
                if (dequeueValue < 0)
                    break;
                Thread.sleep(1000);
                //System.out.println(Thread.currentThread().getId() + ": " + dequeueValue.toString());
                emptyCount.release();
            } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }
}

public class ProducerConsumerIsolation {

    int numProducers;
    int numConsumers;

    ProducerConsumerIsolation(int producers, int consumers){
        numProducers = producers;
        numConsumers = consumers;
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

    // method: runProducerConsumer
    // purpose: encapsulate the setup for running the producer consumer implementation.
    //          It allows for running both 5 producer 2 consumer and 2 producer 5 consumer implementations in main
    public void runProducerConsumer(){
        ArrayList<Thread> producerList = new ArrayList<>();
        ArrayList<Thread> consumerList = new ArrayList<>();
        BlockingQueue blockingQueue = new ArrayBlockingQueue<Integer>(10);
        Semaphore emptySem = new Semaphore(10);
        Semaphore fillSem = new Semaphore(10);
        fillSem.drainPermits();

        long start_time = System.currentTimeMillis();

        for(int i = 0; i < numProducers; i++){
            Thread producer = new ProducerThread(producerList, blockingQueue, emptySem, fillSem);
            producer.start();
            producerList.add(producer);
        }
        Thread consumerThread;
        for ( int j = 0; j < numConsumers; j++){
            consumerThread =  new ConsumerThread(blockingQueue, emptySem, fillSem);
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

    public static void main(String args[]){
        ProducerConsumerIsolation testOne = new ProducerConsumerIsolation(5, 2);
        testOne.runProducerConsumer();

        ProducerConsumerIsolation testTwo = new ProducerConsumerIsolation(2, 5);
        testTwo.runProducerConsumer();
    }
}
