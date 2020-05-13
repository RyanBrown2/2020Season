package frc.subsystems;

import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.util.SerialReader;
import frc.util.Vision;
import frc.utilPackage.Units;

public class Control {
    private static Control instance = null;
    public static Control getInstance() {
        if (instance == null) {
            instance = new Control();
        }
        return instance;
    }

    Timer stateTimer = new Timer();

    public enum States {
        scanning,
        tracking,
        finalTracking,
        spooling,
        shooting,
        transport,
        mixing,
        feeder,
        end
    }

    States state;

    boolean panicMode = false;

    boolean enabled = false;
    double RPM = 0;

    double visionAngle;

    double[] dataLookUp;

    double lastVision = 0;

    Feeder feeder;
    Flywheel flywheel;
    Hood hood;
    Mixer mixer;
    Transport transport;
    Turret turret;

    Vision vision;
    SerialReader serialVision;

    Timer shootWait;


    boolean autoOverride = false;
    double initialSetpoint, prevSetpoint, nextSetpoint;

    private Control() {
        feeder = Feeder.getInstance();
        flywheel = Flywheel.getInstance();
        hood = Hood.getInstance();
        mixer = Mixer.getInstance();
        transport = Transport.getInstance();
        turret = Turret.getInstance();

        // todo Switch over to serial
        vision = Vision.getInstance();
        serialVision = new SerialReader(0);

        shootWait = new Timer();

        state = States.scanning;

        hood.setAngle(60);
    }

    public void setEnabled(boolean enable) {
        enabled = enable;
    }

    // DON'T USE FOR TELEOP
    public void setVelocity(double rpms) {
        RPM = rpms;
        flywheel.setVelocity(rpms);
    }

    public void autoOverride(boolean override) {
        autoOverride = override;
    }

    public void scanClockwise() {
        if(vision.getDistance() == 0) {
            if(turret.atSetpoint(true)) {
                prevSetpoint = turret.setPoint;
                nextSetpoint = prevSetpoint - 10 * Units.Angle.degrees;
                turret.toSetpoint(nextSetpoint);
            }
        }
    }

    public void scanCounterClockwise() {
        if(vision.getDistance() == 0) {
            if(turret.atSetpoint(true)) {
                prevSetpoint = turret.setPoint;
                nextSetpoint = prevSetpoint + 10 * Units.Angle.degrees;
                turret.toSetpoint(nextSetpoint);
            }
        }
    }

    public void manualTrack() {
        turret.toSetpoint(vision.offsetAngle(turret.getAngle(true), vision.getAngle()));
    }

    public void run() {
//        hood.setAngle(33);
//        hood.run();
        flywheel.run();
//        turret.run(true);
        // Don't run anything if the robot is set to panic mode
        if(!panicMode) {
            if (enabled) {
                // State machine handles timing between all subsystems while shooting
                switch (state) {
                    case scanning:
                        // Scan For Target

                        // Get target data

                        // todo

                        // If target found
                        state = States.tracking;
                        break;
                    case tracking:
                        // Move To Target

                        // todo

                        // If at turret at setpoint
                        state = States.finalTracking;
                        break;
                    case finalTracking:
                        // Final Checks

                        // Scan for target

                        // Check Target Angle

                        // todo

                        // If all checks passed
                        state = States.spooling;
                        break;
                    case spooling:
                        flywheel.setVelocity(RPM);

                        // Don't go on to the next state unless the flywheel is +- 200 of its setpoint and turret at setpoint
                        if (Math.abs(flywheel.getVelocity() - RPM) < 200) {
                            state = States.transport;
                        }
                        break;
                    case transport:
                        // Transport always runs
                        transport.rollers(Transport.Rollers.in);
                        state = States.mixing;
                        break;
                    case mixing:
                        // Start the timer if it isn't already started
                        if (stateTimer.get() == 0) {
                            stateTimer.start();
                        }
                        mixer.rollers(Mixer.Rollers.in);
                        // After 0.3 secs of mixer run the feeder
                        if (stateTimer.get() > 0.4) {
                            state = States.feeder;
                        }
                        break;
                    case feeder:
                        feeder.rollers(Feeder.Rollers.maxIn);
                        state = States.end;
                        break;
                    case end:
                        break;
                }
            } else {
                // Else statement serves as a reset for timers and state machine
                if(!autoOverride) {
                    flywheel.setVelocity(0);
                }
                state = States.scanning;
                stateTimer.stop();
                stateTimer.reset();
            }
        } else {
            // If robot is in panic mode, stop everything intake-related
            flywheel.setVelocity(0);
            mixer.rollers(Mixer.Rollers.off);
            transport.rollers(Transport.Rollers.off);
            feeder.rollers(Feeder.Rollers.off);
        }
        feeder.startup();
    }

    public enum Direction {left, right}

    public void turretScan(Direction direction) { // todo
        if (!vision.cameraTracking()) {
            if (direction == Direction.left) {
                turret.toSetpoint(vision.offsetAngle(turret.getAngle(true), 5 * Units.Angle.degrees));
            }

            if (direction == Direction.right) {
                turret.toSetpoint(vision.offsetAngle(turret.getAngle(true), -5 * Units.Angle.degrees));
            }
        }
    }

    public void unJamFlywheel(boolean on) {
        if (on) {
            flywheel.jam = true;
        } else {
            flywheel.jam = false;
        }
    }

    // Panic mode shuts down all intake-related subsystems
    public void panic(boolean isPanic) {
        panicMode = isPanic;
    }

    public void display() {
        vision.display();
        SmartDashboard.putString("Controller State", state.toString());
        SmartDashboard.putNumber("Vision Angle", vision.getAngle());
        feeder.display();
        flywheel.display();
        hood.display();
        turret.display();
    }
}