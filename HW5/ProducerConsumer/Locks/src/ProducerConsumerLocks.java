import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.*;

/***************************************************************
 * file: ProducerConsumerLocks.java
 * author: Roberto Ramirez
 * class: CS 3700 – Parallel Programming
 *
 * assignment: Homework 5
 * date last modified: 3/18/2020
 *
 * purpose: implement a locks solution for the producer consumer problem.
 *
 * Times:
 * 5 producers and 2 consumers: Time of execution(seconds): 254
 * 2 producers and 5 consumers: Time of execution(seconds): 43
 *
 ****************************************************************/

class ConsumerThread extends Thread{
    BlockingQueue queue;
    private Lock consumerLock;
    private Condition emptyQueue;

    ConsumerThread(BlockingQueue q, Lock lock, Condition consumerCondition){
        queue = q;
        consumerLock = lock;
        emptyQueue = consumerCondition;
    }

    // method: run
    // purpose: dequeue integers from the shared buffer queue until the (-1) integer is consumed
    @Override
    public void run() {
        Integer dequeueValue;
        while(true){
            consumerLock.lock();
            try {
                while (queue.remainingCapacity() == 10) {
                    //addToQueue.signalAll();               // thread does not own the monitor
                    System.out.println("Queue is empty");
                    emptyQueue.await(2, TimeUnit.SECONDS);
                }
                if(queue.remainingCapacity() < 10) {
                    dequeueValue = (Integer) queue.take();
                    if(dequeueValue == -1)
                        break;
                    //System.out.println(Thread.currentThread().getId() + ": " + dequeueValue);

                    emptyQueue.signalAll();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                consumerLock.unlock();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class ProducerThread extends Thread{

    private ArrayList<Thread> threadList;
    BlockingQueue queue;
    private Lock producerLock;
    private Condition fullQueue;

    ProducerThread(BlockingQueue q, Lock lock, Condition ProducerCondition, ArrayList<Thread> threads){
        queue = q;
        producerLock = lock;
        fullQueue = ProducerCondition;
        threadList = threads;
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
    // purpose: enqueue 100 integers to the shared buffer queue and signal the consumer thread to stop after producing quota is met
    @Override
    public void run() {
        for (int i = 0; i < 100; i++){
            producerLock.lock();
            try {
            while (queue.remainingCapacity() == 0){
                    //System.out.println("Queue is full");
                    fullQueue.await(1, TimeUnit.SECONDS);
            }
            if (queue.remainingCapacity() > 0) {
                queue.add(i);
                fullQueue.signalAll();
            }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                producerLock.unlock();
            }
        }
        if (threadList.size() == 5) {
            if (producerCheck()) {
                producerLock.lock();
                try {
                    while (queue.remainingCapacity() == 0) {
                        fullQueue.await(1, TimeUnit.SECONDS);
                    }
                    if (queue.remainingCapacity() > 0) {
                        queue.add(-1);
                        fullQueue.signalAll();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    producerLock.unlock();
                }
            }
        }else{
            for (int i = 0; i < 3; i++){
                producerLock.lock();
                try {
                    while (queue.remainingCapacity() == 0) {
                        fullQueue.await(1, TimeUnit.SECONDS);
                    }
                    if (queue.remainingCapacity() > 0) {
                        queue.add(-1);
                        fullQueue.signalAll();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    producerLock.unlock();
                }
            }
        }
    }
}


public class ProducerConsumerLocks {

    private int numProducers;
    private int numConsumers;

    ProducerConsumerLocks(int producers, int consumers){
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

    // method: runProducerConsumerLocks
    // purpose: encapsulate the setup for running the producer consumer implementation.
    //          It allows for running both 5 producer 2 consumer and 2 producer 5 consumer implementations in main
    public void runProducerConsumerLocks(){

        long start_time = System.currentTimeMillis();

        ArrayList<Thread> producerList = new ArrayList<>();
        ArrayList<Thread> consumerList = new ArrayList<>();
        BlockingQueue queue = new ArrayBlockingQueue<Integer>(10);
        Lock producerLock = new ReentrantLock();
        Condition producerCondition = producerLock.newCondition();

        Lock consumerLock = new ReentrantLock();
        Condition consumerCondition = consumerLock.newCondition();

        for(int i = 0; i < numProducers; i++){
            Thread thread = new ProducerThread(queue, producerLock, producerCondition, producerList);
            thread.start();
            producerList.add(thread);
        }

        for (int j = 0; j < numConsumers; j++) {
            Thread consumerThread = new ConsumerThread(queue, consumerLock, consumerCondition);
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

        ProducerConsumerLocks testOne = new ProducerConsumerLocks(5, 2);
        testOne.runProducerConsumerLocks();

        ProducerConsumerLocks testTwo = new ProducerConsumerLocks(2, 5);
        testTwo.runProducerConsumerLocks();

    }
}
