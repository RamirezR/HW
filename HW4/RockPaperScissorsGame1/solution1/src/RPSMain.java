import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RPSMain {

    public static void main(String args[]) throws InterruptedException, ExecutionException {
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Enter the number of players: ");
        int numPlayers = keyboard.nextInt();

        long start_time = System.currentTimeMillis();
        // use a thread pool and Futures to handle the parallel work of initializing the ArrayList
        int numProcessors = Runtime.getRuntime().availableProcessors();
        ExecutorService pool = Executors.newFixedThreadPool(numProcessors);

        // these loops create the threads which select rock, paper, or scissors for the elements in the playerList ArrayList
        ArrayList<PlayerAttributes> playerList = new ArrayList<>();
        for(int j = numPlayers-1; j > 0; j--) {
            for (int i = numPlayers; i > 0; i -= 4) {
                if (i >= 4) {
                    Future<PlayerAttributes> p1 = pool.submit(new GesturePicker(playerList));
                    Future<PlayerAttributes> p2 = pool.submit(new GesturePicker(playerList));
                    Future<PlayerAttributes> p3 = pool.submit(new GesturePicker(playerList));
                    Future<PlayerAttributes> p4 = pool.submit(new GesturePicker(playerList));
                    playerList.add(p1.get());
                    playerList.add(p2.get());
                    playerList.add(p3.get());
                    playerList.add(p4.get());
                } else if (i == 3) {
                    Future<PlayerAttributes> p1 = pool.submit(new GesturePicker(playerList));
                    Future<PlayerAttributes> p2 = pool.submit(new GesturePicker(playerList));
                    Future<PlayerAttributes> p3 = pool.submit(new GesturePicker(playerList));
                    playerList.add(p1.get());
                    playerList.add(p2.get());
                    playerList.add(p3.get());
                } else if (i == 2) {
                    Future<PlayerAttributes> p1 = pool.submit(new GesturePicker(playerList));
                    Future<PlayerAttributes> p2 = pool.submit(new GesturePicker(playerList));
                    playerList.add(p1.get());
                    playerList.add(p2.get());
                } else if (i == 1) {
                    Future<PlayerAttributes> p1 = pool.submit(new GesturePicker(playerList));
                    playerList.add(p1.get());
                }
            }

            // start the Winner Thread
            Thread thread = new Thread(new WinnerThread(playerList));
            thread.start();
            thread.join();

        }

        pool.shutdown();
        long end_time = System.currentTimeMillis();

        System.out.println("Completion time(seconds): " + ((end_time - start_time)/1000) );
        System.out.println("Final thread score: " + playerList.get(0).totalWins + " wins");

    }
}