package frc.subsystems;

import frc.robot.Robot;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.controlBoard.*;
import frc.robot.Constants;

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
            if() {
                climbArms.set(ControlMode.PercentOutput, -0.5);
            }
        } if(!cb.climbArmsReleased()) {
            climbArms.set(ControlMode.PercentOutput, -0.1);
        } if(cb.climb()) {
            climbGearbox.set(ControlMode.PercentOutput, 0.5);
        } if(cb.climbReleased()) {
            climbGearbox.set(ControlMode.PercentOutput, 0);
        }
    }
}