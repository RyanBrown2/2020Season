package frc.controlBoard;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;

public class ControlBoard extends IControlBoard{
    Joystick joy1, wheel, buttonPad, cojoy;

    public ControlBoard() {
        joy1 = new Joystick(0);
        wheel = new Joystick(1);
        buttonPad = new Joystick(2);
        cojoy = new Joystick(3);
    }

    @Override
    public boolean led() {
        return !buttonPad.getRawButton(4);
    }

    @Override
    public boolean getHatchHigh() {
        return buttonPad.getRawButton(13);
    }

    @Override
    public boolean getHatchMid() {
        return buttonPad.getRawButton(17);
    }

    @Override
    public boolean getHatchLow() {
        return buttonPad.getRawButton(20);
    }

    @Override
    public boolean getReset() {
        if (buttonPad.getRawButton(23)) {
            return true;
        } else if (buttonPad.getRawButton(16)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean cargoship() {
        return buttonPad.getRawButton(12);
    }

    @Override
    public boolean ballPickup() {
        return buttonPad.getRawButton(19);
    }

    @Override
    public boolean ballMode() {
        return buttonPad.getRawButton(7);
    }

    @Override
    public boolean telescopeReset() {
        return buttonPad.getRawButton(22);
    }

	@Override
	public double getThrottle() {
        if (Math.abs(joy1.getY()) < 0.05) {
            return 0;
        } else {
            return joy1.getY();
        }
	}

    @Override
    public double getWheel() {
        double output;
//        SmartDashboard.putNumber("Wheel Raw", wheel.getX());
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
    public boolean getQuickTurn() {
        return wheel.getRawButton(5);
    }

    @Override
    public boolean rollers() {
        return joy1.getRawButton(1);
    }

    @Override
    public boolean rollersInvert() {
        return joy1.getRawButton(7);
    }

    @Override
    public boolean visionIn() {
        return joy1.getRawButton(5);
    }

    @Override
    public boolean visionOut() {
        return joy1.getRawButton(6);
    }

    @Override
    public boolean visionPressed() {
        return joy1.getRawButtonPressed(4);
    }

    @Override
    public boolean fullDrive() {
        return joy1.getRawButton(2);
    }

    @Override
    public double cojoyPOV() {
        return cojoy.getPOV(0);
    }

    @Override
    public boolean wristOffset() {
        if (cojoyPOV() == -1) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public int wristDirection() {
        if (cojoyPOV() == 0) {
            return 1;
        } else if (cojoyPOV() == 180) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public boolean xOffset() {
//        return cojoy.getRawButton(5);
        return false;
    }

    @Override
    public boolean yOffset() {
        return cojoy.getRawButton(6);
    }

    @Override
    public double cojoyX() {
        return cojoy.getX();
    }

    @Override
    public double cojoyY() {
        return cojoy.getY();
    }

    @Override
    public boolean climbMode() {
        return buttonPad.getRawButton(6);
    }

    @Override
    public boolean climbUp() {
        return buttonPad.getRawButton(11);
    }

    @Override
    public boolean footForward() {
        return buttonPad.getRawButton(15);
    }

    @Override
    public boolean footUp() {
        return buttonPad.getRawButton(18);
    }

    @Override
    public boolean lowClimb() {
        return buttonPad.getRawButton(2);
    }
}