package frc.subsystems;

import edu.wpi.first.wpilibj.Timer;
import frc.util.Vision;

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
        tracking,
        spooling,
        transport,
        mixing,
        feeder,
        end
    }

    States state;

    boolean panicMode = false;

    boolean enabled = false;
    double RPM = 0;

    Feeder feeder;
    Flywheel flywheel;
    Hood hood;
    Mixer mixer;
    Transport transport;
    Turret turret;

    Vision vision;

    private Control() {
        feeder = Feeder.getInstance();
        flywheel = Flywheel.getInstance();
        hood = Hood.getInstance();
        mixer = Mixer.getInstance();
        transport = Transport.getInstance();
        turret = Turret.getInstance();

        vision = Vision.getInstance();

        state = States.tracking;
    }

    public void setEnabled(boolean enable) {
        enabled = enable;
    }

    public void setVelocity(double rpms) {
        RPM = rpms;
        flywheel.setVelocity(rpms);
    }

    public void run() {
        hood.setAngle(33);
        hood.run();
        flywheel.run();
        turret.run(true);
        // Don't run anything if the robot is set to panic mode
        if(!panicMode) {
            if (enabled) {
                // State machine handles timing between all subsystems while shooting
                switch (state) {
                    // Get tracking data from vision and set turret setpoint, then switch to spooling state
                    case tracking:
                        // Determines and moves the turret to track target
                        turret.toSetpoint(vision.offsetAngle(turret.getAngle(true), vision.getAngle()));
                        state = States.spooling;
                        break;
                    case spooling:
                        flywheel.setVelocity(RPM);
                        // Don't go on to the next state unless the flywheel is +- 200 of its setpoint and turret at setpoint
                        if (Math.abs(flywheel.getVelocity() - RPM) < 200 && turret.atSetpoint(true)) {
//                            turret.toSetpoint(vision.offsetAngle(turret.getAngle(false), vision.getAngle()));
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
                state = States.tracking;
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
    }

    // Panic mode shuts down all intake-related subsystems
    public void panic(boolean isPanic) {
        panicMode = isPanic;
    }

    public void display() {
        feeder.display();
        flywheel.display();
        hood.display();
        turret.display();
    }
}