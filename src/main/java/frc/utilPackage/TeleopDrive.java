package frc.utilPackage;

import frc.controlBoard.ControlBoard;
import frc.controlBoard.IControlBoard;
import frc.coordinates.Coordinate;
import frc.drive.DriveOutput;
import frc.drive.DriveOutput.Modes;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TeleopDrive {
        private static IControlBoard cb = new ControlBoard();
    public static IControlBoard getControlBoard(){
        return cb;
    }
	DriveOutput drive;

	double kWheelNonLinearity = 0.25;

    boolean enabled = true;
    public double maxThrottle;
    
    private double left, right;
	
	public TeleopDrive() {
		drive = DriveOutput.getInstance();
		
		SmartDashboard.putNumber("Wheel Linearity", kWheelNonLinearity);
	}
	
	public void enabled(boolean enable){
		enabled = enable;
	}
	
	@SuppressWarnings("unused")
	public void run(){
		double wheel = cb.getWheel();
		double outwheel = scaleWheel(wheel);
		double throttle = cb.getThrottle();
		throttle *= maxThrottle;
		outwheel *= 0.88;
		
		right = 12*(throttle+outwheel);
        left = 12*(throttle-outwheel);
    }

    public Coordinate output() {
        return new Coordinate(left, right);
    }

	private double scaleWheel(double wheel){
		return wheel * 0.5;
	}

	public double maxThrottle(double percent) {
		maxThrottle = percent;
		return percent;
	}
}
