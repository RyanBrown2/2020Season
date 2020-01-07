package frc.drive;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.*;
import frc.utilPackage.Units;
import frc.utilPackage.Util;

import java.util.Arrays;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Drive {
    private static Drive instance = null;
    public static Drive getInstance(){
        if(instance == null)
            instance = new Drive();
        return instance;
    }

	CANSparkMax spark;
	TalonSRX leftEncoder, rightEncoder;
	SpeedControllerGroup left, right;
    private Drive(){
        //set up drive
		left = Constants.Drive.leftDrive;
		right = Constants.Drive.rightDrive;
		leftEncoder = Constants.Drive.leftEncoder;
		leftEncoder.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
		rightEncoder = Constants.Drive.rightEncoder;
		rightEncoder.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
	}

	public void display(){
		SmartDashboard.putNumber("Encoder Pos", getEnc()[0]);
		SmartDashboard.putNumber("Encoder Vel", getEnc()[1]);

		SmartDashboard.putNumber("Left Encoder Pos", getLeftPosition());
		SmartDashboard.putNumber("Right Encoder Pos", getRightPosition());
	}
	
    /**
     * Output puts a voltage to the drive
     * @param  : right voltage, left voltage
     */
    public void outputToDrive(double rightVoltage, double leftVoltage){
		Constants.Drive.leftDrive.set(-leftVoltage/12);
		Constants.Drive.rightDrive.set(rightVoltage/12);
    }

	/**
	 * Gets encoder velocity and position
	 * @return [0] = position, [1] = velocity
	 */
	public double[]	getEnc(){
		double position = Util.average(Arrays.asList(getLeftPosition(), getRightPosition()));
		double velocity = Util.average(Arrays.asList(getLeftVel(), getRightVel()));
		double[] out = {position, velocity};
		return out;
	}

	public double getLeftPosition(){
			return -leftEncoder.getSelectedSensorPosition()*Units.Angle.encoderTicks*Units.Length.radians;
	}

	public double getRightPosition(){
			return rightEncoder.getSelectedSensorPosition()*Units.Angle.encoderTicks*Units.Length.radians;
	}


	// Returns Left Velocity
	public double getLeftVel(){
		double out = -leftEncoder.getSelectedSensorVelocity()*
				Units.Angle.encoderTicks*Units.Length.radians/(0.1*Units.Time.seconds);
		return out;
	}

	// Returns Right Velocity
	public double getRightVel(){
		double out = rightEncoder.getSelectedSensorVelocity()*
				Units.Angle.encoderTicks*Units.Length.radians/(0.1*Units.Time.seconds);
		return out;
	}

	public void brake(IdleMode mode){
		Constants.Drive.left1.setIdleMode(mode);
		Constants.Drive.left2.setIdleMode(mode);
		Constants.Drive.right1.setIdleMode(mode);
		Constants.Drive.right2.setIdleMode(mode);
	}
}
