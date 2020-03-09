import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class PriorityQueueThread implements Callable {

    public PriorityQueue<TreeNode> call() throws IOException {
        Map<String, Long> frequentChars = Arrays.stream(
                new String(Files.readAllBytes(Paths.get("C:\\Users\\rober\\Desktop\\Spring2020\\CS3700\\Huffman\\src\\const.txt")), StandardCharsets.UTF_8)
                        .split("")).collect(Collectors.groupingBy(c -> c, Collectors.counting()));
        return getPQ(frequentChars);
    }

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
}
/*
public PriorityQueue<TreeNode> call() throws IOException {
        LinkedHashMap<Character, Integer> charFreqMap = new LinkedHashMap<Character, Integer>();

        File file = new File("C:\\Users\\rober\\Desktop\\Spring2020\\CS3700\\Huffman\\src\\const.txt");
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        int c = 0;
        while( (c = bufferedReader.read()) != -1 ){
            char character = (char) c;
            if(charFreqMap.containsKey(character))
                charFreqMap.put(character, charFreqMap.get(character) + 1);
            else
                charFreqMap.put(character, 1);
        }
        return getPQ(charFreqMap);
    }

    public static PriorityQueue<TreeNode> getPQ(LinkedHashMap<Character, Integer> charFreqMap){
        Comparator<TreeNode> freqComparator = new Comparator<>() {
            @Override
            public int compare(TreeNode node1, TreeNode node2) {
                return node1.getFreq() - node2.getFreq();
            }
        };
        PriorityQueue<TreeNode> PQ = new PriorityQueue<>(freqComparator);

        for (Map.Entry entry : charFreqMap.entrySet()) {
            PQ.add(new TreeNode(entry.getKey(), entry.getValue()));
        }
        return  PQ;
    }
 */