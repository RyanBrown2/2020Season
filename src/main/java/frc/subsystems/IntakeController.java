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
    public static IControlBoard getControlBoard(){
        return cb;
    }

    Feeder feeder;

    public enum States {
        disabled,
        enabled,
        feederOut,
        rollers,
        feederIn,
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

                break;
            case feederOut:
                if (feeder.isIn()) {
                    feeder.actuate();
                }
                break;
            case rollers:
                feeder.rollers(cb.rollers());
                break;
            case feederIn:
                if (feeder.isOut()) {
                    feeder.actuate();
                }
                break;
            case panic:

                break;
        }
    }

    public void feederActuate() {
        if (feeder.isIn()) {
            state = States.feederOut;
        } else if (feeder.isOut()) {
            state = States.feederIn;
        }
    }

    public void rollers() {
        if (feeder.isOut()) {
            state = States.rollers;
        }
    }

    public void panic() {

    }
}