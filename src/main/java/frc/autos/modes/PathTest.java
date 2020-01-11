package frc.autos.modes;

import frc.utilPackage.Units;
import frc.utilPackage.TrapezoidalMp;

import frc.autos.actions.*;
import frc.drive.PositionTracker;
import frc.autos.AutoEndedException;

public class PathTest extends AutoMode {

    DrivePath firstPath;

    public PathTest() {

        TrapezoidalMp.constraints constraints = new TrapezoidalMp.constraints(0, 12 * Units.Length.feet, 6 * Units.Length.feet);
        firstPath = DrivePath.createFromFileOnRoboRio("Testing", "toGoal", constraints);
        firstPath.setHorizontalThresh(1 * Units.Length.feet);
        firstPath.setlookAhead(0.5 * Units.Length.feet);

        setInitPos(0, 0);

    }

    public void auto() throws AutoEndedException {
        PositionTracker.getInstance().robotForward();
        runAction(firstPath);
    }
}