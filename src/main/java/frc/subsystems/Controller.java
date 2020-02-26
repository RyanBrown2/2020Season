package frc.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.Constants;
import frc.util.udpServer;

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
        flywheel = Flywheel.getInstance();
        hood = Hood.getInstance();
        mixer = Mixer.getInstance();
        transport = Transport.getInstance();
        turret = Turret.getInstance();

        ballSensor = Constants.Transport.ballSensor;

        feeding = Feeding.idle;
        shooting = Shooting.idle;

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
            case panic:
                feeding = Feeding.idle;
                shooting = Shooting.idle;
                break;
            case idle:
                feeding = Feeding.idle;
                shooting = Shooting.idle;
                break;
            case feedIn:
                if (shooting == Shooting.idle) {
                    feeding = Feeding.runFeeder;
                    shooting = Shooting.idle;
                }
                break;
            case feedOut:
                if (shooting == Shooting.idle) {
//                    feeding = Feeding.feedOut;
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
                turret.toSetpoint(trackAngle());
                shooting = Shooting.tracking;
                break;
            case tracking:
                turret.updateEncoder();
                turret.run();
                if (turret.atSetpoint(false)) {
                    shooting = Shooting.spooling;
                }
                break;
            case spooling:
                turret.updateEncoder();
                turret.run();
                flywheel.setVelocity(flywheelSetpoint);
                flywheel.run();
                if (Math.abs(flywheel.getVelocity() - flywheelSetpoint) < 200) {
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

    public double trackAngle() {
        try {
            double visionAngle = visionServer.getData()[1] * Constants.degreesToRadians;
            double currentAngle = turret.getAngle(false);
            double angleDiff = currentAngle - visionAngle;
            return angleDiff;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return turret.getAngle(false);
        }
    }
}