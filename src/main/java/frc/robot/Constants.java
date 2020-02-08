package frc.robot;

import java.io.FileReader;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;
import com.revrobotics.AlternateEncoderType;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.util.util;

import frc.utilPackage.Units;

public class Constants {

    public static double degreesToRadians = 3.14159/180;

    public static double robotWidth = 26*Units.Length.inches;

    public static int pigeonTalonID = 15;

    public static boolean isCompBot = true;

    public static class Drive {
        public static TalonSRX gyro = new TalonSRX(pigeonTalonID);
		public static TalonSRX rightEncoder = new TalonSRX(16);
        public static TalonSRX leftEncoder = new TalonSRX(19);

        public static PigeonIMU pigeon = new PigeonIMU(gyro);

        public static double wheelDiameter = 5.5, // inches
				wheelCircumference = wheelDiameter * Math.PI, // inches
                robotDiameter = 29; // inches (for estimating angle without a gyro)
        
        public static int[] leftDriveMotors = {11, 12};
        public static int[] rightDriveMotors = {13, 14};

        public static CANSparkMax left1 = new CANSparkMax(leftDriveMotors[0], MotorType.kBrushless);
        public static CANSparkMax left2 = new CANSparkMax(leftDriveMotors[1], MotorType.kBrushless);
        public static CANSparkMax right1 = new CANSparkMax(rightDriveMotors[0], MotorType.kBrushless);
        public static CANSparkMax right2 = new CANSparkMax(rightDriveMotors[1], MotorType.kBrushless);

        public static Boolean headingInvert = false;
    }

    public static class Feeder {
        public static DoubleSolenoid leftSolenoid = new DoubleSolenoid(1, 2); // todo
        public static DoubleSolenoid rightSolenoid = new DoubleSolenoid(3,4); // todo

        public static TalonSRX rollerMotor = new TalonSRX(pigeonTalonID);
    }

    public static class Flywheel {
        public static CANSparkMax flywheelMotor = new CANSparkMax(23, MotorType.kBrushless);
        public static CANSparkMax flywheelMotorI = new CANSparkMax(22, MotorType.kBrushless);

        // Flywheel PID Constants
        public static double kP = 0.01;
        public static double kI = 0;
        public static double kD = 0.1;
        public static double kIz = 0;
        public static double kFF = 0;

        public static double kMaxOutput = 1;
        public static double kMinOutput = 0;
        public static double maxRPM = 1000;

        // Smart Motion Coefficients
        //TODO

        public static double allowedErr;
        public static double minVel;
    }

    public static class Hood {
        public static int hoodServo = 0;
        public static double angleOffset = 30;
        public static double[] range = {30 * Units.Angle.degrees, 180 * Units.Angle.degrees};
    }

    public static class Turret {
        public static boolean fieldOriented = true;

        public static CANSparkMax turret = new CANSparkMax(21, MotorType.kBrushless);
        public static TalonSRX turretEnc = new TalonSRX(20);

        public static double encoderOffset = 0;
//        public static double ticksPerRev = (2048*(140/30))/3.14159;
        public static double ticksPerRev = 18745/(2*3.14159);

        //PID Constants
        public static double kP = 1;
        public static double kI = 0;
        public static double kD = 0;
        public static double kIz = 0;
        public static double kFF = 0;
        public static double kMaxOutput = 1;
        public static double kMinOutput = -1;

        //Smart Motion Coefficients
        public static double maxVel = 100; // rpm
        public static double maxAcc = 50;

        public static double allowedErr = 0;
        public static double minVel;
    }

    public static class Image{
        public static int imageWidth = 320;
        public static int imageHeight = 240;
    }

    public static class Camera{
        public static double horizontalFov = 60*Units.Angle.degrees;
        public static double verticalFov = 45*Units.Angle.degrees;
    }

    public static class Tcp {
        public static int port = 5800;
    }

    public static void readRobotData() throws Exception{
        JSONParser parser = frc.utilPackage.Util.getParser();
        Object tempObj = parser.parse(new FileReader("/home/lvuser/deploy/robot.json"));
        JSONObject fileObj = (JSONObject) tempObj;

        Drive.headingInvert = (boolean)fileObj.get("headingInvert");

        isCompBot = (boolean)fileObj.get("isCompBot");
    }

}