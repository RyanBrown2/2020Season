package frc.splines;

import frc.coordinates.Coordinate;
import frc.coordinates.Pos2D;

public abstract interface Spline{
    public void setPoints(Pos2D start, Pos2D end);
    public Coordinate calculatePosition(double percent);
}