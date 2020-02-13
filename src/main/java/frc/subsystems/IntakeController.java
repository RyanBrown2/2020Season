package frc.subsystems;

import edu.wpi.first.wpilibj.RobotState;
import frc.controlBoard.ControlBoard;
import frc.controlBoard.IControlBoard;

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

    Feeder feeder;

    public enum States {
        disabled,
        enabled,
        idle,
        intakeOut,
        rollers,
        intakeIn,
        feeding,
        panic
    }

    States state;

    private IntakeController() {
        feeder = new Feeder();

        state = States.disabled;
    }

    public void runIntake() {
        switch (state) {
            case disabled:
                if (RobotState.isEnabled()) {
                    state = States.enabled;
                }
                break;
            case enabled:
                state = States.idle;
                break;
            case idle:
                break;
            case intakeOut:
                feeder.actuate();
                break;
            case rollers:

                break;
            case intakeIn:

                break;
            case feeding:

                break;
            case panic:

                break;
        }
    }

    public void feederActuateTeleop() {
        if (busy()) {
            return;
        } else {
            feeder.actuate();
        }
    }

    public void rollersTeleop(boolean on) {
        if (busy()) {
            feeder.rollers(Feeder.Rollers.off);
        } else {
            if (on) {
                feeder.rollers(Feeder.Rollers.in);
            } else {
                feeder.rollers(Feeder.Rollers.off);
            }
        }
    }

    // Used to prevent teleop controls from overriding the feeder
    public boolean busy() {
        if (state==States.feeding) {
            return true;
        } else {
            return false;
        }
    }


    public void panic() {

    }
}