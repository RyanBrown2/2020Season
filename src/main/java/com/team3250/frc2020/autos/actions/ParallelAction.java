package com.team3250.frc2020.autos.actions;

import java.util.*;

public class ParallelAction extends Action{
    List<Action> actions;

//    public ParallelAction(List<Action> actions){
//        this.actions = new ArrayList<>(actions);
//    }

    public ParallelAction(Action... actions){
        this.actions = new ArrayList<Action>(Arrays.asList(actions));
    }

//    public ParallelAction(Action action1, Action action2){
//        this.actions = new ArrayList<>(Arrays.asList(action1, action2));
//    }

    @Override
    public void start() {
        for(Action action : actions){
            action.start();
        }
    }

    @Override
    public void update() {
        for(Action action : actions){
            if(!action.isFinished())
                action.update();
        }
    }

    @Override
    public void done() {
        for(Action action : actions){
            action.done();
        }
    }

    @Override
    public boolean isFinished() {
        for(Action action : actions){
            if(!action.isFinished()){
                return false;
            }
        }
        return true;
    }
}