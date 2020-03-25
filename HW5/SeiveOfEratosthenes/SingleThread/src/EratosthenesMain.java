/***************************************************************
 * file: EratosthenesMain.java
 * author: Roberto Ramirez
 * class: CS 3700 – Parallel Programming
 *
 * assignment: Homework 5
 * date last modified: 3/24/2020
 *
 * purpose: implement sieve of eratosthenes with one thread
 *
 * Execution time: ~26 milliseconds
 *
 ****************************************************************/
public class EratosthenesMain {

    // method: sieveOfEratosthenes
    // purpose: find all prime numbers given a certain range
    public static boolean[] sieveOfEratosthenes(int upperBound){
        boolean[] boolArray = new boolean[upperBound];

        for ( int j = 0; j < boolArray.length; j++){
            boolArray[j] = true;
        }

        for (int i = 2; i < Math.sqrt(upperBound); i ++ ){
            if(boolArray[i]){
                for (int j = (int)Math.pow(i, 2); j < upperBound; j += i){
                    boolArray[j] = false;
                }
            }
        }
        return boolArray;
    }

    public static void main(String args[]){

        long start_time = System.currentTimeMillis();

        boolean[] result = sieveOfEratosthenes(1000000);

        long end_time = System.currentTimeMillis();

        System.out.println("Time in miliseconds: " + (end_time - start_time) );
    }
}
