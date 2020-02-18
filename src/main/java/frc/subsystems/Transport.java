package frc.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import frc.robot.Constants;

public class Transport {

    TalonSRX rampFront, rampBack;

    public Transport() {
        rampFront = Constants.Transport.rampFront;
        rampBack = Constants.Transport.rampBack;
    }

    public void runRamp(double percent, boolean onlyFront) {
        rampFront.set(ControlMode.PercentOutput, -percent);
        if(!onlyFront) {
            rampBack.set(ControlMode.PercentOutput, -percent);
        }
    }
}
