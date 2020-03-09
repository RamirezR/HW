import java.io.File;
import java.util.LinkedHashMap;
import java.util.PriorityQueue;
import java.util.concurrent.*;
/*
     This code replaces FileReaders and BufferedReaders with java streams for the character frequency map task.
     The rest of the methods that were in main are now callable or runnable methods.
 */
public class HuffmanMain {

    public static void main(String args[]) throws ExecutionException, InterruptedException {

        int numProcessors = Runtime.getRuntime().availableProcessors();

        long tree_startTime = System.currentTimeMillis();
        Future<PriorityQueue<TreeNode>> PqFuture;
        Future<PriorityQueue<TreeNode>> tree;

        ExecutorService pool = Executors.newFixedThreadPool(numProcessors);
        PqFuture = pool.submit(new PriorityQueueThread());

        tree = pool.submit(new Encoder(PqFuture.get()));
        long tree_endTime = System.currentTimeMillis();
        System.out.println("Time to build tree(ms): " + (tree_endTime - tree_startTime));


        long encode_startTime = System.currentTimeMillis();
        TreeNode root = tree.get().remove();
        LinkedHashMap<String, String> charOutcodeMap = new LinkedHashMap<String, String>();
        TreeNode.breadthFirstSearch(root, charOutcodeMap);

        Thread t =  new Thread(new WriteBitsToFile(charOutcodeMap));
        t.start();
        t.join();
        long encode_endTime = System.currentTimeMillis();
        System.out.println("Time to encode the file(ms): " + (encode_endTime - encode_startTime));

        Thread decodeThread = new Thread(new Decoder(root));
        decodeThread.start();

        pool.shutdown();

        File constitution = new File("C:\\Users\\rober\\Desktop\\Spring2020\\CS3700\\Huffman\\src\\const.txt");
        File encodedConst = new File("C:\\Users\\rober\\Desktop\\Spring2020\\CS3700\\MultiThreadedHuffman\\src\\encodedFile.txt");
        double constSize = (double)constitution.length()*8;
        double encodedSize = (double)encodedConst.length();
        System.out.println("Compression ratio: " +  (encodedSize / constSize) );
    }
}
