package com.team3250.frc2020.controlBoard;

import edu.wpi.first.wpilibj.Joystick;
import com.team3250.frc2020.Constants;

public class ControlBoard implements IControlBoard{
    private static ControlBoard mInstance = null;

    public static ControlBoard getInstance() {
        if (mInstance == null) {
            mInstance = new ControlBoard();
        }
        return mInstance;
    }

    private final IDriverControlBoard mDriveControlBoard;
    private final ICoDriverControlBoard mCoDriveControlBoard;

    private ControlBoard() {
        mDriveControlBoard = DriverControlBoard.getInstance();
        mCoDriveControlBoard = CoDriverControlBoard.getInstance();
    }

    @Override
    public boolean getFeeder() {
        return mCoDriveControlBoard.getFeeder();
    }

    @Override
    public double getThrottle() {
        return mDriveControlBoard.getThrottle();
    }

    @Override
    public double getWheel() {
        return mDriveControlBoard.getWheel();
    }

    @Override
    public boolean getQuickTurn() {
        return mDriveControlBoard.getQuickTurn();
    }

    @Override
    public boolean getShoot() {
        return mDriveControlBoard.getShoot() || mCoDriveControlBoard.getShoot();
    }

    @Override
    public boolean getRollers() {
        return mDriveControlBoard.getRollers() || mCoDriveControlBoard.getRollers();
    }
}