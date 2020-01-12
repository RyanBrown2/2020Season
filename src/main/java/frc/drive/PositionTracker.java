package frc.drive;

import java.util.Arrays;

import com.kauailabs.navx.frc.AHRS;

import frc.coordinates.*;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.SPI.Port;
import frc.display.DriveDisplay;
import frc.robot.Constants;
import frc.utilPackage.Units;
import frc.utilPackage.Util;

public class PositionTracker extends Thread implements IPositionTracker{
    private static PositionTracker instance = null;
    public static PositionTracker getInstance(){
        if(instance == null)
            instance = new PositionTracker();
        return instance;
    }

    private AHRS vmxPi;
    private Coordinate position = new Coordinate();
    private Heading heading = new Heading();
    private Pos2D fullPos = new Pos2D();
    private Pos2D visionData;
    private double offset;

    private PositionTracker(){
        vmxPi = new AHRS(Port.kMXP);
        this.start();
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

    @Override
    public void run() {
        double last = Timer.getFPGATimestamp();
        heading = new Heading();
        heading.setRobotAngle(getAngle());
        Heading pHeading = new Heading(heading);
        resetHeading();
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

            if(visionData != null){
                Coordinate newPos = new Coordinate();
                final double visionSetting = 0.3;
                newPos = position.multC(1-visionSetting).addC(visionData.multC(visionSetting));
                fullPos.setPos(newPos);
            }else{
                fullPos.setPos(position);
            }

            fullPos.setHeading(heading);

            Timer.delay(0.005);
        }
    }

    private double getRawAngle(){
        return vmxPi.getAngle();
    }

    private double getAngle(){
        return (getRawAngle()-offset)*Units.Angle.degrees;
    }
    
    private double getEncoderAngle(Drive drive){
        return (drive.getLeftPosition() - drive.getRightPosition())/(Constants.robotWidth);
    }

    @Override
    public synchronized Pos2D getPosition() {
        return fullPos;
    }

    public synchronized Pos2D getReversePosition(){
        Pos2D output = new Pos2D(fullPos);
        output.getHeading().multC(-1);
        return output;
    }

    @Override
    public Heading getVelocity() {
        return new Heading(heading);
    }

    public synchronized void setVisionPos(Pos2D visionData){
        this.visionData = visionData;
    }

    public void display(){
        if(DriveDisplay.resetLocation.getBoolean(false)){
            double x = DriveDisplay.posX.getDouble(0);
            double y = DriveDisplay.posY.getDouble(0);
            setInitPosFeet(x, y);
            DriveDisplay.resetLocation.setBoolean(false);
        }

        if(DriveDisplay.resetHeading.getBoolean(false)){
            robotForward();
            DriveDisplay.resetHeading.setBoolean(false);
        }

        try{
            Coordinate position = new Coordinate(this.position);
            position.mult(1/Units.Length.feet);
            DriveDisplay.posX.setDouble(position.getX());
            DriveDisplay.posY.setDouble(position.getY());
            DriveDisplay.angle.setDouble(getAngle()/Units.Angle.degrees);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}