package frc.coordinates;

import frc.utilPackage.Util;

/*
	This class is used to create heading objects.
	These objects create a vector at the origin from an x and y value.
	They are used in calculating angles and defining Pos2D objects.
*/

public class Heading extends Coordinate {
	// Maps the heading from polar coordinates to cartesian coordinates
	public static Heading createPolarHeading(double angle, double mag){
		return new Heading(mag*Math.cos(angle), mag*Math.sin(angle));
	}
	// New headings left undefined will have 
	public Heading(){
		x = 0;
		y = 1;
	}
	public Heading(double _x, double _y) {
		super(_x, _y);
	}
	public Heading(double angle){
		super(Math.cos(angle), Math.sin(angle));
	}
	public Heading(Heading c){
		super(c);
	}
	
	public void setAngle(double angle){
		setXY(Math.cos(angle), Math.sin(angle));
	}
	
	public void setRobotAngle(double angle){
		setXY(Math.sin(angle), Math.cos(angle));
	}
	
	public Heading perpendicularCCw(){
		Heading New = perpendicularCCwC();
		x = New.x;
		y = New.y;
		return this;
	}
	
	public Heading perpendicularCCwC(){
		return new Heading(-this.getY(), this.getX());
	}
	
	/**
	 * Finds the perpendicular based on a 90 deg clockwise rotation and updates current object
	 * @return 90 deg rotation
	 */
	public Heading perpendicularCw(){
		Heading New = perpendicularCwC();
		x = New.x;
		y = New.y;
		return this;
	}
	/**
	 * Finds the perpendicular based on a 90 deg clockwise rotation
	 * @return 90 deg rotation
	 */
	public Heading perpendicularCwC(){
		return new Heading(this.getY(), -this.getX());
	}
	
	public double getAngle(){
		return Math.atan2(y, x);
	}
	
	public Heading inverseC() {
		return new Heading(x, -y);
	}
	
	/**
	 * Finds the heading the connects two points
	 * @param pt1 first point
	 * @param pt2 second point
	 * @return heading = pt2-pt1
	 */
	public static Heading headingBetweenPoints(Coordinate pt1, Coordinate pt2){
		double angle = Math.atan2(pt2.getY() - pt1.getY(), pt2.getX() - pt1.getX());
		Heading out = new Heading(angle);
		// Heading out = new Heading(pt2.getX()+pt1.getX(), pt2.getY()+pt1.getY());
		return out;
	}

	/**
	 * Find the heading thats bisects two other headings
	 * @param h1 heading 1
	 * @param h2 heading 2
	 * @return heading = norm(h2 + h1)
	 */
	public static Heading headingBetweenHeadings(Heading h1, Heading h2){
		Heading out = new Heading(h2.getX()+h1.getX(), h2.getY()+h1.getY());
		return out;
	}
	
	public static double headingsToAngle(Heading vec1, Heading vec2) {
		double dotProduct = Coordinate.dotProduct(vec1.normalizeC(), vec2.normalizeC());
		dotProduct = Math.max(dotProduct, -1);
		dotProduct = Math.min(dotProduct, 1);
		double cAngle = Math.acos(dotProduct);
		return cAngle;
	}

	public static double signedHeadingsToAngle(Heading right, Heading left) {
		double cAngle = Math.acos(Coordinate.dotProduct(right.normalizeC(), left.normalizeC()));
		cAngle *= Util.checkSign(crossProduct(right, left));
		return cAngle;
	}
	
	public static Heading addHeadings(Heading hed1, Heading hed2){
		Heading out = new Heading();
		out.setXY(hed1.getX()+hed2.getX(), hed1.getY()+hed2.getY());
		return out;
	}
	
	public static Heading toHeading(Coordinate pt){
		Heading out = new Heading(pt.x, pt.getY());
		return out;
	}
}