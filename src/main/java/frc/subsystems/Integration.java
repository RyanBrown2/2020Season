package frc.subsystems;

public class Integration extends Thread {
    private static Integration instance = null;
    public static Integration getInstance() {
        if(instance == null) {
            instance = new Integration();
        }
        return instance;
    }

    private enum States {
        disabled,
        starting,
        running,
        panic
    }

    private Feeder feeder;
    private Flywheel flywheel;
    private Hood hood;
    private Turret turret;

    private Integration() {
        feeder = new Feeder();
        flywheel = new Flywheel();
        hood = new Hood();
        turret = new Turret();
    }

    @Override
    public void run() {

    }

}
