package frc.autos.modes;

import frc.utilPackage.Units;
import frc.utilPackage.TrapezoidalMp;

import frc.autos.actions.*;
import frc.drive.PositionTracker;
import frc.autos.AutoEndedException;

public class PathTest extends AutoMode {

    DrivePath underPanel, reverseToShoot, toFirstBall;

    public PathTest() {
        TrapezoidalMp.constraints constraints = new TrapezoidalMp.constraints(0, 10*Units.Length.feet, 5*Units.Length.feet);
        TrapezoidalMp.constraints revConstraints = new TrapezoidalMp.constraints(0, 10*Units.Length.feet, 5*Units.Length.feet);

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

        setInitPos(0, 0);

    }

    public void auto() throws AutoEndedException {
        PositionTracker.getInstance().robotForward();
        runAction(underPanel);
        runAction(reverseToShoot);
        runAction(toFirstBall);
    }
}