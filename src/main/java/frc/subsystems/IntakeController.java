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

    private static IControlBoard cb = new ControlBoard();

    public static IControlBoard getControlBoard() {
        return cb;
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

    private IntakeController() {
        state = States.spooling;
    }

    public void intakeReset() {
        state = States.spooling;
        timer.stop();
        timer.reset();
        stateTimer.stop();
        stateTimer.reset();
    }

    public void runIntake(double RPM, boolean enabled) {
        if(enabled) {
            switch (state) {
                case spooling:
                    Robot.flywheel.setVelocity(RPM);
                    if (Math.abs(Robot.flywheel.getVelocity() - RPM) < 200) {
                        state = States.transport;
                    }
                    break;
                case transport:
                    Robot.transport.rollers(Transport.Rollers.in);
                    state = States.mixing;
                    break;
                case mixing:
                    if (stateTimer.get() == 0) {
                        stateTimer.start();
                    }
                    pulseMixer();
                    if (stateTimer.get() > 0.65) {
                        state = States.feeder;
                    }
                    break;
                case feeder:
                    Robot.feeder.rollers(Feeder.Rollers.maxIn);
                    if (stateTimer.get() > 1) {
                        Robot.feeder.rollers(Feeder.Rollers.off);
                        state = States.end;
                    }
                    break;
                case end:
                    break;
            }
        }
    }

    public void pulseMixer() {
        if(timer.get() == 0) {
            timer.start();
        }
        if ((timer.get() % 0.75) < 0.5) {
            Robot.mixer.rollers(Mixer.Rollers.in);
        } else {
            Robot.mixer.rollers(Mixer.Rollers.off);
        }
    }

    public void panic() {

    }
}