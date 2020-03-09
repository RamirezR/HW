import java.io.*;
import java.util.Scanner;
import java.util.Stack;

// similar to the single threaded program in main I create a stack of lines and pass that object to each thread.
// the threads write complete lines backward with the protection of a mutex.
public class BackwardsMultiThreaded {

    public static void main(String args[]) throws FileNotFoundException, UnsupportedEncodingException, InterruptedException {
        File file = new File("C:\\Users\\rober\\Desktop\\Spring2020\\CS3700\\DeclarationOfIndependence\\src\\DeclarationOfIndependence.txt");
        Scanner sc = new Scanner(new FileReader(file));

        Stack<String> stack = new Stack<>();

        long start_time = System.currentTimeMillis();

        while(sc.hasNext()){
            // push on to a stack
            stack.push(sc.nextLine());
        }

        PrintWriter writer = new PrintWriter("C:\\Users\\rober\\Desktop\\Spring2020\\CS3700\\DeclarationOfIndependence\\src\\backwardsMulti.txt", "UTF-8");

        Thread thread_1 = new Thread(new Bthread(stack, writer, stack.size()));
        Thread thread_2 = new Thread(new Bthread(stack, writer, stack.size()));
        thread_1.start();
        thread_2.start();
        thread_1.join();
        thread_2.join();

        long end_time = System.currentTimeMillis();
        System.out.println( "Time in miliseconds: " + (end_time - start_time) );

    }
}
