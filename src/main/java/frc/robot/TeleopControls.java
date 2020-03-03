package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import frc.controlBoard.IControlBoard;
import frc.subsystems.*;

public class TeleopControls {
    private static IControlBoard cb = Robot.getControlBoard();

    Control controller = Control.getInstance();
    Transport transport = Transport.getInstance();
    Mixer mixer = Mixer.getInstance();
    Feeder feeder = Feeder.getInstance();
    ColorWheel colorWheel = ColorWheel.getInstance();

    DigitalInput ballSensor = Constants.Transport.ballSensor;

    boolean ballState = false;

    boolean isTracking = false;

    Timer ballTimer = new Timer();

    public TeleopControls() {

    }

    public void run() {
        if(ballSensor.get()) {
            if(ballTimer.get() == 0) {
                ballTimer.start();
            } if(ballTimer.get() >= 0.25) {
                ballState = true;
                ballTimer.stop();
                ballTimer.reset();
            }
        }

        // Buttons run different actions
        if(cb.rollers()) {
            feeder.rollers(Feeder.Rollers.maxIn);
            if(!ballState) {
                mixer.rollers(Mixer.Rollers.slowIn);
                transport.rollers(Transport.Rollers.onlyFront);
            } else {
                mixer.rollers(Mixer.Rollers.off);
                transport.rollers(Transport.Rollers.off);
            }
        } if(cb.rollersReleased()) {
            feeder.rollers(Feeder.Rollers.off);
            mixer.rollers(Mixer.Rollers.off);
            transport.rollers(Transport.Rollers.off);
        } if(cb.feederActuatePressed()) {
            feeder.actuate();
        } if(cb.reverseFeederPressed()) {
            feeder.rollers(Feeder.Rollers.out);
        } if(cb.reverseFeederReleased()) {
            feeder.rollers(Feeder.Rollers.off);
        } if(cb.shoot()) {
            // Enable the shooter
//            controller.setVelocity(4100);
            controller.setEnabled(true);
        } if(cb.shootReleased()) {
            // Disable the shooter
//            controller.setVelocity(0);
            controller.setEnabled(false);
            ballState = false;
            mixer.rollers(Mixer.Rollers.off);
            transport.rollers(Transport.Rollers.off);
            feeder.rollers(Feeder.Rollers.off);
        } if(cb.colorWheelActuate()) {
            colorWheel.actuate();
        } if(cb.colorWheelRoller()) {
            colorWheel.roller(ColorWheel.Roller.clockWise);
        } if(cb.colorWheelRollerReleased()) {
            colorWheel.roller(ColorWheel.Roller.off);
        } if(cb.trackClockwise()) {
            controller.scanClockwise();
        } if(cb.trackCounterClockwise()) {
            controller.scanCounterClockwise();
        } if(cb.panic()) {
            controller.panic(true);
        }
    }

    public void display() {}
}