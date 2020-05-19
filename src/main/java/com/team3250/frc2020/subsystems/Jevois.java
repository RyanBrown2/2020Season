package com.team3250.frc2020.subsystems;

import com.team3250.lib.util.SerialReader;

/**
 * Subsystem for interacting with the Jevois
 */
public class Jevois extends Subsystem {
    public final static int kDefaultPipeline = 0;

    public static class JevoisConstants {
        public String kName = "";
        public int kPort = 0; // Port number camera is plugged into
        public double kHeight = 0; // Height at which camera is mounted (meters)
        public double kMountAngle = 0; // Vertical angle at which camera is mounted (radians)
    }

    SerialReader mSerialReader;

    public Jevois(JevoisConstants constants) {
        mConstants = constants;
        mSerialReader = new SerialReader(constants.kPort);
    }

    public static class PeriodIO {
        // INPUTS
        public double xVal;
        public double yVal;

        // OUTPUTS

    }

    private JevoisConstants mConstants = null;
    private PeriodIO mPeriodIO = new PeriodIO();
    private boolean mOutputsHaveChanged = true;


    @Override
    public synchronized void readPeriodicInputs() {
        mPeriodIO.xVal = Double.parseDouble(mSerialReader.separateValues(",")[0]);
        mPeriodIO.yVal = Double.parseDouble(mSerialReader.separateValues(",")[1]);
    }

    @Override
    public synchronized void writePeriodicOutputs() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean checkSystem() {
        return false;
    }

    @Override
    public void outputTelemetry() {

    }
}
