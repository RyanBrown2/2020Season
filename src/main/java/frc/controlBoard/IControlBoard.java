package frc.controlBoard;

public abstract class IControlBoard {
    // Driver
    public abstract double getThrottle();
    public abstract double getWheel();
    public  abstract boolean rollers();
    public abstract boolean mixer();
    public abstract boolean ramp();
    public abstract boolean reverseFeeder();
    public abstract boolean runAllNoFeeder();

    // CoDrive
    public abstract boolean feederActuate();
    public abstract boolean panic();

}