import java.util.ArrayList;
import java.util.Comparator;

// after the picker thread is done this class tallies scores and eliminates the thread with the lowest score.
public class WinnerThread implements Runnable{

    ArrayList<PlayerAttributes> playerObjectList;

    WinnerThread(ArrayList<PlayerAttributes> PO){
        playerObjectList = PO;
    }

    @Override
    public void run() {
        // process all the scores
        for (int i = 0; i < playerObjectList.size(); i++){
            for (int j = 0; j < playerObjectList.size(); j++){
                if( i == j) {}
                else{
                    if (playerObjectList.get(i).choice.equals(playerObjectList.get(j).choice))
                        playerObjectList.get(i).score += 0;
                    if (playerObjectList.get(i).choice.equals("rock") && playerObjectList.get(j).choice.equals("scissors")) {
                        playerObjectList.get(i).score += 1;
                        playerObjectList.get(i).totalWins += 1;
                    }else if (playerObjectList.get(i).choice.equals("rock") && playerObjectList.get(j).choice.equals("paper"))
                        playerObjectList.get(i).score -= 1;

                    if (playerObjectList.get(i).choice.equals("paper") && playerObjectList.get(j).choice.equals("scissors"))
                        playerObjectList.get(i).score -= 1;
                    else if (playerObjectList.get(i).choice.equals("paper") && playerObjectList.get(j).choice.equals("rock")) {
                        playerObjectList.get(i).score += 1;
                        playerObjectList.get(i).totalWins += 1;
                    }
                    if (playerObjectList.get(i).choice.equals("scissors") && playerObjectList.get(j).choice.equals("paper")) {
                        playerObjectList.get(i).score += 1;
                        playerObjectList.get(i).totalWins += 1;
                    }else if (playerObjectList.get(i).choice.equals("scissors") && playerObjectList.get(j).choice.equals("rock"))
                        playerObjectList.get(i).score -= 1;

                }
            }
        }
        // eliminate lowest score or random lowest score
        playerObjectList.sort(Comparator.comparingInt(o -> o.score));
        for (int j = playerObjectList.size()-1; j > 0; j--){
            playerObjectList.remove(0);
        }
        playerObjectList.get(0).score = 0;

    }

}

