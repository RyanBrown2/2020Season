package frc.subsystems;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.Robot;

public class Control {
    private static Control instance = null;

    public static Control getInstance() {
        if (instance == null) {
            instance = new Control();
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

    boolean panicMode = false;

    boolean enabled = false;
    double RPM = 0;

    Feeder feeder;
    Flywheel flywheel;
    Hood hood;
    Mixer mixer;
    Transport transport;
    Turret turret;

    private Control() {
        feeder = Feeder.getInstance();
        flywheel = Flywheel.getInstance();
        hood = Hood.getInstance();
        mixer = Mixer.getInstance();
        transport = Transport.getInstance();
        turret = Turret.getInstance();

        state = States.spooling;
    }

    public void setEnabled(boolean enable) {
        enabled = enable;
    }

    public void setVelocity(double rpms) {
        RPM = rpms;
        flywheel.setVelocity(rpms);
    }

    public void run() {
        flywheel.run();
        // Don't run anything if the robot is set to panic mode
        if(!panicMode) {
            if (enabled) {
                // State machine handles timing between all subsystems while shooting
                switch (state) {
                    case spooling:
                        flywheel.setVelocity(RPM);
                        // Don't go on to the next state unless the flywheel is +- 200 of its setpoint
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
                        // Stop running feeder after 0.25 seconds
                        if (stateTimer.get() > 0.8) {
                            feeder.rollers(Feeder.Rollers.off);
                            state = States.end;
                        }
                        break;
                    case end:
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
            flywheel.setVelocity(0);
            mixer.rollers(Mixer.Rollers.off);
            transport.rollers(Transport.Rollers.off);
            feeder.rollers(Feeder.Rollers.off);
        }
    }

    // Run the mixer on fixed intervals for optimal performance
    public void pulseMixer() {
        // If timer is not already running, start it
        if(timer.get() == 0) {
            timer.start();
        }
        // Use modulus operator to limit the timer value to 0.75 seconds
        if ((timer.get() % (0.5 + 0.125)) < 0.5) {
            // Run rollers for 0.5 seconds
            mixer.rollers(Mixer.Rollers.in);
        } else {
            // Stop rollers for 0.25 seconds
            mixer.rollers(Mixer.Rollers.off);
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