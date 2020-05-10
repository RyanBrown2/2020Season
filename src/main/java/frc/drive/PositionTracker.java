package frc.drive;

import java.util.Arrays;
import com.ctre.phoenix.sensors.PigeonIMU;
import frc.coordinates.*;
import edu.wpi.first.wpilibj.Timer;
import frc.display.PositionTrackerDisplay;
import frc.robot.Constants;
import frc.utilPackage.Units;
import frc.utilPackage.Util;

public class PositionTracker implements Runnable {
    private static PositionTracker instance = null;
    public static PositionTracker getInstance(){
        if(instance == null)
            instance = new PositionTracker();
        return instance;
    }

    private PigeonIMU pigeon;
    private double[] ypr = new double[3];
    private Coordinate position = new Coordinate();
    private Heading heading = new Heading();
    private Pos2D fullPos = new Pos2D();
    private Pos2D visionData;
    private double offset;
    private PositionTrackerDisplay positionTrackerDisplay;

    private PositionTracker(){
        positionTrackerDisplay = new PositionTrackerDisplay();
        pigeon = new PigeonIMU(Constants.Drive.gyro);
        pigeon.setFusedHeading(0.0);
    }

    public void setInitPos(Coordinate pos){
        position = new Coordinate(pos);
    }

    public void setInitPosFeet(double x, double y){
        position = new Coordinate(x, y).mult(Units.Length.feet);
    }

    public void resetHeading(){
        offset = getRawAngle();
    }

    public void robotForward(){
        offset = getRawAngle();
    }

    public void robotBackward(){
        offset = getRawAngle() + 180;
    }

    public void run() {

        double last = Timer.getFPGATimestamp();
        heading = new Heading();
        heading.setRobotAngle(getAngle());
        Heading pHeading = new Heading(heading);
        Timer.delay(0.02);
        Drive mDrive = Drive.getInstance();
        double pCircum = Util.average(Arrays.asList(mDrive.getLeftPosition(), mDrive.getRightPosition()));
        double cCircum = pCircum;

        double cAngle = getEncoderAngle(mDrive);
        double pAngle = cAngle;
        while(true){
            double dt = Timer.getFPGATimestamp() - last;
            last = Timer.getFPGATimestamp();

            pHeading.setAngle(heading.getAngle());
            heading.setRobotAngle(getAngle());
            Heading tempHeading = Heading.headingBetweenHeadings(heading, pHeading);
            
            pCircum = cCircum;
            cCircum = Util.average(Arrays.asList(mDrive.getLeftPosition(), mDrive.getRightPosition()));
            double dCircum = cCircum - pCircum;
            pAngle = cAngle;
            cAngle = getEncoderAngle(mDrive);
            double dAngle = Math.abs(cAngle - pAngle);

            double radius = dCircum/dAngle;
            double distance;
            if(!Double.isFinite(radius) || Double.isNaN(radius) || radius == 0 || 
                Util.inErrorRange(dAngle/dt, 0, 1*Units.Angle.degrees)){
                distance = dCircum;
            }else{
                distance = radius*Math.sqrt(Math.max(2-2*Math.cos(dAngle), 0));
            }
            tempHeading.setMagnitude(distance);
    
            Pos2D nextPos = new Pos2D(position, tempHeading);
            position = nextPos.getEndPos();


            fullPos.setPos(position);
            fullPos.setHeading(heading);

//            Timer.delay(0.005);
        }
    }

    private double getRawAngle(){
        PigeonIMU.GeneralStatus genStatus = new PigeonIMU.GeneralStatus();
        PigeonIMU.FusionStatus fusionStatus = new PigeonIMU.FusionStatus();
        double angle = pigeon.getFusedHeading(fusionStatus);
        return angle;
    }

    private double getAngle(){
        return (getRawAngle()-offset)*Units.Angle.degrees;
    }
    
    private double getEncoderAngle(Drive drive){
        return (drive.getLeftPosition() - drive.getRightPosition())/(Constants.robotWidth);
    }

    public synchronized Pos2D getPosition() {
        return fullPos;
    }

    public synchronized Pos2D getReversePosition(){
        Pos2D output = new Pos2D(fullPos);
        output.getHeading().multC(-1);
        return output;
    }

    public Heading getVelocity() {
        return new Heading(heading);
    }

    public synchronized void setVisionPos(Pos2D visionData){
        this.visionData = visionData;
    }

    public void display(){
        if(positionTrackerDisplay.locationReset()){
            double x = positionTrackerDisplay.getXReset();
            double y = positionTrackerDisplay.getYReset();
            setInitPosFeet(x, y);
        }

        if(positionTrackerDisplay.headingReset()){
            robotForward();
        }

        try{
            Coordinate position = new Coordinate(this.position);
            position.mult(1/Units.Length.feet);

            positionTrackerDisplay.xPosition(position.getX());
            positionTrackerDisplay.yPosition(position.getY());
            positionTrackerDisplay.setAngle(getAngle()/Units.Angle.degrees);

        }catch(Exception e){
            e.printStackTrace();
        }
        positionTrackerDisplay.untoggleButtons();
    }
}