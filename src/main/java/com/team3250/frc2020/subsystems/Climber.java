package com.team3250.frc2020.subsystems;

import com.team3250.frc2020.Robot;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import com.team3250.frc2020.controlBoard.*;
import com.team3250.frc2020.Constants;

public class Climber {
    private static Climber instance = null;
    public static Climber getInstance() {
        if (instance == null) {
            instance = new Climber();
        }
        return instance;
    }

    private static IControlBoard cb = Robot.getControlBoard();

    TalonSRX climbArms = Constants.Climber.climbArms;
    TalonSRX climbGearbox = Constants.Climber.climbGearbox;
    DigitalInput sensor = Constants.Climber.climbLimit;

    public Climber() {

    }

    public void run() {
        if(cb.climbArms()) {
            if(sensor.get()) {
                climbArms.set(ControlMode.PercentOutput, -1);
            } else if(!sensor.get()) {
                climbArms.set(ControlMode.PercentOutput, -0.1);
            }
        } if(cb.climbArmsReleased()) {
            climbArms.set(ControlMode.PercentOutput, -0.1);
        } if(cb.climb()) {
            climbArms.set(ControlMode.PercentOutput, 0);
            climbGearbox.set(ControlMode.PercentOutput, 1);
        } if(cb.climbReleased()) {
            climbArms.set(ControlMode.PercentOutput, 0);
            climbGearbox.set(ControlMode.PercentOutput, 0);
        }
    }
}