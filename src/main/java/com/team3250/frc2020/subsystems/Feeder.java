package com.team3250.frc2020.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Timer;
import com.team3250.frc2020.Constants;

public class Feeder {
    private static Feeder instance = null;
    public static Feeder getInstance() {
        if (instance == null) {
            instance = new Feeder();
        }
        return instance;
    }

    Timer timer;

    DoubleSolenoid feederPiston = Constants.Feeder.solenoid;
    public enum Rollers {off, in, out, maxIn, maxOut}

    boolean isOut = false;

    private Feeder() {
        timer = new Timer();
    }

    public void rollers(Rollers rollers) {
        if (rollers == Rollers.off) {
            Constants.Feeder.rollerMotor.set(ControlMode.PercentOutput, 0);
        } else if (rollers == Rollers.in) {
            Constants.Feeder.rollerMotor.set(ControlMode.PercentOutput, -0.75);
        } else if (rollers == Rollers.out) {
            Constants.Feeder.rollerMotor.set(ControlMode.PercentOutput, 0.25);
        } else if (rollers == Rollers.maxIn) {
            Constants.Feeder.rollerMotor.set(ControlMode.PercentOutput, -1);
        } else if (rollers == Rollers.maxOut) {
            Constants.Feeder.rollerMotor.set(ControlMode.PercentOutput, 1);
        }
    }

    // Actuate solenoids and track the state using boolean isOut
    public void actuate() {
        if(!isOut) {
            feederPiston.set(DoubleSolenoid.Value.kForward);
            isOut = true;
        } else if(isOut) {
            feederPiston.set(DoubleSolenoid.Value.kReverse);
            isOut = false;
        } else {
            isOut = false;
        }
    }

    // Specific functions for deploying and retracting prevent autos from getting flipped
    public void retract() {
        feederPiston.set(DoubleSolenoid.Value.kReverse);
        isOut = false;
    }

    public void deploy() {
        feederPiston.set(DoubleSolenoid.Value.kForward);
        isOut = true;
    }

    private boolean hasStarted = false;
    public void startup() {
        if (!hasStarted) {
            if (timer.get() == 0) {
                timer.start();
            }
            feederPiston.set(DoubleSolenoid.Value.kForward);
            if (timer.get() > 1.5) {
                feederPiston.set(DoubleSolenoid.Value.kReverse);
                hasStarted = true;
            }
        } else {
            timer.stop();
        }
    }

    public void panic() {
    }

    public void display() {
    }
}
