package frc.controlBoard;

public abstract class IControlBoard {
    // Co-Driver
    public abstract boolean led();
    public abstract boolean getHatchHigh();
    public abstract boolean getHatchMid();
    public abstract boolean getHatchLow();
    public abstract boolean getReset();
    public abstract boolean ballPickup();
    public abstract boolean ballMode();
    public abstract boolean cargoship();
    public abstract boolean wristOffset();
    public abstract boolean yOffset();
    public abstract boolean xOffset();
    public abstract double cojoyPOV();
    public abstract double cojoyX();
    public abstract double cojoyY();
    public abstract boolean telescopeReset();

    public abstract boolean climbMode();
    public abstract boolean climbUp();
    public abstract boolean footForward();
    public abstract boolean footUp();
    public abstract boolean lowClimb();

    // Driver
    public abstract double getThrottle();
    public abstract double getWheel();
    public abstract boolean getQuickTurn();
    public abstract boolean rollers();
    public abstract boolean rollersInvert();
    public abstract boolean visionIn();
    public abstract boolean visionOut();
    public abstract boolean fullDrive();

    // System
    public abstract boolean visionPressed();
    public abstract int wristDirection();

}