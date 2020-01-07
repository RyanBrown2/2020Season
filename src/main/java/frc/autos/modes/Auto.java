package frc.autos.modes;

import frc.autos.AutoEndedException;
import frc.autos.actions.DrivePath;
import frc.autos.actions.DriveVision;
import frc.autos.actions.VisionPursuit;
import frc.coordinates.Coordinate;
import frc.drive.PositionTracker;
import frc.path.TrajectoryList;
import frc.utilPackage.TrapezoidalMp;
import frc.utilPackage.Units;

public class Auto extends AutoMode{

    DrivePath path;
    VisionPursuit vision;
    DriveVision combo;

    public Auto(){
        TrajectoryList pathList = new TrajectoryList(new Coordinate(0,0));
        pathList.add(new Coordinate(0*Units.Length.feet, 6*Units.Length.feet));
        // pathList.add(new Coordinate(-5*Units.Length.feet, 4*Units.Length.feet));

        path = new DrivePath(pathList, new TrapezoidalMp.constraints(0, 5*Units.Length.feet, 1*Units.Length.feet));
        // path = DrivePath.createFromFileOnRoboRio("Auto", "path", new TrapezoidalMp.constraints(0, 5*Units.Length.feet, 1*Units.Length.feet));
        path.setTurnCorrection(.8);
        path.setlookAhead(1*Units.Length.feet);
        path.setVerticalThresh(0.15*Units.Length.feet);
        setInitPos(0, 0);

        vision = new VisionPursuit();
        vision.setMaxVel(5*Units.Length.feet);
        vision.setDeccelDist(1*Units.Length.feet);

        combo = new DriveVision(path, vision, 0);
    }

    @Override
    public void auto() throws AutoEndedException {
        PositionTracker.getInstance().robotForward();
        // runAction(path);
        runAction(combo);
        // runAction(vision);
    }

}