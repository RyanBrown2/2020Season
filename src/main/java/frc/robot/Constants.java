package frc.robot;

import java.io.FileReader;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.util.util;

import frc.utilPackage.Units;

public class Constants {

    public static double robotWidth = 26*Units.Length.inches;

    public static boolean isCompBot = true;

    public static class Drive {
		public static TalonSRX rightEncoder = new TalonSRX(41);
        public static TalonSRX leftEncoder = new TalonSRX(22); //was 22
		public static double wheelDiameter = 5.5, // inches
				wheelCircumference = wheelDiameter * Math.PI, // inches
                robotDiameter = 29; // inches (for estimating angle without a gyro)
        
        public static int[] leftDriveMotors = {10, 11};
        public static int[] rightDriveMotors = {13, 14};

        public static CANSparkMax left1 = new CANSparkMax(leftDriveMotors[0], MotorType.kBrushless);
        public static CANSparkMax left2 = new CANSparkMax(leftDriveMotors[0], MotorType.kBrushless);
        public static CANSparkMax right1 = new CANSparkMax(rightDriveMotors[0], MotorType.kBrushless);
        public static CANSparkMax right2 = new CANSparkMax(rightDriveMotors[1], MotorType.kBrushless);

        public static SpeedControllerGroup leftDrive = new SpeedControllerGroup(left1, left2);
		public static SpeedControllerGroup rightDrive = new SpeedControllerGroup(right1, right2);
    }

    public static class Image{
        public static int imageWidth = 320;
        public static int imageHeight = 240;
    }

    public static class Camera{
        public static double horizontalFov = 60*Units.Angle.degrees;
        public static double verticalFov = 45*Units.Angle.degrees;
    }

    public static void readRobotData() throws Exception{
        JSONParser parser = frc.utilPackage.Util.getParser();
        Object tempObj = parser.parse(new FileReader("/home/lvuser/robot.json"));
        JSONObject fileObj = (JSONObject)tempObj;
        isCompBot = (boolean)fileObj.get("isCompBot");
    }

}