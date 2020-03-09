import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// I protect the counter used in the while loop with an atomicInteger and a ReentrantLock to protect the thread while writing to the file.
public class Bthread implements Runnable{

    static Lock lock = new ReentrantLock();
    static AtomicInteger index = new AtomicInteger(0);
    Stack<String> stack;
    PrintWriter writer;
    int stackSize;

    String[] tmp;

    Bthread(Stack<String> stack1, PrintWriter w, int size){
        stack = stack1;
        writer = w;
        stackSize = size;
    }

    @Override
    public void run() {

        while(index.get() < stackSize) {
            lock.lock();
            if (!stack.empty()){
                if (!stack.peek().equals("")) {
                    tmp = stack.pop().split(" ");

                    for (int j = tmp.length-1; j > 0; j--) {
                        writer.append(tmp[j] + " ");
                    }
                    writer.append("\n");
                } else {
                    stack.pop();
                    writer.append("\n");
                }
                index.incrementAndGet();
                //System.out.println(Thread.currentThread());
                lock.unlock();
            }
        }
        try{
            writer.close();
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public static void main(String args[]){

    }
}