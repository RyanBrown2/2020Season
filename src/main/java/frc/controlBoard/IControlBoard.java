package frc.controlBoard;

public abstract class IControlBoard {
    // Driver
    public abstract double getThrottle();
    public abstract double getWheel();
    public abstract boolean rollers();
    public abstract boolean rollersPressed();
    public abstract boolean rollersReleased();
    public abstract boolean reverseFeeder();
    public abstract boolean reverseFeederPressed();
    public abstract boolean reverseFeederReleased();


    // CoDrive
    public abstract boolean feederActuatePressed();
    public abstract boolean feederActuateReleased();
    public abstract boolean climbArms();
    public abstract boolean climbArmsPressed();
    public abstract boolean climbArmsReleased();
    public abstract boolean climb();
    public abstract boolean climbPressed();
    public abstract boolean climbReleased();
    public abstract boolean colorWheelActuate();
    public abstract boolean colorWheelRoller();
    public abstract boolean colorWheelRollerReleased();
    public abstract boolean trackClockwise();
    public abstract boolean trackCounterClockwise();
    public abstract boolean unjam();
    public abstract boolean unjamReleased();
    public abstract boolean visionTrackPressed();
    public abstract boolean panic();


    // Both
    public abstract boolean shoot();
    public abstract boolean shootPressed();
    public abstract boolean shootReleased();

}