import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.BiFunction;

/*
     This implementation uses java streams like the first multi-threaded solution but instead of using one
     it implements four java streams for each core. The file is read as one large string and split among the threads.
 */

public class HuffmanMain {

    public static PriorityQueue<TreeNode> getPQ(Map<String, Long> charFreqMap){
        Comparator<TreeNode> freqComparator = new Comparator<>() {
            @Override
            public int compare(TreeNode node1, TreeNode node2) {
                return (int) (node1.getFreq() - node2.getFreq());
            }
        };
        PriorityQueue<TreeNode> PQ = new PriorityQueue<>(freqComparator);

        for (Map.Entry entry : charFreqMap.entrySet()) {
            PQ.add(new TreeNode(entry.getKey(), entry.getValue()));
        }
        return  PQ;
    }

    public static void mergeHashMaps(Future<Map<String, Long>> map_1, Future<Map<String, Long>> map_2, Future<Map<String, Long>> map_3, Future<Map<String, Long>> map_4) throws ExecutionException, InterruptedException {
        for (Map.Entry entry: map_2.get().entrySet()){
            map_1.get().merge((String) entry.getKey(), (Long) entry.getValue(), new BiFunction<Long, Long, Long>() {
                @Override
                public Long apply(Long aLong, Long aLong2) {
                    return aLong + aLong2;
                }
            });
        }
        for (Map.Entry entry: map_3.get().entrySet()){
            map_1.get().merge((String) entry.getKey(), (Long) entry.getValue(), new BiFunction<Long, Long, Long>() {
                @Override
                public Long apply(Long aLong, Long aLong2) {
                    return aLong + aLong2;
                }
            });
        }

        for (Map.Entry entry: map_4.get().entrySet()){
            map_1.get().merge((String) entry.getKey(), (Long) entry.getValue(), (aLong, aLong2) -> aLong + aLong2);
        }
    }

    public static void main(String args[]) throws IOException, ExecutionException, InterruptedException {

        long tree_startTime = System.currentTimeMillis();

        String string =new String(Files.readAllBytes(Paths.get("C:\\Users\\rober\\Desktop\\Spring2020\\CS3700\\Huffman\\src\\const.txt")), StandardCharsets.UTF_8);
        int length = string.length();

        int numProcessors = Runtime.getRuntime().availableProcessors();

        ExecutorService pool = Executors.newFixedThreadPool(numProcessors);
        Future<Map<String, Long>> freqMap_1 = pool.submit(new FreqMapThread(string.substring( 0, (length/4) )));
        Future<Map<String, Long>> freqMap_2 = pool.submit(new FreqMapThread(string.substring( (length/4) + 1, (length/2) )));
        Future<Map<String, Long>> freqMap_3 = pool.submit(new FreqMapThread(string.substring( (length/2) + 1, ((3*length)/4) )));
        Future<Map<String, Long>> freqMap_4 = pool.submit(new FreqMapThread(string.substring( ((3*length)/4), (length - 1) )));

        mergeHashMaps(freqMap_1, freqMap_2, freqMap_3, freqMap_4);

        PriorityQueue<TreeNode> PQ = getPQ(freqMap_1.get());
        Future<PriorityQueue<TreeNode>> tree = pool.submit(new Encoder(PQ));

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
        File encodedConst = new File("C:\\Users\\rober\\Desktop\\Spring2020\\CS3700\\HuffmanSplitFreqCounting\\src\\encodedConst.txt");
        double constSize = (double)constitution.length()*8;
        double encodedSize = (double)encodedConst.length();
        System.out.println("Compression ratio: " +  (encodedSize / constSize) );
    }


}
