package com.example;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;

import java.util.ArrayList;

/***************************************************************
 * file: PrimeFinderMain.java
 * author: Roberto Ramirez
 * class: CS 3700 – Parallel Programming
 *
 * assignment: Homework 5
 * date last modified: 3/24/2020
 *
 * purpose: start the PrimeFinder actor
 *
 * Execution time: 4 minutes and 18 seconds
 *
 ****************************************************************/
public class PrimeFinderMain {

    public static ArrayList<Integer> initializeArray(int startRange, int endRange) {
        ArrayList<Integer> range = new ArrayList<>();
        for (int i = startRange; i <= endRange; i++){
            range.add(i);
        }
        return range;
    }

    public static void main(String args[]){
        ArrayList<Integer> range = initializeArray(2, 10000);
        ActorRef<PrimeFinder.Command> testKit = ActorSystem.create(PrimeFinder.create(), "first");
        testKit.tell(new PrimeFinder.RangeObj(range));
    }
}
