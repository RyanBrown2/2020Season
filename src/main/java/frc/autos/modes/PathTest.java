package frc.autos.modes;

import frc.utilPackage.Units;
import frc.utilPackage.TrapezoidalMp;

import frc.autos.actions.*;
import frc.drive.PositionTracker;
import frc.autos.AutoEndedException;

public class PathTest extends AutoMode {

    DrivePath underPanel, reverseToShoot, toFirstBall, underClimbArea, reverseUnderClimb, finalShot;

    public PathTest() {
        TrapezoidalMp.constraints constraints = new TrapezoidalMp.constraints(0, 4*Units.Length.feet, 2*Units.Length.feet);
        TrapezoidalMp.constraints revConstraints = new TrapezoidalMp.constraints(0, 2*Units.Length.feet, 1*Units.Length.feet);

        underPanel = DrivePath.createFromFileOnRoboRio("TestAuto", "underPanel", constraints);
        underPanel.setReverse(false);
        underPanel.setHorizontalThresh(1*Units.Length.feet);
        underPanel.setlookAhead(1*Units.Length.feet);

        reverseToShoot = DrivePath.createFromFileOnRoboRio("TestAuto", "reverseToShoot", revConstraints);
        reverseToShoot.setReverse(true);
        reverseToShoot.setHorizontalThresh(1*Units.Length.feet);
        reverseToShoot.setlookAhead(1*Units.Length.feet);

        toFirstBall = DrivePath.createFromFileOnRoboRio("TestAuto", "toFirstBall", constraints);
        toFirstBall.setReverse(false);
        toFirstBall.setHorizontalThresh(1*Units.Length.feet);
        toFirstBall.setlookAhead(1*Units.Length.feet);

        underClimbArea = DrivePath.createFromFileOnRoboRio("TestAuto", "underClimbArea", constraints);
        underClimbArea.setReverse(false);
        underClimbArea.setHorizontalThresh(1*Units.Length.feet);
        underClimbArea.setlookAhead(1*Units.Length.feet);

        reverseUnderClimb = DrivePath.createFromFileOnRoboRio("TestAuto", "reverseUnderClimb", revConstraints);
        reverseUnderClimb.setReverse(true);
        reverseUnderClimb.setHorizontalThresh(1*Units.Length.feet);
        reverseUnderClimb.setlookAhead(1*Units.Length.feet);

        finalShot = DrivePath.createFromFileOnRoboRio("TestAuto", "finalShot", constraints);
        finalShot.setReverse(false);
        finalShot.setHorizontalThresh(1*Units.Length.feet);
        finalShot.setlookAhead(4*Units.Length.feet);

        setInitPos(0, 0);

    }

    public void auto() throws AutoEndedException {
        PositionTracker.getInstance().robotForward();
        runAction(underPanel);
        runAction(reverseToShoot);
        runAction(toFirstBall);
        runAction(underClimbArea);
        runAction(reverseUnderClimb);
        runAction(finalShot);
    }
}