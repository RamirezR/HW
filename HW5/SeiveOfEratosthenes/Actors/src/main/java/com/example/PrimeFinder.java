package com.example;

import akka.actor.typed.Behavior;
import akka.actor.typed.PostStop;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.util.ArrayList;
import java.util.Optional;

/***************************************************************
 * file: PrimeFinder.java
 * author: Roberto Ramirez
 * class: CS 3700 – Parallel Programming
 *
 * assignment: Homework 5
 * date last modified: 3/24/2020
 *
 * purpose: the single actor for finding primes
 *
 * execution time: 4 minutes and 18 seconds
 *
 ****************************************************************/
public class PrimeFinder extends AbstractBehavior<PrimeFinder.Command> {

    public interface Command {}


    public static final class RangeObj implements Command {
        final ArrayList<Integer> rangeList;

        public RangeObj(ArrayList<Integer> rangeList) {
            this.rangeList = rangeList;
        }

        // method: removeNonPrime
        // purpose: passed a value and removes any multiples of that value from the ArrayList
        public void removeNonPrime(Integer primeVal){
            for(int i = 0; i != this.rangeList.size(); i++){
                if(this.rangeList.get(i) % primeVal == 0) {
                    this.rangeList.remove(i);
                    i--;
                }
            }
        }
    }

    private Optional<Integer> primeNum = Optional.empty();

    static Behavior<Command> create() {
        // not sure about this line.
        return Behaviors.setup(PrimeFinder::new);
    }

    private PrimeFinder(ActorContext<Command> context) {
        super(context);
        //System.out.println("actor started");
        // create a new actor or send a message to this same actor
        //context.spawn(PrimeFinder.create(), "prime_finder");
    }

    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(RangeObj.class, this::findPrime)
                .onSignal(PostStop.class, signal -> onPostStop())
                .build();
    }

    // method: findPrime
    // purpose: remove the head of the array list, call the removeNonPrime, spawn another actor with the updated list, and call the system to terminate
    private Behavior<Command> findPrime(RangeObj rangeObj) {
        if(!rangeObj.rangeList.isEmpty()){
            primeNum = Optional.of(rangeObj.rangeList.remove(0));
            rangeObj.removeNonPrime(primeNum.get());
            System.out.println(primeNum.get().toString());
            getContext().spawn(PrimeFinder.create(), "primeFinder").tell(new RangeObj(rangeObj.rangeList));
            return this;
        }
        //if(rangeObj.rangeList.size() > 0)
        getContext().getSystem().terminate();
        return Behaviors.stopped();
    }

    private Behavior<Command> onPostStop() {
        //System.out.println("prime finder stopped");
        return this;
    }
}
