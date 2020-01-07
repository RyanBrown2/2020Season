package frc.utilPackage;

public class LowPassFilter {
    double oldValue = 0;
    double a = .9;

    public LowPassFilter(double a){
        this.a = a;
    }

    public LowPassFilter(double a, double defaultVal){
        this.a = a;
        oldValue = defaultVal;
    }

    public double update(double newValue){
//        System.out.println(newValue);
        if(Double.isNaN(newValue) || !Util.inErrorRange(newValue, 0, 10000)){
            return newValue;
        }
//        System.out.println("Old Val: "+oldValue);
        double update = oldValue*a + newValue*(1.0 - a);
//        System.out.println("New Value:"+update);
        oldValue = update;
        return update;
    }
}
