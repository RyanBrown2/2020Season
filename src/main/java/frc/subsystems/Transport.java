package frc.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import frc.robot.Constants;

public class Transport {

    TalonSRX rampLeft, rampRight;

    public Transport() {
        rampLeft = Constants.Transport.rampLeft;
        rampRight = Constants.Transport.rampRight;
    }

    public void runRamp(double percent) {
        rampLeft.set(ControlMode.PercentOutput, percent);
        rampRight.set(ControlMode.PercentOutput, percent);
    }
}
