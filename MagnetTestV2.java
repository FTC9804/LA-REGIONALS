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

    @Override
    public void init(){
        magnet1 = hardwareMap.digitalChannel.get("mag1");
        magnet2 = hardwareMap.digitalChannel.get("mag2");
        magLED1 = hardwareMap.digitalChannel.get("LED1");
        magLED2 = hardwareMap.digitalChannel.get("LED2");
        magLED1.setMode(DigitalChannelController.Mode.OUTPUT);
        magLED2.setMode(DigitalChannelController.Mode.OUTPUT);
        magLED1.setState(true);
        magLED2.setState(true);
    }
    @Override
    public void loop(){
        boolean digVal1 = magnet1.getState();
        boolean digVal2 = magnet2.getState();
        telemetry.addData("Digital1:", String.format("%1d" , (digVal1 ? 1:0)));
        telemetry.addData("Digital2:", String.format("%1d", (digVal2 ? 1:0)));
        if (digVal1){
            magLED1.setState(false);
        }
        else {
            magLED1.setState(true);
        }
        if (digVal2){
            magLED2.setState(false);
        }
        else {
            magLED2.setState(true);
        }
    }
}