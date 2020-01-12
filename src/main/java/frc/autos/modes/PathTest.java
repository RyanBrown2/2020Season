package frc.autos.modes;

import frc.path.TrajectoryList;
import frc.utilPackage.Units;
import frc.utilPackage.TrapezoidalMp;

import frc.autos.actions.*;
import frc.drive.PositionTracker;
import frc.autos.AutoEndedException;

public class PathTest extends AutoMode {

    DrivePath pathOne, pathTwo, pathThree, revPathOne;

    public PathTest() {

        TrapezoidalMp.constraints constraints = new TrapezoidalMp.constraints(0, 2 * Units.Length.feet, 1 * Units.Length.feet);
        pathOne = DrivePath.createFromFileOnRoboRio("TestAuto", "pt1", constraints);
        pathOne.setHorizontalThresh(1 * Units.Length.feet);
        pathOne.setlookAhead(0.5 * Units.Length.feet);

        pathTwo = DrivePath.createFromFileOnRoboRio("TestAuto", "pt2", constraints);
        pathTwo.setHorizontalThresh(1 * Units.Length.feet);
        pathTwo.setlookAhead(0.5 * Units.Length.feet);

        pathThree = DrivePath.createFromFileOnRoboRio("TestAuto", "pt3", constraints);
        pathThree.setHorizontalThresh(1 * Units.Length.feet);
        pathThree.setlookAhead(0.5 * Units.Length.feet);

        revPathOne = DrivePath.createFromFileOnRoboRio("TestAuto", "rpt1", constraints);
        revPathOne.setHorizontalThresh(1 * Units.Length.feet);
        revPathOne.setlookAhead(0.5 * Units.Length.feet);
        revPathOne.setReverse(true);

        setInitPos(0, 0);

    }

    public void auto() throws AutoEndedException {
        PositionTracker.getInstance().robotForward();
        runAction(pathOne);
        runAction(pathTwo);
        runAction(pathThree);
        runAction(revPathOne);
    }
}