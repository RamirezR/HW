import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Queue;

import java.util.*;

public class TreeNode{

    public String character;
    private Long frequency;

    public TreeNode left;
    public TreeNode right;

    private String outcode;

    int level;

    public TreeNode(){

    }

    public TreeNode(Object key, Object value) {
        character = (String)key;
        frequency = (Long)value;
    }

    public TreeNode(TreeNode p, TreeNode q){
        left = p;
        right = q;
        frequency = left.getFreq() + right.getFreq();
    }

    public TreeNode(String c, Long freq, TreeNode l, TreeNode r){
        this.character = c;
        this.frequency = freq;
        this.left = l;
        this.right = r;
    }

    public TreeNode(TreeNode node){
        this(node.character, node.frequency, node.left, node.right);
    }

    public Long getFreq(){
        return this.frequency;
    }

    public String getCharacter(){ return this.character; }

    public static void printTree(TreeNode node) {
        if (node == null) {

        }
        if (node.left == null && node.right == null) {
            System.out.println(node.character + " " + node.frequency);
        }else {
            printTree(node.left);
            printTree(node.right);
        }
    }

    public static int getTreeDepth(TreeNode root){
        if (root == null)
            return 0;

        int leftDepth = getTreeDepth(root.left);
        int rightDepth = getTreeDepth(root.right);

        if (leftDepth > rightDepth)
            return leftDepth + 1;
        else
            return rightDepth + 1;
    }

    public static void breadthFirstSearch(TreeNode root, LinkedHashMap<String, String> outcodeMap){

        if(root == null){

        }else {
            Queue<TreeNode> queue = new LinkedList<>();
            queue.add(root);

            int count = queue.size();

            while (count != 0) {
                TreeNode node = queue.remove();

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


