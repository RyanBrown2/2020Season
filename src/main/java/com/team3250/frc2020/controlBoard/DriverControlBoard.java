package com.team3250.frc2020.controlBoard;

import edu.wpi.first.wpilibj.Joystick;

public class DriverControlBoard implements IDriverControlBoard {
    private static DriverControlBoard mInstance = null;

    public static DriverControlBoard getInstance() {
        if (mInstance == null) {
            mInstance = new DriverControlBoard();
        }
        return mInstance;
    }

    private final Joystick mJoystick;
    private final Joystick mWheel;

    private DriverControlBoard() {
        mJoystick = new Joystick(ControlBindings.Driver.Joystick.kUsbSlot);
        mWheel = new Joystick(ControlBindings.Driver.Wheel.kUsbSlot);
    }

    @Override
    public double getThrottle() {
        return mJoystick.getY();
    }

    @Override
    public double getWheel() {
        return mWheel.getX();
    }

    @Override
    public boolean getQuickTurn() {
        return mJoystick.getRawButton(ControlBindings.Driver.Joystick.kQuickTurn);
    }

    @Override
    public boolean getShoot() {
        return mJoystick.getRawButton(ControlBindings.Driver.Joystick.kShoot);
    }

    @Override
    public boolean getRollers() {
        return mJoystick.getRawButton(ControlBindings.Driver.Joystick.kRollers);
    }
}
