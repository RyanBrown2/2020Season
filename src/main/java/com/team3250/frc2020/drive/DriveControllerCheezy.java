package com.team3250.frc2020.drive;

import com.team3250.frc2020.coordinates.Coordinate;
import com.team3250.frc2020.utilPackage.Util;

/**
 * Gotta get more wheel
 *      -Unknown 254 programmer
 * 
 * Based on Cheesydrive but modified
 */

public class DriveControllerCheezy {
    private static final double throttleDeadband = 0.02;
    private static final double wheelDeadband = 0.02;

    // This factor determine how fast the wheel traverses the "non linear" sine curve.
    private static final double wheelNonLinearity = 0.5;

    // Used to determine when to switch between wheelCloseFactor and wheelFarFactor for scaling the wheel during normal turning
    private static final double TurnThreshold = 0.65;

    // Factor to scale the wheel by during tight turns 
    private static final double tightTurningFactor = 3.5;

    // Factors to scale the wheel by during normal turns
    private static final double wheelCloseFactor = 4.0;
    private static final double wheelFarFactor = 5.0;

    private static final double sensitivity = 0.55;

    private static final double quickStopDeadband = 0.5;
    private static final double quickStopWeight = 0.9;
    private static final double quickStopFactor = 0.1;

    private double oldWheel = 0.0;
    private double quickStopAccumlator = 0.0;
    private double wheelAccumlator = 0.0;

    private double leftPwm, rightPwm;

    public void drive(double throttle, double wheel, boolean quickTurn) {

        wheel = handleDeadband(wheel, wheelDeadband);
        throttle = handleDeadband(throttle, throttleDeadband);
        
        // Scaling Throttle
        throttle = throttle*0.5;

        // if deltaWheel is negative, your turn is slowing down
        // if defltWheel is positive, your turn is speeding up
        double deltaWheel = wheel - oldWheel;
        oldWheel = wheel;

        // Scales wheel for better control
        final double denominator = Math.sin(Math.PI / 2.0 * wheelNonLinearity);
        // Apply a sin function that's scaled to make it feel better.
        wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel) / denominator;
        wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel) / denominator;
        wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel) / denominator;

        // double leftPwm, rightPwm, overPower;
        double overPower;

        double angularPower;
        double linearPower;

        // Turning Factor
        double turnFactor;
        if (wheel * deltaWheel > 0) {
            // If we are turning the wheel away from 0, aka, turn tighter.
            turnFactor = tightTurningFactor;
        } else {
            // If we are turning the wheel towards 0
            if (Math.abs(wheel) > TurnThreshold) {
                turnFactor = wheelFarFactor;
            } else {
                turnFactor = wheelCloseFactor;
            }
        }
        
        wheelAccumlator += deltaWheel * turnFactor;

        wheel = wheel + wheelAccumlator;
        if (wheelAccumlator > 1) {
            wheelAccumlator -= 1;
        } else if (wheelAccumlator < -1) {
            wheelAccumlator += 1;
        } else {
            wheelAccumlator = 0;
        }
        linearPower = throttle;

        // Quickturn!
        if (quickTurn) {
            if (linearPower < quickStopDeadband) {
                double alpha = quickStopWeight;
                quickStopAccumlator = (1 - alpha) * quickStopAccumlator + alpha * Util.limit(wheel, 1.0) * quickStopFactor;
            }
            overPower = 1.0;
            angularPower = wheel;
        } else {
            overPower = 0.0;
            angularPower = throttle * wheel * sensitivity - quickStopAccumlator;
            if (quickStopAccumlator > 1) {
                quickStopAccumlator -= 1;
            } else if (quickStopAccumlator < -1) {
                quickStopAccumlator += 1;
            } else {
                quickStopAccumlator = 0.0;
            }
        }

        // angularPower is difference between left and right side in order to turn
        // linearPower is throttle
        rightPwm = leftPwm = linearPower;
        leftPwm += angularPower;
        rightPwm -= angularPower;


        // Keeps output values between -1 and 1 (-100% and +100%)
        if (leftPwm > 1.0) {
            rightPwm -= overPower * (leftPwm - 1.0);
            leftPwm = 1.0;
        } else if (rightPwm > 1.0) {
            leftPwm -= overPower * (rightPwm - 1.0);
            rightPwm = 1.0;
        } else if (leftPwm < -1.0) {
            rightPwm += overPower * (-1.0 - leftPwm);
            leftPwm = -1.0;
        } else if (rightPwm < -1.0) {
            leftPwm += overPower * (-1.0 - rightPwm);
            rightPwm = -1.0;
        }
    }

    public double handleDeadband(double val, double deadband) {
        return (Math.abs(val) > Math.abs(deadband)) ? val : 0.0;
    }

    public Coordinate output() {
        return new Coordinate(leftPwm, rightPwm);
    }
}