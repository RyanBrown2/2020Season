package com.team3250.frc2020.splines;

import com.team3250.frc2020.coordinates.Coordinate;
import com.team3250.frc2020.coordinates.Pos2D;

public abstract interface Spline{
    public void setPoints(Pos2D start, Pos2D end);
    public Coordinate calculatePosition(double percent);
}