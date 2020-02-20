package frc.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.controlBoard.ControlBoard;
import frc.controlBoard.IControlBoard;
import frc.subsystems.*;

public class TeleopControls {
    private static IControlBoard cb = new ControlBoard();
    public static IControlBoard getControlBoard(){
        return cb;
    }

    Feeder feeder = Robot.feeder;
    Flywheel flywheel = Robot.flywheel;
    Mixer mixer = Robot.mixer;
    Transport transport = Robot.transport;

    IntakeController intakeController = Robot.intakeController;

    // Timer for shooting timing
    Timer timer = new Timer();

    // Whether or not to shoot
    boolean shooting = false;

    public TeleopControls() {

    }

    public void run() {


        // Run flywheel PID loops
        flywheel.run();

        // Run shooter when enabled
        intakeController.runIntake(3000, shooting);
//        shooter();

        // Buttons
        if(cb.rollersPressed()) {
            feeder.rollers(Feeder.Rollers.maxIn);
        } if(cb.rollersReleased()) {
            feeder.rollers(Feeder.Rollers.off);
        } if(cb.mixerPressed()) {
            transport.rollers(Transport.Rollers.onlyFront);
            mixer.rollers(Mixer.Rollers.slowIn);
        } if(cb.mixerReleased()) {
            transport.rollers(Transport.Rollers.off);
            mixer.rollers(Mixer.Rollers.off);
        } if(cb.feederActuatePressed()) {
            feeder.actuate();
        } if(cb.feederActuateReleased()) {
            feeder.rollers(Feeder.Rollers.off);
        } if(cb.reverseFeederPressed()) {
            feeder.rollers(Feeder.Rollers.out);
        } if(cb.reverseFeederReleased()) {
            feeder.rollers(Feeder.Rollers.off);
        } if(cb.rampPressed()) {
            transport.rollers(Transport.Rollers.in);
        } if(cb.rampReleased()) {
            transport.rollers(Transport.Rollers.off);
        } if(cb.shootPressed()) {
            intakeController.intakeReset();
            shooting = true;
//            flywheel.setVelocity(3000);
//            shooting = true;
        } if(cb.shootReleased()) {
//            timer.stop();
//            timer.reset();
            shooting = false;
            flywheel.setVelocity(0);
            mixer.rollers(Mixer.Rollers.off);
            transport.rollers(Transport.Rollers.off);
            feeder.rollers(Feeder.Rollers.off);
        }
    }

    public void shooter() {
        if(shooting) {
            if (Math.abs(flywheel.getVelocity() - 3000) < 200) {
                if(timer.get() == 0) {
                    timer.start();
                }
                transport.rollers(Transport.Rollers.in);
                if ((timer.get() % 0.75) < 0.5) {
                    mixer.rollers(Mixer.Rollers.in);
                } else if (timer.get() >= 0.5 && timer.get() <= 0.65) {
                    mixer.rollers(Mixer.Rollers.off);
                } else if (timer.get() > 0.65 && timer.get() <= 1) {
                    feeder.rollers(Feeder.Rollers.maxIn);
                } else if (timer.get() > 1) {
                    feeder.rollers(Feeder.Rollers.off);
                    mixer.rollers(Mixer.Rollers.off);
                } else {
                    mixer.rollers(Mixer.Rollers.off);
                    feeder.rollers(Feeder.Rollers.off);
                }
            }
        }
    }

    public void display() {
        flywheel.display();
        SmartDashboard.putNumber("Timer Val Absolute", timer.get());
    }
}
