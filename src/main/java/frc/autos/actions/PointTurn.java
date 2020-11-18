package frc.autos.actions;

import frc.coordinates.Heading;
import frc.drive.DriveOutput;
import frc.drive.PositionTracker;
import frc.drive.DriveOutput.Modes;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.Constants;
import frc.utilPackage.Derivative;
import frc.utilPackage.Units;
import frc.utilPackage.Util;

public class PointTurn extends Action{

    Heading setpoint;
    final double kP = 4; //3.25
    final double kD = 0.75; //1.25
    Derivative dError;
    DriveOutput drive;
    double startTime;

    public PointTurn(Heading setpoint){
        this.setpoint = setpoint;
        drive = DriveOutput.getInstance();
        dError = new Derivative();
    }

    @Override
    public void start() {
        startTime = Timer.getFPGATimestamp();

        dError.reset(0, calcError());

        drive.setNoVelocity();
    }

    @Override
    public void update() {
        double error = calcError();
        double turnVel = kP*error+kD*dError.Calculate(error, Timer.getFPGATimestamp()-startTime);
        double outputVel = turnVel*Constants.robotWidth/2;
        drive.set(Modes.Velocity, -outputVel, outputVel);
    }
    
    @Override
    public boolean isFinished() {
        return Util.inErrorRange(0, calcError(), 2*Units.Angle.degrees);
    }

    @Override
    public void done() {
        drive.setNoVelocity();
    }

    public double calcError(){
        Heading cHeading = PositionTracker.getInstance().getPosition().getHeading();
        double error = Heading.signedHeadingsToAngle(cHeading, setpoint);
        return error;
    }
}