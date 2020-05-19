package com.team3250.frc2020.controlBoard;

import edu.wpi.first.wpilibj.Joystick;

public class CoDriverControlBoard implements ICoDriverControlBoard {
    private static CoDriverControlBoard mInstance = null;

    public static CoDriverControlBoard getInstance() {
        if (mInstance == null) {
            mInstance = new CoDriverControlBoard();
        }
        return mInstance;
    }

    private final Joystick mButtonBoard;
    private final Joystick mJoystick;

    private CoDriverControlBoard() {
        mButtonBoard = new Joystick(ControlBindings.CoDriver.ButtonBoard.kUsbSlot);
        mJoystick = new Joystick(ControlBindings.CoDriver.Joystick.kUsbSlot);
    }

    @Override
    public boolean getShoot() {
        return mButtonBoard.getRawButton(ControlBindings.CoDriver.ButtonBoard.kShoot);
    }

    @Override
    public boolean getFeeder() {
        return mJoystick.getRawButton(ControlBindings.CoDriver.Joystick.kFeeder);
    }

    @Override
    public boolean getRollers() {
        return mJoystick.getRawButton(ControlBindings.CoDriver.Joystick.kRollers);
    }
}
