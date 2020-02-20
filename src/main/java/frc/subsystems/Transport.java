package frc.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import frc.robot.Constants;

public class Transport {

    TalonSRX rampFront, rampBack;

    public enum Rollers {in, out, off, onlyFront}

    public Transport() {
        rampFront = Constants.Transport.rampFront;
        rampBack = Constants.Transport.rampBack;
    }

    public void rollers(Rollers rollers) {
        if (rollers == Rollers.off) {
            rampFront.set(ControlMode.PercentOutput, 0);
            rampBack.set(ControlMode.PercentOutput, 0);
        } else if (rollers == Rollers.in) {
            rampFront.set(ControlMode.PercentOutput, -1);
            rampBack.set(ControlMode.PercentOutput, -1);
        } else if (rollers == Rollers.out) {
            rampFront.set(ControlMode.PercentOutput, 1);
            rampFront.set(ControlMode.PercentOutput, 1);
        } else if (rollers == Rollers.onlyFront) {
            rampFront.set(ControlMode.PercentOutput, -0.25);
        }
    }
}
