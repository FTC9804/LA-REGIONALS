package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DigitalChannelController;

/**
 * Created by STEVE on 1-28-16.
 */
public class MagnetTestV2 extends OpMode {
    DigitalChannel magnet1;
    DigitalChannel magLED1;
    DigitalChannel magnet2;
    DigitalChannel magLED2;
    boolean digVal1;                    //true if no magnet
    boolean digVal2;                    //false if magnet nearby

    @Override
    public void init(){
        magnet1 = hardwareMap.digitalChannel.get("mag1");
        magnet2 = hardwareMap.digitalChannel.get("mag2");
        magLED1 = hardwareMap.digitalChannel.get("LED1");
        magLED2 = hardwareMap.digitalChannel.get("LED2");
        magLED1.setMode(DigitalChannelController.Mode.OUTPUT);
        magLED2.setMode(DigitalChannelController.Mode.OUTPUT);
        magLED1.setState(false);        //initialize LED to off
        magLED2.setState(false);
    }
    @Override
    public void loop(){
        digVal1 = magnet1.getState();
        digVal2 = magnet2.getState();
        telemetry.addData("Mag sensor 1:", String.format("%1d" , (digVal1 ? 1:0)));
        telemetry.addData("Mag sensor 2:", String.format("%1d", (digVal2 ? 1:0)));
        if (!digVal1){                      //if switch is closed (magnet nearby),
            magLED1.setState(true);         //+5 volts to LED
        }
        else {                              //if switch is open (no magnet),
            magLED1.setState(false);        //0 volts to LED
        }
        if (!digVal2){
            magLED2.setState(true);
        }
        else {
            magLED2.setState(false);
        }
    }
}