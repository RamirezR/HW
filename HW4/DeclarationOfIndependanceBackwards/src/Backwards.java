import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;
// this class handles writing the file backwards for the single thread portion.
// How, it first reads the file line by line in main and places them on a stack.
// next that stack is passed to a method which pops off each and writes the lines backwards to the file
public class Backwards {

    static void writeFileBackwards(Stack<String> stackOfLines) throws FileNotFoundException, UnsupportedEncodingException {
        String[] tmp;
        int size = stackOfLines.size();
        PrintWriter writer = new PrintWriter("C:\\Users\\rober\\Desktop\\Spring2020\\CS3700\\DeclarationOfIndependence\\src\\backwards.txt", "UTF-8");
        for (int i = 0; i < size; i++){

            if(!stackOfLines.peek().equals("")){
                tmp = stackOfLines.pop().split(" ");

                for(int j = tmp.length-1; j > 0; j--){
                    writer.append(tmp[j] + " ");
                }
                writer.append("\n");
            }
            else {
                stackOfLines.pop();
                writer.append("\n");
            }
        }
        writer.close();
    }

    public static void main(String args[]) throws Exception{
        //C:\\Users\\rober\\Documents\\NetBeansProjects\\JavaApplication2\\src\\coordinates.txt

        File file = new File("C:\\Users\\rober\\Desktop\\Spring2020\\CS3700\\DeclarationOfIndependence\\src\\DeclarationOfIndependence.txt");
        Scanner sc = new Scanner(new FileReader(file));
        Stack<String> stack = new Stack<>();

        long start_time = System.currentTimeMillis();
        while(sc.hasNext()){
            // push on to a stack
            stack.push(sc.nextLine());
        }
        writeFileBackwards(stack);
        long end_time = System.currentTimeMillis();

        System.out.println( "Time to complete (miliseconds): " + (end_time - start_time) );


    }
}
