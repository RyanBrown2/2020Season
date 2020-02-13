package frc.robot;

import frc.controlBoard.ControlBoard;
import frc.controlBoard.IControlBoard;
import frc.subsystems.Feeder;
import frc.subsystems.Mixer;

public class TeleopControls {
    private static IControlBoard cb = new ControlBoard();
    public static IControlBoard getControlBoard(){
        return cb;
    }

    public enum Controls {
        none,
        actuateFeeder,
        feederRollers,
        mixer
    }

    Feeder feeder;
    Mixer mixer;
    Controls control = Controls.none;

    public TeleopControls() {
        feeder = new Feeder();
        mixer = new Mixer();
    }

    public void run() {
        // Set state based on Control Board
        if(cb.feederActuate()) {
            control = Controls.actuateFeeder;
        }
        if(cb.rollers()) {
            control = Controls.feederRollers;
        }
        if(cb.mixer()) {
            control = Controls.mixer;
        }

        // State Machine safely handles possible problems
        switch(control) {
            case none:
                feeder.rollers(Feeder.Rollers.off);
                mixer.rollers(Mixer.Rollers.off);
                break;
            case actuateFeeder:
                feeder.actuate();
                control = Controls.none;
                break;
            case feederRollers:
                feeder.rollers(Feeder.Rollers.in);
                control = Controls.none;
                break;
            case mixer:
                mixer.rollers(Mixer.Rollers.in);
                control = Controls.none;
            default:
                control = Controls.none;
                break;
        }
    }
}
