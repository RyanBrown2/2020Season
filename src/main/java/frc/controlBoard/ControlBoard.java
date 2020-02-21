package frc.controlBoard;

import edu.wpi.first.wpilibj.Joystick;
import frc.robot.Constants;

public class ControlBoard extends IControlBoard{
    Joystick joy, wheel, buttonPad, cojoy;

    public ControlBoard() {
        joy = new Joystick(0);
        wheel = new Joystick(1);
        cojoy = new Joystick(2);
        buttonPad = new Joystick(3);
    }

	@Override
	public double getThrottle() {
        if (Math.abs(joy.getY()) < 0.025) {
            return 0;
        } else {
            return joy.getY();
        }
	}

    @Override
    public double getWheel() {
        if (Math.abs(wheel.getX()) < 0.05) {
            return 0;
        } else {
            if (Constants.Drive.headingInvert) {
                return -wheel.getX();
            } else {
                return wheel.getX();
            }
        }
    }

    @Override
    public boolean feederActuatePressed() {
        return cojoy.getRawButtonPressed(ControlBindings.CoDriver.feeder);
    }

    @Override
    public boolean feederActuateReleased() {
        return cojoy.getRawButtonReleased(ControlBindings.CoDriver.feeder);
    }

    @Override
    public boolean rollersPressed() {
        return (joy.getRawButton(ControlBindings.Driver.rollers) || cojoy.getRawButton(ControlBindings.CoDriver.rollers));
    }

    @Override
    public boolean rollersReleased() {
        return ((joy.getRawButtonReleased(ControlBindings.Driver.rollers) ||
                cojoy.getRawButtonReleased(ControlBindings.CoDriver.rollers)) &&
                !joy.getRawButton(ControlBindings.Driver.rollers) &&
                !cojoy.getRawButton(ControlBindings.CoDriver.rollers));
    }

    @Override
    public boolean rampPressed() {
        return cojoy.getRawButton(ControlBindings.CoDriver.ramp);
    }

    @Override
    public boolean rampReleased() {
        return cojoy.getRawButtonReleased(ControlBindings.CoDriver.ramp);
    }

    @Override
    public boolean reverseFeederPressed() {
        return buttonPad.getRawButton(ControlBindings.ButtonPad.reverseFeeder);
    }

    @Override
    public boolean reverseFeederReleased() {
        return buttonPad.getRawButtonReleased(ControlBindings.ButtonPad.reverseFeeder);
    }

    @Override
    public boolean shootPressed() {
        return (joy.getRawButtonPressed(ControlBindings.Driver.shoot) || buttonPad.getRawButtonPressed(ControlBindings.ButtonPad.shoot));
    }

    @Override
    public boolean shootReleased() {
        return ((joy.getRawButtonReleased(ControlBindings.Driver.shoot) ||
                buttonPad.getRawButtonReleased(ControlBindings.ButtonPad.shoot)) &&
                !joy.getRawButton(ControlBindings.Driver.shoot) &&
                !buttonPad.getRawButton(ControlBindings.ButtonPad.shoot));
    }

    @Override
    public boolean mixerPressed() {
        return buttonPad.getRawButton(ControlBindings.ButtonPad.mixer);
    }

    @Override
    public boolean mixerReleased() {
        return buttonPad.getRawButtonReleased(ControlBindings.ButtonPad.mixer);
    }

    @Override
    public boolean panic() {
        return buttonPad.getRawButtonPressed(ControlBindings.CoDriver.panic);
    }

}