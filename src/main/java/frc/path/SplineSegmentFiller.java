package frc.path;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import frc.coordinates.Coordinate;
import frc.coordinates.Heading;
import frc.coordinates.Pos2D;
import frc.splines.CubicSpline;
import frc.splines.QuinticSpline;
import frc.splines.Spline;
import frc.utilPackage.Units;
import frc.utilPackage.Util;

public class SplineSegmentFiller{
    public enum Splines{
        CubicSpline,
        QuinticSpline;
    }
    Splines splineMode = Splines.CubicSpline;
    Spline cSpline;
    int pointsPerSpline = 60;
    
    ArrayList<Pos2D> points;
    TrajectoryList segment = null;

    public SplineSegmentFiller(String filePath, String path){
        points = new ArrayList<>(pointsFromJSON(filePath, path));
        switch(splineMode){
            case CubicSpline:
                cSpline = new CubicSpline();
                break;
            case QuinticSpline:
                cSpline = new QuinticSpline();
                break;
        }
    }

    public SplineSegmentFiller(List<Pos2D> points){
        this.points = new ArrayList<>(points);
        switch(splineMode){
            case CubicSpline:
                cSpline = new CubicSpline();
                break;
            case QuinticSpline:
                cSpline = new QuinticSpline();
                break;
        }
    }

    public void setPointsPerSpline(int num){
        pointsPerSpline = num;
    }

    public TrajectoryList generate(){
        segment = null;
        for(int i = 0; i < points.size()-1; i++){
            cSpline.setPoints(points.get(i), points.get(i+1));
            // System.out.println("Adding spline #"+(i+1));
            addSpline();
        }
        return segment;
    }

    private void addSpline(){
        for(int i = 0; i <= pointsPerSpline; i++){
            double t = ((double)i)/pointsPerSpline;
            Coordinate point = cSpline.calculatePosition(t);
            // System.out.println("Adding point t:"+"\t"+point.display("point"));
            if(segment == null){
                segment = new TrajectoryList(point);
            }else{
                segment.add(point);
            }
        }
    }

    protected List<Pos2D> pointsFromJSON(String filePath, String points){
        JSONParser parser = Util.getParser();
        JSONObject jsonObject;
        Object obj = null;
        try{
            obj = parser.parse(new FileReader(filePath));
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        jsonObject = (JSONObject) obj;
        
        JSONArray pointList = (JSONArray) jsonObject.get(points);
        Iterator iterator = pointList.iterator();
        System.out.println("Current path");
        List<Pos2D> output = new ArrayList<>();
        while(iterator.hasNext()){
            JSONArray cArray = (JSONArray) iterator.next();
            System.out.println("Raw data: "+cArray.toString());
            double Px = Double.parseDouble(cArray.get(0).toString())*Units.Length.feet; //x pos
            double Py = Double.parseDouble(cArray.get(1).toString())*Units.Length.feet; //y pos
            double Va = Double.parseDouble(cArray.get(2).toString())*Units.Angle.degrees; //angle
            double Vr = Double.parseDouble(cArray.get(3).toString())*Units.Length.feet; //length
            Pos2D correspondingVec = new Pos2D(new Coordinate(Px, Py), 
                Heading.createPolarHeading(Va, Vr));
            System.out.println("Calculated data: "+correspondingVec.outputData());
            output.add(correspondingVec);
        }

        return output;
    }
}