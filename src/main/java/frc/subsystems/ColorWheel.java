package frc.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;
import frc.robot.Constants;

public class ColorWheel {
    private static ColorWheel instance = null;
    public static ColorWheel getInstance() {
        if (instance == null) {
            instance = new ColorWheel();
        }
        return instance;
    }

    I2C.Port ic2Port;
    ColorSensorV3 colorSensor;
    Color detectedColor;

    TalonSRX wheelMotor = Constants.ColorWheel.wheelRoller;
    DoubleSolenoid wheelPiston = Constants.ColorWheel.wheelPiston;

    boolean isOut = true;

    public enum Roller {clockWise, antiClockWise, off}

    private ColorWheel() {
        ic2Port = I2C.Port.kOnboard;
        colorSensor = new ColorSensorV3(ic2Port);
    }

    // Actuate solenoids and track the state using boolean isOut
    public void actuate() {
        if(!isOut) {
            wheelPiston.set(DoubleSolenoid.Value.kForward);
            isOut = true;
        } else if(isOut) {
            wheelPiston.set(DoubleSolenoid.Value.kReverse);
            isOut = false;
        } else {
            isOut = false;
        }
    }

    public void roller(Roller roller) {
        if(roller == Roller.clockWise) {
            wheelMotor.set(ControlMode.PercentOutput, 1);
        } else if(roller == Roller.antiClockWise) {
            wheelMotor.set(ControlMode.PercentOutput, -1);
        } else if(roller == Roller.off) {
            wheelMotor.set(ControlMode.PercentOutput, 0);
        }
    }

    public String getColor() {
        detectedColor = colorSensor.getColor();
        if(detectedColor.red > 0.53) {
            return "red";
        } else if(detectedColor.green > 0.55) {
            return "green";
        } else if(detectedColor.red > 0.35 && detectedColor.green>0.5) {
            return "yellow";
        } else if(detectedColor.green > 0.45 && detectedColor.blue > 0.3) {
            return "blue";
        } else {
            return "none";
        }
    }

}
