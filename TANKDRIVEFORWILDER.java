package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

//MADE BY STEVE

public class TANKDRIVEFORWILDER extends OpMode {
    DigitalChannel digital;

    DcMotor drive1;
    DcMotor drive2;

    @Override
    public void init(){
        digital = hardwareMap.digitalChannel.get("touch1");
        drive1 = hardwareMap.dcMotor.get("d1");
        drive2 = hardwareMap.dcMotor.get("d2");
    }
    @Override
    public void loop() {
        boolean digVal = digital.getState();
        telemetry.addData("Digital1", String.format("%1d", (digVal ? 1 : 0)));
        drive1.setPower(gamepad1.left_stick_y);
        drive2.setPower(gamepad1.left_stick_y);
    }
}
