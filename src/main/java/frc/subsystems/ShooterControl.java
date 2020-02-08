package frc.subsystems;

public class ShooterControl {
    private static ShooterControl instance = null;
    public static ShooterControl getInstance() {
        if (instance == null) {
            instance = new ShooterControl();
        }
        return instance;
    }

    Flywheel flywheel;
    Hood hood;
    Turret turret;

    public enum States {
        disbabled,
        enabled,
        shooting
    }

    private ShooterControl() {
        flywheel = new Flywheel();
        hood = new Hood();
        turret = new Turret();
    }

}
