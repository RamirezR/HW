import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RPSplayer implements Runnable {

    int playerNumber;
    int totalPlayers;
    String choice = null;
    int score = 0;
    ArrayList<String> playerChoices;
    ArrayList<Thread> threadsList;


    static Lock lock = new ReentrantLock();
    static CyclicBarrier barrier;
    static Semaphore semaphore;

    public void run() {
        // lock WinnerThread


        int round = 1;
        Random rand = new Random();
        int intChoice = rand.nextInt(3);

        while( threadsList.size() > 1) {
            if(semaphore.availablePermits() == 1) {
                if (intChoice == 0)
                    choice = "Rock";
                else if (intChoice == 1)
                    choice = "Paper";
                else
                    choice = "Scissors";

                lock.lock();
                playerChoices.add(choice);
                lock.unlock();

                // unlocks WinnerThread when all threads are done picking a hand gesture
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                Thread.yield();

                // wait for WinnerThread to finish, Winner Thread could hold a lock


                System.out.println(choice);
            }
        }

    }

    RPSplayer(int pNum, int numberOfPlayers, ArrayList<String> playerL, ArrayList<Thread> threadL, CyclicBarrier b, Semaphore s){
        playerNumber = pNum;
        totalPlayers = numberOfPlayers;
        playerChoices = playerL;
        threadsList = threadL;
        barrier = b;
        semaphore = s;
    }

    public static void main(String args[]){

        Scanner keyboard = new Scanner(System.in);
        System.out.print("Enter the number of players: ");
        int numPlayers = keyboard.nextInt();

        ArrayList<Thread> playerThreadsList = new ArrayList<>();
        ArrayList<String> choiceList = new ArrayList<>();
        ArrayList<RPSplayer> playerObjList = new ArrayList<>();

        CyclicBarrier barrier = new CyclicBarrier(numPlayers + 1);
        Semaphore semaphore = new Semaphore(1);

        for (int i = 0; i < numPlayers; i++){
            RPSplayer tmp = new RPSplayer(i, numPlayers, choiceList, playerThreadsList, barrier, semaphore);
            playerObjList.add(tmp);
            Thread playerThread = new Thread(tmp);
            playerThreadsList.add(playerThread);
            playerThread.start();
        }

        Thread WinThread = new Thread(new WinnerThread(playerThreadsList, choiceList, playerObjList, numPlayers, barrier, semaphore));
        WinThread.start();


        System.out.println("Started all threads");
    }
}
