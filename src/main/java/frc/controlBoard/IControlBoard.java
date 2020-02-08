package frc.controlBoard;

public abstract class IControlBoard {
    // Driver
    public abstract double getThrottle();
    public abstract double getWheel();
    public  abstract boolean rollers();

    // CoDrive
    public abstract boolean feederActuate();
    public abstract boolean panic();

}