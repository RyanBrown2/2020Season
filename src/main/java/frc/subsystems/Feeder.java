package frc.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import frc.robot.Constants;

public class Feeder {

    DoubleSolenoid feederPiston = Constants.Feeder.solenoid;
    public enum Rollers {off, in, out, maxIn, maxOut}

    boolean isOut = false;

    public Feeder() {
    }

    public void rollers(Rollers rollers) {
        if (rollers == Rollers.off) {
            Constants.Feeder.rollerMotor.set(ControlMode.PercentOutput, 0);
        } else if (rollers == Rollers.in) {
            Constants.Feeder.rollerMotor.set(ControlMode.PercentOutput, -0.1);
        } else if (rollers == Rollers.out) {
            Constants.Feeder.rollerMotor.set(ControlMode.PercentOutput, 0.25);
        } else if (rollers == Rollers.maxIn) {
            Constants.Feeder.rollerMotor.set(ControlMode.PercentOutput, -1);
        } else if (rollers == Rollers.maxOut) {
            Constants.Feeder.rollerMotor.set(ControlMode.PercentOutput, 1);
        }
    }

    public void actuate() {
        if(!isOut) {
            feederPiston.set(DoubleSolenoid.Value.kForward);
            isOut = true;
        } else if(isOut) {
            rollers(Rollers.in);
            feederPiston.set(DoubleSolenoid.Value.kReverse);
            isOut = false;
        } else {
            isOut = false;
        }
    }

    public void panic() {

    }

    public void display() {

    }

}
