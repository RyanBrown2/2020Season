package frc.autos.modes;

import frc.autos.AutoEndedException;
import frc.autos.actions.Action;
import frc.coordinates.Coordinate;
import frc.drive.PositionTracker;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.utilPackage.Units;

public abstract class AutoMode extends Thread{
    private Coordinate initPos = new Coordinate();
    private Timer autoTime = new Timer();
    private boolean isActive = true;

    public AutoMode(){
        super("Auto Thread");
    }

    @Override
    public void run() {
        while(!RobotState.isAutonomous());
        autoTime.start();
        PositionTracker.getInstance().setInitPos(initPos);
        try{
            auto();
        }catch(AutoEndedException e){
            DriverStation.reportError("Auto Mode finished early!", false);
        }
        autoTime.stop();
    }

    public abstract void auto() throws AutoEndedException;

    protected void setInitPos(double xInFeet, double yInFeet){
        initPos.setXY(xInFeet, yInFeet);
        initPos.mult(Units.Length.feet);
    }

    public boolean isActive(){
        return isActive;
    }

    public boolean isActiveWithThrow() throws AutoEndedException{
        if(!isActive()){
            throw new AutoEndedException();
        }

        return isActive();
    }
    
    public void runAction(Action action) throws AutoEndedException{
        // if(Robot.getControlBoard().autoStop()){
        //     return;
        // }
        if(!RobotState.isAutonomous())
            return;

        SmartDashboard.putString("Current Action", action.getClass().getName());

        isActiveWithThrow();
        action.start();
        while(!action.isFinished() && isActiveWithThrow() && RobotState.isAutonomous() /* && !Robot.getControlBoard().autoStop() */){
            try{
                action.update();
            }catch(RuntimeException e){
                e.printStackTrace();
            }
        }
        // if(Robot.getControlBoard().autoStop()){
        //     return;
        // }

        if(!RobotState.isAutonomous())
            return;
        action.done();
    }

    public void end(){
        isActive = false;
    }

    public void display() {
        SmartDashboard.putNumber("Auto Time", autoTime.get());
    }
}