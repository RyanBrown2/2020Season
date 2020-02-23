package frc.subsystems;

import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;

public class ColorWheel {

    I2C.Port ic2Port;
    ColorSensorV3 colorSensor;
    Color detectedColor;

    public ColorWheel() {
        ic2Port = I2C.Port.kOnboard;
        colorSensor = new ColorSensorV3(ic2Port);
    }

    public String getColor() {
        detectedColor = colorSensor.getColor();
        if(detectedColor.red > 0.53) {
            return "red";
        } else if(detectedColor.green > 0.55) {
            return "green";
        } else if(detectedColor.red > 0.35 && detectedColor.green>0.5) {
            return "yellow";
        } else if(detectedColor.green > 0.45 && detectedColor.blue > 0.3) {
            return "blue";
        } else {
            return "none";
        }
    }

}
