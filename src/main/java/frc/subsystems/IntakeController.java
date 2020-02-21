package frc.subsystems;

import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.Timer;
import frc.controlBoard.ControlBoard;
import frc.controlBoard.IControlBoard;
import frc.robot.Robot;

public class IntakeController {
    private static IntakeController instance = null;

    public static IntakeController getInstance() {
        if (instance == null) {
            instance = new IntakeController();
        }
        return instance;
    }

    Timer timer = new Timer();
    Timer stateTimer = new Timer();

    public enum States {
        spooling,
        transport,
        mixing,
        feeder,
        end
    }

    States state;

    Boolean panicMode = false;

    private IntakeController() {
        state = States.spooling;
    }

    public void runIntake(double RPM, boolean enabled) {
        // Don't run anything if the robot is set to panic mode
        if(!panicMode) {
            if (enabled) {
                // State machine handles timing between all subsystems while shooting
                switch (state) {
                    case spooling:
                        Robot.flywheel.setVelocity(RPM);
                        // Don't go on to the next state unless the flywheel is +- 200 of its setpoint
                        if (Math.abs(Robot.flywheel.getVelocity() - RPM) < 200) {
                            state = States.transport;
                        }
                        break;
                    case transport:
                        // Transport always runs
                        Robot.transport.rollers(Transport.Rollers.in);
                        state = States.mixing;
                        break;
                    case mixing:
                        // Start the timer if it isn't already started
                        if (stateTimer.get() == 0) {
                            stateTimer.start();
                        }
                        // Run the pulsing of the mixer
                        pulseMixer();
                        // After 0.65 secs of mixer run the feeder
                        if (stateTimer.get() > 0.65) {
                            state = States.feeder;
                        }
                        break;
                    case feeder:
                        pulseMixer();
                        Robot.feeder.rollers(Feeder.Rollers.maxIn);
                        // Stop running feeder after 1 second
                        if (stateTimer.get() > 1) {
                            Robot.feeder.rollers(Feeder.Rollers.off);
                            state = States.end;
                        }
                        break;
                    case end:
                        // Continue pulsing mixer to ensure all balls go through
                        pulseMixer();
                        break;
                }
            } else {
                // Else statement serves as a reset for timers and state machine
                state = States.spooling;
                timer.stop();
                timer.reset();
                stateTimer.stop();
                stateTimer.reset();
            }
        } else {
            // If robot is in panic mode, stop everything intake-related
            Robot.flywheel.setVelocity(0);
            Robot.mixer.rollers(Mixer.Rollers.off);
            Robot.transport.rollers(Transport.Rollers.off);
            Robot.feeder.rollers(Feeder.Rollers.off);
        }
    }

    // Run the mixer on fixed intervals for optimal performance
    public void pulseMixer() {
        // If timer is not already running, start it
        if(timer.get() == 0) {
            timer.start();
        }
        // Use modulus operator to limit the timer value to 0.75 seconds
        if ((timer.get() % 0.75) < 0.5) {
            // Run rollers for 0.5 seconds
            Robot.mixer.rollers(Mixer.Rollers.in);
        } else {
            // Stop rollers for 0.25 seconds
            Robot.mixer.rollers(Mixer.Rollers.off);
        }
    }

    // Panic mode shuts down all intake-related subsystems
    public void panic(boolean isPanic) {
        panicMode = isPanic;
    }
}