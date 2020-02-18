package frc.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.controlBoard.ControlBoard;
import frc.controlBoard.IControlBoard;
import frc.subsystems.Feeder;
import frc.subsystems.Flywheel;
import frc.subsystems.Mixer;
import frc.subsystems.Transport;

public class TeleopControls {
    private static IControlBoard cb = new ControlBoard();

    public static IControlBoard getControlBoard(){
        return cb;
    }

    public enum Controls {
        none,
        actuateFeeder,
        feederRollers,
        mixer,
        ramp,
        reverseFeeder
    }

    Feeder feeder;
    Mixer mixer;
    Transport transport;
    public static Flywheel flywheel = new Flywheel();
    Timer timer;
    Controls control = Controls.none;

    public TeleopControls() {

        feeder = new Feeder();
        mixer = new Mixer();
        transport = new Transport();
        timer = new Timer();
        timer.start();
    }

    public void resetTimers() {
        timer.reset();
    }

    public void display() {
        flywheel.display();
    }

    public void run() {
        flywheel.run();
        SmartDashboard.putNumber("Timer w/ Modulus", (timer.get() % 1.25));
        // Set state based on Control Board
        if(cb.feederActuate()) {
            control = Controls.actuateFeeder;
        }
        if(cb.rollers()) {
            control = Controls.feederRollers;
//            feeder.rollers(Feeder.Rollers.in);
        }
        if(cb.mixer()) {
            control = Controls.mixer;
        }
        if(cb.reverseFeeder()) {
            control = Controls.reverseFeeder;
        }
        if(cb.ramp()) {
            control = Controls.ramp;
        }
        if(cb.runAllNoFeeder()) {
//            control = Controls.runAllNoFeeder;
            flywheel.setVelocity(2000);
            if(Math.abs(flywheel.getVelocity() - 2000) < 200) {
                transport.runRamp(1, false);
                if ((timer.get() % 1.25) <= 1) {
                    mixer.rollers(Mixer.Rollers.in);
                } else if (timer.get() >= 1.5) {
                    feeder.rollers(Feeder.Rollers.in);
                    mixer.rollers(Mixer.Rollers.off);
                } else {
                    mixer.rollers(Mixer.Rollers.off);
                }
            }
        }
        else {
            flywheel.setVelocity(0);
            transport.runRamp(0, false);
            feeder.rollers(Feeder.Rollers.off);
            mixer.rollers(Mixer.Rollers.off);
        }

        // State Machine safely handles possible problems
        switch(control) {
            case none:
                feeder.rollers(Feeder.Rollers.off);
//                mixer.rollers(Mixer.Rollers.off);
//                transport.runRamp(0);
                break;
            case actuateFeeder:
                feeder.actuate();
                control = Controls.none;
                break;
            case feederRollers:
                feeder.rollers(Feeder.Rollers.maxIn);
                control = Controls.none;
                break;
            case reverseFeeder:
                feeder.rollers(Feeder.Rollers.out);
                control = Controls.none;
                break;
            case mixer:
                transport.runRamp(1, true);
                mixer.rollers(Mixer.Rollers.in);
                control = Controls.none;
                break;
            case ramp:
                transport.runRamp(1, false);
                control = Controls.none;
                break;
            default:
                control = Controls.none;
                break;
        }
    }
}
