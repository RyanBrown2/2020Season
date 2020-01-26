package frc.autos.modes;

import frc.utilPackage.Units;
import frc.utilPackage.TrapezoidalMp;

import frc.autos.actions.*;
import frc.drive.PositionTracker;
import frc.autos.AutoEndedException;

public class PathTest extends AutoMode {

    DrivePath pathOne, pathTwo;

    public PathTest() {
        TrapezoidalMp.constraints constraints = new TrapezoidalMp.constraints(0, 16 * Units.Length.feet, 16 * Units.Length.feet);
        TrapezoidalMp.constraints revConstraints = new TrapezoidalMp.constraints(0, 10 * Units.Length.feet, 8 * Units.Length.feet);

        pathOne = DrivePath.createFromFileOnRoboRio("TestAuto", "underThing", constraints);
        pathOne.setHorizontalThresh(1 * Units.Length.feet);
        pathOne.setlookAhead(4 * Units.Length.feet);

        pathTwo = DrivePath.createFromFileOnRoboRio("TestAuto", "reverseToShoot", revConstraints);
        pathTwo.setReverse(true);
        pathTwo.setHorizontalThresh(1*Units.Length.feet);
        pathTwo.setlookAhead(4 * Units.Length.feet);

        setInitPos(0, 0);

    }

    public void auto() throws AutoEndedException {
        PositionTracker.getInstance().robotForward();
        runAction(pathOne);
        runAction(pathTwo);
    }
}