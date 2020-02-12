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

    public void runRamp(double percent) {
        rampFront.set(ControlMode.PercentOutput, -percent);
        rampBack.set(ControlMode.PercentOutput, -percent);
    }
}
