package frc.autos.modes;

import frc.utilPackage.Units;
import frc.utilPackage.TrapezoidalMp;

import frc.autos.actions.*;
import frc.drive.PositionTracker;
import frc.autos.AutoEndedException;

public class PathTest extends AutoMode {

    DrivePath firstPath;

    public PathTest() {

        TrapezoidalMp.constraints constraints = new TrapezoidalMp.constraints(0, 4 * Units.Length.feet, 2 * Units.Length.feet);
        firstPath = DrivePath.createFromFileOnRoboRio("PathTest", "testPath", constraints);
        firstPath.setHorizontalThresh(1 * Units.Length.feet);
        firstPath.setlookAhead(2 * Units.Length.feet);

        setInitPos(0, 0);

    }

    public void auto() throws AutoEndedException {
        PositionTracker.getInstance().robotForward();
        runAction(firstPath);
    }
}