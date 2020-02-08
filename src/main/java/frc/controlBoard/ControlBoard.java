package frc.controlBoard;

import edu.wpi.first.wpilibj.Joystick;
import frc.robot.Constants;

public class ControlBoard extends IControlBoard{
    Joystick joy, wheel, buttonPad, cojoy;

    public ControlBoard() {
        joy = new Joystick(0);
        wheel = new Joystick(1);
        buttonPad = new Joystick(2);
        cojoy = new Joystick(3);
    }

	@Override
	public double getThrottle() {
        if (Math.abs(joy.getY()) < 0.05) {
            return 0;
        } else {
            return joy.getY();
        }
	}

    @Override
    public double getWheel() {
        double output;
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
    public boolean feeder() {
        return cojoy.getRawButtonPressed(ControlBindings.CoDriver.feeder);
    }

    @Override
    public boolean rollers() {
        return joy.getRawButton(ControlBindings.Driver.rollers);
    }

    @Override
    public boolean panic() {
        return buttonPad.getRawButtonPressed(ControlBindings.CoDriver.panic);
    }

}