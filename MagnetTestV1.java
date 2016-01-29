package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.util.ElapsedTime;
/**
 * Created by bridget on 1/23/16.
 */
public class MagnetTestV1 extends OpMode {
    DigitalChannel digital;

    @Override
    public void init(){
        digital = hardwareMap.digitalChannel.get("touch1");
    }
    @Override
    public void loop(){
        boolean digVal = digital.getState();
        telemetry.addData("Digital1:", String.format("%1d" , (digVal ? 1:0))) ;
    }
}