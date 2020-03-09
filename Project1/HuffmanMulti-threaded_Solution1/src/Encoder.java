import java.util.PriorityQueue;
import java.util.concurrent.Callable;

public class Encoder implements Callable {

    PriorityQueue<TreeNode> PQ;

    public Encoder(PriorityQueue<TreeNode> treeNodes) {
        PQ = treeNodes;
    }


    @Override
    public PriorityQueue<TreeNode> call() throws Exception {

        int size = PQ.size();
        for (int i = 0; i < size - 1; i++){
            TreeNode newNode = new TreeNode(PQ.remove(), PQ.remove() );
            PQ.add(newNode);
        }
        return PQ;
    }

    public void printQueue(){
        while (!this.PQ.isEmpty()) {
            System.out.println(this.PQ.remove().getFreq());
        }
    }
}
