package frc.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.controlBoard.ControlBoard;
import frc.controlBoard.IControlBoard;
import frc.subsystems.Feeder;
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
        runAll,
        runAll2,
        runAllNoFeeder
    }

    Feeder feeder;
    Mixer mixer;
    Transport transport;
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

    public void run() {
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
        if(cb.ramp()) {
            control = Controls.ramp;
        }
        if(cb.runAll()) {
            timer.reset();
            control = Controls.runAll;
        }
        if(cb.runAllNoFeeder()) {
//            control = Controls.runAllNoFeeder;
            transport.runRamp(1);
            if((timer.get() % 1.25) <= 1) {
                mixer.rollers(Mixer.Rollers.in);
            } else if(timer.get() >= 1.5) {
                feeder.rollers(Feeder.Rollers.in);
            } else {
                mixer.rollers(Mixer.Rollers.off);


            }
        }
        else {
            transport.runRamp(0);
//            feeder.rollers(Feeder.Rollers.off);
            mixer.rollers(Mixer.Rollers.off);
        }

        // State Machine safely handles possible problems
        switch(control) {
            case none:
//                feeder.rollers(Feeder.Rollers.off);
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
            case mixer:
                mixer.rollers(Mixer.Rollers.in);
                control = Controls.none;
                break;
            case ramp:
                transport.runRamp(1);
                control = Controls.none;
                break;
            case runAll:
                transport.runRamp(1);
                mixer.rollers(Mixer.Rollers.in);
                if(timer.get() > 1.5) {
                    control = Controls.runAll2;
                }
                break;
            case runAll2:
                feeder.rollers(Feeder.Rollers.in);
                transport.runRamp(1);
                mixer.rollers(Mixer.Rollers.in);
                if(timer.get() > 4) {
                    control = Controls.none;
                }
                break;
            case runAllNoFeeder:
                transport.runRamp(1);
                mixer.rollers(Mixer.Rollers.in);
                control = Controls.none;
                break;
            default:
                control = Controls.none;
                break;
        }
    }
}
