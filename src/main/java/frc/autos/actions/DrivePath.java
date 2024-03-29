package frc.autos.actions;

import frc.drive.DriveOutput;
import frc.drive.PositionTracker;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.path.DriveFollower;
import frc.path.ProfileHolder;
import frc.path.SplineSegmentFiller;
import frc.path.TrajectoryList;
import frc.utilPackage.TrapezoidalMp;
import frc.utilPackage.Units;

public class DrivePath extends Action{
    TrajectoryList segment;
    DriveFollower follower;
    boolean isDone = false;
    double parallelTrackThresh = 0.5*Units.Length.feet;
    double crossTrackThresh = 0.5*Units.Length.feet;
    TrapezoidalMp mp; 
    TrapezoidalMp.constraints constraints; 
    ProfileHolder pHolder; 
    boolean reverse = false;

    public static DrivePath createFromFileOnRoboRio(String fileName, String path,
    TrapezoidalMp.constraints constraints){
        String filePath = "/home/lvuser/deploy/Autos/"+fileName+".json";
        System.out.println("filler");
        SplineSegmentFiller filler = new SplineSegmentFiller(filePath, path);
        System.out.println("segment");
        TrajectoryList segment = filler.generate();
        return new DrivePath(segment, constraints);
    }

    public static DrivePath createFromFileOnRoboRio(String fileName, String path, 
    TrapezoidalMp.constraints constraints, int numOfSplines){
        String filePath = "/home/lvuser/deploy/Autos/"+fileName+".json";
        SplineSegmentFiller filler = new SplineSegmentFiller(filePath, path);
        filler.setPointsPerSpline(numOfSplines);
        TrajectoryList segment = filler.generate();
        return new DrivePath(segment, constraints);
    }

    public DrivePath(TrajectoryList segment, TrapezoidalMp.constraints constraints){
        follower = new DriveFollower();
        this.segment = segment;
        this.constraints = constraints;
    }

    public DrivePath(TrajectoryList segment, TrapezoidalMp.constraints constraints, double driveThresh){
        follower = new DriveFollower();
        this.segment = segment;
        this.constraints = constraints;
        this.parallelTrackThresh = driveThresh;
    }

    public void setlookAhead(double lookAhead){
        follower.setLookAhead(lookAhead);
    }

    public void setTurnCorrection(double turnCorrection){
        follower.setTurnCorrection(turnCorrection);
    }

    public void setReverse(boolean reverse){
        this.reverse = reverse;
    }

    public void setVerticalThresh(double thresh){
        parallelTrackThresh = thresh;
    }
    public void setHorizontalThresh(double thresh){
        crossTrackThresh = thresh;
    }

    public void setMp(TrapezoidalMp mp) {
        this.mp = mp;
    }

    @Override
    public void start() {
        isDone = false;
        constraints.setpoint = segment.getTotalDistance();
        mp = new TrapezoidalMp(constraints);
        pHolder = new ProfileHolder(mp);
        pHolder.setTimeSeg(0.05);
        pHolder.generate();
        // double totalDist = segment.getTotalDistance();
        // for(double dist = 0; dist < totalDist; dist += totalDist/10){
        //     System.out.println("Dist: "+dist+"\tVel: "+pHolder.calculateVel(dist));
        // }
    }

    @Override
    public void update() {
        double dist = segment.getDistOnPath();
        double vel = pHolder.calculateVel(dist);
        vel = Math.max(2*Units.Length.feet, vel);
        if(reverse)
            vel *= -1;
        follower.update(segment, vel);
        SmartDashboard.putNumber("Current Trajectory ID", segment.getCurrentID());
        SmartDashboard.putNumber("Distance on Path", dist);
        SmartDashboard.putNumber("Total Distance", segment.getTotalDistance());
        isDone = segment.isDone(PositionTracker.getInstance().getPosition().getPos(), parallelTrackThresh, crossTrackThresh);
        if(dist/segment.getTotalDistance() < 0.1){
            isDone = false;
        }
        SmartDashboard.putBoolean("DrivePath done?", isDone);
        if(isDone){
            done();
        }
    }

    @Override
    public void done() {
        DriveOutput.getInstance().setNoVoltage();
    }

    @Override
    public boolean isFinished() {
        return isDone;
    }
}