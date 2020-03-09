import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.util.*;

public class LetterNode{

    public Character character;
    private Integer frequency;

    public LetterNode left;
    public LetterNode right;

    private String outcode;

    int level;

    public LetterNode(){

    }

    public LetterNode(Object key, Object value) {
        character = (Character)key;
        frequency = (Integer)value;
    }

    public LetterNode(LetterNode p, LetterNode q){
        left = p;
        right = q;
        frequency = left.getFreq() + right.getFreq();
    }

    public LetterNode(Character c, Integer freq, LetterNode l, LetterNode r){
        this.character = c;
        this.frequency = freq;
        this.left = l;
        this.right = r;
    }

    public LetterNode(LetterNode node){
        this(node.character, node.frequency, node.left, node.right);
    }

    public Integer getFreq(){
        return this.frequency;
    }

    public Character getCharacter(){ return this.character; }

    public static void printTree(LetterNode node) {
        if (node == null) {

        }
        if (node.left == null && node.right == null) {
            System.out.println(node.character + " " + node.frequency);
        }else {
            printTree(node.left);
            printTree(node.right);
        }
    }

    public static int getTreeDepth(LetterNode root){
        if (root == null)
            return 0;

        int leftDepth = getTreeDepth(root.left);
        int rightDepth = getTreeDepth(root.right);

        if (leftDepth > rightDepth)
            return leftDepth + 1;
        else
            return rightDepth + 1;
    }

    // level traversal of the binary tree that concats 0 to left nodes and 1's to right nodes.
    public static void breadthFirstSearch(LetterNode root, LinkedHashMap<Character, String> outcodeMap){

        if(root == null){

        }else {
            Queue<LetterNode> queue = new LinkedList<>();
            queue.add(root);

            int count = queue.size();

            while (count != 0) {
                LetterNode node = queue.remove();

                if (node.character != null){
                    outcodeMap.put(node.character, node.outcode);
                }
                //System.out.print(" " + "Node Freq: " + node.frequency + " " + "Node Char: [ " + node.character + " ] Node outcode: [" + node.outcode + "]");

                if (node.left != null) {
                    if (node.outcode == null){
                        node.left.outcode = "0";
                    }else
                        node.left.outcode = node.outcode + "0";
                    queue.add(node.left);
                }
                if (node.right != null){
                    if (node.outcode == null)
                        node.right.outcode = "1";
                    else
                        node.right.outcode = node.outcode + "1";
                    queue.add(node.right);
                }


                count--;

                if (count == 0){
                    //System.out.print("\n\n");
                    count = queue.size();
                }
            }
        }
    }
}


