package com.team3250.frc2020.states;

public class SuperstructureState {
    public double turret; // degrees
    public double hood; // degrees
    public double flywheel; // rpm

    public SuperstructureState(double turret, double hood, double flywheel) {
        this.turret = turret;
        this.hood = hood;
        this.flywheel = flywheel;
    }

    public SuperstructureState(SuperstructureState other) {
        this.turret = other.turret;
        this.hood = other.hood;
        this.flywheel = other.flywheel;
    }

    // default robot position
    public SuperstructureState() {
        this(0,0,0);
    }

    public void setFrom(SuperstructureState source) {
        this.turret = source.turret;
        this.hood = source.hood;
        this.flywheel = source.flywheel;
    }
}
