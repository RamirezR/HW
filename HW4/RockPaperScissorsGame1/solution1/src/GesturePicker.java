import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Callable;

// class implements Runnable in order to parallelize choice selection
public class GesturePicker implements Callable{

    private ArrayList<PlayerAttributes> pAttributes;

    public GesturePicker(ArrayList<PlayerAttributes> mainList){
        pAttributes = mainList;
    }

    public PlayerAttributes call(){
        PlayerAttributes threadPlayer = new PlayerAttributes();

        Random rand = new Random();
        int intChoice = rand.nextInt(3);

        if (intChoice == 0)
            threadPlayer.choice = "rock";
        else if (intChoice == 1)
            threadPlayer.choice = "paper";
        else
            threadPlayer.choice = "scissors";

        //pAttributes.add(threadPlayer);

        return threadPlayer;
    }
}
