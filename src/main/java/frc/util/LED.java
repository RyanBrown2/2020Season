package frc.util;

import com.ctre.phoenix.CANifier;
import com.ctre.phoenix.CANifier.LEDChannel;

public class LED {
    private static LED instance;
    public static LED getInstance(){
        if(instance == null){
            instance = new LED();
        }
        return instance;
    }

    CANifier led;

    private LED(){
        led = new CANifier(1);
        led.configFactoryDefault();
    }

    public void setLED(boolean on){
        if(on){
            led.setLEDOutput(0, LEDChannel.LEDChannelA);
            led.setLEDOutput(0, LEDChannel.LEDChannelB);
            // C is both
            led.setLEDOutput(1, LEDChannel.LEDChannelC);
        }else{
            led.setLEDOutput(0, LEDChannel.LEDChannelA);
            led.setLEDOutput(0, LEDChannel.LEDChannelB);
            led.setLEDOutput(0, LEDChannel.LEDChannelC);
        }
    }
}
