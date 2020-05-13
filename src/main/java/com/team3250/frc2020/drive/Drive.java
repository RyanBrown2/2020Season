package com.team3250.frc2020.drive;

import com.team3250.frc2020.Constants;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import com.team3250.frc2020.display.DriveDisplay;
import com.team3250.frc2020.utilPackage.Units;
import com.team3250.frc2020.utilPackage.Util;

import java.util.Arrays;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax.IdleMode;

public class Drive {
    private static Drive instance = null;
    public static Drive getInstance(){
        if(instance == null)
            instance = new Drive();
        return instance;
    }

	TalonSRX leftEncoder, rightEncoder;
	SpeedControllerGroup left, right;
	DriveDisplay driveDisplay;

    private Drive(){
    	driveDisplay = new DriveDisplay();

        //set up drive
		leftEncoder = Constants.Drive.leftEncoder;
		leftEncoder.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
		rightEncoder = Constants.Drive.rightEncoder;
		rightEncoder.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
	}

	public void display(){
    	driveDisplay.position(getEnc()[0]);
    	driveDisplay.velocity(getEnc()[1]);

		driveDisplay.left(getLeftPosition());
		driveDisplay.right(getRightPosition());
	}
	
    /**
     * Output puts a voltage to the drive
     * @param  : right voltage, left voltage
     */
    public void outputToDrive(double rightVoltage, double leftVoltage){
		if (Constants.Drive.headingInvert) {
			Constants.Drive.left1.setVoltage(leftVoltage);
			Constants.Drive.left2.setVoltage(leftVoltage);
			Constants.Drive.right1.setVoltage(-rightVoltage);
			Constants.Drive.right2.setVoltage(-rightVoltage);
		} else {
			Constants.Drive.left1.setVoltage(-leftVoltage);
			Constants.Drive.left2.setVoltage(-leftVoltage);
			Constants.Drive.right1.setVoltage(rightVoltage);
			Constants.Drive.right2.setVoltage(rightVoltage);
		}
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
