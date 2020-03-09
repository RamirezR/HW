import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class WinnerThread implements Runnable {

    private static ArrayList<Thread> playerThreadsList = new ArrayList<>();
    private static ArrayList<RPSplayer> playerObjectList = new ArrayList<>();
    private static ArrayList<String> choiceList = new ArrayList<>();
    private int numPlayers;

    static CyclicBarrier barrier;
    static Semaphore semaphore;



    public void run() {

        while(playerObjectList.size() > 1){

            try {
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            barrier = new CyclicBarrier(numPlayers - 1);

            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // process all the scores
            for (int i = 0; i < numPlayers; i++ ) {
                for (int j = 0; j < numPlayers; j++) {
                    if (playerObjectList.get(i).playerNumber != i) {
                        if (playerObjectList.get(i).choice.equals(playerObjectList.get(j).choice)) {
                            playerObjectList.get(i).score += 0;
                        }
                        if (playerObjectList.get(i).choice.equals("rock") && playerObjectList.get(j).choice.equals("scissors"))
                            playerObjectList.get(i).score += 1;
                        else
                            playerObjectList.get(i).score -= 1;
                        if (playerObjectList.get(i).choice.equals("paper") && playerObjectList.get(j).choice.equals("scissors"))
                            playerObjectList.get(i).score -= 1;
                        else
                            playerObjectList.get(i).score += 1;
                        if (playerObjectList.get(i).choice.equals("scissors") && playerObjectList.get(j).choice.equals("paper"))
                            playerObjectList.get(i).score += 1;
                        else
                            playerObjectList.get(i).score -= 1;

                    }
                }
                playerObjectList.get(i).totalPlayers -= 1;
            }
            // done processing scores, Now eliminate one of the lowest scores

            playerThreadsList.get(0).interrupt();
            playerThreadsList.remove(0);
            playerObjectList.remove(0);

            numPlayers -= 1;

            semaphore.release();
        }
    }

    WinnerThread(ArrayList<Thread> playerT, ArrayList<String> choiceL, ArrayList<RPSplayer> objList, int numP, CyclicBarrier b, Semaphore s){
        playerThreadsList = playerT;
        choiceList = choiceL;
        playerObjectList = objList;
        numPlayers = numP;
        barrier = b;
        semaphore = s;
    }

    public static void main(String args[]){


    }
}
