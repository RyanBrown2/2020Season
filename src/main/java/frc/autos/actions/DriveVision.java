package frc.autos.actions;

import frc.coordinates.Coordinate;
import frc.coordinates.Heading;
import frc.drive.DriveOutput;
import frc.drive.PositionTracker;
import frc.drive.DriveOutput.Modes;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.path.DriveFollower;
import frc.path.ProfileHolder;
import frc.path.SplineSegmentFiller;
import frc.path.TrajectoryList;
import frc.util.Jevois;
import frc.utilPackage.Derivative;
import frc.utilPackage.TrapezoidalMp;
import frc.utilPackage.Units;
import frc.utilPackage.Util;

public class DriveVision extends Action{

    DrivePath path;
    VisionPursuit vision;

    Boolean isDone = false;
    
    double distanceOnPath;

    public enum States {
        pathSetup,
        path,
        visionSetup,
        vision,
        finish;
    }

    States state;

    public DriveVision(DrivePath path, VisionPursuit vision, double percentOfPath) {
        this.path = path;
        this.vision = vision;
        distanceOnPath = percentOfPath*path.segment.getTotalDistance();
    }

    @Override
    public void start() {
        state = States.pathSetup;
    }

    @Override
    public void update() {
        SmartDashboard.putString("State", getState());
        switch (state) {
            case pathSetup:
                path.start();
                isDone = false;
                state = States.path;
                break;
            case path:
                if (Jevois.getInstance().useVision() && path.segment.getDistOnPath() > distanceOnPath){
                    isDone = false;
                    state = States.visionSetup;
                } else {
                    isDone = false;
                    path.update();
                }
                if(path.isFinished()){
                    path.done();
                    isDone = true;
                    return;
                }
                break;
            case visionSetup:
                isDone = false;
                path.done();
                vision.start();
                state = States.vision;
                break;
            case vision:
                isDone = false;
                vision.update();
                if(vision.isFinished()){
                    isDone = true;
                    state = States.finish;
                }
                break;
            case finish:
                vision.done();
                isDone = true;
                break;
        }        
    }

    @Override
    public void done() {
        vision.done();
    }

    @Override
    public boolean isFinished() {
        return isDone;
    }

    public String getState(){
        return state.toString();
    }
    
}