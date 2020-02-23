package frc.robot;

import frc.controlBoard.IControlBoard;
import frc.subsystems.*;

public class TeleopControls {
    private static IControlBoard cb = Robot.getControlBoard();

    Feeder feeder = Robot.feeder;
    Flywheel flywheel = Robot.flywheel;
    Mixer mixer = Robot.mixer;
    Transport transport = Robot.transport;

    IntakeController intakeController = Robot.intakeController;

    public TeleopControls() {

    }

    public void run() {
        // Buttons run different actions
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
        } if(cb.reverseFeederPressed()) {
            feeder.rollers(Feeder.Rollers.out);
        } if(cb.reverseFeederReleased()) {
            feeder.rollers(Feeder.Rollers.off);
        } if(cb.rampPressed()) {
            transport.rollers(Transport.Rollers.in);
        } if(cb.rampReleased()) {
            transport.rollers(Transport.Rollers.off);
        } if(cb.shootPressed()) {
            // Enable the shooter
            intakeController.setVelocity(3000);
            intakeController.setEnabled(true);
        } if(cb.shootReleased()) {
            // Disable the shooter
            intakeController.setVelocity(0);
            intakeController.setEnabled(false);
            // Disable running subsystems
            mixer.rollers(Mixer.Rollers.off);
            transport.rollers(Transport.Rollers.off);
            feeder.rollers(Feeder.Rollers.off);
        }
    }

    public void display() {
        flywheel.display();
    }
}