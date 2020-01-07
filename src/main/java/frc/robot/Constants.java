package frc.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.util.util;

import frc.utilPackage.Units;

public class Constants {

    public static double robotWidth = 26*Units.Length.inches;

    public static boolean isCompBot = true;

    public static class Drive {
		public static double wheelDiameter = 5.5, // inches
				wheelCircumference = wheelDiameter * Math.PI, // inches
                robotDiameter = 29; // inches (for estimating angle without a gyro)
        
        public static int[] leftDriveMotors = {10, 11, 12};
        public static int[] rightDriveMotors = {13, 14, 15};
        
        public static SpeedControllerGroup leftDrive = new SpeedControllerGroup(new CANSparkMax(leftDriveMotors[0], MotorType.kBrushless), new CANSparkMax(leftDriveMotors[1], MotorType.kBrushless), new CANSparkMax(leftDriveMotors[2], MotorType.kBrushless));
		public static SpeedControllerGroup rightDrive = new SpeedControllerGroup(new CANSparkMax(rightDriveMotors[0], MotorType.kBrushless), new CANSparkMax(rightDriveMotors[1], MotorType.kBrushless), new CANSparkMax(rightDriveMotors[2], MotorType.kBrushless));
    }

}