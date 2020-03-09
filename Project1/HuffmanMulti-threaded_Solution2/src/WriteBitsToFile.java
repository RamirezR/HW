import java.io.*;
import java.util.LinkedHashMap;
import java.util.concurrent.Callable;

public class WriteBitsToFile implements Runnable {

    private LinkedHashMap<String, String> outcodeMap;

    WriteBitsToFile(LinkedHashMap<String, String> outMap){
        outcodeMap = outMap;
    }

    @Override
    public void run(){
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("C:\\Users\\rober\\Desktop\\Spring2020\\CS3700\\HuffmanSplitFreqCounting\\src\\encodedConst.txt", "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        File file = new File("C:\\Users\\rober\\Desktop\\Spring2020\\CS3700\\Huffman\\src\\const.txt");
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        int c = 0;
        while(true){
            try {
                if (!((c = bufferedReader.read()) != -1)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            String character = String.valueOf((char)c);
            writer.append(outcodeMap.get(character));
        }
        writer.close();
    }

    //private String toString(char c) {
    //    return ;
    //}
}

/*
public static void encodeFile(LinkedHashMap<Character, String> outcodeMap) throws IOException {

    }
 */


