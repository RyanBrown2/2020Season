package frc.util;


import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Timer;
import frc.drive.Drive;
import frc.drive.DriveOutput;
import frc.robot.Constants;
import frc.subsystems.Hood;
import frc.robot.Constants;

//hopefully runs drivetrain, flywheel, turret, maybe feeder and transport, and hood

public class FunctionTest {

    Hood hood = new Hood();
    Timer timer = new Timer();
    //time is in seconds
    int state = 1;

    public FunctionTest() {
        timer.reset();
        timer.start();
    }

    public void reset() {
        state = 1;
    }

    public void run() {
        switch (state) {
            case 1:
                Drive.getInstance().outputToDrive(-0.25, 0.25);
//                Constants.Drive.left1.set(-0.5);
//                Constants.Drive.left2.set(-0.5);
//                Constants.Drive.right1.set(0.5);
//                Constants.Drive.right2.set(0.5);
                if (timer.get() >= 5) {
                    Constants.Drive.left1.set(0);
                    Constants.Drive.left2.set(0);
                    Constants.Drive.right1.set(0);
                    Constants.Drive.right2.set(0);
                    timer.reset();
                    state = 2;
                }
                break;
            case 2:
                Constants.Drive.left1.set(0.5);
                Constants.Drive.left2.set(0.5);
                Constants.Drive.right1.set(-0.5);
                Constants.Drive.right2.set(-0.5);
                if (timer.get() >= 5) {
                    Constants.Drive.left1.set(0);
                    Constants.Drive.left2.set(0);
                    Constants.Drive.right1.set(0);
                    Constants.Drive.right2.set(0);
                    timer.reset();
                    state = 3;
                }
                break;
            case 3:
                Constants.Flywheel.flywheelMotor.set(0.1);
                Constants.Flywheel.flywheelMotorI.set(0.1);
                if (timer.get() >= 5) {
                    Constants.Flywheel.flywheelMotor.set(0);
                    Constants.Flywheel.flywheelMotorI.set(0);
                    timer.reset();
                    state = 4;
                }
                break;
            case 4:
                Constants.Turret.turret.set(0.05);
                if (timer.get() >= 2){
                    Constants.Turret.turret.set(0);
                    timer.reset();
                    state = 5;
                }
                break;
            case 5:
                Constants.Turret.turret.set(-0.05);
                if (timer.get() >= 2){
                    Constants.Turret.turret.set(0);
                    timer.reset();
                    state = 6;
                }
                break;
            case 6:
                //todo
//                Constants.Feeder.leftSolenoid.set(DoubleSolenoid.Value.kForward);
//                Constants.Feeder.rightSolenoid.set(DoubleSolenoid.Value.kForward);
                if (timer.get() >= 5){
                    timer.reset();
                    state = 7;
                }
                break;
            case 7:
                //todo
//                Constants.Feeder.leftSolenoid.set(DoubleSolenoid.Value.kReverse);
//                Constants.Feeder.rightSolenoid.set(DoubleSolenoid.Value.kReverse);
                if (timer.get() >= 5){
//                    Constants.Feeder.leftSolenoid.set(DoubleSolenoid.Value.kOff);
//                    Constants.Feeder.rightSolenoid.set(DoubleSolenoid.Value.kOff);
                    timer.reset();
                    state = 8;
                }
                break;
            case 8:
                //todo
//                Constants.Transport.rampLeft.set(ControlMode.PercentOutput, 50);
//                Constants.Transport.rampRight.set(ControlMode.PercentOutput, 50);
                if(timer.get() >= 5){
//                    Constants.Transport.rampLeft.set(ControlMode.PercentOutput, 0);
//                    Constants.Transport.rampRight.set(ControlMode.PercentOutput, 0);
                    timer.reset();
                    state = 9;
                }
                break;
            case 9:
                hood.setAngle(1);
                state = 10;
                break;
            case 10:
                hood.setAngle(149);
                state = 11;
                break;
            case 11:
                timer.stop();
                break;
        }

    }

}
