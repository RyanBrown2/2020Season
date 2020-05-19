package com.team3250.frc2020;

import com.team3250.frc2020.auto.modes.AutoModeBase;
import com.team3250.frc2020.auto.modes.DoNothingMode;
import com.team3250.frc2020.auto.modes.TestMode;
import com.team3250.frc2020.utilPackage.Derivative;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.Optional;

public class AutoModeSelector {

    enum StartingPosition {
        CENTER
    }

    enum DesiredMode {
        DO_NOTHING,
        TEST
    }

    private DesiredMode mCachedDesiredMode = null;
    private StartingPosition mCachedStartingPosition = null;

    private SendableChooser<DesiredMode> mModeChooser;
    private SendableChooser<StartingPosition> mStartPositionChooser;

    private Optional<AutoModeBase> mAutoMode = Optional.empty();

    public AutoModeSelector() {
        mStartPositionChooser = new SendableChooser<>();
        mStartPositionChooser.setDefaultOption("Center", StartingPosition.CENTER);

        SmartDashboard.putData("Starting Position", mStartPositionChooser);

        mModeChooser = new SendableChooser<>();
        mModeChooser.setDefaultOption("Test", DesiredMode.TEST);
        mModeChooser.addOption("Do Nothing", DesiredMode.DO_NOTHING);
        SmartDashboard.putData("Auto Mode", mModeChooser);
    }

    public void updateModeCreator() {
        DesiredMode desiredMode = mModeChooser.getSelected();
        StartingPosition startingPosition = mStartPositionChooser.getSelected();
        if (mCachedDesiredMode != desiredMode || startingPosition != mCachedStartingPosition) {
            System.out.println("Auto selection changed, updating creator: desiredMode->" + desiredMode.name()
                    + ", starting position->" + startingPosition.name());
            mAutoMode = getAutoModeForParams(desiredMode, startingPosition);
        }
        mCachedDesiredMode = desiredMode;
        mCachedStartingPosition = startingPosition;
    }

    private Optional<AutoModeBase> getAutoModeForParams(DesiredMode mode, StartingPosition position) {
        switch (mode) {
            case DO_NOTHING:
                return Optional.of(new DoNothingMode());
            case TEST:
                return Optional.of(new TestMode());
            default:
                break;
        }

        System.err.println("No valid auto mode found for  " + mode);
        return Optional.empty();
    }


}
