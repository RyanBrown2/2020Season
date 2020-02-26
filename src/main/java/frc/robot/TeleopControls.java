package frc.robot;

import frc.controlBoard.IControlBoard;
import frc.subsystems.*;

public class TeleopControls {
    private static IControlBoard cb = Robot.getControlBoard();

    Controller controller = Controller.getInstance();

    public TeleopControls() {

    }

    public void run() {
        // Buttons run different actions
        if(cb.rollersPressed()) {
            controller.driverInput(Controller.Commands.feedIn);
        } if(cb.rollersReleased()) {
            controller.driverInput(Controller.Commands.idle);
        } if(cb.mixerPressed()) {

        } if(cb.mixerReleased()) {

        } if(cb.feederActuatePressed()) {
            controller.feederActuate();
        } if(cb.reverseFeederPressed()) {
            controller.driverInput(Controller.Commands.feedOut);
        } if(cb.reverseFeederReleased()) {
            controller.driverInput(Controller.Commands.idle);
        } if(cb.rampPressed()) {

        } if(cb.rampReleased()) {

        } if(cb.shootPressed()) {
            // Enable the shooter
            controller.driverInput(Controller.Commands.shoot);
        } if(cb.shootReleased()) {
            // Disable the shooter
            controller.driverInput(Controller.Commands.idle);
        } if(cb.panic()) {
            controller.driverInput(Controller.Commands.panic);
        }
    }

    public void display() {}
}