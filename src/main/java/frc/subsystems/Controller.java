package frc.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.display.UtilDisplay;
import frc.robot.Constants;
import frc.util.udpServer;
import frc.utilPackage.Units;

import java.io.IOException;
import java.net.SocketException;

public class Controller {
    private static Controller instance = null;
    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    // Adjust flywheel rpm
    double tempRPM = 3000;

    UtilDisplay utilDisplay;

    Timer timer;

    ColorWheel colorWheel;
    Feeder feeder;
    Flywheel flywheel;
    Hood hood;
    Mixer mixer;
    Transport transport;
    Turret turret;

    DigitalInput ballSensor;

    udpServer visionServer;

    private enum Feeding {
        idle, // nothing is running
        panic,
        feedIn, // start feeding in
        feedOut, // run rollers in other direction to clear jams
        runTransport, // running everything
        stopTransport, // stop running the transport system once a ball is loaded into the ramp
        shoot, // start sending balls to the shooter
        feederShoot
    }

    private enum Shooting {
        idle,
        panic,

        // States used for teleop
        start, // start shooting, get vision setpoint
        tracking, // turret tracking
        spooling, // flywheel spool up, continue to track turret
        shooting, // actively shooting balls

        // States used for auto
        running, // default state for manual control
        flywheelspool, // starts spooling flywheel then switches to running state
        flywheelstop, // stops turns off flywheel then switches to running state
        turretTrack, // starts turret tracking then switches to running state
        turretstop, // stops turret tracking then switches to running state
        turretfield /* stops turret tracking but keeps it relative to the field
                            based on its current angle, then switches to running state*/
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

    double turretSetpoint, flywheelSetpoint = 0, hoodSetpoint = 0;

    private Controller() {
        colorWheel = ColorWheel.getInstance();
        feeder = Feeder.getInstance();
        flywheel = Flywheel.getInstance();
        hood = Hood.getInstance();
        mixer = Mixer.getInstance();
        transport = Transport.getInstance();
        turret = Turret.getInstance();

        ballSensor = Constants.Transport.ballSensor;

        feeding = Feeding.idle;
        shooting = Shooting.idle;

        utilDisplay = new UtilDisplay();

        timer = new Timer();

        try {
            visionServer = new udpServer(5100);
            Thread thread = new Thread(visionServer);
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        previousCommand = Commands.idle;
    }

    public void driverInput(Commands command) {
        switch (command) {
            case panic: // panic button pressed
                feeding = Feeding.panic;
                shooting = Shooting.panic;
                break;
            case idle: // nothing is running
                feeding = Feeding.idle;
                shooting = Shooting.idle;
                break;
            case feedIn: // feeding balls in
                if (shooting == Shooting.idle) {
                    feeding = Feeding.feedIn;
                }
                break;
            case feedOut:
                feeding = Feeding.feedOut;
                break;
            case shoot: // shoot button pressed
                shooting = Shooting.start;
                break;
            case feederActuate:
                break;
        }
    }

    public void run() {
        updateSetpoints();
        switch (feeding) {
            case idle:
                feeder.rollers(Feeder.Rollers.off);
                break;
            case feedIn:
                feeding = Feeding.runTransport;
                break;
            case runTransport:
                mixer.rollers(Mixer.Rollers.in);
                transport.rollers(Transport.Rollers.onlyFront);
                if (ballSensor.get()) {
                    feeding = Feeding.stopTransport;
                }
                break;
            case stopTransport:
                feeder.rollers(Feeder.Rollers.in);
                mixer.rollers(Mixer.Rollers.off);
                transport.rollers(Transport.Rollers.off);
                break;
            case shoot:
                mixer.rollers(Mixer.Rollers.in);
                transport.rollers(Transport.Rollers.in);
                break;
            case feederShoot:
                feeder.rollers(Feeder.Rollers.in);
                break;
        }

        switch (shooting) {
            case idle:
//                flywheelSetpoint = 0;
//                turret.updateEncoder();
                flywheel.setVelocity(flywheelSetpoint);
                break;
            case start:
                feeding = Feeding.idle;
                turret.toSetpoint(trackAngle());
                shooting = Shooting.tracking;
                break;
            case tracking:
//                turret.updateEncoder();
//                turret.run();
                if (turret.atSetpoint(true)) {
//                    shooting = Shooting.spooling;
                    break;
                }
                break;
            case spooling:
//                turret.updateEncoder();
//                turret.run();
                flywheel.setVelocity(flywheelSetpoint);
                flywheel.run();
                if (Math.abs(flywheel.getVelocity() - flywheelSetpoint) < 200) {
//                    shooting = Shooting.shooting;
                    break;
                }
                break;
            case shooting:
                feeding = Feeding.shoot;
//                turret.updateEncoder();
//                turret.run();
                flywheel.run();
                break;
            /*case manualTrack:
                turret.updateEncoder();
                turret.run();
                flywheel.setVelocity(flywheelSetpoint);
                flywheel.run();
                break;
            case manualSpool:
                flywheel.setVelocity(flywheelSetpoint);
                flywheel.run();
                break;*/
        }

        hood.setAngle(50);
        hood.run();

        turret.updateEncoder();
        turret.run();
    }

    public void feederActuate() {
        feeder.actuate();
    }

    public void updateSetpoints() {
        if(utilDisplay.update()) {
            flywheelSetpoint = utilDisplay.getFlywheel();
            hoodSetpoint = utilDisplay.getHood();
        }
    }

    public void runRollers(Feeder.Rollers rollers) {
        feeder.rollers(rollers);
    }

    public void display() {
        SmartDashboard.putNumber("Vision Angle", trackData()[1]/Units.Angle.degrees);
        SmartDashboard.putNumber("Vision Distance", trackData()[0]);
        feeder.display();
        flywheel.display();
        hood.display();
        turret.display();
    }

    public double[] trackData() {
        try {
            double visionAngle = visionServer.getData()[1] * Constants.degreesToRadians;
            double currentAngle = turret.getAngle(true);
            double angleDiff = currentAngle - visionAngle;
            return new double[](visionServer.getData()[0], angleDiff);
        } catch (InterruptedException e) {
            e.printStackTrace();
            double[] emptyData = [turret.getAngle(true), turret.getAngle(true)];
        }
    }
}