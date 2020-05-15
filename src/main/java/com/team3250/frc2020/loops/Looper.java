package com.team3250.frc2020.loops;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.wpilibj.Timer;

/**
 * This code runs all of the robot's loops. Loop objects are stored in a List object. They are started when the robot
 * powers up and stopped after the match.
 */
public class Looper implements ILooper{

    private final double kPeriod = 0.05;

    private boolean running, pRunning;

    private final List<Loop> loops;

    public Looper(){
        running = true;
        pRunning = false;
        loops = new ArrayList<>();
    }

    @Override
    public synchronized void register(Loop loop) {
        loops.add(loop);
    }

    public synchronized void run(){
        double timestamp = Timer.getFPGATimestamp();
        if(running){
            for(Loop loop : loops){
                if(!pRunning){
                    loop.onStart(timestamp);
                }
                loop.onLoop(timestamp);
            }
        }else {
            if(pRunning){
                for(Loop loop : loops){
                    loop.onStop(timestamp);
                }
            }
        }
        pRunning = running;
    }

    public synchronized void start(){
        running = true;
        pRunning = false;
    }

    public synchronized void stop(){
        running = false;
    }
}