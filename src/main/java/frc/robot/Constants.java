package frc.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import frc.utilPackage.Units;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;

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
        public static DoubleSolenoid solenoid = new DoubleSolenoid(2, 6); // todo

        public static TalonSRX rollerMotor = new TalonSRX(pigeonTalonID);
    }

    public static class Flywheel {
        public static CANSparkMax flywheelMotor = new CANSparkMax(23, MotorType.kBrushless);
        public static CANSparkMax flywheelMotorI = new CANSparkMax(22, MotorType.kBrushless);

        // Flywheel shooting PID Constants
        public static double kP = 320; //0.001
        public static double kI = 64; //0.0000125
        public static double kD = 0; //0
        public static double kIz = 0.2; //0.25
        public static double kFF = 0.0001851; // 0.0001953

        public static double kMaxOutput = 1;
        public static double kMinOutput = 0; //0
        public static double maxRPM = 5500;
    }

    public static class Hood {
        public static int hoodServo = 0;
        public static double angleOffset = 0;
        public static double[] range = {30 * Units.Angle.degrees, 180 * Units.Angle.degrees};
    }

    public static class Transport {
        public static TalonSRX rampFront = new TalonSRX(20);
        public static TalonSRX rampBack = new TalonSRX(17);
        public static TalonSRX mixer = new TalonSRX(14);

        public static DigitalInput ballSensor = new DigitalInput(9);
    }

    public static class Turret {
        public static double acceptedError = 0.044;

        public static CANSparkMax turret = new CANSparkMax(21, MotorType.kBrushless);
        public static TalonSRX turretEnc = Transport.rampBack;

        public static double encoderOffset = 0;
//        public static double ticksPerRev = (2048*(140/30))/3.14159;
        public static double ticksPerRev = 18745/(2*3.14159);

        public static double lowerRadianLimit = -3.14159;
        public static double upperRadianLimit = 3.14159/2;

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

    public static class Climber {
        public static TalonSRX climbArms = new TalonSRX(13);
        public static TalonSRX climbGearbox = Drive.rightEncoder; // TODO

        public static DigitalInput climbLimit = new DigitalInput(8);
    }

    public static class ColorWheel {
        public static TalonSRX wheelRoller = new TalonSRX(102); // TODO
        public static DoubleSolenoid wheelPiston = new DoubleSolenoid(1, 5);
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