package frc.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.Constants;
import frc.robot.Robot;

public class Controller {
    private static Controller instance = null;

    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    ColorWheel colorWheel;
    Feeder feeder;
    Flywheel flywheel;
    Hood hood;
    Mixer mixer;
    Transport transport;
    Turret turret;

    DigitalInput ballSensor;

    private enum Feeding {
        idle,
        feedIn,
        feedOut,
        runAll,
        runFeeder
    }

    private enum Shooting {
        idle,
        startShooting,
        tracking,
        spooling,
        shooting
    }

    public enum Commands {
        idle,
        feedIn,
        feedOut,
        feederActuate,
        trackingToggle,
        shoot,
        panic
    }

    Feeding feeding;
    Shooting shooting;
    Commands previousCommand;


    double turretSetpoint, flywheelSetpoint;

    private Controller() {
        colorWheel = ColorWheel.getInstance();
        feeder = Feeder.getInstance();
        hood = Hood.getInstance();
        mixer = Mixer.getInstance();
        transport = Transport.getInstance();
        turret = Turret.getInstance();

        ballSensor = Constants.Transport.ballSensor;

        feeding = Feeding.idle;
        shooting = Shooting.idle;

        previousCommand = Commands.idle;
    }

    public void driverInput(Commands command){
        switch (command) {
            case panic:
                feeding = Feeding.idle;
                shooting = Shooting.idle;
                break;
            case idle:
                break;
            case feedIn:
                if (shooting == Shooting.idle) {
                    feeding = Feeding.runFeeder;
                    shooting = Shooting.idle;
                }
                break;
            case feedOut:
                if (shooting == Shooting.idle) {
                    feeding = Feeding.feedOut;
                }
                break;
            case shoot:
                feeding = Feeding.idle;
                shooting = Shooting.startShooting;
                break;
            case feederActuate:
                if (shooting == Shooting.idle) {
                    feederActuate();
                }
                break;
            case trackingToggle:
                break;
        }
        previousCommand = command;
    }

    public void run() {
        switch (feeding) {
            case idle:
                feeder.rollers(Feeder.Rollers.off);
                break;
            case feedIn:
                feeding = Feeding.runAll;
                break;
            case runAll:
                feeder.rollers(Feeder.Rollers.in);
                mixer.rollers(Mixer.Rollers.in);
                transport.rollers(Transport.Rollers.onlyFront);
                if (ballSensor.get()) {
                    feeding = Feeding.runAll;
                }
                break;
            case runFeeder:
                feeder.rollers(Feeder.Rollers.in);
                mixer.rollers(Mixer.Rollers.off);
                transport.rollers(Transport.Rollers.off);
                break;
        }

        switch (shooting) {
            case idle:
                turret.updateEncoder();
                flywheel.setVelocity(0);
                break;
            case startShooting:
                shooting = Shooting.tracking;
                break;
            case tracking:
                turret.updateEncoder();
                turret.toSetpoint(turretSetpoint);
                turret.run();
                break;
            case spooling:
                turret.updateEncoder();
                turret.run();
                flywheel.setVelocity(flywheelSetpoint);
                flywheel.run();
                if(Math.abs(flywheel.getVelocity() - flywheelSetpoint) < 200) {
                    shooting = Shooting.shooting;
                }
                break;
            case shooting:
                turret.updateEncoder();
                turret.run();
                flywheel.run();
                break;
        }

    }

    public void feederActuate() {
        feeder.actuate();
    }

    public void display() {
        feeder.display();
        flywheel.display();
        hood.display();
        turret.display();
    }

//    public void trackVision() {
//        visionAngle = getAngle() * Constants.degreesToRadians;
//        currentAngle = Robot.turret.getAngle(true);
//        angleDiff = currentAngle - visionAngle;
//        turret.toSetpoint(angleDiff);
}