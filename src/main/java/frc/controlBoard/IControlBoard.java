package frc.controlBoard;

public abstract class IControlBoard {
    // Driver
    public abstract double getThrottle();
    public abstract double getWheel();
    public abstract boolean rollersPressed();
    public abstract boolean rollersReleased();
    public abstract boolean mixerPressed();
    public abstract boolean mixerReleased();
    public abstract boolean rampPressed();
    public abstract boolean rampReleased();
    public abstract boolean reverseFeederPressed();
    public abstract boolean reverseFeederReleased();


    // CoDrive
    public abstract boolean feederActuatePressed();
    public abstract boolean feederActuateReleased();
    public abstract boolean panic();

    // Both
    public abstract boolean shootPressed();
    public abstract boolean shootReleased();

}