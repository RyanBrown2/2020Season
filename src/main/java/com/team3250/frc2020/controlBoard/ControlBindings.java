package com.team3250.frc2020.controlBoard;

public class ControlBindings {
    public static final class Driver {
        public static final class Joystick {
            public static final int kUsbSlot = 0;

            public static final int kRollers = 1;
            public static final int kShoot = 6;
            public static final int kQuickTurn = 2;

        }

        public static class Wheel {
            public static final int kUsbSlot = 1;
        }
    }

    public static class CoDriver {
        public static class ButtonBoard {
            public static final int kUsbSlot = 3;

            public static final int kShoot = 14;
        }

        public static class Joystick {
            public static final int kUsbSlot = 2;

            public static final int kFeeder = 2;
            public static final int kRollers = 1;
        }
    }

//    public static class Driver {
//        public static int rollers = 1;
//        public static int shoot = 6;
//        public static int quickTurn = 2;
//    }

//    public static class CoDriver {
//        public static int rollers = 1;
//        public static int feeder = 2;
//    }
//
//    public static class ButtonPad {
//        public static int shoot = 14;
//        public static int reverseFeeder = 20;
//        public static int climbArms = 11;
//        public static int climb = 15;
//        public static int colorWheelActuate = 12;
//        public static int colorWheelRoller = 16; //TODO
//        public static int unjam = 23;
//        public static int visionTrack = 13; //TODO
//        public static int panic = 5;
//    }
}
