import java.io.*;
import java.util.*;

// with the single threaded program I was able to establish the data structures needed.
public class HuffmanMain {

    // this method returns a LinkedHashMap that contains the character frequencies in the Constitution.
    public static LinkedHashMap<Character, Integer> getCharFreq() throws IOException {
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
        //LinkedHashMap<Character, Integer> sortedByValue = sortMap(charFreqMap);

        return charFreqMap;
    }

    // receives the Map of frequencies and characters and put them into a PriorityQueue sorted by frequency
    public static PriorityQueue<LetterNode> getPQ(LinkedHashMap<Character, Integer> charFreqMap){
        Comparator<LetterNode> freqComparator = new Comparator<>() {
            @Override
            public int compare(LetterNode node1, LetterNode node2) {
                return node1.getFreq() - node2.getFreq();
            }
        };
        PriorityQueue<LetterNode> PQ = new PriorityQueue<>(freqComparator);

        for (Map.Entry entry : charFreqMap.entrySet()) {
            PQ.add(new LetterNode(entry.getKey(), entry.getValue()));
        }
        return  PQ;
    }

    // This method removes the top two and smallest frequencies and creates a new node with the only its
    // frequency initialized to the sum of the two dequeued nodes. This builds the tree from the bottom up
    public static PriorityQueue<LetterNode> huffmanEncoder(PriorityQueue<LetterNode> priorityQueue){
        int size = priorityQueue.size();
        for (int i = 0; i < size - 1; i++){
            LetterNode newNode = new LetterNode(priorityQueue.remove(), priorityQueue.remove() );
            priorityQueue.add(newNode);
        }
        return priorityQueue;
    }

    public static void printQueue(PriorityQueue<LetterNode> PQ){
        while (!PQ.isEmpty()) {
            System.out.println(PQ.remove().getFreq());
        }
    }

    // this method receives a LinkedHashMap called outcodeMap which is initialized by a method
    // in the LetterNode class, breadthFirstSearch. The outcodeMap contains the new binary patterns
    // that represent the characters. Here we read in the original file character by character and
    // for each character I write the outcode to a new file of encoded 1's and 0's.
    public static void encodeFile(LinkedHashMap<Character, String> outcodeMap) throws IOException {
        PrintWriter writer = new PrintWriter("C:\\Users\\rober\\Desktop\\Spring2020\\CS3700\\Huffman\\src\\encodedConst.txt", "UTF-8");

        File file = new File("C:\\Users\\rober\\Desktop\\Spring2020\\CS3700\\Huffman\\src\\const.txt");
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        int c = 0;
        while( (c = bufferedReader.read()) != -1 ){
            char character = (char) c;
            writer.append(outcodeMap.get(Character.valueOf(character)));
        }
    }

    // decodes the encoded file.
    public static void decodeFile(LetterNode root) throws IOException {
        PrintWriter writer = new PrintWriter("C:\\Users\\rober\\Desktop\\Spring2020\\CS3700\\Huffman\\src\\decodedConst.txt", "UTF-8");

        File file = new File("C:\\Users\\rober\\Desktop\\Spring2020\\CS3700\\Huffman\\src\\encodedConst.txt");
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        LetterNode nodePtr = new LetterNode(root);

        int c = 0;
        while( (c = bufferedReader.read()) != -1 ){
            char character = (char) c;

            if(nodePtr.left != null && nodePtr.right != null){
                if(character == '0')
                    nodePtr = nodePtr.left;
                else if ( character == '1')
                    nodePtr = nodePtr.right;
            }
            if (nodePtr.character != null) {
                writer.append(nodePtr.character);
                nodePtr = new LetterNode(root);
            }
        }
        writer.close();
    }

    // calls methods to perform encoding and decoding.
    public static void main(String args[]) throws Exception {

        long tree_startTime = System.currentTimeMillis();

        LinkedHashMap<Character, Integer> charFreqMap = getCharFreq();
        PriorityQueue<LetterNode> PQ = getPQ(charFreqMap);

        //PriorityQueue<LetterNode> tmpPQ = new PriorityQueue<>(PQ);
        //printQueue(tmpPQ);

        PriorityQueue<LetterNode> tree = huffmanEncoder(PQ);
        long tree_endTime = System.currentTimeMillis();
        System.out.println("Time to build tree(ms): " + (tree_endTime - tree_startTime));

        long encode_startTime = System.currentTimeMillis();
        LetterNode root = tree.remove();
        LinkedHashMap<Character, String> charOutcodeMap = new LinkedHashMap<Character, String>();

            LetterNode.breadthFirstSearch(root, charOutcodeMap);

        // now create a method to write the 0's and 1's to a file.

        encodeFile(charOutcodeMap);
        long encode_endTime = System.currentTimeMillis();
        System.out.println("Time to encode the file(ms): " + (encode_endTime - encode_startTime));

        //decode to check if it works
        decodeFile(root);

        File constitution = new File("C:\\Users\\rober\\Desktop\\Spring2020\\CS3700\\Huffman\\src\\const.txt");
        File encodedConst = new File("C:\\Users\\rober\\Desktop\\Spring2020\\CS3700\\Huffman\\src\\encodedConst.txt");
        double constSize = (double)constitution.length()*8;
        double encodedSize = (double)encodedConst.length();
        System.out.println("Compression ratio: " +  (encodedSize / constSize) );

    }
}