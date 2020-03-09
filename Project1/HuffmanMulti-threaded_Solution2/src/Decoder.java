import java.io.*;

public class Decoder implements Runnable {

    TreeNode root;

    Decoder(TreeNode node){
        root = node;
    }

    @Override
    public void run() {
        try {
            PrintWriter writer = new PrintWriter("C:\\Users\\rober\\Desktop\\Spring2020\\CS3700\\HuffmanSplitFreqCounting\\src\\decodedConst.txt", "UTF-8");


            File file = new File("C:\\Users\\rober\\Desktop\\Spring2020\\CS3700\\HuffmanSplitFreqCounting\\src\\encodedConst.txt");
            FileReader fileReader = null;
            try {
                fileReader = new FileReader(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            TreeNode nodePtr = new TreeNode(root);

            int c = 0;
            while(true){
                try {
                    if (!((c = bufferedReader.read()) != -1)) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                char character = (char) c;

                if(nodePtr.left != null && nodePtr.right != null){
                    if(character == '0')
                        nodePtr = nodePtr.left;
                    else if ( character == '1')
                        nodePtr = nodePtr.right;
                }
                if (nodePtr.character != null) {
                    writer.append(nodePtr.character);
                    nodePtr = new TreeNode(root);
                }
            }
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}


/*
    public static void decodeFile(LetterNode root) throws IOException {
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
 */
